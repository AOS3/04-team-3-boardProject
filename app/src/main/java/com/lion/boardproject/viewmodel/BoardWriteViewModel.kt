package com.lion.boardproject.viewmodel

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButtonToggleGroup
import com.lion.boardproject.R
import com.lion.boardproject.fragment.BoardWriteFragment
import com.lion.boardproject.util.BoardType

class BoardWriteViewModel(val boardWriteFragment: BoardWriteFragment) : ViewModel() {

    // textFieldBoardWriteTitle - text
    val textFieldBoardWriteTitleText = MutableLiveData("")
    // textFieldBoardWriteText - text
    val textFieldBoardWriteTextText = MutableLiveData("")
    // buttonGroupBoardWriteType
    val buttonGroupBoardWriteType = MutableLiveData(BoardType.BOARD_TYPE_1)

    init{
        //  buttonGroupBoardWriteType의 감시자
        buttonGroupBoardWriteType.observe(boardWriteFragment){
            // 게시판 종류에 따라서 버튼을 선택해준다.
            val buttonId = when(it){
                BoardType.BOARD_TYPE_1 -> R.id.buttonBoardWriteType1
                BoardType.BOARD_TYPE_2 -> R.id.buttonBoardWriteType2
                BoardType.BOARD_TYPE_3 -> R.id.buttonBoardWriteType3
                BoardType.BOARD_TYPE_4 -> R.id.buttonBoardWriteType4
                BoardType.BOARD_TYPE_ALL -> 0
            }
            // 버튼의 id 값을 설정한다.
            boardWriteFragment.fragmentBoardWriteBinding.buttonGroupBoardWriteType.check(buttonId)
        }
    }

    // buttonBoardWriteImageDelete - onClick
    fun buttonBoardWriteImageDeleteOnClick(){
        boardWriteFragment.resetImageView()
    }
    companion object{
        // toolbarBoardWrite - onNavigationClickBoardWrite
        @JvmStatic
        @BindingAdapter("onNavigationClickBoardWrite")
        fun onNavigationClickBoardWrite(materialToolbar: MaterialToolbar, boardWriteFragment: BoardWriteFragment){
            materialToolbar.setNavigationOnClickListener {
                boardWriteFragment.movePrevFragment()
            }
        }

        // buttonGroupBoardWriteType - onButtonCheckedChange
        @JvmStatic
        @BindingAdapter("onButtonCheckedChange")
        fun onButtonCheckedChange(materialButtonToggleGroup: MaterialButtonToggleGroup, boardWriteFragment: BoardWriteFragment){
            // 리스너를 설정한다.
            materialButtonToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
                boardWriteFragment.fragmentBoardWriteBinding.boardWriteViewModel?.buttonGroupBoardWriteType?.apply {
                    if(isChecked){
                        when(checkedId){
                            R.id.buttonBoardWriteType1 -> {
                                value = BoardType.BOARD_TYPE_1
                            }
                            R.id.buttonBoardWriteType2 -> {
                                value = BoardType.BOARD_TYPE_2
                            }
                            R.id.buttonBoardWriteType3 -> {
                                value = BoardType.BOARD_TYPE_3
                            }
                            R.id.buttonBoardWriteType4 -> {
                                value = BoardType.BOARD_TYPE_4
                            }
                        }
                    }
                }

            }
        }
    }
}