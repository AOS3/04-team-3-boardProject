package com.lion.boardproject.viewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.checkbox.MaterialCheckBox
import com.lion.boardproject.fragment.UserModifyFragment

class UserModifyViewModel(val userModifyFragment: UserModifyFragment) : ViewModel() {
    
    // textFieldUserModifyPw1 - text
    val textFieldUserModifyPw1Text = MutableLiveData("")
    // textFieldUserModifyPw2 - text
    val textFieldUserModifyPw2Text = MutableLiveData("")
    // textFieldUserModifyNickName - text
    val textFieldUserModifyNickNameText = MutableLiveData(" ")
    // textFieldUserModifyAge - text
    val textFieldUserModifyAgeText = MutableLiveData(" ")

    // checkBoxUserModifyHobbyAll - checkedState
    val checkBoxUserModifyHobbyAllCheckedState = MutableLiveData(MaterialCheckBox.STATE_UNCHECKED)
    // checkBoxUserModifyHobbyAll - checked
    val checkBoxUserModifyHobbyAllChecked = MutableLiveData(false)
    // checkBoxUserModifyHobby1 - checked
    val checkBoxUserModifyHobby1Checked = MutableLiveData(false)
    // checkBoxUserModifyHobby2 - checked
    val checkBoxUserModifyHobby2Checked = MutableLiveData(false)
    // checkBoxUserModifyHobby3 - checked
    val checkBoxUserModifyHobby3Checked = MutableLiveData(false)
    // checkBoxUserModifyHobby4 - checked
    val checkBoxUserModifyHobby4Checked = MutableLiveData(false)
    // checkBoxUserModifyHobby5 - checked
    val checkBoxUserModifyHobby5Checked = MutableLiveData(false)
    // checkBoxUserModifyHobby6 - checked
    val checkBoxUserModifyHobby6Checked = MutableLiveData(false)

    // checkBoxUserModifyHobbyAll - onClick
    fun checkBoxUserModifyHobbyAllOnClick(){
        // 체크되어 있다면
        if(checkBoxUserModifyHobbyAllChecked.value == true){
            checkBoxUserModifyHobby1Checked.value = true
            checkBoxUserModifyHobby2Checked.value = true
            checkBoxUserModifyHobby3Checked.value = true
            checkBoxUserModifyHobby4Checked.value = true
            checkBoxUserModifyHobby5Checked.value = true
            checkBoxUserModifyHobby6Checked.value = true
        } else {
            checkBoxUserModifyHobby1Checked.value = false
            checkBoxUserModifyHobby2Checked.value = false
            checkBoxUserModifyHobby3Checked.value = false
            checkBoxUserModifyHobby4Checked.value = false
            checkBoxUserModifyHobby5Checked.value = false
            checkBoxUserModifyHobby6Checked.value = false
        }
    }

    // 하위 취미 체크박스들
    fun checkBoxUserModifyHobbyOnClick(){
        // 체크되어 있는 체크박스 개수
        var checkedCount = 0
        if(checkBoxUserModifyHobby1Checked.value == true){
            checkedCount++
        }
        if(checkBoxUserModifyHobby2Checked.value == true){
            checkedCount++
        }
        if(checkBoxUserModifyHobby3Checked.value == true){
            checkedCount++
        }
        if(checkBoxUserModifyHobby4Checked.value == true){
            checkedCount++
        }
        if(checkBoxUserModifyHobby5Checked.value == true){
            checkedCount++
        }
        if(checkBoxUserModifyHobby6Checked.value == true){
            checkedCount++
        }

        if(checkedCount == 0){
            // 체크된 것이 없으면
            checkBoxUserModifyHobbyAllChecked.value = false
            checkBoxUserModifyHobbyAllCheckedState.value = MaterialCheckBox.STATE_UNCHECKED
        } else if(checkedCount == 6){
            // 모두 체크되어 있다면
            checkBoxUserModifyHobbyAllChecked.value = true
            checkBoxUserModifyHobbyAllCheckedState.value = MaterialCheckBox.STATE_CHECKED
        } else {
            checkBoxUserModifyHobbyAllCheckedState.value = MaterialCheckBox.STATE_INDETERMINATE
        }
    }

    // buttonUserModifyCheckNickName - onClick
    fun buttonUserModifyCheckNickNameOnClick(){
        userModifyFragment.checkUserNickName()
    }

    // buttonUserModifyCheckNickName - onTextChanged
    fun buttonUserModifyCheckNickNameOnTextChanged(){
        userModifyFragment.isCheckUserNickName = false
    }

    companion object{
        // toolbarUserModify - onNavigationClickUserModify
        @JvmStatic
        @BindingAdapter("onNavigationClickUserModify")
        fun onNavigationClickUserModify(materialToolbar: MaterialToolbar, userModifyFragment: UserModifyFragment){
            materialToolbar.setNavigationOnClickListener {
                userModifyFragment.boardMainFragment.showNavigationView()
            }
        }
    }
}