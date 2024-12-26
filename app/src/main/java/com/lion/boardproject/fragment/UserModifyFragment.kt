package com.lion.boardproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.lion.boardproject.BoardActivity
import com.lion.boardproject.R
import com.lion.boardproject.databinding.FragmentUserModifyBinding
import com.lion.boardproject.model.UserModel
import com.lion.boardproject.service.UserService
import com.lion.boardproject.viewmodel.UserModifyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class UserModifyFragment(val boardMainFragment: BoardMainFragment) : Fragment() {

    lateinit var fragmentUserModifyBinding: FragmentUserModifyBinding
    lateinit var boardActivity: BoardActivity

    // 사용자 정보를 담을 변수
    lateinit var userModel:UserModel

    // 중복 확인을 하였는지...
    var isCheckUserNickName = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentUserModifyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_modify, container, false)
        fragmentUserModifyBinding.userModifyViewModel = UserModifyViewModel(this@UserModifyFragment)
        fragmentUserModifyBinding.lifecycleOwner = this@UserModifyFragment

        boardActivity = activity as BoardActivity

        // 데이터를 읽어와 입력 요소를 채워준다.
        settingInputData()
        // Toolbar를 구성하는 메서드를 호출한다.
        settingToolbar()

        return fragmentUserModifyBinding.root
    }

    // 데이터를 읽어와 입력 요소를 채워준다.
    fun settingInputData(){
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                UserService.selectUserDataByUserDocumentIdOne(boardActivity.loginUserDocumentId)
            }
            userModel = work1.await()

            fragmentUserModifyBinding.apply {
                userModifyViewModel?.textFieldUserModifyNickNameText?.value = userModel.userNickName
                userModifyViewModel?.textFieldUserModifyAgeText?.value = userModel.userAge.toString()
                userModifyViewModel?.checkBoxUserModifyHobby1Checked?.value = userModel.userHobby1
                userModifyViewModel?.checkBoxUserModifyHobby2Checked?.value = userModel.userHobby2
                userModifyViewModel?.checkBoxUserModifyHobby3Checked?.value = userModel.userHobby3
                userModifyViewModel?.checkBoxUserModifyHobby4Checked?.value = userModel.userHobby4
                userModifyViewModel?.checkBoxUserModifyHobby5Checked?.value = userModel.userHobby5
                userModifyViewModel?.checkBoxUserModifyHobby6Checked?.value = userModel.userHobby6

                userModifyViewModel?.checkBoxUserModifyHobbyOnClick()
            }
        }
    }

    // 닉네임 중복 확인 버튼을 누르면 호출되는 메서드
    fun checkUserNickName(){
        fragmentUserModifyBinding.apply {
            val nickName = userModifyViewModel?.textFieldUserModifyNickNameText?.value!!

            if(nickName.isEmpty()){
                boardActivity.showMessageDialog("닉네임 입력 오류", "닉네임을 입력해주세요", "확인"){
                    boardActivity.showSoftInput(textFieldUserModifyNickName.editText!!)
                }
                return
            }

            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    UserService.checkJoinUserNickName(nickName)
                }
                isCheckUserNickName = work1.await()

                if(isCheckUserNickName) {
                    boardActivity.showMessageDialog("닉네임", "사용할 수 있는 닉네임 입니다", "확인"){

                    }
                } else {
                    boardActivity.showMessageDialog("닉네임", "사용할 수 없는 닉네임 입니다", "확인"){

                    }
                }
            }
        }
    }

    // Toolbar를 구성하는 메서드
    fun settingToolbar(){
        fragmentUserModifyBinding.apply {
            toolbarUserModify.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.menuItemUserModifyDone -> {
                        // 수정 처리 메서드를 호출한다.
                        proModifyUserData()
                    }
                }
                true
            }
        }
    }

    // 수정 처리 메서드
    fun proModifyUserData(){
        fragmentUserModifyBinding.apply {
            // 입력에 대한 검사
            
            // 비밀번호
            // 둘 중 하나라도 입력이 되어 있는 경우 서로 다르면 입력 오류로 처리한다.
            val pw1 = userModifyViewModel?.textFieldUserModifyPw1Text?.value!!
            val pw2 = userModifyViewModel?.textFieldUserModifyPw2Text?.value!!

            if(pw1.isNotEmpty() or pw2.isNotEmpty()){
                if(pw1 != pw2){
                    boardActivity.showMessageDialog("비밀번호 입력 오류", "비밀번호가 다릅니다", "확인"){
                        userModifyViewModel?.textFieldUserModifyPw1Text?.value = ""
                        userModifyViewModel?.textFieldUserModifyPw2Text?.value = ""
                        boardActivity.showSoftInput(textFieldUserModifyPw1.editText!!)
                    }
                    return
                }
            }

            // 원래의 닉네임과 입력 요소에 입력된 닉네임이 서로 다르고 중복 검사를 안했을 경우 입력 오류로 처리
            val nickName = userModifyViewModel?.textFieldUserModifyNickNameText?.value!!
            if(userModel.userNickName != nickName){
                if(isCheckUserNickName == false){
                    boardActivity.showMessageDialog("닉네임 입력 오류", "닉네임 중복 확인을 해주세요", "확인"){

                    }
                    return
                }
            }
            // 나이 입력 요소에 입력이 되어 있지 않다면 입력 오류 처리
            val age = userModifyViewModel?.textFieldUserModifyAgeText?.value!!
            if(age.isEmpty()){
                boardActivity.showMessageDialog("나이 입력 오류", "나이를 입력해주세요", "확인"){
                    boardActivity.showSoftInput(textFieldUserModifyAge.editText!!)
                }
                return
            }

            // 수정할 데이터를 담아 준다.
            if(pw1.isNotEmpty()){
                userModel.userPw = pw1
            }
            userModel.userNickName = nickName
            userModel.userAge = age.toInt()
            userModel.userHobby1 = userModifyViewModel?.checkBoxUserModifyHobby1Checked?.value!!
            userModel.userHobby2 = userModifyViewModel?.checkBoxUserModifyHobby2Checked?.value!!
            userModel.userHobby3 = userModifyViewModel?.checkBoxUserModifyHobby3Checked?.value!!
            userModel.userHobby4 = userModifyViewModel?.checkBoxUserModifyHobby4Checked?.value!!
            userModel.userHobby5 = userModifyViewModel?.checkBoxUserModifyHobby5Checked?.value!!
            userModel.userHobby6 = userModifyViewModel?.checkBoxUserModifyHobby6Checked?.value!!

            // 수정한다.
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    UserService.updateUserData(userModel)
                }
                work1.join()

                val snackbar = Snackbar.make(boardActivity.activityBoardBinding.root, "수정이 완료되었습니다", Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
        }
    }
}










