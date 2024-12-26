package com.lion.boardproject.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.boardproject.R
import com.lion.boardproject.UserActivity
import com.lion.boardproject.UserFragmentName
import com.lion.boardproject.databinding.FragmentUserJoinStep1Binding
import com.lion.boardproject.service.UserService
import com.lion.boardproject.viewmodel.UserJoinStep1ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class UserJoinStep1Fragment : Fragment() {

    lateinit var fragmentUserJoinStep1Binding: FragmentUserJoinStep1Binding
    lateinit var userActivity: UserActivity

    // 아이디 중복 확인 검사를 했는지 확인하는 변수
    var isCheckJoinUserIdExist = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentUserJoinStep1Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_join_step1, container, false)
        fragmentUserJoinStep1Binding.userJoinStep1ViewModel = UserJoinStep1ViewModel(this@UserJoinStep1Fragment)
        fragmentUserJoinStep1Binding.lifecycleOwner = this@UserJoinStep1Fragment

        userActivity = activity as UserActivity

        // 툴바를 구성하는 메서드를 호출한다.
        settingToolbar()

        return fragmentUserJoinStep1Binding.root
    }
    
    // 툴바를 구성하는 메서드
    fun settingToolbar(){
        fragmentUserJoinStep1Binding.userJoinStep1ViewModel?.apply {
            // 타이틀
            toolbarUserLoginTitle.value = "회원 가입"
            // 네비게이션 아이콘
            toolbarUserLoginNavigationIcon.value = R.drawable.arrow_back_24px

        }
    }

    // 다음 입력 화면으로 이동한다.
    fun moveToUserJoinStep2(){
        fragmentUserJoinStep1Binding.apply {
            // 사용자가 입력한 데이터를 가져온다.
            val userId = userJoinStep1ViewModel?.textFieldUserJoinStep1IdEditTextText?.value!!
            val userPw = userJoinStep1ViewModel?.textFieldUserJoinStep1Pw1EditTextText?.value!!
            // Log.d("test100", userId)
            // Log.d("test100", userPw)
            // 데이터를 담는다.
            val dataBundle = Bundle()
            dataBundle.putString("userId", userId)
            dataBundle.putString("userPw", userPw)
            userActivity.replaceFragment(UserFragmentName.USER_JOIN_STEP2_FRAGMENT, true, true, dataBundle)
        }
    }

    // 아이디 중복 확인처리
    fun checkJoinUserId(){
        // 사용자가 입력한 아이디
        val userId = fragmentUserJoinStep1Binding.userJoinStep1ViewModel?.textFieldUserJoinStep1IdEditTextText?.value!!
        // 입력한 것이 없다면
        if(userId.isEmpty()){
            userActivity.showMessageDialog("아이디 입력", "아이디를 입력해주세요", "확인"){
                userActivity.showSoftInput(fragmentUserJoinStep1Binding.textFieldUserJoinStep1Id)
            }
            return
        }
        // 사용할 수 있는 아이디인지 검사한다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                UserService.checkJoinUserId(userId)
            }
            val chk = work1.await()

            // 사용할 수 있는 아이디인지 여부값을 담아준다.
            isCheckJoinUserIdExist = chk

            if(chk){
                userActivity.showMessageDialog("중복확인", "사용할 수 있는 아이디 입니다", "확인"){

                }
            } else{
                userActivity.showMessageDialog("중복확인", "이미 존재하는 아이디 입니다", "확인"){
                    fragmentUserJoinStep1Binding.userJoinStep1ViewModel?.textFieldUserJoinStep1IdEditTextText?.value = ""
                    userActivity.showSoftInput(fragmentUserJoinStep1Binding.textFieldUserJoinStep1Id)

                }
            }
        }
    }
}