package com.lion.boardproject.vo

import com.lion.boardproject.model.ReplyModel
import com.lion.boardproject.util.ReplyState

class ReplyVO {
    // 댓글 작성자 닉네임
    var replyNickName = ""
    // 댓글 내용
    var replyText = ""
    // 댓글이 달린 글 구분 값
    var replyBoardId = ""
    // 시간
    var replyTimeStamp = 0L
    // 상태
    var replyState = 0

    fun toReplyModel(replyDocumentId:String) : ReplyModel{
        val replyModel = ReplyModel()
        replyModel.replyDocumentId = replyDocumentId
        replyModel.replyNickName = replyNickName
        replyModel.replyText = replyText
        replyModel.replyBoardId = replyBoardId
        replyModel.replyTimeStamp = replyTimeStamp
        when(replyState){
            ReplyState.REPLY_STATE_NORMAL.number -> {
                replyModel.replyState = ReplyState.REPLY_STATE_NORMAL
            }
            ReplyState.REPLY_STATE_DELETE.number -> {
                replyModel.replyState = ReplyState.REPLY_STATE_DELETE
            }
        }
        return replyModel
    }
}