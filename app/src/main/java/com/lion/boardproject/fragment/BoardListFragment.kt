package com.lion.boardproject.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.search.SearchView
import com.lion.boardproject.BoardActivity
import com.lion.boardproject.R
import com.lion.boardproject.databinding.FragmentBoardListBinding
import com.lion.boardproject.databinding.RowBoardListBinding
import com.lion.boardproject.model.BoardModel
import com.lion.boardproject.repository.BoardRepository
import com.lion.boardproject.service.BoardService
import com.lion.boardproject.util.BoardType
import com.lion.boardproject.viewmodel.BoardListViewModel
import com.lion.boardproject.viewmodel.RowBoardListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class BoardListFragment(val boardMainFragment: BoardMainFragment) : Fragment() {

    lateinit var fragmentBoardListBinding: FragmentBoardListBinding
    lateinit var boardActivity: BoardActivity

    // 게시판 타입값을 담을 변수
    lateinit var boardType:BoardType

//    // ReyclerView 구성을 위한 임시 데이터
//    val tempList1 = Array(100) {
//        "글제목 ${it + 1}"
//    }
//    val tempList2 = Array(100){
//        "닉네임 ${it + 1}"
//    }

    // 메인 RecyclerView를 구성하기 위해 사용할 리스트
    var recyclerViewList = mutableListOf<BoardModel>()
    // 검색 RecyclerView를 구성하기 위해 사용할 리스트
    var recyclerViewSearchList = mutableListOf<BoardModel>()
    // 현재 SearchView가 보여지고 있는지.
    var isShownSearchView = false
    // 검색어를 담을 변수
    var searchKeyword = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentBoardListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_board_list, container, false)
        fragmentBoardListBinding.boardListViewModel = BoardListViewModel(this@BoardListFragment)
        fragmentBoardListBinding.lifecycleOwner = this@BoardListFragment

        boardActivity = activity as BoardActivity

        // RecyclerView 구성을 위한 리스트를 초기화한다.
        recyclerViewList.clear()

        // 게시판 타입 값을 담는 메서드를 호출한다.
        settingBoardType()
        // 툴바를 구성하는 메서드를 호출한다.
        settingToolbar()
        // SearchBar를 구성하는 메서드
        settingSearchBar()
        // SearchView 셋팅 메서드
        settingSearchView()
        // 메인 RecyclerView 구성 메서드를 호출한다.
        settingMainRecyclerView()
        // 검색 결과 RecyclerView 구성 메서드를 호출한다.
        settingResultRecyclerView()
        // 데이터를 가져와 MainRecyclerView를 갱신하는 메서드를 호출한다.
        refreshMainRecyclerView()

        // 만약 검색화면이 보여지고 있는 상태라면..
        if(isShownSearchView == true){
            refreshSearchRecyclerView()
        }


        return fragmentBoardListBinding.root
    }
    
    // 툴바를 구성하는 메서드
    fun settingToolbar(){
        // 타이틀
        fragmentBoardListBinding.boardListViewModel?.toolbarBoardListTitle?.value = boardType.str
    }

    // SearchBar를 구성하는 메서드
    fun settingSearchBar(){
        fragmentBoardListBinding.apply {
            searchBarBoardList.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.menuItemBoardListAdd -> {
                        // 글 작성 화면으로 이동한다.
                        boardMainFragment.replaceFragment(BoardSubFragmentName.BOARD_WRITE_FRAGMENT, true, true, null)
                    }
                }
                true
            }
        }
    }

    // 검색 결과를 가져와 RecyclerView를 갱신하는 메서드
    fun refreshSearchRecyclerView(){
        fragmentBoardListBinding.apply {
            // 검색어
            // val keyword = searchViewBoardList.editText.text.toString()
            // 데이터를 가져온다.
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    BoardService.gettingBoardSearchList(boardType, searchKeyword)
                }
                recyclerViewSearchList = work1.await()
                recyclerViewBoardListResult.adapter?.notifyDataSetChanged()
            }
        }
    }

    // SearchView 셋팅 메서드
    fun settingSearchView(){
        fragmentBoardListBinding.apply {
            // SerachView의 입력 창에 대한 엔터키 이벤트
            searchViewBoardList.editText.setOnEditorActionListener { v, actionId, event ->
                // 사용자가 입력한 검색어를 가져와 변수에 담아둔다.
                searchKeyword = searchViewBoardList.editText.text.toString()
                // 검색 결과를 가져와 RecyclerView를 갱신하는 메서드를 호출한다.
                refreshSearchRecyclerView()
                true
            }

            // SearchView 노출에 대한 이벤트
            searchViewBoardList.addTransitionListener { searchView, previousState, newState ->

                when(newState){
                    // 보이기 전
                    SearchView.TransitionState.SHOWING -> {
                        isShownSearchView = true
                        recyclerViewSearchList.clear()
                    }
                    // 보이고난 후
                    SearchView.TransitionState.SHOWN -> {

                    }
                    // 사라지기 전
                    SearchView.TransitionState.HIDING -> {

                    }
                    // 사라지고 난 후
                    SearchView.TransitionState.HIDDEN -> {
                        isShownSearchView = false
                        recyclerViewSearchList.clear()
                        recyclerViewBoardListResult.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    // 메인 RecyclerView 구성 메서드
    fun settingMainRecyclerView(){
        fragmentBoardListBinding.apply {
            recyclerViewBoardListMain.adapter = MainRecyclerViewAdapter()
            recyclerViewBoardListMain.layoutManager = LinearLayoutManager(boardActivity)
            val deco = MaterialDividerItemDecoration(boardActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewBoardListMain.addItemDecoration(deco)
        }
    }

    // 검색 결과 RecyclerView 구성 메서드
    fun settingResultRecyclerView(){
        fragmentBoardListBinding.apply {
            recyclerViewBoardListResult.adapter = ResultRecyclerViewAdapter()
            recyclerViewBoardListResult.layoutManager = LinearLayoutManager(boardActivity)
            val deco = MaterialDividerItemDecoration(boardActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewBoardListResult.addItemDecoration(deco)
        }
    }

    // 데이터를 가져와 MainRecyclerView를 갱신하는 메서드
    fun refreshMainRecyclerView(){
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                BoardService.gettingBoardList(boardType)
            }
            recyclerViewList = work1.await()

            fragmentBoardListBinding.recyclerViewBoardListMain.adapter?.notifyDataSetChanged()
        }
    }

    // 게시판 타입 값을 담는 메서드
    fun settingBoardType(){
        val tempType = arguments?.getInt("BoardType")!!
        when(tempType){
            BoardType.BOARD_TYPE_ALL.number -> {
                boardType = BoardType.BOARD_TYPE_ALL
            }
            BoardType.BOARD_TYPE_1.number -> {
                boardType = BoardType.BOARD_TYPE_1
            }
            BoardType.BOARD_TYPE_2.number -> {
                boardType = BoardType.BOARD_TYPE_2
            }
            BoardType.BOARD_TYPE_3.number -> {
                boardType = BoardType.BOARD_TYPE_3
            }
            BoardType.BOARD_TYPE_4.number -> {
                boardType = BoardType.BOARD_TYPE_4
            }
        }
    }


    // 메인 RecyclerView의 어뎁터
    inner class MainRecyclerViewAdapter : RecyclerView.Adapter<MainRecyclerViewAdapter.MainViewHolder>(){
        inner class MainViewHolder(val rowBoardListBinding: RowBoardListBinding) : RecyclerView.ViewHolder(rowBoardListBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val rowBoardListBinding = DataBindingUtil.inflate<RowBoardListBinding>(layoutInflater, R.layout.row_board_list, parent, false)
            rowBoardListBinding.rowBoardListViewModel = RowBoardListViewModel(this@BoardListFragment)
            rowBoardListBinding.lifecycleOwner = this@BoardListFragment

            val mainViewHolder = MainViewHolder(rowBoardListBinding)

            rowBoardListBinding.root.setOnClickListener {
                // 사용자가 누른 항목의 게시글 문서 번호를 담아서 전달한다.
                val dataBundle = Bundle()
                dataBundle.putString("boardDocumentId", recyclerViewList[mainViewHolder.adapterPosition].boardDocumentId)
                boardMainFragment.replaceFragment(BoardSubFragmentName.BOARD_READ_FRAGMENT, true, true, dataBundle)
            }

            return mainViewHolder
        }

        override fun getItemCount(): Int {
            return recyclerViewList.size
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            holder.rowBoardListBinding.rowBoardListViewModel?.textViewRowBoardListTitleText?.value = recyclerViewList[position].boardTitle
            holder.rowBoardListBinding.rowBoardListViewModel?.textViewRowBoardListNickNameText?.value = recyclerViewList[position].boardWriterNickName
        }
    }

    // 검색결과 RecyclerView의 어뎁터
    inner class ResultRecyclerViewAdapter : RecyclerView.Adapter<ResultRecyclerViewAdapter.ResultViewHolder>(){
        inner class ResultViewHolder(val rowBoardListBinding: RowBoardListBinding) : RecyclerView.ViewHolder(rowBoardListBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
            val rowBoardListBinding = DataBindingUtil.inflate<RowBoardListBinding>(layoutInflater, R.layout.row_board_list, parent, false)
            rowBoardListBinding.rowBoardListViewModel = RowBoardListViewModel(this@BoardListFragment)
            rowBoardListBinding.lifecycleOwner = this@BoardListFragment

            val resultViewHolder = ResultViewHolder(rowBoardListBinding)

            rowBoardListBinding.root.setOnClickListener {
                // 사용자가 누른 항목의 게시글 문서 번호를 담아서 전달한다.
                val dataBundle = Bundle()
                dataBundle.putString("boardDocumentId", recyclerViewSearchList[resultViewHolder.adapterPosition].boardDocumentId)
                boardMainFragment.replaceFragment(BoardSubFragmentName.BOARD_READ_FRAGMENT, true, true, dataBundle)
            }

            return resultViewHolder
        }

        override fun getItemCount(): Int {
            return recyclerViewSearchList.size
        }

        override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
            holder.rowBoardListBinding.rowBoardListViewModel?.textViewRowBoardListTitleText?.value = recyclerViewSearchList[position].boardTitle
            holder.rowBoardListBinding.rowBoardListViewModel?.textViewRowBoardListNickNameText?.value = recyclerViewSearchList[position].boardWriterNickName
        }
    }
}