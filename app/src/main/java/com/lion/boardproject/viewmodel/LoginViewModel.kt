package com.lion.boardproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lion.boardproject.fragment.LoginFragment

data class LoginViewModel(val loginFragment: LoginFragment) : ViewModel() {
    // toolbarUserLogin - title
    val toolbarUserLoginTitle = MutableLiveData("")
    // textFieldUserLoginId - EditText - text
    val textFieldUserLoginIdEditTextText = MutableLiveData("")
    // textFieldUserLoginPw - EditText - text
    val textFieldUserLoginPwEditTextText = MutableLiveData("")
    // checkBoxUserLoginAuto - checked
    val checkBoxUserLoginAutoChecked = MutableLiveData(false)

    // buttonUserLoginJoin - onClick
    fun buttonUserLoginJoinOnClick(){
        loginFragment.moveToUserJoinStep1()
    }

    // buttonUserLoginSubmit - onClick
    fun buttonUserLoginSubmitOnClick(){
        loginFragment.proLogin()
    }
}