package com.lion.boardproject.vo

import com.lion.boardproject.model.BoardModel
import com.lion.boardproject.util.BoardState
import com.lion.boardproject.util.BoardType

class BoardVO {
    // 게시판 구분값
    var boardTypeValue = 0
    // 게시글 제목
    var boardTitle = ""
    // 작성자 구분값
    var boardWriteId = ""
    // 게시글 내용
    var boardText = ""
    // 첨부 사진 파일 이름
    var boardFileName = ""
    // 댓글
    var boardReply = mutableListOf<ReplyVO>()
    // 시간
    var boardTimeStamp = 0L
    // 상태
    var boardState = 0

    fun toBoardModel(boardDocumentId:String) : BoardModel {
        val boardModel = BoardModel()

        boardModel.boardDocumentId = boardDocumentId
        boardModel.boardTitle = boardTitle
        boardModel.boardWriteId = boardWriteId
        boardModel.boardText = boardText
        boardModel.boardFileName = boardFileName
        boardModel.boardTimeStamp = boardTimeStamp

        when(boardTypeValue){
            BoardType.BOARD_TYPE_1.number -> boardModel.boardTypeValue = BoardType.BOARD_TYPE_1
            BoardType.BOARD_TYPE_2.number -> boardModel.boardTypeValue = BoardType.BOARD_TYPE_2
            BoardType.BOARD_TYPE_3.number -> boardModel.boardTypeValue = BoardType.BOARD_TYPE_3
            BoardType.BOARD_TYPE_4.number -> boardModel.boardTypeValue = BoardType.BOARD_TYPE_4
        }

        when(boardState){
            BoardState.BOARD_STATE_NORMAL.number -> boardModel.boardState = BoardState.BOARD_STATE_NORMAL
            BoardState.BOARD_STATE_DELETE.number -> boardModel.boardState = BoardState.BOARD_STATE_DELETE
        }

        boardModel.boardReply = mutableListOf()

        boardReply.forEach {
            val replyModel = it.toReplyModel("")
            boardModel.boardReply.add(replyModel)
        }

        return boardModel
    }
}