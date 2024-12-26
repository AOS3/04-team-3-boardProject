package com.lion.boardproject.util

// 게시판 구분 값
enum class BoardType(val number:Int, val str:String){
    // 전체게시판
    BOARD_TYPE_ALL(0, "전체게시판"),
    // 자유게시판
    BOARD_TYPE_1(1, "자유게시판"),
    // 유머게시판
    BOARD_TYPE_2(2, "유머게시판"),
    // 시사게시판
    BOARD_TYPE_3(3, "시사게시판"),
    // 운동게시판
    BOARD_TYPE_4(4, "운동게시판")
}

// 사용자 상태
enum class UserState(val number:Int, val str:String){
    // 정상
    USER_STATE_NORMAL(1, "정상"),
    // 탈퇴
    USER_STATE_SIGNOUT(2, "탈퇴")
}

// 게시글 상태
enum class BoardState(val number:Int, val str:String){
    // 정상
    BOARD_STATE_NORMAL(1, "정상"),
    // 삭제
    BOARD_STATE_DELETE(2, "삭제"),
}

// 댓글 상태
enum class ReplyState(val number:Int, val str:String){
    // 정상
    REPLY_STATE_NORMAL(1, "정상"),
    // 삭제
    REPLY_STATE_DELETE(2, "삭제")
}

// 로그인 결과
enum class LoginResult(val number:Int, val str:String){
    LOGIN_RESULT_SUCCESS(1, "로그인 성공"),
    LOGIN_RESULT_ID_NOT_EXIST(2, "존재하지 않는 아이디"),
    LOGIN_RESULT_PASSWORD_INCORRECT(3, "잘못된 비밀번호"),
    LOGIN_RESULT_SIGNOUT_MEMBER(4, "탈퇴한 회원"),
}

