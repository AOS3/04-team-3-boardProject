package com.lion.boardproject.viewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.lion.boardproject.fragment.BoardListFragment
import com.lion.boardproject.fragment.UserJoinStep2Fragment

class BoardListViewModel(val boardListFragment: BoardListFragment) : ViewModel() {
    // toolbarBoardList - title
    val toolbarBoardListTitle = MutableLiveData("")

    companion object{
        // toolbarBoardList - onNavigationClickBoardList
        @JvmStatic
        @BindingAdapter("onNavigationClickBoardList")
        fun onNavigationClickBoardList(materialToolbar: MaterialToolbar, boardListFragment: BoardListFragment){
            materialToolbar.setNavigationOnClickListener {
                boardListFragment.boardMainFragment.showNavigationView()
            }
        }
    }
}