package com.lion.boardproject.fragment

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialSharedAxis
import com.lion.boardproject.BoardActivity
import com.lion.boardproject.R
import com.lion.boardproject.UserActivity
import com.lion.boardproject.databinding.FragmentBoardMainBinding
import com.lion.boardproject.databinding.NavigationBoardMainHeaderBinding
import com.lion.boardproject.service.UserService
import com.lion.boardproject.util.BoardType
import com.lion.boardproject.util.UserState
import com.lion.boardproject.viewmodel.BoardMainViewModel
import com.lion.boardproject.viewmodel.NavigationBoardMainHeaderViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class BoardMainFragment : Fragment() {

    lateinit var fragmentBoardMainBinding:FragmentBoardMainBinding
    lateinit var boardActivity: BoardActivity

    // 현재 Fragment와 다음 Fragment를 담을 변수(애니메이션 이동 때문에...)
    var newFragment: Fragment? = null
    var oldFragment: Fragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentBoardMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_board_main, container, false)
        fragmentBoardMainBinding.boardMainViewModel = BoardMainViewModel(this@BoardMainFragment)
        fragmentBoardMainBinding.lifecycleOwner = this@BoardMainFragment

        boardActivity = activity as BoardActivity
        // 첫 프래그먼트 설정
        val dataBundle = Bundle()
        dataBundle.putInt("BoardType", BoardType.BOARD_TYPE_ALL.number)
        replaceFragment(BoardSubFragmentName.BOARD_LIST_FRAGMENT, false, false, dataBundle)

        // NavigationView를 구성하는 메서드호출
        settingNavigationView()

        return fragmentBoardMainBinding.root
    }

    // 프래그먼트를 교체하는 함수
    fun replaceFragment(fragmentName: BoardSubFragmentName, isAddToBackStack:Boolean, animate:Boolean, dataBundle: Bundle?){
        // newFragment가 null이 아니라면 oldFragment 변수에 담아준다.
        if(newFragment != null){
            oldFragment = newFragment
        }
        // 프래그먼트 객체
        newFragment = when(fragmentName){
            // 게시글 목록 화면
            BoardSubFragmentName.BOARD_LIST_FRAGMENT -> BoardListFragment(this@BoardMainFragment)
            // 게시글 작성 화면
            BoardSubFragmentName.BOARD_WRITE_FRAGMENT -> BoardWriteFragment(this@BoardMainFragment)
            // 게시글 읽는 화면
            BoardSubFragmentName.BOARD_READ_FRAGMENT -> BoardReadFragment(this@BoardMainFragment)
            // 게시글 수정 화면
            BoardSubFragmentName.BOARD_MODIFY_FRAGMENT -> BoardModifyFragment(this@BoardMainFragment)
            // 사용자 정보 수정 화면
            BoardSubFragmentName.USER_MODIFY_FRAGMENT -> UserModifyFragment(this@BoardMainFragment)
            // 댓글
            BoardSubFragmentName.COMMENT_FRAGMENT -> CommentFragment(this@BoardMainFragment)
        }

        // bundle 객체가 null이 아니라면
        if(dataBundle != null){
            newFragment?.arguments = dataBundle
        }

        // 프래그먼트 교체
        boardActivity.supportFragmentManager.commit {

            if(animate) {
                // 만약 이전 프래그먼트가 있다면
                if(oldFragment != null){
                    oldFragment?.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                    oldFragment?.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
                }

                newFragment?.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                newFragment?.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
                newFragment?.enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                newFragment?.returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
            }

            replace(R.id.fragmentContainerViewBoardMain, newFragment!!)
            if(isAddToBackStack){
                addToBackStack(fragmentName.str)
            }
        }
    }


    // 프래그먼트를 BackStack에서 제거하는 메서드
    fun removeFragment(fragmentName: BoardSubFragmentName){
        boardActivity.supportFragmentManager.popBackStack(fragmentName.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    // NavigationView를 구성하는 메서드
    fun settingNavigationView(){
        fragmentBoardMainBinding.apply {
            // Header 구성
            val navigationBoardMainHeaderBinding = DataBindingUtil.inflate<NavigationBoardMainHeaderBinding>(
                layoutInflater, R.layout.navigation_board_main_header, null, false
            )
            navigationBoardMainHeaderBinding.navigationBoardMainHeaderViewModel = NavigationBoardMainHeaderViewModel(this@BoardMainFragment)
            navigationBoardMainHeaderBinding.lifecycleOwner = this@BoardMainFragment

            // 닉네임 설정
            navigationBoardMainHeaderBinding.navigationBoardMainHeaderViewModel?.textViewNavigationBoardMainHeaderNickNameText?.value = "${boardActivity.loginUserNickName}님"

            navigationViewBoardMain.addHeaderView(navigationBoardMainHeaderBinding.root)

            // 메뉴 구성
            navigationViewBoardMain.inflateMenu(R.menu.menu_board_main_navigation)
            navigationViewBoardMain.setCheckedItem(R.id.menuItemBoardNavigationAll)
            navigationViewBoardMain.setNavigationItemSelectedListener {





                when(it.itemId){
                    // 전체 게시판
                    R.id.menuItemBoardNavigationAll -> {
                        val dataBundle = Bundle()
                        dataBundle.putInt("BoardType", BoardType.BOARD_TYPE_ALL.number)
                        replaceFragment(BoardSubFragmentName.BOARD_LIST_FRAGMENT, false, false, dataBundle)
                    }
                    // 자유 게시판
                    R.id.menuItemBoardNavigation1 -> {
                        val dataBundle = Bundle()
                        dataBundle.putInt("BoardType", BoardType.BOARD_TYPE_1.number)
                        replaceFragment(BoardSubFragmentName.BOARD_LIST_FRAGMENT, false, false, dataBundle)
                    }
                    // 유머 게시판
                    R.id.menuItemBoardNavigation2 -> {
                        val dataBundle = Bundle()
                        dataBundle.putInt("BoardType", BoardType.BOARD_TYPE_2.number)
                        replaceFragment(BoardSubFragmentName.BOARD_LIST_FRAGMENT, false, false, dataBundle)
                    }
                    // 시사 게시판
                    R.id.menuItemBoardNavigation3 -> {
                        val dataBundle = Bundle()
                        dataBundle.putInt("BoardType", BoardType.BOARD_TYPE_3.number)
                        replaceFragment(BoardSubFragmentName.BOARD_LIST_FRAGMENT, false, false, dataBundle)
                    }
                    // 운동 게시판
                    R.id.menuItemBoardNavigation4 -> {
                        val dataBundle = Bundle()
                        dataBundle.putInt("BoardType", BoardType.BOARD_TYPE_4.number)
                        replaceFragment(BoardSubFragmentName.BOARD_LIST_FRAGMENT, false, false, dataBundle)
                    }
                    // 사용자 정보 수정
                    R.id.menuItemBoardNavigationModifyUserInfo -> replaceFragment(BoardSubFragmentName.USER_MODIFY_FRAGMENT, false, false, null)
                    // 로그 아웃
                    R.id.menuItemBoardNavigationLogout -> {
                        logoutProcess()
                    }
                    // 회원 탈퇴
                    R.id.menuItemBoardNavigationSignOut -> {
                        signOutProcess()
                    }
                }

                drawerLayoutBoardMain.close()



                true
            }
        }
    }

    // NavigationView를 보여주는 메서드
    fun showNavigationView(){
        fragmentBoardMainBinding.drawerLayoutBoardMain.open()
    }

    // 로그아웃 처리 메서드
    fun logoutProcess(){
        val builder = MaterialAlertDialogBuilder(boardActivity)
        builder.setTitle("로그아웃")
        builder.setMessage("로그아웃 하시겠습니까?")
        builder.setNegativeButton("취소", null)
        builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
            // 자동 로그인을 위한 토큰값이 있다면 삭제한다.
            val pref = boardActivity.getSharedPreferences("LoginToken", Context.MODE_PRIVATE)
            pref.edit {
                remove("token")
            }

            // UserActivity를 실행한다.
            val userIntent = Intent(boardActivity, UserActivity::class.java)
            startActivity(userIntent)
            // 현재 Activity를 종료한다.
            boardActivity.finish()
        }
        builder.show()
    }

    // 탈퇴처리 메서드
    fun signOutProcess(){
        val builder = MaterialAlertDialogBuilder(boardActivity)
        builder.setTitle("회원탈퇴")
        builder.setMessage("회원 탈퇴 시 로그인할 수 없습니다")
        builder.setNegativeButton("취소", null)
        builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    UserService.updateUserState(boardActivity.loginUserDocumentId, UserState.USER_STATE_SIGNOUT)
                }
                work1.join()

                // 자동 로그인을 위한 토큰값이 있다면 삭제한다.
                val pref = boardActivity.getSharedPreferences("LoginToken", Context.MODE_PRIVATE)
                pref.edit {
                    remove("token")
                }

                // UserActivity를 실행한다.
                val userIntent = Intent(boardActivity, UserActivity::class.java)
                startActivity(userIntent)
                // 현재 Activity를 종료한다.
                boardActivity.finish()
            }
        }
        builder.show()
    }
}

// 하위 프래그먼트들의 이름
enum class BoardSubFragmentName(var number:Int, var str:String){
    // 게시글 목록 화면
    BOARD_LIST_FRAGMENT(1, "BoardListFragment"),
    // 게시글 작성 화면
    BOARD_WRITE_FRAGMENT(2, "BoardWriteFragment"),
    // 게시글 읽기  화면
    BOARD_READ_FRAGMENT(3, "BoardReadFragment"),
    // 게시글 수정 화면
    BOARD_MODIFY_FRAGMENT(4, "BoardModifyFragment"),
    // 사용자 정보 수정 화면
    USER_MODIFY_FRAGMENT(5, "UserModifyFragment"),
    // 댓글
    COMMENT_FRAGMENT(6, "CommentFragment"),
}
