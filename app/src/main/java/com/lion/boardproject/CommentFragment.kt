package com.lion.boardproject.fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.util.copy
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.protobuf.Value
import com.lion.boardproject.BoardActivity
import com.lion.boardproject.R
import com.lion.boardproject.databinding.FragmentCommentBinding
import com.lion.boardproject.databinding.RowCommentBinding
import com.lion.boardproject.model.BoardModel
import com.lion.boardproject.model.ReplyModel
import com.lion.boardproject.service.BoardService
import com.lion.boardproject.service.CommentService
import com.lion.boardproject.service.CommentService.deleteComment
import com.lion.boardproject.util.ReplyState
import com.lion.boardproject.viewmodel.CommentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class CommentFragment(val boardMainFragment: BoardMainFragment) : BottomSheetDialogFragment() {

    lateinit var fragmentCommentBinding: FragmentCommentBinding
    lateinit var boardActivity: BoardActivity


    // 현재 글의 문서 id를 담을 변수
    lateinit var boardDocumentId:String


    var recyclerViewCommentList = mutableListOf<ReplyModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentCommentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_comment, container, false)
        fragmentCommentBinding.commentViewModel = CommentViewModel(this@CommentFragment)
        fragmentCommentBinding.lifecycleOwner = this@CommentFragment

        boardActivity = activity as BoardActivity


        gettingArguments()
        setupRecyclerView()
        setupAddCommentInput()
        loadComments()

        return fragmentCommentBinding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheet = (dialogInterface as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true
                behavior.isDraggable = true
            }
        }
        return dialog
    }


    // RecyclerView 구성 메서드
    fun setupRecyclerView() {
        fragmentCommentBinding.recyclerViewComments.adapter = CommentRecyclerViewAdapter()
        fragmentCommentBinding.recyclerViewComments.layoutManager = LinearLayoutManager(boardActivity)
        val deco = MaterialDividerItemDecoration(boardActivity, MaterialDividerItemDecoration.VERTICAL)
        fragmentCommentBinding.recyclerViewComments.addItemDecoration(deco)
    }

    // arguments의 값을 변수에 담아준다.
    fun gettingArguments(){
        boardDocumentId = arguments?.getString("boardDocumentId")!!
    }

    // 댓글 입력 버튼 누르면
    fun setupAddCommentInput() {
        fragmentCommentBinding.commentInput.setEndIconOnClickListener {
            val commentText = fragmentCommentBinding.commentInput.editText?.text.toString()
            if (commentText.isNotEmpty()) {
                fragmentCommentBinding.commentInput.editText?.setText("")
                addComment(commentText)
            } else {
                Toast.makeText(context, "댓글을 입력하시오", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun addComment(commentText: String) {
        fragmentCommentBinding.apply {
            val commentNick = boardActivity.loginUserNickName
            val boardTimeStamp = System.nanoTime()

            CoroutineScope(Dispatchers.IO).launch {
                val replyModel = ReplyModel().apply {
                    replyNickName = commentNick
                    replyText = commentText
                    replyBoardId = boardDocumentId
                    replyTimeStamp = boardTimeStamp
                    replyState = ReplyState.REPLY_STATE_NORMAL
                }

                // 게시글 데이터 가져오기
                val boardModel = BoardService.selectBoardDataOneById(boardDocumentId)


                withContext(Dispatchers.Main) {

                    val result =
                        CommentService.addCommentData(replyModel, boardModel)

                    if (result) {
                        Toast.makeText(context, "댓글이 추가되었습니다.", Toast.LENGTH_SHORT).show()
                        loadComments() // 댓글 추가 후 다시 데이터 로드
                    } else {
                        Toast.makeText(context, "댓글 추가에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }



    fun loadComments() {
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                CommentService.getComments(boardDocumentId) // 댓글 가져오는 서비스 호출
            }
            recyclerViewCommentList = work1.await().toMutableList()
            Log.d("CommentFragment", "Loaded comments: ${recyclerViewCommentList.size}")
            fragmentCommentBinding.recyclerViewComments.adapter?.notifyDataSetChanged()
        }
    }


    private fun showDeleteCommentDialog(commentId: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("댓글을 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                deleteComment(commentId)
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun deleteComment(commentId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val boardModel = BoardService.selectBoardDataOneById(boardDocumentId)

            val result = CommentService.deleteComment(commentId, boardModel)

            if (result) {
                withContext(Dispatchers.Main) {
                    loadComments()
                    Toast.makeText(context, "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "댓글 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    // RecyclerView의 어뎁터
    inner class CommentRecyclerViewAdapter : RecyclerView.Adapter<CommentRecyclerViewAdapter.CommentViewHolder>() {
        inner class CommentViewHolder(val rowCommentBinding: RowCommentBinding) : RecyclerView.ViewHolder(rowCommentBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
            val rowCommentBinding = DataBindingUtil.inflate<RowCommentBinding>(
                layoutInflater, R.layout.row_comment, parent, false
            )
            rowCommentBinding.commentViewModel = CommentViewModel(this@CommentFragment)
            rowCommentBinding.lifecycleOwner = this@CommentFragment

            return CommentViewHolder(rowCommentBinding)
        }

        override fun getItemCount(): Int {
            return recyclerViewCommentList.size
            Log.d("CommentFragment", "RecyclerView size: ${recyclerViewCommentList.size}")

        }

        override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
            val replyModel = recyclerViewCommentList[position]

            holder.rowCommentBinding.commentViewModel?.textViewCommentUser?.value = recyclerViewCommentList[position].replyNickName
            holder.rowCommentBinding.commentViewModel?.textViewCommentText?.value = recyclerViewCommentList[position].replyText

            // 길게 누르면 삭제
            holder.rowCommentBinding.root.setOnLongClickListener {

                if (replyModel.replyNickName == boardActivity.loginUserNickName) {
                    showDeleteCommentDialog(replyModel.replyDocumentId ?: "")
                } else {
                    Toast.makeText(context, "본인이 작성한 댓글만 삭제할 수 있습니다.", Toast.LENGTH_SHORT).show()
                }
                true
            }
        }
    }
}
