package com.lion.boardproject.fragment

import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.boardproject.BoardActivity
import com.lion.boardproject.R
import com.lion.boardproject.databinding.FragmentBoardModifyBinding
import com.lion.boardproject.model.BoardModel
import com.lion.boardproject.service.BoardService
import com.lion.boardproject.viewmodel.BoardModifyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class BoardModifyFragment(val boardMainFragment: BoardMainFragment) : Fragment() {

    lateinit var fragmentBoardModifyBinding: FragmentBoardModifyBinding
    lateinit var boardActivity: BoardActivity

    // 현재 글의 문서 id를 담을 변수
    lateinit var boardDocumentId:String

    // 서버에서 받아온 데이터를 담을 변수
    lateinit var boardModel: BoardModel
    lateinit var boardBitmap: Bitmap
    // 원본 글에 이미지가 있는가..
    var isHasBitmap = false
    // 이미지를 사용자가 삭제하였는가..
    var isRemoveBitmap = false
    // 이미지를 수정하였는가..
    var isModifyBitmap = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentBoardModifyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_board_modify, container, false)
        fragmentBoardModifyBinding.boardModifyViewModel = BoardModifyViewModel(this@BoardModifyFragment)
        fragmentBoardModifyBinding.lifecycleOwner = this@BoardModifyFragment

        boardActivity = activity as BoardActivity

        // 이미지 관련 버튼 두 개를 안보이게 한다.
        fragmentBoardModifyBinding.apply {
            buttonBoardModifyRemoveImage.isVisible = false
            buttonBoardModifyResetImage.isVisible = false
        }


        // arguments의 값을 변수에 담아주는 메서드를 호출한다.
        gettingArguments()
        // Toolbar를 구성하는 메서드를 호출한다.
        settingToolbar()
        // 글 데이터를 가져와 보여주는 메서드를 호출한다.
        settingBoardData()

        return fragmentBoardModifyBinding.root
    }


    // Toolbar를 구성하는 메서드
    fun settingToolbar(){
        fragmentBoardModifyBinding.apply {
            // 메뉴의 항목을 눌렀을 때
            toolbarBoardModify.setOnMenuItemClickListener {
                when(it.itemId){
                    // 카메라
                    R.id.menuItemBoardModifyCamera -> {
                        if(isHasBitmap){
                            // 이미지 뷰에 있는 이미지를 변수에 담아준다.
                            getBitmapFromImageView()
                        }
                        boardMainFragment.boardActivity.startCameraLauncher(this@BoardModifyFragment)
                    }
                    // 앨범
                    R.id.menuItemBoardModifyAlbum -> {
                        if(isHasBitmap){
                            // 이미지 뷰에 있는 이미지를 변수에 담아준다.
                            getBitmapFromImageView()
                        }
                        boardMainFragment.boardActivity.startAlbumLauncher(this@BoardModifyFragment)
                    }
                    // 초기화
                    R.id.menuItemBoardModifyReset -> {
                        // 글 정보를 초기화 하는 메서드를 호출한다.
                        resetBoardData()
                    }
                    // 완료
                    R.id.menuItemBoardModifyDone -> {
                        // boardMainFragment.removeFragment(BoardSubFragmentName.BOARD_MODIFY_FRAGMENT)
                        val builder = MaterialAlertDialogBuilder(boardActivity)
                        builder.setTitle("글 수정")
                        builder.setMessage("수정시 복구할 수 없습니다")
                        builder.setNegativeButton("취소", null)
                        builder.setPositiveButton("수정"){ dialogInterface: DialogInterface, i: Int ->
                            proBoardUpdate()
                        }
                        builder.show()
                    }
                }
                true
            }
        }
    }

    // 이전 화면으로 돌아간다.
    fun movePrevFragment(){
        boardMainFragment.removeFragment(BoardSubFragmentName.BOARD_MODIFY_FRAGMENT)
    }

    // arguments의 값을 변수에 담아준다.
    fun gettingArguments(){
        boardDocumentId = arguments?.getString("boardDocumentId")!!
    }

    fun settingBoardData() {
        // 서버에서 데이터를 가져온다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                BoardService.selectBoardDataOneById(boardDocumentId)
            }
            boardModel = work1.await()

            fragmentBoardModifyBinding.apply {
                boardModifyViewModel?.textFieldBoardModifyTitleText?.value = boardModel.boardTitle
                boardModifyViewModel?.textFieldBoardModifyTextText?.value = boardModel.boardText
                boardModifyViewModel?.buttonGroupBoardModifyType?.value = boardModel.boardTypeValue
            }

            // 첨부 이미지가 있다면
            if(boardModel.boardFileName != "none"){
                val work1 = async(Dispatchers.IO) {
                    // 이미지에 접근할 수 있는 uri를 가져온다.
                    BoardService.gettingImage(boardModel.boardFileName)
                }

                val imageUri = work1.await()
                boardActivity.showServiceImage(imageUri, fragmentBoardModifyBinding.imageViewBoardModify)
                // 이미지 뷰에 있는 이미지를 추출하여 변수에 담아준다.
//                val bitmapDrawable = fragmentBoardModifyBinding.imageViewBoardModify.drawable as BitmapDrawable
//                boardBitmap = bitmapDrawable.bitmap

                // 글에 이미지가 있는지...
                isHasBitmap = true

                // 이미지 삭제 버튼을 보여준다.
                fragmentBoardModifyBinding.buttonBoardModifyRemoveImage.isVisible = true
            }
            // 이미지 초기화 버튼을 보여준다.
            fragmentBoardModifyBinding.buttonBoardModifyResetImage.isVisible = true
        }
    }

    // 글 정보를 초기화 하는 메서드
    fun resetBoardData(){
        fragmentBoardModifyBinding.apply {
            boardModifyViewModel?.textFieldBoardModifyTitleText?.value = boardModel.boardTitle
            boardModifyViewModel?.textFieldBoardModifyTextText?.value = boardModel.boardText
            boardModifyViewModel?.buttonGroupBoardModifyType?.value = boardModel.boardTypeValue

            if(isHasBitmap){
                imageViewBoardModify.setImageBitmap(boardBitmap)
                isRemoveBitmap = false
            } else {
                imageViewBoardModify.setImageResource(R.drawable.panorama_24px)
            }

            isModifyBitmap = false
        }
    }

    // 이미지 뷰에 있는 이미지를 변수에 담아준다.
    fun getBitmapFromImageView(){
        if(::boardBitmap.isInitialized == false){
            val bitmapDrawable = fragmentBoardModifyBinding.imageViewBoardModify.drawable as BitmapDrawable
            boardBitmap = bitmapDrawable.bitmap
        }
    }

    // 이미지뷰를 초기화 하는 메서드
    fun resetImageView(){
        fragmentBoardModifyBinding.apply {
            if(isHasBitmap){
                if(::boardBitmap.isInitialized) {
                    imageViewBoardModify.setImageBitmap(boardBitmap)
                    isRemoveBitmap = false
                    isModifyBitmap = false
                }
            } else {
                imageViewBoardModify.setImageResource(R.drawable.panorama_24px)
                isModifyBitmap = false
            }
        }
    }

    // 이미지뷰의 이미지를 삭제하는 메서드
    fun removeImageView(){
        fragmentBoardModifyBinding.apply {
            // 이미지를 추출에 변수에 담아준다.
            getBitmapFromImageView()

            imageViewBoardModify.setImageResource(R.drawable.panorama_24px)
            isRemoveBitmap = true
            isModifyBitmap = false
        }
    }

    // 글 수정 처리 메서드(입력칸 검사는 생략)
    fun proBoardUpdate(){
        fragmentBoardModifyBinding.apply {
            // boardModel에 새로운 글 내용을 넣어준다.
            boardModel.boardTitle = boardModifyViewModel?.textFieldBoardModifyTitleText?.value!!
            boardModel.boardText = boardModifyViewModel?.textFieldBoardModifyTextText?.value!!
            boardModel.boardTypeValue = boardModifyViewModel?.buttonGroupBoardModifyType?.value!!

            CoroutineScope(Dispatchers.Main).launch {
                if(isHasBitmap) {
                    // 만약 이미지를 삭제했다면
                    if (isRemoveBitmap) {
                        // 이미지 파일을 삭제한다.
                        val work1 = async(Dispatchers.IO) {
                            BoardService.removeImageFile(boardModel.boardFileName)
                        }
                        work1.join()
                        boardModel.boardFileName = "none"
                    }
                }
                // 이미지를 수정한적이 있다면
                if(isModifyBitmap){
                    if(boardModel.boardFileName != "none") {
                        // 이미지 파일을 삭제한다.
                        val work1 = async(Dispatchers.IO) {
                            BoardService.removeImageFile(boardModel.boardFileName)
                        }
                        work1.join()
                    }

                    // 서버상에서의 파일 이름
                    boardModel.boardFileName = "image_${System.currentTimeMillis()}.jpg"
                    // 로컬에 ImageView에 있는 이미지 데이터를 저장한다.
                    boardActivity.saveImageView(fragmentBoardModifyBinding.imageViewBoardModify)

                    val work2 = async(Dispatchers.IO){
                        BoardService.uploadImage("${boardActivity.filePath}/uploadTemp.jpg", boardModel.boardFileName)
                    }
                    work2.join()
                }

                // 글 데이터를 업로드한다.
                val work3 = async(Dispatchers.IO){
                    BoardService.updateBoardData(boardModel)
                }
                work3.join()

                boardMainFragment.removeFragment(BoardSubFragmentName.BOARD_MODIFY_FRAGMENT)
            }
        }
    }
}