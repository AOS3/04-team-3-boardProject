package com.lion.boardproject.viewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.lion.boardproject.fragment.BoardReadFragment
import com.lion.boardproject.fragment.BoardWriteFragment

class BoardReadViewModel(val boardReadFragment: BoardReadFragment) : ViewModel() {
    // textFieldBoardReadTitle - text
    val textFieldBoardReadTitleText = MutableLiveData(" ")
    // textFieldBoardReadNickName - text
    val textFieldBoardReadNickName = MutableLiveData(" ")
    // textFieldBoardReadType - text
    val textFieldBoardReadTypeText = MutableLiveData(" ")
    // textFieldBoardReadText - text
    val textFieldBoardReadTextText = MutableLiveData(" ")

    companion object{
        // toolbarBoardRead - onNavigationClickBoardRead
        @JvmStatic
        @BindingAdapter("onNavigationClickBoardRead")
        fun onNavigationClickBoardRead(materialToolbar: MaterialToolbar, boardReadFragment: BoardReadFragment){
            materialToolbar.setNavigationOnClickListener {
                boardReadFragment.movePrevFragment()
            }
        }
    }
}