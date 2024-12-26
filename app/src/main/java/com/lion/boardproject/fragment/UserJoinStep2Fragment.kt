package com.lion.boardproject.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.lion.boardproject.R
import com.lion.boardproject.UserActivity
import com.lion.boardproject.UserFragmentName
import com.lion.boardproject.databinding.FragmentLoginBinding
import com.lion.boardproject.databinding.FragmentUserJoinStep2Binding
import com.lion.boardproject.model.UserModel
import com.lion.boardproject.service.UserService
import com.lion.boardproject.util.UserState
import com.lion.boardproject.viewmodel.UserJoinStep2ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserJoinStep2Fragment : Fragment() {

    lateinit var fragmentUserJoinStep2Binding: FragmentUserJoinStep2Binding
    lateinit var userActivity: UserActivity

    // 번들로 전달된 데이터를 담을 변수
    lateinit var userId:String
    lateinit var userPw:String

    // 닉네임 중복 확인을 했는지 확인하기 위한 변수
    var isCheckUserNickExist = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentUserJoinStep2Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_join_step2, container, false)
        fragmentUserJoinStep2Binding.userJoinStep2ViewModel = UserJoinStep2ViewModel(this@UserJoinStep2Fragment)
        fragmentUserJoinStep2Binding.lifecycleOwner = this@UserJoinStep2Fragment

        userActivity = activity as UserActivity

        // 번들에 담겨져있는 데이터를 변수에 담아주는 메서드를 호출한다.
        gettingArguments()


        return fragmentUserJoinStep2Binding.root
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        userActivity.removeFragment(UserFragmentName.USER_JOIN_STEP2_FRAGMENT)
    }

    // 가입 완료 처리 메서드
    fun proUserJoin(){
        fragmentUserJoinStep2Binding.apply {
            // 입력 검사
            if(userJoinStep2ViewModel?.textFieldUserJoinStep2NickNameEditTextText?.value?.isEmpty()!!){
                userActivity.showMessageDialog("닉네임 입력", "닉네임을 입력해주세요", "확인"){
                    userActivity.showSoftInput(textFieldUserJoinStep2NickName.editText!!)
                }
                return
            }
            if(userJoinStep2ViewModel?.textFieldUserJoinStep2AgeEditTextText?.value?.isEmpty()!!){
                userActivity.showMessageDialog("나이 입력", "나이를 입력해주세요", "확인"){
                    userActivity.showSoftInput(textFieldUserJoinStep2Age.editText!!)
                }
                return
            }
            // 닉네임 중복 확인
            if(isCheckUserNickExist == false){
                userActivity.showMessageDialog("중복확인", "닉네임 중복 확인을 해주세요", "확인"){
                    userActivity.showSoftInput(textFieldUserJoinStep2NickName.editText!!)
                }
                return
            }

            // 저장할 데이터를 추출한다.
            val userNickname = userJoinStep2ViewModel?.textFieldUserJoinStep2NickNameEditTextText?.value!!
            val userAge = userJoinStep2ViewModel?.textFieldUserJoinStep2AgeEditTextText?.value!!.toInt()
            var userHobby1 = userJoinStep2ViewModel?.checkBoxUserJoinStep2Hobby1Checked?.value!!
            var userHobby2 = userJoinStep2ViewModel?.checkBoxUserJoinStep2Hobby2Checked?.value!!
            var userHobby3 = userJoinStep2ViewModel?.checkBoxUserJoinStep2Hobby3Checked?.value!!
            var userHobby4 = userJoinStep2ViewModel?.checkBoxUserJoinStep2Hobby4Checked?.value!!
            var userHobby5 = userJoinStep2ViewModel?.checkBoxUserJoinStep2Hobby5Checked?.value!!
            var userHobby6 = userJoinStep2ViewModel?.checkBoxUserJoinStep2Hobby6Checked?.value!!
            var userTimeStamp = System.nanoTime()
            var userState = UserState.USER_STATE_NORMAL

            val userModel = UserModel().also {
                it.userId = userId
                it.userPw = userPw
                it.userNickName = userNickname
                it.userAge = userAge
                it.userHobby1 = userHobby1
                it.userHobby2 = userHobby2
                it.userHobby3 = userHobby3
                it.userHobby4 = userHobby4
                it.userHobby5 = userHobby5
                it.userHobby6 = userHobby6
                it.userTimeStamp = userTimeStamp
                it.userState = userState
            }

            // 저장한다.
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    // 서비스의 저장 메서드를 호출한다.
                    UserService.addUserData(userModel)
                }
                work1.join()
                userActivity.showMessageDialog("가입 완료", "가입이 완료되었습니다\n로그인해주세요", "확인"){
                    userActivity.removeFragment(UserFragmentName.USER_JOIN_STEP1_FRAGMENT)
                }
            }
        }
    }

    // 번들에 담겨져있는 데이터를 변수에 담아준다.
    fun gettingArguments(){
        userId = arguments?.getString("userId")!!
        userPw = arguments?.getString("userPw")!!

//        Log.d("test100", userId)
//        Log.d("test100", userPw)
    }

    // 닉네임 중복 확인처리
    fun checkJoinNickName(){
        // 사용자가 입력한 닉네임
        val userNickName = fragmentUserJoinStep2Binding.userJoinStep2ViewModel?.textFieldUserJoinStep2NickNameEditTextText?.value!!
        // 입력한 것이 없다면
        if(userNickName.isEmpty()){
            userActivity.showMessageDialog("닉네임 입력", "닉네임을 입력해주세요", "확인"){
                userActivity.showSoftInput(fragmentUserJoinStep2Binding.textFieldUserJoinStep2NickName)
            }
            return
        }
        // 사용할 수 있는 닉네임인지 검사한다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                UserService.checkJoinUserNickName(userNickName)
            }
            val chk = work1.await()

            // 사용할 수 있는 닉네임인지 여부값을 담아준다.
            isCheckUserNickExist = chk

            if(chk){
                userActivity.showMessageDialog("중복확인", "사용할 수 있는 닉네임 입니다", "확인"){

                }
            } else{
                userActivity.showMessageDialog("중복확인", "이미 존재하는 닉네임 입니다", "확인"){
                    fragmentUserJoinStep2Binding.userJoinStep2ViewModel?.textFieldUserJoinStep2NickNameEditTextText?.value = ""
                    userActivity.showSoftInput(fragmentUserJoinStep2Binding.textFieldUserJoinStep2NickName)

                }
            }
        }
    }
}