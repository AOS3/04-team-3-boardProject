package com.lion.boardproject.viewmodel

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.checkbox.MaterialCheckBox
import com.lion.boardproject.UserFragmentName
import com.lion.boardproject.fragment.UserJoinStep1Fragment
import com.lion.boardproject.fragment.UserJoinStep2Fragment

class UserJoinStep2ViewModel(val userJoinStep2Fragment: UserJoinStep2Fragment) : ViewModel() {
    // textFieldUserJoinStep2NickName - EditText - text
    val textFieldUserJoinStep2NickNameEditTextText = MutableLiveData("")
    // textFieldUserJoinStep2Age - EditExt - text
    val textFieldUserJoinStep2AgeEditTextText = MutableLiveData("")
    // checkBoxUserJoinStep2HobbyAll - checkedState
    val checkBoxUserJoinStep2HobbyAllCheckedState = MutableLiveData(MaterialCheckBox.STATE_UNCHECKED)
    // checkBoxUserJoinStep2HobbyAll - checked
    val checkBoxUserJoinStep2HobbyAllChecked = MutableLiveData(false)
    // checkBoxUserJoinStep2Hobby1 - checked
    val checkBoxUserJoinStep2Hobby1Checked = MutableLiveData(false)
    // checkBoxUserJoinStep2Hobby2 - checked
    val checkBoxUserJoinStep2Hobby2Checked = MutableLiveData(false)
    // checkBoxUserJoinStep2Hobby3 - checked
    val checkBoxUserJoinStep2Hobby3Checked = MutableLiveData(false)
    // checkBoxUserJoinStep2Hobby4 - checked
    val checkBoxUserJoinStep2Hobby4Checked = MutableLiveData(false)
    // checkBoxUserJoinStep2Hobby5 - checked
    val checkBoxUserJoinStep2Hobby5Checked = MutableLiveData(false)
    // checkBoxUserJoinStep2Hobby6 - checked
    val checkBoxUserJoinStep2Hobby6Checked = MutableLiveData(false)

    // checkBoxUserJoinStep2HobbyAll - onClick
    fun checkBoxUserJoinStep2HobbyAllOnClick(){
        // 체크되어 있다면
        if(checkBoxUserJoinStep2HobbyAllChecked.value == true){
            checkBoxUserJoinStep2Hobby1Checked.value = true
            checkBoxUserJoinStep2Hobby2Checked.value = true
            checkBoxUserJoinStep2Hobby3Checked.value = true
            checkBoxUserJoinStep2Hobby4Checked.value = true
            checkBoxUserJoinStep2Hobby5Checked.value = true
            checkBoxUserJoinStep2Hobby6Checked.value = true
        } else {
            checkBoxUserJoinStep2Hobby1Checked.value = false
            checkBoxUserJoinStep2Hobby2Checked.value = false
            checkBoxUserJoinStep2Hobby3Checked.value = false
            checkBoxUserJoinStep2Hobby4Checked.value = false
            checkBoxUserJoinStep2Hobby5Checked.value = false
            checkBoxUserJoinStep2Hobby6Checked.value = false
        }
    }

    // 하위 취미 체크박스들
    fun checkBoxUserJoinStep2HobbyOnClick(){
        // 체크되어 있는 체크박스 개수
        var checkedCount = 0
        if(checkBoxUserJoinStep2Hobby1Checked.value == true){
            checkedCount++
        }
        if(checkBoxUserJoinStep2Hobby2Checked.value == true){
            checkedCount++
        }
        if(checkBoxUserJoinStep2Hobby3Checked.value == true){
            checkedCount++
        }
        if(checkBoxUserJoinStep2Hobby4Checked.value == true){
            checkedCount++
        }
        if(checkBoxUserJoinStep2Hobby5Checked.value == true){
            checkedCount++
        }
        if(checkBoxUserJoinStep2Hobby6Checked.value == true){
            checkedCount++
        }

        if(checkedCount == 0){
            // 체크된 것이 없으면
            checkBoxUserJoinStep2HobbyAllChecked.value = false
            checkBoxUserJoinStep2HobbyAllCheckedState.value = MaterialCheckBox.STATE_UNCHECKED
        } else if(checkedCount == 6){
            // 모두 체크되어 있다면
            checkBoxUserJoinStep2HobbyAllChecked.value = true
            checkBoxUserJoinStep2HobbyAllCheckedState.value = MaterialCheckBox.STATE_CHECKED
        } else {
            checkBoxUserJoinStep2HobbyAllCheckedState.value = MaterialCheckBox.STATE_INDETERMINATE
        }
    }

    // buttonUserJoinStep2Submit - onClick
    fun buttonUserJoinStep2SubmitOnClick(){
        // 가입 완료 처리 메서드를 호출한다.
        userJoinStep2Fragment.proUserJoin()
    }

    // buttonUserJoinStep2CheckNickName - onClick
    fun buttonUserJoinStep2CheckNickNameOnClick(){
        userJoinStep2Fragment.checkJoinNickName()
    }

    // textFieldUserJoinStep2NickName - onTextChanged
    fun textFieldUserJoinStep2NickNameOnTextChanged(){
        userJoinStep2Fragment.isCheckUserNickExist = false
    }

    companion object{
        // toolbarUserJoinStep2 - onNavigationClickUserJoinStep2
        @JvmStatic
        @BindingAdapter("onNavigationClickUserJoinStep2")
        fun onNavigationClickUserJoinStep2(materialToolbar: MaterialToolbar, userJoinStep2Fragment: UserJoinStep2Fragment){
            materialToolbar.setNavigationOnClickListener {
                userJoinStep2Fragment.movePrevFragment()
            }
        }
    }
}