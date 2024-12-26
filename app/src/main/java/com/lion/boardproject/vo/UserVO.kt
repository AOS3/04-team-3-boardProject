package com.lion.boardproject.vo

import com.lion.boardproject.model.UserModel
import com.lion.boardproject.util.UserState

class UserVO(){
    // 아이디
    var userId = ""
    // 비밀번호
    var userPw = ""
    // 자동 로그인 토큰
    var userAutoLoginToken = ""
    // 닉네임
    var userNickName = ""
    // 나이
    var userAge = 0
    // 게임
    var userHobby1 = false
    // 독서
    var userHobby2 = false
    // 요리
    var userHobby3 = false
    //낚시
    var userHobby4 = false
    // 영화감상
    var userHobby5 = false
    // 기타
    var userHobby6 = false
    // 시간
    var userTimeStamp:Long = 0
    // 상태값
    var userState = 0

    fun toUserModel(userDocumentId:String) : UserModel{
        val userModel = UserModel()

        userModel.userDocumentId = userDocumentId
        userModel.userId = userId
        userModel.userPw = userPw
        userModel.userAutoLoginToken = userAutoLoginToken
        userModel.userNickName = userNickName
        userModel.userAge = userAge
        userModel.userHobby1 = userHobby1
        userModel.userHobby2 = userHobby2
        userModel.userHobby3 = userHobby3
        userModel.userHobby4 = userHobby4
        userModel.userHobby5 = userHobby5
        userModel.userHobby6 = userHobby6
        userModel.userTimeStamp = userTimeStamp

        when(userState){
            UserState.USER_STATE_NORMAL.number -> userModel.userState = UserState.USER_STATE_NORMAL
            UserState.USER_STATE_SIGNOUT.number -> userModel.userState = UserState.USER_STATE_SIGNOUT
        }

        return userModel
    }
}