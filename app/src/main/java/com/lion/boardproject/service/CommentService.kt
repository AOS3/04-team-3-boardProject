package com.lion.boardproject.service

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.lion.boardproject.model.BoardModel
import com.lion.boardproject.model.ReplyModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

object CommentService {
    // 댓글 데이터 저장
    suspend fun addCommentData(replyModel: ReplyModel, boardModel: BoardModel ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val firestore = FirebaseFirestore.getInstance()
                val collectionReference = firestore.collection("comments")
                val documentReference = collectionReference.add(replyModel).await() // Firestore에 댓글 추가 후 DocumentReference 반환
                replyModel.replyDocumentId = documentReference.id
                // 댓글 문서의 업데이트를 파이어베이스에 반영
                documentReference.set(replyModel).await()

                val collectionReference2 = firestore.collection("BoardData")
                val documentReference2 = collectionReference2.document(boardModel.boardDocumentId)
                // boardModel.boardReply.add(replyModel)
                documentReference2.update("boardReply", boardModel.boardReply).await()

                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun getComments(boardDocumentId: String): List<ReplyModel> {
        return withContext(Dispatchers.IO) {
            val snapshot = Firebase.firestore.collection("comments")
                .whereEqualTo("replyBoardId", boardDocumentId)
                .get()
                .await()

            snapshot.mapNotNull { doc ->
                doc.toObject(ReplyModel::class.java)
            }
        }
    }


    // 댓글 삭제
    suspend fun deleteComment(commentId: String, boardModel: BoardModel): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val firestore = FirebaseFirestore.getInstance()
                val collectionReference = firestore.collection("comments")
                val documentReference = collectionReference.document(commentId)
                documentReference.delete().await()

                boardModel.boardReply.removeAll { it.replyDocumentId == commentId }
                val collectionReference2 = firestore.collection("BoardData")
                val documentReference2 = collectionReference2.document(boardModel.boardDocumentId)
                documentReference2.update("boardReply", boardModel.boardReply).await()

                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

}

