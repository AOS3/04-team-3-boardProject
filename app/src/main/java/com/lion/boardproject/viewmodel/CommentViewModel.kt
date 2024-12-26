package com.lion.boardproject.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.lion.boardproject.fragment.CommentFragment
import com.lion.boardproject.model.ReplyModel

class CommentViewModel(val commentFragment: CommentFragment) : ViewModel() {

    var textViewCommentUser = MutableLiveData("")
    var textViewCommentText = MutableLiveData("")
}
