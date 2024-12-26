package com.lion.boardproject.model

import com.lion.boardproject.util.UserState
import com.lion.boardproject.vo.UserVO

class UserModel {
    // 문서 ID
    var userDocumentId = ""
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
    var userTimeStamp = 0L
    // 상태값
    var userState = UserState.USER_STATE_NORMAL

    fun toUserVO() : UserVO{
        val userVO = UserVO()
        userVO.userId = userId
        userVO.userPw = userPw
        userVO.userAutoLoginToken = userAutoLoginToken
        userVO.userNickName = userNickName
        userVO.userAge = userAge
        userVO.userHobby1 = userHobby1
        userVO.userHobby2 = userHobby2
        userVO.userHobby3 = userHobby3
        userVO.userHobby4 = userHobby4
        userVO.userHobby5 = userHobby5
        userVO.userHobby6 = userHobby6
        userVO.userTimeStamp = userTimeStamp
        userVO.userState = userState.number

        return userVO
    }
}