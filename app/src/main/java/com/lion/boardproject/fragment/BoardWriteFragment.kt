package com.lion.boardproject.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.lion.boardproject.BoardActivity
import com.lion.boardproject.R
import com.lion.boardproject.databinding.FragmentBoardWriteBinding
import com.lion.boardproject.model.BoardModel
import com.lion.boardproject.model.ReplyModel
import com.lion.boardproject.repository.BoardRepository
import com.lion.boardproject.service.BoardService
import com.lion.boardproject.util.BoardState
import com.lion.boardproject.util.BoardType
import com.lion.boardproject.viewmodel.BoardWriteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class BoardWriteFragment(val boardMainFragment: BoardMainFragment) : Fragment() {

    lateinit var fragmentBoardWriteBinding: FragmentBoardWriteBinding
    lateinit var boardActivity: BoardActivity

    // 카메라나 앨범에서 이미지를 가져왔는지...
    var isSetImageView = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentBoardWriteBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_board_write, container, false)
        fragmentBoardWriteBinding.boardWriteViewModel = BoardWriteViewModel(this@BoardWriteFragment)
        fragmentBoardWriteBinding.lifecycleOwner = this@BoardWriteFragment

        boardActivity = activity as BoardActivity

        // Toolbar를 구성하는 메서드를 호출한다.
        settingToolbar()

        return fragmentBoardWriteBinding.root
    }

    // Toolbar를 구성하는 메서드
    fun settingToolbar(){
        fragmentBoardWriteBinding.apply {
            // 메뉴의 항목을 눌렀을 때
            toolbarBoardWrite.setOnMenuItemClickListener {
                when(it.itemId){
                    // 카메라
                    R.id.menuItemBoardWriteCamera -> {
                        boardMainFragment.boardActivity.startCameraLauncher(this@BoardWriteFragment)
                    }
                    // 앨범
                    R.id.menuItemBoardWriteAlbum -> {
                        boardMainFragment.boardActivity.startAlbumLauncher(this@BoardWriteFragment)
                    }
                    // 초기화
                    R.id.menuItemBoardWriteReset -> {
                        resetInput()
                    }
                    // 완료
                    R.id.menuItemBoardWriteDone -> {
                        // boardMainFragment.replaceFragment(BoardSubFragmentName.BOARD_READ_FRAGMENT, true, true, null)
                        proBoardWriteSubmit()
                    }
                }
                true
            }
        }
    }


    // 이전 화면으로 돌아간다.
    fun movePrevFragment(){
        boardMainFragment.removeFragment(BoardSubFragmentName.BOARD_WRITE_FRAGMENT)
    }

    // 이미지 뷰 초기화
    fun resetImageView(){
        fragmentBoardWriteBinding.imageViewBoardWrite.setImageResource(R.drawable.panorama_24px)
        isSetImageView = false
    }

    // 입력요소 초기화
    fun resetInput(){
        fragmentBoardWriteBinding.apply {
            boardWriteViewModel?.textFieldBoardWriteTitleText?.value = ""
            boardWriteViewModel?.textFieldBoardWriteTextText?.value = ""

            fragmentBoardWriteBinding.boardWriteViewModel?.buttonGroupBoardWriteType?.value = BoardType.BOARD_TYPE_1

            resetImageView()
        }
    }

    // 글 작성 완료 처리 메서드
    fun proBoardWriteSubmit(){
        fragmentBoardWriteBinding.apply {
            // 게시판 구분값
            var boardTypeValue = boardWriteViewModel?.buttonGroupBoardWriteType?.value!!
            // 게시글 제목
            var boardTitle = boardWriteViewModel?.textFieldBoardWriteTitleText?.value!!
            // 작성자 구분값
            var boardWriteId = boardActivity.loginUserDocumentId
            // 게시글 내용
            var boardText = boardWriteViewModel?.textFieldBoardWriteTextText?.value!!
            // 첨부 사진 파일 이름
            var boardFileName = "none"
            // 시간
            var boardTimeStamp = System.nanoTime()

            if(boardTitle.isEmpty()){
                boardActivity.showMessageDialog("입력 오류", "제목을 입력해주세요", "확인"){
                    boardActivity.showSoftInput(textFieldBoardWriteTitle.editText!!)
                }
                return
            }

            if(boardText.isEmpty()){
                boardActivity.showMessageDialog("입력 오류", "내용을 입력해주세요", "확인"){
                    boardActivity.showSoftInput(textFieldBoardWriteText.editText!!)
                }
                return
            }

            // 업로드
            CoroutineScope(Dispatchers.Main).launch {
                // 이미지가 첨부되어 있다면
                if(isSetImageView){
                    // 서버상에서의 파일 이름
                    boardFileName = "image_${System.currentTimeMillis()}.jpg"
                    // 로컬에 ImageView에 있는 이미지 데이터를 저장한다.
                    boardActivity.saveImageView(fragmentBoardWriteBinding.imageViewBoardWrite)

                    val work1 = async(Dispatchers.IO){
                        BoardService.uploadImage("${boardActivity.filePath}/uploadTemp.jpg", boardFileName)
                    }
                    work1.join()
                }

                // 서버에 저장할 글 데이터
                val boardModel = BoardModel()
                boardModel.boardTypeValue = boardTypeValue
                boardModel.boardTitle = boardTitle
                boardModel.boardWriteId = boardWriteId
                boardModel.boardText = boardText
                boardModel.boardFileName = boardFileName
                boardModel.boardTimeStamp = boardTimeStamp
                // 저장한다.
                val work2 = async(Dispatchers.IO){
                    BoardService.addBoardData(boardModel)
                }
                val documentId = work2.await()
                // Log.d("test100", documentId)
                // 글을 보는 화면으로 이동한다.
                // 문서의 아이디를 전달한다.
                val dataBundle = Bundle()
                dataBundle.putString("boardDocumentId", documentId)
                boardMainFragment.replaceFragment(BoardSubFragmentName.BOARD_READ_FRAGMENT, true, true, dataBundle)
            }
        }
    }
}