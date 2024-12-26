package com.lion.boardproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lion.boardproject.fragment.BoardMainFragment

class NavigationBoardMainHeaderViewModel(val boardMainFragment: BoardMainFragment) : ViewModel() {
    // textViewNavigationBoardMainHeaderNickName - text
    val textViewNavigationBoardMainHeaderNickNameText = MutableLiveData("")
}