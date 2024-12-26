package com.lion.boardproject.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.lion.boardproject.util.BoardType
import com.lion.boardproject.vo.BoardVO
import kotlinx.coroutines.tasks.await
import java.io.File

class BoardRepository {

    companion object{
        // 이미지 데이터를 서버로 업로드 하는 메서드
        suspend fun uploadImage(sourceFilePath:String, serverFilePath:String){
            // 저장되어 있는 이미지의 경로
            val file = File(sourceFilePath)
            val fileUri = Uri.fromFile(file)
            // 업로드 한다.
            val firebaseStorage = FirebaseStorage.getInstance()
            val childReference = firebaseStorage.reference.child("image/$serverFilePath")
            childReference.putFile(fileUri).await()
        }

        // 글 데이터를 저장하는 메서드
        // 새롭게 추가된 문서의 id를 반환한다.
        suspend fun addBoardData(boardVO:BoardVO) : String{
            val fireStore = FirebaseFirestore.getInstance()
            val collectionReference = fireStore.collection("BoardData")
            val documentReference = collectionReference.add(boardVO).await()
            return documentReference.id
        }

        // 글의 문서 id를 통해 글 데이터를 가져온다.
        suspend fun selectBoardDataOneById(documentId:String) : BoardVO{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("BoardData")
            val documentReference = collectionReference.document(documentId)
            val documentSnapShot = documentReference.get().await()
            val boardVO = documentSnapShot.toObject(BoardVO::class.java)!!
            return boardVO
        }

        // 이미지 데이터를 가져온다.
        suspend fun gettingImage(imageFileName:String) : Uri{
            val storageReference = FirebaseStorage.getInstance().reference
            // 파일명을 지정하여 이미지 데이터를 가져온다.
            val childStorageReference = storageReference.child("image/$imageFileName")
            val imageUri = childStorageReference.downloadUrl.await()
            return imageUri
        }

        // 글 목록을 가져오는 메서드
        suspend fun gettingBoardList(boardType:BoardType) : MutableList<Map<String, *>>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("BoardData")
            // 데이터를 가져온다.
            val result = if(boardType == BoardType.BOARD_TYPE_ALL){
                collectionReference.orderBy("boardTimeStamp", Query.Direction.DESCENDING).get().await()
            } else {
                collectionReference.whereEqualTo("boardTypeValue", boardType.number)
                                   .orderBy("boardTimeStamp", Query.Direction.DESCENDING).get().await()
            }
            // 반환할 리스트
            val resultList = mutableListOf<Map<String, *>>()
            // 데이터의 수 만큼 반환한다.
            result.forEach {
                val map = mapOf(
                    // 문서의 id
                    "documentId" to it.id,
                    // 데이터를 가지고 있는 객체
                    "boardVO" to it.toObject(BoardVO::class.java)
                )
                resultList.add(map)
            }
            return resultList
        }

        // 글정보를 수정하는 메서드
        suspend fun updateBoardData(boardVO: BoardVO, boardDocumentId:String){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("BoardData")
            val documentReference = collectionReference.document(boardDocumentId)

            // 수정할 데이터를 맵에 담는다
            // 데이터의 이름을 필드의 이름으로 해준다.
            val updateMap = mapOf(
                "boardTitle" to boardVO.boardTitle,
                "boardText" to boardVO.boardText,
                "boardTypeValue" to boardVO.boardTypeValue,
                "boardFileName" to boardVO.boardFileName
            )
            // 수정한다.
            documentReference.update(updateMap).await()
        }

        // 서버에서 이미지 파일을 삭제한다.
        suspend fun removeImageFile(imageFileName:String){
            val imageReference = FirebaseStorage.getInstance().reference.child("image/$imageFileName")
            imageReference.delete().await()
        }

        // 서버에서 글을 삭제한다.
        suspend fun deleteBoardData(boardDocumentId:String){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("BoardData")
            val documentReference = collectionReference.document(boardDocumentId)
            documentReference.delete().await()
        }
    }
}