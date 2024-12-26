package com.lion.boardproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lion.boardproject.fragment.BoardListFragment

class RowBoardListViewModel(val boardListFragment: BoardListFragment) : ViewModel(){
    // textViewRowBoardListTitle - text
    val textViewRowBoardListTitleText = MutableLiveData("")
    // textViewRowBoardListNickName - text
    val textViewRowBoardListNickNameText = MutableLiveData("")
}