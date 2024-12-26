package com.lion.boardproject.viewmodel

import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.FirebaseFirestore
import com.lion.boardproject.UserFragmentName
import com.lion.boardproject.fragment.UserJoinStep1Fragment
import com.lion.boardproject.vo.UserVO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class UserJoinStep1ViewModel(val userJoinStep1Fragment: UserJoinStep1Fragment) : ViewModel(){
    // toolbarUserJoinStep1 - title
    val toolbarUserLoginTitle = MutableLiveData<String>()
    // toolbarUserJoinStep1 - navigationIcon
    val toolbarUserLoginNavigationIcon = MutableLiveData<Int>()
    // textFieldUserJoinStep1Id - EditText - text
    val textFieldUserJoinStep1IdEditTextText = MutableLiveData("")
    // textFieldUserJoinStep1Pw1 - EditText - text
    val textFieldUserJoinStep1Pw1EditTextText = MutableLiveData("")
    // textFieldUserJoinStep1Pw2 - EditText - text
    val textFieldUserJoinStep1Pw2EditTextText = MutableLiveData("")


    // buttonUserJoinStep1Next - onClick
    fun buttonUserJoinStep1NextOnClick(){
        userJoinStep1Fragment.apply {
            // 입력요소 검사
            if(textFieldUserJoinStep1IdEditTextText.value?.isEmpty()!!){
                userActivity.showMessageDialog("아이디 입력", "아이디를 입력해주세요", "확인"){
                    userActivity.showSoftInput(fragmentUserJoinStep1Binding.textFieldUserJoinStep1Id.editText!!)
                }
                return
            }
            if(textFieldUserJoinStep1Pw1EditTextText.value?.isEmpty()!!){
                userActivity.showMessageDialog("비밀번호 입력", "비밀번호를 입력해주세요", "확인"){
                    userActivity.showSoftInput(fragmentUserJoinStep1Binding.textFieldUserJoinStep1Pw1.editText!!)
                }
                return
            }
            if(textFieldUserJoinStep1Pw2EditTextText.value?.isEmpty()!!){
                userActivity.showMessageDialog("비밀번호 입력", "비밀번호를 입력해주세요", "확인"){
                    userActivity.showSoftInput(fragmentUserJoinStep1Binding.textFieldUserJoinStep1Pw2.editText!!)
                }
                return
            }
            if(textFieldUserJoinStep1Pw1EditTextText.value != textFieldUserJoinStep1Pw2EditTextText.value){
                userActivity.showMessageDialog("비밀번호 입력", "비밀번호가 다릅니다", "확인"){
                    textFieldUserJoinStep1Pw1EditTextText.value = ""
                    textFieldUserJoinStep1Pw2EditTextText.value = ""
                    userActivity.showSoftInput(fragmentUserJoinStep1Binding.textFieldUserJoinStep1Pw1.editText!!)
                }
                return
            }

            // 중복확인을 안했다면..
            if(userJoinStep1Fragment.isCheckJoinUserIdExist == false){
                userActivity.showMessageDialog("아이디 중복 확인", "아이디 중복 확인을 해주세요", "확인"){

                }
                return
            }

            // 다음 화면으로 이동한다.
            moveToUserJoinStep2()
        }
    }

    // buttonUserJoinStep1CheckId - onClick
    fun buttonUserJoinStep1CheckIdOnClick(){
        userJoinStep1Fragment.checkJoinUserId()
    }

    // textFieldUserJoinStep1Id - onTextChanged
    fun textFieldUserJoinStep1IdOnTextChanged(){
        userJoinStep1Fragment.isCheckJoinUserIdExist = false
    }

    companion object{
        // toolbarUserJoinStep1 - onNavigationClickUserJoinStep1
        @JvmStatic
        // xml 에 설정할 속성이름을 설정한다.
        @BindingAdapter("onNavigationClickUserJoinStep1")
        // 호출되는 메서드를 구현한다.
        // 첫 번째 매개변수 : 이 속성이 설정되어 있는 View 객체
        // 그 이후 : 전달해주는 값이 들어온다. xml 에서는 ViewModel이 가는 프로퍼티에 접근할 수 있기 때문에
        // 이것을 통해 Fragment객체를 받아 사용할것이다.
        fun onNavigationClickUserJoinStep1(materialToolbar:MaterialToolbar, userJoinStep1Fragment: UserJoinStep1Fragment){
            materialToolbar.setNavigationOnClickListener {
                // 이전으로 돌아간다.
                userJoinStep1Fragment.userActivity.removeFragment(UserFragmentName.USER_JOIN_STEP1_FRAGMENT)
            }
        }
    }


}
