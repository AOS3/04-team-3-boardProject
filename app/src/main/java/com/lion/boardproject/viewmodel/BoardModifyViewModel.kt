package com.lion.boardproject.viewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButtonToggleGroup
import com.lion.boardproject.R
import com.lion.boardproject.fragment.BoardModifyFragment
import com.lion.boardproject.fragment.BoardWriteFragment
import com.lion.boardproject.util.BoardType

class BoardModifyViewModel(val boardModifyFragment: BoardModifyFragment) : ViewModel() {

    // textFieldBoardModifyTitle - text
    val textFieldBoardModifyTitleText = MutableLiveData(" ")
    // textFieldBoardModifyText - text
    val textFieldBoardModifyTextText = MutableLiveData(" ")
    // buttonGroupBoardModifyType
    val buttonGroupBoardModifyType = MutableLiveData(BoardType.BOARD_TYPE_1)

    init{
        //  buttonGroupBoardModifyType의 감시자
        buttonGroupBoardModifyType.observe(boardModifyFragment){
            // 게시판 종류에 따라서 버튼을 선택해준다.
            val buttonId = when(it){
                BoardType.BOARD_TYPE_1 -> R.id.buttonBoardModifyType1
                BoardType.BOARD_TYPE_2 -> R.id.buttonBoardModifyType2
                BoardType.BOARD_TYPE_3 -> R.id.buttonBoardModifyType3
                BoardType.BOARD_TYPE_4 -> R.id.buttonBoardModifyType4
                BoardType.BOARD_TYPE_ALL -> 0
            }
            // 버튼의 id 값을 설정한다.
            boardModifyFragment.fragmentBoardModifyBinding.buttonGroupBoardModifyType.check(buttonId)
        }
    }

    // buttonBoardModifyResetImage - onClick
    fun buttonBoardModifyResetImageOnClick(){
        boardModifyFragment.resetImageView()
    }

    // buttonBoardModifyRemoveImage - onClick
    fun buttonBoardModifyRemoveImageOnClick(){
        boardModifyFragment.removeImageView()
    }


    companion object{
        // toolbarBoardModify - onNavigationClickBoardModify
        @JvmStatic
        @BindingAdapter("onNavigationClickBoardModify")
        fun onNavigationClickBoardModify(materialToolbar: MaterialToolbar, boardModifyFragment: BoardModifyFragment){
            materialToolbar.setNavigationOnClickListener {
                boardModifyFragment.movePrevFragment()
            }
        }

        // buttonGroupBoardModifyType - onButtonCheckedChangeModify
        @JvmStatic
        @BindingAdapter("onButtonCheckedChangeModify")
        fun onButtonCheckedChangeModify(materialButtonToggleGroup: MaterialButtonToggleGroup, boardModifyFragment: BoardModifyFragment) {
            // 리스너를 설정한다.
            materialButtonToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
                boardModifyFragment.fragmentBoardModifyBinding.boardModifyViewModel?.buttonGroupBoardModifyType?.apply {
                    if (isChecked) {
                        when (checkedId) {
                            R.id.buttonBoardModifyType1 -> {
                                value = BoardType.BOARD_TYPE_1
                            }

                            R.id.buttonBoardModifyType2 -> {
                                value = BoardType.BOARD_TYPE_2
                            }

                            R.id.buttonBoardModifyType3 -> {
                                value = BoardType.BOARD_TYPE_3
                            }

                            R.id.buttonBoardModifyType4 -> {
                                value = BoardType.BOARD_TYPE_4
                            }
                        }
                    }
                }

            }
        }
    }
}