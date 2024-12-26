package com.lion.boardproject.model

import com.lion.boardproject.util.BoardState
import com.lion.boardproject.util.BoardType
import com.lion.boardproject.util.ReplyState
import com.lion.boardproject.vo.BoardVO
import com.lion.boardproject.vo.ReplyVO

class BoardModel {
    // 게시글 문서 id
    var boardDocumentId = ""
    // 게시판 구분값
    var boardTypeValue = BoardType.BOARD_TYPE_1
    // 게시글 제목
    var boardTitle = ""
    // 작성자 구분값
    var boardWriteId = ""
    // 게시글 내용
    var boardText = ""
    // 첨부 사진 파일 이름
    var boardFileName = ""
    // 댓글
    var boardReply = mutableListOf<ReplyModel>()
    // 시간
    var boardTimeStamp = 0L
    // 상태
    var boardState = BoardState.BOARD_STATE_NORMAL
    // 작성자 닉네임
    var boardWriterNickName = ""

    fun toBoardVO():BoardVO{
        val boardVO = BoardVO()
        boardVO.boardTypeValue = boardTypeValue.number
        boardVO.boardTitle = boardTitle
        boardVO.boardWriteId = boardWriteId
        boardVO.boardText = boardText
        boardVO.boardFileName = boardFileName
        boardVO.boardTimeStamp = boardTimeStamp
        boardVO.boardState = boardState.number

        boardVO.boardReply = mutableListOf()
        boardReply.forEach {
            val replyVO = it.toReplyVO()
            boardVO.boardReply.add(replyVO)
        }

        return boardVO
    }
}