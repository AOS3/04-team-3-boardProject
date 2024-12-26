package com.lion.boardproject.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.lion.boardproject.BoardActivity
import com.lion.boardproject.MainActivity
import com.lion.boardproject.R
import com.lion.boardproject.UserActivity
import com.lion.boardproject.UserFragmentName
import com.lion.boardproject.databinding.FragmentLoginBinding
import com.lion.boardproject.service.UserService
import com.lion.boardproject.util.LoginResult
import com.lion.boardproject.viewmodel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    lateinit var fragmentLoginBinding: FragmentLoginBinding
    lateinit var userActivity: UserActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        fragmentLoginBinding.loginViewModel = LoginViewModel(this@LoginFragment)
        fragmentLoginBinding.lifecycleOwner = this@LoginFragment

        userActivity = activity as UserActivity

        // 툴바를 구성하는 메서드 호출
        settingToolbar()

        return fragmentLoginBinding.root
    }
    
    // 툴바를 구성하는 메서드
    fun settingToolbar(){
        fragmentLoginBinding.loginViewModel?.apply {
            toolbarUserLoginTitle.value = "로그인"
        }
    }

    // 회원 가입 화면으로 이동시키는 메서드
    fun moveToUserJoinStep1(){
        userActivity.replaceFragment(UserFragmentName.USER_JOIN_STEP1_FRAGMENT, true, true, null)
    }

    // 로그인 처리 메서드
    fun proLogin(){
        fragmentLoginBinding.apply {
            // 입력 요소 검사
            if(loginViewModel?.textFieldUserLoginIdEditTextText?.value?.isEmpty()!!){
                userActivity.showMessageDialog("아이디 입력", "아이디를 입력해주세요", "확인"){
                    userActivity.showSoftInput(textFieldUserLoginId.editText!!)
                }
                return
            }
            if(loginViewModel?.textFieldUserLoginPwEditTextText?.value?.isEmpty()!!){
                userActivity.showMessageDialog("비밀번호 입력", "비밀번호를 입력해주세요", "확인"){
                    userActivity.showSoftInput(textFieldUserLoginPw.editText!!)
                }
                return
            }

            // 사용자가 입력한 아이디와 비밀번호
            val loginUserId = loginViewModel?.textFieldUserLoginIdEditTextText?.value!!
            val loginUserPw = loginViewModel?.textFieldUserLoginPwEditTextText?.value!!

            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    UserService.checkLogin(loginUserId, loginUserPw)
                }
                // 로그인 결과를 가져온다.
                val loginResult = work1.await()
                // Log.d("test100", loginResult.str)
                // 로그인 결과로 분기한다.
                when(loginResult){
                    LoginResult.LOGIN_RESULT_ID_NOT_EXIST -> {
                        userActivity.showMessageDialog("로그인 실패", "존재하지 않는 아이디 입니다", "확인"){
                            loginViewModel?.textFieldUserLoginIdEditTextText?.value = ""
                            loginViewModel?.textFieldUserLoginPwEditTextText?.value = ""
                            userActivity.showSoftInput(textFieldUserLoginId.editText!!)
                        }
                    }
                    LoginResult.LOGIN_RESULT_PASSWORD_INCORRECT -> {
                        userActivity.showMessageDialog("로그인 실패", "잘못된 비밀번호 입니다", "확인"){
                            loginViewModel?.textFieldUserLoginPwEditTextText?.value = ""
                            userActivity.showSoftInput(textFieldUserLoginPw.editText!!)
                        }
                    }
                    LoginResult.LOGIN_RESULT_SIGNOUT_MEMBER -> {
                        userActivity.showMessageDialog("로그인 실패", "탈퇴한 회원입니다", "확인"){
                            loginViewModel?.textFieldUserLoginIdEditTextText?.value = ""
                            loginViewModel?.textFieldUserLoginPwEditTextText?.value = ""
                            userActivity.showSoftInput(textFieldUserLoginId.editText!!)
                        }
                    }
                    LoginResult.LOGIN_RESULT_SUCCESS -> {
                        // 로그인한 사용자 정보를 가져온다.
                        val work2 = async(Dispatchers.IO){
                            UserService.selectUserDataByUserIdOne(loginUserId)
                        }
                        val loginUserModel = work2.await()



                        // 만약 자동로그인이 체크되어 있다면
                        if(loginViewModel?.checkBoxUserLoginAutoChecked?.value!!){
                            CoroutineScope(Dispatchers.Main).launch{
                                val work1 = async(Dispatchers.IO){
                                    UserService.updateUserAutoLoginToken(userActivity, loginUserModel.userDocumentId)
                                }
                                work1.join()
                            }
                        }


                        // BoardActivity를 실행하고 현재 Activity를 종료한다.
                        val boardIntent = Intent(userActivity, BoardActivity::class.java)
                        boardIntent.putExtra("user_document_id", loginUserModel.userDocumentId)
                        boardIntent.putExtra("user_nick_name", loginUserModel.userNickName)
                        startActivity(boardIntent)
                        userActivity.finish()
                    }
                }
            }
        }
    }
}