package com.lion.boardproject.model

import com.lion.boardproject.util.BoardType
import com.lion.boardproject.vo.TypeVO

class TypeModel {
    // 게시판 구분값
    var typeValue = BoardType.BOARD_TYPE_1
    // 게시판 이름
    var typeName = ""

    fun toTypeVO() : TypeVO{
        val typeVO = TypeVO()
        typeVO.typeValue = typeValue.number
        typeVO.typeName = typeName

        return typeVO
    }
}