package com.lion.boardproject.vo

import com.lion.boardproject.model.TypeModel
import com.lion.boardproject.util.BoardType

class TypeVO {
    // 게시판 구분값
    var typeValue = 0
    // 게시판 이름
    var typeName = ""

    fun toTypeModel() : TypeModel{
        val typeModel = TypeModel()

        when(typeValue){
            BoardType.BOARD_TYPE_1.number -> typeModel.typeValue = BoardType.BOARD_TYPE_1
            BoardType.BOARD_TYPE_2.number -> typeModel.typeValue = BoardType.BOARD_TYPE_2
            BoardType.BOARD_TYPE_3.number -> typeModel.typeValue = BoardType.BOARD_TYPE_3
            BoardType.BOARD_TYPE_4.number -> typeModel.typeValue = BoardType.BOARD_TYPE_4
        }

        typeModel.typeName = typeName

        return typeModel
    }
}