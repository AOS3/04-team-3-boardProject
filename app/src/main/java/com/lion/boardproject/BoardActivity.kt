package com.lion.boardproject

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialSharedAxis
import com.lion.boardproject.databinding.ActivityBoardBinding
import com.lion.boardproject.fragment.BoardMainFragment
import com.lion.boardproject.fragment.BoardModifyFragment
import com.lion.boardproject.fragment.BoardWriteFragment
import java.io.File
import java.io.FileOutputStream
import kotlin.concurrent.thread


class BoardActivity : AppCompatActivity() {

    lateinit var activityBoardBinding: ActivityBoardBinding

    // 현재 Fragment와 다음 Fragment를 담을 변수(애니메이션 이동 때문에...)
    var newFragment: Fragment? = null
    var oldFragment: Fragment? = null

    // 카메라나 앨범을 사용하는 Fragment를 받을 변수
    var pictureFragment:Fragment? = null
    // 촬영된 사진이 위치할 경로
    lateinit var filePath:String
    // 저장된 파일에 접근하기 위한 Uri
    lateinit var contentUri:Uri
    // 사진 촬영을 위한 런처
    lateinit var cameraLauncher:ActivityResultLauncher<Intent>
    // 앨범을 위한 런처
    lateinit var albumLauncher:ActivityResultLauncher<PickVisualMediaRequest>

    // 사용자 문서 id와 닉네임을 받을 변수
    var loginUserDocumentId = ""
    var loginUserNickName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        activityBoardBinding = DataBindingUtil.setContentView(this@BoardActivity, R.layout.activity_board)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 외부 저장소 경로를 가져온다.
        filePath = getExternalFilesDir(null).toString()

        // 사용자 문서 id와 닉네임을 받는다.
        loginUserDocumentId = intent.getStringExtra("user_document_id")!!
        loginUserNickName = intent.getStringExtra("user_nick_name")!!

        // 카메라 실행을 위한 런처를 구성하는 메서드를 호출한다.
        settingCameraLauncher()

        // 앨범에서 사진을 가져오는 런처 구성하는 메서드를 호출한다.
        settingAlbumLauncher()

        // 첫 프래그먼트를 보여준다.
        replaceFragment(BoardFragmentName.BOARD_MAIN_FRAGMENT, false, false, null)
    }


    // 프래그먼트를 교체하는 함수
    fun replaceFragment(fragmentName: BoardFragmentName, isAddToBackStack:Boolean, animate:Boolean, dataBundle: Bundle?){
        // newFragment가 null이 아니라면 oldFragment 변수에 담아준다.
        if(newFragment != null){
            oldFragment = newFragment
        }
        // 프래그먼트 객체
        newFragment = when(fragmentName){
            // 게시판 메인 화면
            BoardFragmentName.BOARD_MAIN_FRAGMENT -> BoardMainFragment()
        }

        // bundle 객체가 null이 아니라면
        if(dataBundle != null){
            newFragment?.arguments = dataBundle
        }

        // 프래그먼트 교체
        supportFragmentManager.commit {

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

            replace(R.id.fragmentContainerViewBoard, newFragment!!)
            if(isAddToBackStack){
                addToBackStack(fragmentName.str)
            }
        }
    }

    // 프래그먼트를 BackStack에서 제거하는 메서드
    fun removeFragment(fragmentName: BoardFragmentName){
        supportFragmentManager.popBackStack(fragmentName.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    // 다이얼로그를 통해 메시지를 보여주는 함수
    fun showMessageDialog(title:String, message:String, posTitle:String, callback:()-> Unit){
        val builder = MaterialAlertDialogBuilder(this@BoardActivity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(posTitle){ dialogInterface: DialogInterface, i: Int ->
            callback()
        }
        builder.show()
    }

    // 키보드 올리는 메서드
    fun showSoftInput(view: View){
        // 입력을 관리하는 매니저
        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        // 포커스를 준다.
        view.requestFocus()

        thread {
            SystemClock.sleep(500)
            // 키보드를 올린다.
            inputManager.showSoftInput(view, 0)
        }
    }
    // 키보드를 내리는 메서드
    fun hideSoftInput(){
        // 포커스가 있는 뷰가 있다면
        if(currentFocus != null){
            // 입력을 관리하는 매니저
            val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            // 키보드를 내린다.
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            // 포커스를 해제한다.
            currentFocus?.clearFocus()
        }
    }


    // 이미지를 회전시키는 메서드
    fun rotateBitmap(bitmap: Bitmap, degree:Int): Bitmap {
        // 회전 이미지를 구하기 위한 변환 행렬
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        // 회전 행렬을 적용하여 회전된 이미지를 생성한다.
        val resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
        return resultBitmap
    }

    // 회전 각도값을 구하는 메서드
    fun getDegree(uri: Uri):Int{

        // 이미지의 태그 정보에 접근할 수 있는 객체를 생성한다.
        // andorid 10 버전 이상이라면
        val exifInterface = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            // 이미지 데이터를 가져올 수 있는 Content Provider의 Uri를 추출한다.
            val photoUri = MediaStore.setRequireOriginal(uri)
            // 컨텐츠 프로바이더를 통해 파일에 접근할 수 있는 스트림을 추출한다.
            val inputStream = contentResolver.openInputStream(photoUri)
            // ExifInterface 객체를 생성한다.
            ExifInterface(inputStream!!)
        } else {
            ExifInterface(uri.path!!)
        }

        // ExifInterface 정보 중 회전 각도 값을 가져온다
        val ori = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)

        // 회전 각도값을 담는다.
        val degree = when(ori){
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }

        return degree
    }

    // 이미지의 사이즈를 줄이는 메서드
    fun resizeBitmap(targetWidth:Int, bitmap:Bitmap):Bitmap{
        // 이미지의 축소/확대 비율을 구한다.
        val ratio = targetWidth.toDouble() / bitmap.width.toDouble()
        // 세로 길이를 구한다.
        val targetHeight = (bitmap.height.toDouble() * ratio).toInt()
        // 크기를 조절한 Bitmap 객체를 생성한다.
        val result = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false)
        return result
    }

    // 카메라 실행을 위한 런처를 구성하는 메서드
    fun settingCameraLauncher(){
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            // 사진을 찍고 돌아왔다면
            if(it.resultCode == RESULT_OK){
                // uri를 통해 저장된 사진 데이터를 가져온다.
                val bitmap = BitmapFactory.decodeFile(contentUri.path)
                // 이미지의 회전 각도값을 가져온다.
                val degree = getDegree(contentUri)
                // 회전 값을 이용해 이미지를 회전시킨다.
                val rotateBitmap = rotateBitmap(bitmap, degree)
                // 크기를 조정한 이미지를 가져온다.
                val resizeBitmap = resizeBitmap(1024, rotateBitmap)

                // 현재 프래그먼트가 무엇인지 분기한다.
                if(pictureFragment != null){
                    // 글을 작성하는 Fragment라면
                    if(pictureFragment is BoardWriteFragment){
                        val f1 = pictureFragment as BoardWriteFragment
                        // 이미지 뷰에 설정해준다.
                        f1.fragmentBoardWriteBinding.imageViewBoardWrite.setImageBitmap(resizeBitmap)
                        f1.isSetImageView = true
                    }
                    // 글을 수정하는 Fragment 라면
                    else if(pictureFragment is BoardModifyFragment){
                        val f1 = pictureFragment as BoardModifyFragment
                        f1.fragmentBoardModifyBinding.imageViewBoardModify.setImageBitmap(resizeBitmap)
                        f1.isModifyBitmap = true
                    }
                }

                // 사진 파일은 삭제한다.
                val file = File(contentUri.path!!)
                file.delete()
            }
        }
    }

    // 카메라를 실행시키는 메서드
    fun startCameraLauncher(fragment:Fragment){
        pictureFragment = fragment

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // 촬영한 사진이 저장될 파일 이름
        val fileName = "/temp_${System.currentTimeMillis()}.jpg"
        // 경로 + 파일이름
        val picPath = "${filePath}${fileName}"
        val file = File(picPath)

        // 사진이 저장될 위치를 관리하는 Uri 객체를 생성
        contentUri = FileProvider.getUriForFile(this@BoardActivity, "com.lion.boardproject.camera", file)
        // Activity를 실행한다.
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
        cameraLauncher.launch(cameraIntent)
    }

    // 앨범에서 사진을 가져오는 런처 구성한다.
    fun settingAlbumLauncher(){
        // PhotoPicker를 실행할 수 있도록 런처를 구성해준다.
        albumLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
            // 가져온 사진이 있다면
            if(it != null){
                var bitmap:Bitmap? = null

                // android 10 버전 이상이라면
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    // 이미지 객체를 생성할 수 있는 디코드를 생성한다.
                    val source = ImageDecoder.createSource(contentResolver, it)
                    // Bitmap 객체를 생성한다.
                    bitmap = ImageDecoder.decodeBitmap(source)
                } else {
                    // ContentProvider를 통해 사진 데이터를 가져온다.
                    val cursor = contentResolver.query(it, null, null, null, null)
                    if(cursor != null){
                        cursor.moveToNext()

                        // 이미지의 경로를 가져온다.
                        val idx = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                        val source = cursor.getString(idx)

                        // 이미지를 생성한다.
                        bitmap = BitmapFactory.decodeFile(source)
                    }
                }

//                // 이미지의 회전 각도값을 가져온다.
//                 val degree = getDegree(Uri.parse(source))
//                 //회전 값을 이용해 이미지를 회전시킨다.
//                 val rotateBitmap = rotateBitmap(bitmap!!, degree)
                // 크기를 조정한 이미지를 가져온다.
                 val resizeBitmap = resizeBitmap(1024, bitmap!!)

                // 현재 프래그먼트가 무엇인지 분기한다.
                if(pictureFragment != null){
                    // 글을 작성하는 Fragment라면
                    if(pictureFragment is BoardWriteFragment){
                        val f1 = pictureFragment as BoardWriteFragment
                        // 이미지 뷰에 설정해준다.
                        f1.fragmentBoardWriteBinding.imageViewBoardWrite.setImageBitmap(resizeBitmap)
                        f1.isSetImageView = true
                    }
                    // 글을 수정하는 Fragment 라면
                    else if(pictureFragment is BoardModifyFragment){
                        val f1 = pictureFragment as BoardModifyFragment
                        f1.fragmentBoardModifyBinding.imageViewBoardModify.setImageBitmap(resizeBitmap)
                        f1.isModifyBitmap = true
                    }
                }
            }
        }
    }

    fun startAlbumLauncher(fragment:Fragment){
        pictureFragment = fragment

        // Activity를 실행한다.
        val pickVisualMediaRequest = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        albumLauncher.launch(pickVisualMediaRequest)
    }

    // Activity에서 터치가 발생하면 호출되는 메서드
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        // 만약 포커스가 주어진 View가 있다면
        if(currentFocus != null){
            // 현재 포커스가 주어진 View의 화면상의 영역 정보를 가져온다.
            val rect = Rect()
            currentFocus?.getGlobalVisibleRect(rect)
            // 현재 터치 지점이 포커스를 가지고 있는 View의 영역 내부가 아니라면
            if(rect.contains(ev?.x?.toInt()!!, ev?.y?.toInt()!!) == false){
                // 키보드를 내리고 포커스를 제거한다.
                hideSoftInput()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    // 이미지 뷰에 있는 이미지를 파일로 저장한다.
    fun saveImageView(imageView:ImageView){
        // ImageView에서 이미지 데이터를 추출한다.
        val bitmapDrawable = imageView.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap

        // 저장할 파일의 경로
        val file = File("${filePath}/uploadTemp.jpg")
        // 파일로 저장한다.
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
    }

    // 서버에 있는 이미지를 가져와 ImageView에 보여준다.
    fun showServiceImage(imageUri:Uri, imageView: ImageView){
        Glide.with(this@BoardActivity).load(imageUri).into(imageView)
    }
}

// 프래그먼트들을 나타내는 값들
enum class BoardFragmentName(var number:Int, var str:String){
    // 게시판 메인 화면
    BOARD_MAIN_FRAGMENT(1, "BoardMainFragment"),
}