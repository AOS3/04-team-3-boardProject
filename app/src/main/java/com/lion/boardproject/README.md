# 01_프로젝트 기본설정

### DataBinding 설정

[build.gradle.kts]
```kt
    dataBinding{
        enable = true
    }
```

```kt
dependencies {
    ...
    implementation("androidx.fragment:fragment-ktx:1.8.5")
}
```

### 아이콘 파일 복사
- 제공해드린 아이콘 파일들을 res/drawable 폴더에 넣어주세요

---

# 02_앱 아이콘 설정

### 프로젝트에서 마우스 우클릭 > new > Image Assets 도구를 통해 만들어준다.

### AndroidManifest.xml 에 아이콘을 등록해준다.

```xml
        android:icon="@mipmap/like_lion_icon"
        android:roundIcon="@mipmap/like_lion_icon_round"
```


### 어플 이름을 변경해준다.

[res/values/strings.xml]
```xml
    <string name="app_name">멋쟁이사자처럼게시판</string>
```

--- 

# 03_Splash Screen 구성

### 프로젝트에서 마우스 우클릭 > new > Image Assets 도구를 통해 아이콘을 만들어준다.

### 브랜드 이미지로 사용할 이미지를 drawable 폴더에 넣어준다.

### 라이브러리를 추가한다.

[build.gradle.kts]
```kt
implementation("androidx.core:core-splashscreen:1.0.1")
```

### SplashScreen용 테마를 작성한다.

[res/values/themes.xml]

```xml
    <style name="AppTheme.Splash" parent="Theme.Material3.DayNight.NoActionBar">
        <!-- 배경 색상 또는 이미지 -->
        <item name="windowSplashScreenBackground">@color/white</item>
        <!-- 중앙에 표시될 아이콘 이미지 -->
        <item name="windowSplashScreenAnimatedIcon">@mipmap/like_lion_logo</item>
        <!-- Splash Screen이 보여질 시간 -->
        <item name="windowSplashScreenAnimationDuration">1000</item>
        <!-- windowSplashScreenAnimationDuration 에서 지정한 시간이 끝나면 적용할 테마 -->
        <!-- AndroidManifest.xml 에 적용되어 있는 테마를 적용해준다 -->
        <item name="postSplashScreenTheme">@style/Theme.BoardProject</item>
    </style>
```

### MainActivity의 테마를 설정해준다.

[AndroidManifest.xml]
```xml
        <activity
            ...
            android:theme="@style/AppTheme.Splash">
```

### SplashScreen 관련 메서드를 호출해준다.

[MainActivity.kt - onCreate()]
```kt
        // SplashScreen 적용
        installSplashScreen()
```

---

# 04_회원관련 Activity 구성

### UserActivity를 만들어준다.

### MainActivity를 종료하고 UserActivity를 실행한다.

[MainActivity.kt]
```kt
        // UserActivity를 실행한다.
        val userIntent = Intent(this@MainActivity, UserActivity::class.java)
        startActivity(userIntent)
        // MainActivity를 종료한다.
        finish()
```

### UserActivity의 화면을 구성한다.

[res/layout/activity_user.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:id="@+id/main"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".UserActivity">

        <androidx.fragment.app.FragmentContainerView
            android:id="@+id/fragmentContainerViewUser"
            android:layout_width="0dp"
            android:layout_height="0dp"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />
    </androidx.constraintlayout.widget.ConstraintLayout>

</layout>

```

### DataBinding 코드를 작성한다

[UserActivity.kt]
```kt
    lateinit var activityUserBinding:ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        activityUserBinding = DataBindingUtil.setContentView(this@UserActivity, R.layout.activity_user)
```

### 프래그먼트들의 이름을 가지고 있는 enum class를 작성한다.

[UserActivity.kt]
```kt
// 프래그먼트들을 나타내는 값들
enum class UserFragmentName(var number:Int, var str:String){

}
```

### 프래그먼트를 제거하는 메서드를 구현한다.

[UserActivity.kt]
```kt
    // 프래그먼트를 BackStack에서 제거하는 메서드
    fun removeFragment(fragmentName: UserFragmentName){
        supportFragmentManager.popBackStack(fragmentName.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
```

### 프래그먼트를 담을 변수를 선언한다.

[UserActivity.kt]
```kt
    // 현재 Fragment와 다음 Fragment를 담을 변수(애니메이션 이동 때문에...)
    var newFragment:Fragment? = null
    var oldFragment:Fragment? = null
```

### 프래그먼트 교체 메서드를 구현한다.

[UserActivity.kt]
```kt

    // 프래그먼트를 교체하는 함수
    fun replaceFragment(fragmentName: UserFragmentName, isAddToBackStack:Boolean, animate:Boolean, dataBundle: Bundle?){
        // newFragment가 null이 아니라면 oldFragment 변수에 담아준다.
        if(newFragment != null){
            oldFragment = newFragment
        }
        // 프래그먼트 객체
        newFragment = when(fragmentName){

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

            replace(R.id.fragmentContainerViewUser, newFragment!!)
            if(isAddToBackStack){
                addToBackStack(fragmentName.str)
            }
        }
    }
```

--- 

# 05_로그인 화면 구성

### 두 개의 패키지를 만들어준다.
- fragment
- viewmodel

### LoginFragment를 만들어준다.

### LoginViewModel을 만들어준다.

[viewmodel/LoginViewModel.kt]
```kt
data class LoginViewModel(val loginFragment: LoginFragment) : ViewModel() {
}
```

### layout 태그로 묶어준다.

[res/layout/fragment_login.xml]

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="loginViewModel"
            type="com.lion.boardproject.viewmodel.LoginViewModel" />
    </data>

    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".fragment.LoginFragment">

        <!-- TODO: Update blank fragment layout -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:text="@string/hello_blank_fragment" />

    </FrameLayout>

</layout>

```

### 프래그먼트의 기본 코드를 작성한다.

[fragment/LoginFragment.kt]
```kt
class LoginFragment : Fragment() {

    lateinit var fragmentLoginBinding: FragmentLoginBinding
    lateinit var userActivity: UserActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        fragmentLoginBinding.loginViewModel = LoginViewModel(this@LoginFragment)
        fragmentLoginBinding.lifecycleOwner = this@LoginFragment

        userActivity = activity as UserActivity

        return fragmentLoginBinding.root
    }
}
```

### Fragment의 이름을 정의해준다.

[UserActivity.kt]
```kt
    // 로그인 화면
    USER_LOGIN_FRAGMENT(1, "UserLoginFragment"),
```

### Fragment의 객체를 생성한다.

[UserActivity.kt - replaceFragment()]
```kt
            // 로그인 화면
            UserFragmentName.USER_LOGIN_FRAGMENT -> LoginFragment()
```

### 첫 번째 프래그먼트를 설정해준다.

[UserActivity.kt - onCreate()]
```kt
        // 첫번째 Fragment를 설정한다.
        replaceFragment(UserFragmentName.USER_LOGIN_FRAGMENT, false, false, null)
```

### 입력 요소의 digit에 설정할 문자열을 만들어준다.

[res/values/stings.xml]
```xml 
    <!-- 영어, 숫자, 특수문자만 입력을 위한 문자열 -->
    <string name="digit_value">abcdefghijklmnopqrstuvwxyz0123456789_!@#</string>
```

### 화면을 구성한다.

[res/layout/fragment_login.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="loginViewModel"
            type="com.lion.boardproject.viewmodel.LoginViewModel" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:transitionGroup="true">

        <com.google.android.material.appbar.MaterialToolbar
            android:id="@+id/toolbarUserLogin"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@android:color/transparent"
            android:minHeight="?attr/actionBarSize"
            android:theme="?attr/actionBarTheme" />

        <ScrollView
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:padding="10dp" >

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldUserLoginId"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="아이디"
                    app:endIconMode="clear_text"
                    app:startIconDrawable="@drawable/person_24px">

                    <com.google.android.material.textfield.TextInputEditText
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:digits="@string/digit_value"
                        android:singleLine="true"
                        android:textAppearance="@style/TextAppearance.AppCompat.Large" />
                </com.google.android.material.textfield.TextInputLayout>

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldUserLoginPw"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="비밀번호"
                    app:endIconMode="password_toggle"
                    app:startIconDrawable="@drawable/key_24px"
                    android:layout_marginTop="10dp">

                    <com.google.android.material.textfield.TextInputEditText
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:digits="@string/digit_value"
                        android:singleLine="true"
                        android:inputType="text|textPassword"
                        android:textAppearance="@style/TextAppearance.AppCompat.Large" />
                </com.google.android.material.textfield.TextInputLayout>

                <CheckBox
                    android:id="@+id/checkBoxUserLoginAuto"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    android:text="자동 로그인"
                    android:textAppearance="@style/TextAppearance.AppCompat.Large" />

                <Button
                    android:id="@+id/buttonUserLoginSubmit"
                    style="@style/Widget.Material3.Button.OutlinedButton"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    android:text="로그인"
                    android:textAppearance="@style/TextAppearance.AppCompat.Large" />

                <com.google.android.material.divider.MaterialDivider
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp" />

                <Button
                    android:id="@+id/buttonUserLoginJoin"
                    style="@style/Widget.Material3.Button.OutlinedButton"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    android:text="회원 가입"
                    android:textAppearance="@style/TextAppearance.AppCompat.Large" />
            </LinearLayout>
        </ScrollView>
    </LinearLayout>

</layout>

```

### LiveData를 작성한다.

[viewmodel/LoginViewModel.kt]
```kt
    // toolbarUserLogin - title
    val toolbarUserLoginTitle = MutableLiveData<String>()
```

### layout 파일의 툴바에 LiveData를 설정해준다.

[res/layout/fragment_login.xml]
```xml
        <com.google.android.material.appbar.MaterialToolbar
            ...
            app:title="@{loginViewModel.toolbarUserLoginTitle}"/>
```

### 툴바를 구성하는 메서드를 구현한다.
[fragment/LoginFragment.kt]
```kt
    // 툴바를 구성하는 메서드
    fun settingToolbar(){
        fragmentLoginBinding.loginViewModel?.apply {
            toolbarUserLoginTitle.value = "로그인"
        }
    }
```

### 메서드를 호출한다.
[fragment/LoginFragment.kt - onCreateView()]
```kt
        // 툴바를 구성하는 메서드 호출
        settingToolbar()
```

--- 

# 06 회원 가입 화면 구성 1

### Fragment와 ViewModel을 만들어준다.
- UserJoinStep1Fragment
- UserJoinStep1ViewModel

### ViewModel의 코드를 작성한다.

[viewmodel/UserJoinStep1ViewModel.kt]
```kt
data class UserJoinStep1ViewModel(val userJoinStep1Fragment: UserJoinStep1Fragment) : ViewModel(){
}
```

### 화면을 구성해준다.

[res/layout/fragment_user_join_step1.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>

<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="userJoinStep1ViewModel"
            type="com.lion.boardproject.viewmodel.UserJoinStep1ViewModel" />
    </data>

    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".fragment.UserJoinStep1Fragment">

        <!-- TODO: Update blank fragment layout -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:text="@string/hello_blank_fragment" />

    </FrameLayout>

</layout>

```

### Fragment의 기본 코드를 작성한다.

[fragment/UserJoinStep1Fragment.kt]
```kt
class UserJoinStep1Fragment : Fragment() {

    lateinit var fragmentUserJoinStep1Binding: FragmentUserJoinStep1Binding
    lateinit var userActivity: UserActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentUserJoinStep1Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_join_step1, container, false)
        fragmentUserJoinStep1Binding.userJoinStep1ViewModel = UserJoinStep1ViewModel(this@UserJoinStep1Fragment)
        fragmentUserJoinStep1Binding.lifecycleOwner = this@UserJoinStep1Fragment

        userActivity = activity as UserActivity

        return fragmentUserJoinStep1Binding.root
    }

}
```

### Fragment의 이름을 정의한다.

[UserActivity.kt - UserFragmentName]
```kt
    // 회원 가입 화면
    USER_JOIN_STEP1_FRAGMENT(2, "UserJoinStep1Fragment"),
```

### Fragment의 객체를 생성한다.

[UserActivity.kt - replaceFragment()]
```kt
            // 회원 가입 과정1 화면
            UserFragmentName.USER_JOIN_STEP1_FRAGMENT -> UserJoinStep1Fragment()
```

### UserJoinStep1Fragment로 이동할 수 있는 메서드를 구현한다.
[fragment/LoginFragment.kt]
```kt
    // 회원 가입 화면으로 이동시키는 메서드
    fun moveToUserJoinStep1(){
        userActivity.replaceFragment(UserFragmentName.USER_JOIN_STEP1_FRAGMENT, true, true, null)
    }
```

### 버튼과 연결되는 메서드를 구현한다.
[viewmodel/LoginViewModel.kt]
```kt
    // buttonUserLoginJoin - onClick
    fun buttonUserLoginJoinOnClick(){
        loginFragment.moveToUserJoinStep1()
    }
```

### 버튼의 onclick 속성에 메서드 호출을 설정해준다.

[res/layout/fragment_login.xml]
```xml
    <Button
        ...
        android:onClick="@{(view) -> loginViewModel.buttonUserLoginJoinOnClick()}"/>
```

### 화면을 구성한다.
[res/layout/fragment_user_join_step1.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>

<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <data>
        <variable
            name="userJoinStep1ViewModel"
            type="com.lion.boardproject.viewmodel.UserJoinStep1ViewModel" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:transitionGroup="true">

        <com.google.android.material.appbar.MaterialToolbar
            android:id="@+id/toolbarUserJoinStep1"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@android:color/transparent"
            android:minHeight="?attr/actionBarSize"
            android:theme="?attr/actionBarTheme"/>

        <ScrollView
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:padding="10dp" >

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldUserJoinStep1Id"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="아이디"
                    app:endIconMode="clear_text"
                    app:startIconDrawable="@drawable/person_24px">

                    <com.google.android.material.textfield.TextInputEditText
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:digits="@string/digit_value"
                        android:singleLine="true"
                        android:textAppearance="@style/TextAppearance.AppCompat.Large" />
                </com.google.android.material.textfield.TextInputLayout>

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldUserJoinStep1Pw1"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="비밀번호"
                    app:endIconMode="password_toggle"
                    app:startIconDrawable="@drawable/key_24px"
                    android:layout_marginTop="10dp">

                    <com.google.android.material.textfield.TextInputEditText
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:digits="@string/digit_value"
                        android:singleLine="true"
                        android:inputType="text|textPassword"
                        android:textAppearance="@style/TextAppearance.AppCompat.Large" />
                </com.google.android.material.textfield.TextInputLayout>

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldUserJoinStep1Pw2"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="비밀번호 확인"
                    app:endIconMode="password_toggle"
                    app:startIconDrawable="@drawable/key_24px"
                    android:layout_marginTop="10dp">

                    <com.google.android.material.textfield.TextInputEditText
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:digits="@string/digit_value"
                        android:singleLine="true"
                        android:inputType="text|textPassword"
                        android:textAppearance="@style/TextAppearance.AppCompat.Large" />
                </com.google.android.material.textfield.TextInputLayout>

                <Button
                    android:id="@+id/buttonUserJoinStep1Next"
                    style="@style/Widget.Material3.Button.OutlinedButton"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    android:text="다음"
                    android:textAppearance="@style/TextAppearance.AppCompat.Large" />

            </LinearLayout>
        </ScrollView>

    </LinearLayout>

</layout>

```

### ViewModel에 Live 데이터를 정의한다.

[viewmodel/userJoinStep1ViewModel.kt]
```kt
    // toolbarUserLogin - title
    val toolbarUserLoginTitle = MutableLiveData<String>()
    // toolbarUserLogin - navigationIcon
    val toolbarUserLoginNavigationIcon = MutableLiveData<Int>()
```

### Live 데이터와 UI 요소의 속성과 연결해준다.

[res/layout/fragment_user_join_step1.xml]
```xml
        <com.google.android.material.appbar.MaterialToolbar
            ...
            app:title="@{userJoinStep1ViewModel.toolbarUserLoginTitle}"
            app:navigationIcon="@{userJoinStep1ViewModel.toolbarUserLoginNavigationIcon}"/>
```

### Toolbar를 구성하는 메서드를 작성한다

[fragment/UserJoinStep1Fragment.kt]
```kt
    fun settingToolbar(){
        fragmentUserJoinStep1Binding.userJoinStep1ViewModel?.apply { 
            // 타이틀
            toolbarUserLoginTitle.value = "회원 가입"
            // 네비게이션 아이콘
            toolbarUserLoginNavigationIcon.value = R.drawable.arrow_back_24px
        }
    }
```

### 메서드를 호출한다.
[fragment/UserJoinStep1Fragment.kt - onCreateView()]
```kt
        // 툴바를 구성하는 메서드를 호출한다.
        settingToolbar()
```

### 네비게이션 아이콘을 클릭했을때 뒤로 가는 기능을 구현한다.
- toolbar의 속성에는 네비게이션 아이콘 클릭에 대한 리스너 속성이 없다.
- 이번 작업은 없는 속성을 만들어주는 작업을 해본다.

- build.gradle.kts 에 kept 설정을 해준다.
[build.gradle.kts]
```kt
plugins{
    ....
    kotlin("kapt")
} 
```

- ViewModel에 호출될 메서드를 작성해준다.

[viewmoddel/UserJoinStep1ViewModel.kt]
```kt
    companion object{
        // toolbarUserJoinStep1 - onNavigationClickUserJoinStep1
        @JvmStatic
        // xml 에 설정할 속성이름을 설정한다.
        @BindingAdapter("onNavigationClickUserJoinStep1")
        // 호출되는 메서드를 구현한다.
        // 첫 번째 매개변수 : 이 속성이 설정되어 있는 View 객체
        // 그 이후 : 전달해주는 값이 들어온다. xml 에서는 ViewModel이 가는 프로퍼티에 접근할 수 있기 때문에
        // 이것을 통해 Fragment객체를 받아 사용할것이다.
        fun onNavigationClickUserJoinStep1(materialToolbar:MaterialToolbar, userJoinStep1Fragment: UserJoinStep1Fragment){
            materialToolbar.setNavigationOnClickListener {
                // 이전으로 돌아간다.
                userJoinStep1Fragment.userActivity.removeFragment(UserFragmentName.USER_JOIN_STEP1_FRAGMENT)
            }
        }
    }
```

### layout에 해당 속성을 적용해준다.

[res/layout/fragment_user_join_step1.xml]
```xml
app:onNavigationClickUserJoinStep1="@{userJoinStep1ViewModel.userJoinStep1Fragment}"
```

---

# 07_회원 가입 단계 1의 입력 요소 처리

### 입력 요소와 연결할 MutableLiveData를 정의해준다.

[viewmodel/UserJoinStep1ViewModel.kt]
```kt
    // textFieldUserJoinStep1Id - EditText - text
    val textFieldUserJoinStep1IdEditTextText = MutableLiveData("")
    // textFieldUserJoinStep1Pw1 - EditText - text
    val textFieldUserJoinStep1Pw1EditTextText = MutableLiveData("")
    // textFieldUserJoinStep1Pw2 - EditText - text
    val textFieldUserJoinStep1Pw2EditTextText = MutableLiveData("")
```

### layout 파일에 LiveData를 설정해준다.

```xml
                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldUserJoinStep1Id"
                    ...

                    <com.google.android.material.textfield.TextInputEditText
                        ...
                        android:text="@={userJoinStep1ViewModel.textFieldUserJoinStep1IdEditTextText}"/>
                </com.google.android.material.textfield.TextInputLayout>

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldUserJoinStep1Pw1"
                    ...

                    <com.google.android.material.textfield.TextInputEditText
                        ...
                        android:text="@={userJoinStep1ViewModel.textFieldUserJoinStep1Pw1EditTextText}"/>
                </com.google.android.material.textfield.TextInputLayout>

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldUserJoinStep1Pw2"
                    ...

                    <com.google.android.material.textfield.TextInputEditText
                        ...
                        android:text="@={userJoinStep1ViewModel.textFieldUserJoinStep1Pw2EditTextText}"/>
                </com.google.android.material.textfield.TextInputLayout>
```

### 키보드 관련 메서드를 넣어준다.
[UserActivity.kt]

```kt
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
```

### Dialog를 띄우는 메서드를 구현한다.

[UserActivity.kt]
```kt
    // 다이얼로그를 통해 메시지를 보여주는 함수
    fun showMessageDialog(title:String, message:String, posTitle:String, callback:()-> Unit){
        val builder = MaterialAlertDialogBuilder(this@UserActivity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(posTitle){ dialogInterface: DialogInterface, i: Int ->
            callback()
        }
        builder.show()
    }
```

### 버튼을 누르면 호출될 메서드를 구현해준다.

[viewmodel/UserJoinStep1ViewModel.kt]
```kt
    // buttonUserJoinStep1Next - onClick
    fun buttonUserJoinStep1NextOnClick(){
        userJoinStep1Fragment.apply {
            // 입력요소 검사
            if(textFieldUserJoinStep1IdEditTextText.value?.isEmpty()!!){
                userActivity.showMessageDialog("아이디 입력", "아이디를 입력해주세요", "확인"){
                    userActivity.showSoftInput(fragmentUserJoinStep1Binding.textFieldUserJoinStep1Id.editText!!)
                }
                return
            }
            if(textFieldUserJoinStep1Pw1EditTextText.value?.isEmpty()!!){
                userActivity.showMessageDialog("비밀번호 입력", "비밀번호를 입력해주세요", "확인"){
                    userActivity.showSoftInput(fragmentUserJoinStep1Binding.textFieldUserJoinStep1Pw1.editText!!)
                }
                return
            }
            if(textFieldUserJoinStep1Pw2EditTextText.value?.isEmpty()!!){
                userActivity.showMessageDialog("비밀번호 입력", "비밀번호를 입력해주세요", "확인"){
                    userActivity.showSoftInput(fragmentUserJoinStep1Binding.textFieldUserJoinStep1Pw2.editText!!)
                }
                return
            }
            if(textFieldUserJoinStep1Pw1EditTextText.value != textFieldUserJoinStep1Pw2EditTextText.value){
                userActivity.showMessageDialog("비밀번호 입력", "비밀번호가 다릅니다", "확인"){
                    textFieldUserJoinStep1Pw1EditTextText.value = ""
                    textFieldUserJoinStep1Pw2EditTextText.value = ""
                    userActivity.showSoftInput(fragmentUserJoinStep1Binding.textFieldUserJoinStep1Pw1.editText!!)
                }
                return
            }
        }
    }
```

### 메서드를 버튼의 onclick 속성과 연결해준다.

```xml
                <Button
                    ...
                    android:onClick="@{(view) -> userJoinStep1ViewModel.buttonUserJoinStep1NextOnClick()}"/>
```

---

# 08_회원 추가 정보 입력 화면 만들기

### viewmodel/UserJoinStep2ViewModel 를 만들어준다.

### fragment/UserJoinStep2Fragment 를 만들어준다.

### viewmodel/UserJoinStep2ViewModel 의 기본 코드를 작성한다.

```kt
class UserJoinStep2ViewModel(val userJoinStep2Fragment: UserJoinStep2Fragment) : ViewModel() {
}
```

### layout을 수정한다.

[res/layout/fragment_user_join_step2.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>

<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="userJoinStep2ViewModel"
            type="com.lion.boardproject.viewmodel.UserJoinStep2ViewModel" />
    </data>

    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".fragment.UserJoinStep2Fragment">

        <!-- TODO: Update blank fragment layout -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:text="@string/hello_blank_fragment" />

    </FrameLayout>
</layout>


```

### Fragment의 기본 코드를 작성한다.

[fragment/UserJoinStep2Fragment.kt]
```kt
class UserJoinStep2Fragment : Fragment() {

    lateinit var fragmentUserJoinStep2Binding: FragmentUserJoinStep2Binding
    lateinit var userActivity: UserActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentUserJoinStep2Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_join_step2, container, false)
        fragmentUserJoinStep2Binding.userJoinStep2ViewModel = UserJoinStep2ViewModel(this@UserJoinStep2Fragment)
        fragmentUserJoinStep2Binding.lifecycleOwner = this@UserJoinStep2Fragment

        userActivity = activity as UserActivity

        return fragmentUserJoinStep2Binding.root
    }

}
```

### 프래그먼트의 이름을 정의해준다.

[UserActivity.kt]
```kt
    // 회원 가입 화면 2
    USER_JOIN_STEP2_FRAGMENT(3, "UserJoinStep2Fragment"),
```

### 프래그먼트 객체를 생성한다.

[UserActivity.kt]
```kt
            // 회원 가입 과정2 화면
            UserFragmentName.USER_JOIN_STEP2_FRAGMENT -> UserJoinStep2Fragment()
```

### 다음 화면으로 이동하는 메서드를 구현해준다.

[fragment/UserJoinStep1Fragment.kt]
```kt
    // 다음 입력 화면으로 이동한다.
    fun moveToUserJoinStep2(){
        userActivity.replaceFragment(UserFragmentName.USER_JOIN_STEP2_FRAGMENT, true, true, null)
    }
```

### 메서드를 호출한다.

[viewmodel/UserJoinStep1ViewModel.kt]
```kt
            // 다음 화면으로 이동한다.
            moveToUserJoinStep2()
```

--- 

# 09_회면 추가 정보 입력 화면 구성

### 화면을 구성한다.

[res/layout/fragment_user_join_step2.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>

<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <data>
        <variable
            name="userJoinStep2ViewModel"
            type="com.lion.boardproject.viewmodel.UserJoinStep2ViewModel" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:transitionGroup="true" >

        <com.google.android.material.appbar.MaterialToolbar
            android:id="@+id/toolbarUserJoinStep2"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@android:color/transparent"
            android:minHeight="?attr/actionBarSize"
            android:theme="?attr/actionBarTheme"
            app:navigationIcon="@drawable/arrow_back_24px"
            app:title="회원 가입" />


        <ScrollView
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:padding="10dp" >

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldUserJoinStep2NickName"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="닉네임"
                    app:endIconMode="clear_text"
                    app:startIconDrawable="@drawable/person_add_24px">

                    <com.google.android.material.textfield.TextInputEditText
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:singleLine="true"
                        android:textAppearance="@style/TextAppearance.AppCompat.Large"
                        android:text="@={userJoinStep2ViewModel.textFieldUserJoinStep2NickNameEditTextText}"/>
                </com.google.android.material.textfield.TextInputLayout>

                <Button
                    android:id="@+id/buttonUserJoinStep2CheckNickName"
                    style="@style/Widget.Material3.Button.OutlinedButton"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    android:text="닉네임 중복 확인"
                    android:textAppearance="@style/TextAppearance.AppCompat.Large"/>


                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldUserJoinStep2Age"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="나이"
                    app:endIconMode="clear_text"
                    app:startIconDrawable="@drawable/face_24px"
                    android:layout_marginTop="10dp">

                    <com.google.android.material.textfield.TextInputEditText
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:singleLine="true"
                        android:inputType="number|numberDecimal"
                        android:textAppearance="@style/TextAppearance.AppCompat.Large"
                        android:text="@={userJoinStep2ViewModel.textFieldUserJoinStep2AgeEditTextText}"/>
                </com.google.android.material.textfield.TextInputLayout>

                <com.google.android.material.divider.MaterialDivider
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    app:dividerColor="@color/black"/>

                <com.google.android.material.checkbox.MaterialCheckBox
                    android:id="@+id/checkBoxUserJoinStep2HobbyAll"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    android:text="취미"
                    android:textAppearance="@style/TextAppearance.AppCompat.Large"
                    app:checkedState="@{userJoinStep2ViewModel.checkBoxUserJoinStep2HobbyAllCheckedState}" />

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    android:orientation="vertical">

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="20dp"
                        android:layout_marginTop="10dp"
                        android:orientation="horizontal">

                        <CheckBox
                            android:id="@+id/checkBoxUserJoinStep2Hobby1"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:layout_weight="1"
                            android:text="게임"
                            android:checked="@={userJoinStep2ViewModel.checkBoxUserJoinStep2Hobby1Checked}"/>

                        <CheckBox
                            android:id="@+id/checkBoxUserJoinStep2Hobby2"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:layout_weight="1"
                            android:text="독서"
                            android:checked="@={userJoinStep2ViewModel.checkBoxUserJoinStep2Hobby2Checked}"/>

                        <CheckBox
                            android:id="@+id/checkBoxUserJoinStep2Hobby3"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:layout_weight="1"
                            android:text="요리"
                            android:checked="@={userJoinStep2ViewModel.checkBoxUserJoinStep2Hobby3Checked}"/>
                    </LinearLayout>

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="20dp"
                        android:layout_marginTop="10dp"
                        android:orientation="horizontal">

                        <CheckBox
                            android:id="@+id/checkBoxUserJoinStep2Hobby4"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:layout_weight="1"
                            android:text="낚시"
                            android:checked="@={userJoinStep2ViewModel.checkBoxUserJoinStep2Hobby4Checked}"/>

                        <CheckBox
                            android:id="@+id/checkBoxUserJoinStep2Hobby5"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:layout_weight="1"
                            android:text="영화감상"
                            android:checked="@={userJoinStep2ViewModel.checkBoxUserJoinStep2Hobby5Checked}"/>

                        <CheckBox
                            android:id="@+id/checkBoxUserJoinStep2Hobby6"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:layout_weight="1"
                            android:text="기타"
                            android:checked="@={userJoinStep2ViewModel.checkBoxUserJoinStep2Hobby6Checked}"/>
                    </LinearLayout>
                </LinearLayout>

                <com.google.android.material.divider.MaterialDivider
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    app:dividerColor="@color/black"/>

                <Button
                    android:id="@+id/buttonUserJoinStep2Submit"
                    style="@style/Widget.Material3.Button.OutlinedButton"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    android:text="가입 완료"
                    android:textAppearance="@style/TextAppearance.AppCompat.Large"/>
            </LinearLayout>

        </ScrollView>
    </LinearLayout>
</layout>


```

### MutableLiveData를 정의한다.

[viewmodel/UserJoinStep2ViewModel.kt]
```kt
    // textFieldUserJoinStep2NickName - EditText - text
    val textFieldUserJoinStep2NickNameEditTextText = MutableLiveData("")
    // textFieldUserJoinStep2Age - EditExt - text
    val textFieldUserJoinStep2AgeEditTextText = MutableLiveData("")
    // checkBoxUserJoinStep2HobbyAll - checkedState
    val checkBoxUserJoinStep2HobbyAllCheckedState = MutableLiveData(MaterialCheckBox.STATE_UNCHECKED)
    // checkBoxUserJoinStep2Hobby1 - checked
    val checkBoxUserJoinStep2Hobby1Checked = MutableLiveData(false)
    // checkBoxUserJoinStep2Hobby2 - checked
    val checkBoxUserJoinStep2Hobby2Checked = MutableLiveData(false)
    // checkBoxUserJoinStep2Hobby3 - checked
    val checkBoxUserJoinStep2Hobby3Checked = MutableLiveData(false)
    // checkBoxUserJoinStep2Hobby4 - checked
    val checkBoxUserJoinStep2Hobby4Checked = MutableLiveData(false)
    // checkBoxUserJoinStep2Hobby5 - checked
    val checkBoxUserJoinStep2Hobby5Checked = MutableLiveData(false)
    // checkBoxUserJoinStep2Hobby6 - checked
    val checkBoxUserJoinStep2Hobby6Checked = MutableLiveData(false)
```

### LiveData와 View 의 속성들과 연결해준다.

```xml

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldUserJoinStep2NickName"
                   ...
                    <com.google.android.material.textfield.TextInputEditText
                        ...
                        android:text="@={userJoinStep2ViewModel.textFieldUserJoinStep2NickNameEditTextText}"/>
                </com.google.android.material.textfield.TextInputLayout>

               ...
                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldUserJoinStep2Age"
                    ...
                    <com.google.android.material.textfield.TextInputEditText
                        ...
                        android:text="@={userJoinStep2ViewModel.textFieldUserJoinStep2AgeEditTextText}"/>
                </com.google.android.material.textfield.TextInputLayout>

                ...
                
                <com.google.android.material.checkbox.MaterialCheckBox
                    android:id="@+id/checkBoxUserJoinStep2HobbyAll"
                    ...
                    app:checkedState="@{userJoinStep2ViewModel.checkBoxUserJoinStep2HobbyAllCheckedState}" />
                    ...
                        <CheckBox
                            android:id="@+id/checkBoxUserJoinStep2Hobby1"
                            ...
                            android:checked="@={userJoinStep2ViewModel.checkBoxUserJoinStep2Hobby1Checked}"/>

                        <CheckBox
                            android:id="@+id/checkBoxUserJoinStep2Hobby2"
                            ...
                            android:checked="@={userJoinStep2ViewModel.checkBoxUserJoinStep2Hobby2Checked}"/>

                        <CheckBox
                            android:id="@+id/checkBoxUserJoinStep2Hobby3"
                            ...
                            android:checked="@={userJoinStep2ViewModel.checkBoxUserJoinStep2Hobby3Checked}"/>
                    </LinearLayout>

                    ...

                        <CheckBox
                            android:id="@+id/checkBoxUserJoinStep2Hobby4"
                           ...
                            android:checked="@={userJoinStep2ViewModel.checkBoxUserJoinStep2Hobby4Checked}"/>

                        <CheckBox
                            android:id="@+id/checkBoxUserJoinStep2Hobby5"
                            ...
                            android:checked="@={userJoinStep2ViewModel.checkBoxUserJoinStep2Hobby5Checked}"/>

                        <CheckBox
                            android:id="@+id/checkBoxUserJoinStep2Hobby6"
                           ...
                            android:checked="@={userJoinStep2ViewModel.checkBoxUserJoinStep2Hobby6Checked}"/>
                    </LinearLayout>
                </LinearLayout>
                ...


```

### 취미 체크박스 구현을 위한 LiveData를 추가한다.
- checkedState는 단방향 (@) 만 지원하기 때문에 checked 속성을 추가로 이용한다.

[viewmodel/UserJoinStep2ViewModel.kt]
```kt
    // checkBoxUserJoinStep2HobbyAll - checked
    val checkBoxUserJoinStep2HobbyAllChecked = MutableLiveData(false)
```

### LiveData와 연결해준다.

[res/layout/fragment_user_join_step2.xml]
```xml
                <com.google.android.material.checkbox.MaterialCheckBox
                    android:id="@+id/checkBoxUserJoinStep2HobbyAll"
                    ...
                    android:checked="@={userJoinStep2ViewModel.checkBoxUserJoinStep2HobbyAllChecked}"/>
```

### 체크 상태에 따라 다른 체크박스 체크 상태를 설정하는 메서드를 만들어준다.

[viewmodel/UserJoinStep2ViewModel.kt]
```kt
    // checkBoxUserJoinStep2HobbyAll - onClick
    fun checkBoxUserJoinStep2HobbyAllOnClick(){
        // 체크되어 있다면
        if(checkBoxUserJoinStep2HobbyAllChecked.value == true){
            checkBoxUserJoinStep2Hobby1Checked.value = true
            checkBoxUserJoinStep2Hobby2Checked.value = true
            checkBoxUserJoinStep2Hobby3Checked.value = true
            checkBoxUserJoinStep2Hobby4Checked.value = true
            checkBoxUserJoinStep2Hobby5Checked.value = true
            checkBoxUserJoinStep2Hobby6Checked.value = true
        } else {
            checkBoxUserJoinStep2Hobby1Checked.value = false
            checkBoxUserJoinStep2Hobby2Checked.value = false
            checkBoxUserJoinStep2Hobby3Checked.value = false
            checkBoxUserJoinStep2Hobby4Checked.value = false
            checkBoxUserJoinStep2Hobby5Checked.value = false
            checkBoxUserJoinStep2Hobby6Checked.value = false
        }
    }
```

### layout에 적용해준다.

[res/layout/fragment_user_join_step2.xml]
```xml
                <com.google.android.material.checkbox.MaterialCheckBox
                    android:id="@+id/checkBoxUserJoinStep2HobbyAll"
                    ...
                    android:onClick="@{(view) -> userJoinStep2ViewModel.checkBoxUserJoinStep2HobbyAllOnClick()}"/>
```

### 각 체크박스를 눌렀을 때 호출될 메서드를 구현한다.
[viewmodel/UserJoinStep2ViewModel.kt]
```kt
    // 하위 취미 체크박스들
    fun checkBoxUserJoinStep2HobbyOnClick(){
        // 체크되어 있는 체크박스 개수
        var checkedCount = 0
        if(checkBoxUserJoinStep2Hobby1Checked.value == true){
            checkedCount++
        }
        if(checkBoxUserJoinStep2Hobby2Checked.value == true){
            checkedCount++
        }
        if(checkBoxUserJoinStep2Hobby3Checked.value == true){
            checkedCount++
        }
        if(checkBoxUserJoinStep2Hobby4Checked.value == true){
            checkedCount++
        }
        if(checkBoxUserJoinStep2Hobby5Checked.value == true){
            checkedCount++
        }
        if(checkBoxUserJoinStep2Hobby6Checked.value == true){
            checkedCount++
        }

        if(checkedCount == 0){
            // 체크된 것이 없으면
            checkBoxUserJoinStep2HobbyAllChecked.value = false
            checkBoxUserJoinStep2HobbyAllCheckedState.value = MaterialCheckBox.STATE_UNCHECKED
        } else if(checkedCount == 6){
            // 모두 체크되어 있다면
            checkBoxUserJoinStep2HobbyAllChecked.value = true
            checkBoxUserJoinStep2HobbyAllCheckedState.value = MaterialCheckBox.STATE_CHECKED
        } else {
            checkBoxUserJoinStep2HobbyAllCheckedState.value = MaterialCheckBox.STATE_INDETERMINATE
        }
    }
```

### 체크박스들에게 설정해준다.

[res/layout/fragment_user_join_step2.xml]
```xml

                        <CheckBox
                            android:id="@+id/checkBoxUserJoinStep2Hobby1"
                            ...
                            android:onClick="@{(view) -> userJoinStep2ViewModel.checkBoxUserJoinStep2HobbyOnClick()}"/>

                        <CheckBox
                            android:id="@+id/checkBoxUserJoinStep2Hobby2"
                            ...
                            android:onClick="@{(view) -> userJoinStep2ViewModel.checkBoxUserJoinStep2HobbyOnClick()}"/>

                        <CheckBox
                            android:id="@+id/checkBoxUserJoinStep2Hobby3"
                            ...
                            android:onClick="@{(view) -> userJoinStep2ViewModel.checkBoxUserJoinStep2HobbyOnClick()}"/>

                        ...

                        <CheckBox
                            android:id="@+id/checkBoxUserJoinStep2Hobby4"
                            ...
                            android:onClick="@{(view) -> userJoinStep2ViewModel.checkBoxUserJoinStep2HobbyOnClick()}"/>

                        <CheckBox
                            android:id="@+id/checkBoxUserJoinStep2Hobby5"
                            ...
                            android:onClick="@{(view) -> userJoinStep2ViewModel.checkBoxUserJoinStep2HobbyOnClick()}"/>

                        <CheckBox
                            android:id="@+id/checkBoxUserJoinStep2Hobby6"
                            ...
                            android:onClick="@{(view) -> userJoinStep2ViewModel.checkBoxUserJoinStep2HobbyOnClick()}"/>
```

### 이전 화면으로 돌아가는 메서드를 만들어준다.

[fragment/UserJoinStep2Fragment.kt]
```kt
    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        userActivity.removeFragment(UserFragmentName.USER_JOIN_STEP2_FRAGMENT)
    }
```

### NavigationIcon을 누를 때 동작할 메서드를 만들어준다

[viewmodel/UserJoinStep2ViewModel.kt]
```kt
    companion object{
        // toolbarUserJoinStep2 - onNavigationClickUserJoinStep2
        @JvmStatic
        @BindingAdapter("onNavigationClickUserJoinStep2")
        fun onNavigationClickUserJoinStep2(materialToolbar: MaterialToolbar, userJoinStep2Fragment: UserJoinStep2Fragment){
            materialToolbar.setNavigationOnClickListener {
                userJoinStep2Fragment.movePrevFragment()
            }
        }
    }
```

### toolar에 설정해준다.

[res/layout/fragment_user_join_step2.xml]
```xml
        <com.google.android.material.appbar.MaterialToolbar
            android:id="@+id/toolbarUserJoinStep2"
            ...
            app:onNavigationClickUserJoinStep2="@{userJoinStep2ViewModel.userJoinStep2Fragment}"/>
```

### 가입 완료 처리하는 메서드를 구현해준다.

[fragment/UserJoinStep2Fragment.kt]
```kt
    // 가입 완료 처리 메서드
    fun proUserJoin(){
        fragmentUserJoinStep2Binding.apply {
            // 입력 검사
            if(userJoinStep2ViewModel?.textFieldUserJoinStep2NickNameEditTextText?.value?.isEmpty()!!){
                userActivity.showMessageDialog("닉네임 입력", "닉네임을 입력해주세요", "확인"){
                    userActivity.showSoftInput(textFieldUserJoinStep2NickName.editText!!)
                }
                return
            }
            if(userJoinStep2ViewModel?.textFieldUserJoinStep2AgeEditTextText?.value?.isEmpty()!!){
                userActivity.showMessageDialog("나이 입력", "나이를 입력해주세요", "확인"){
                    userActivity.showSoftInput(textFieldUserJoinStep2Age.editText!!)
                }
                return
            }
    
            userActivity.showMessageDialog("가입 완료", "가입이 완료되었습니다\n로그인해주세요", "확인"){
                userActivity.removeFragment(UserFragmentName.USER_JOIN_STEP1_FRAGMENT)
            }
        }
    }
```

### 버튼과 연결해줄 메서드를 구현한다.

[viewmodel/UserJoinStep2ViewModel.kt]
```kt
    // buttonUserJoinStep2Submit - onClick
    fun buttonUserJoinStep2SubmitOnClick(){
        // 가입 완료 처리 메서드를 호출한다.
        userJoinStep2Fragment.proUserJoin()
    }
```

### 버튼에 설정해준다.,

[res/layout/fragment_user_join_step2.xml]
```xml
                <Button
                    android:id="@+id/buttonUserJoinStep2Submit"
                    ...
                    android:onClick="@{(view) -> userJoinStep2ViewModel.buttonUserJoinStep2SubmitOnClick()}"/>
```

---

# 10_로그인 화면 처리

### 입력 요소와 연결할 LiveData를 정의해준다.

[viewmodel/LoginViewModel.kt]
```kt
    // textFieldUserLoginId - EditText - text
    val textFieldUserLoginIdEditTextText = MutableLiveData("")
    // textFieldUserLoginPw - EditText - text
    val textFieldUserLoginPwEditTextText = MutableLiveData("")
    // checkBoxUserLoginAuto - checked
    val checkBoxUserLoginAutoChecked = MutableLiveData(false)
```

### layout 파일에 적용해준다.

```xml

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldUserLoginId"
                   ...
                    <com.google.android.material.textfield.TextInputEditText
                        ...
                        android:text="@={loginViewModel.textFieldUserLoginIdEditTextText}"/>
                </com.google.android.material.textfield.TextInputLayout>

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldUserLoginPw"
                   ...

                    <com.google.android.material.textfield.TextInputEditText
                       ...
                        android:text="@={loginViewModel.textFieldUserLoginPwEditTextText}"/>
                </com.google.android.material.textfield.TextInputLayout>

                <CheckBox
                    ...
                    android:checked="@={loginViewModel.checkBoxUserLoginAutoChecked}"/>
```

### 로그인 처리 하는 메서드를 구현한다.

[fragment/LoginFragment.kt]
```kt
    // 로그인 처리 메서드
    fun proLogin(){
        fragmentLoginBinding.apply { 
            // 입력 요소 검사
            if(loginViewModel?.textFieldUserLoginIdEditTextText?.value?.isEmpty()!!){
                userActivity.showMessageDialog("아이디 입력", "아이디를 입력해주세요", "확인"){
                    userActivity.showSoftInput(textFieldUserLoginId.editText!!)
                }
                return
            }
            if(loginViewModel?.textFieldUserLoginPwEditTextText?.value?.isEmpty()!!){
                userActivity.showMessageDialog("비밀번호 입력", "비밀번호를 입력해주세요", "확인"){
                    userActivity.showSoftInput(textFieldUserLoginPw.editText!!)
                }
                return
            }
        }
    }
```

### 버튼을 누르면 호출될 메서드를 구현한다.

[viewmodel/LoginViewModel.kt]
```kt
    // buttonUserLoginSubmit - onClick
    fun buttonUserLoginSubmitOnClick(){
        loginFragment.proLogin()
    }
```

### 로그인 버튼에 메서드를 설정해준다.
[res/layout/fragment_login.xml]
```xml
                <Button
                    android:id="@+id/buttonUserLoginSubmit"
                    ...
                    android:onClick="@{(view) -> loginViewModel.buttonUserLoginSubmitOnClick()}"/>
```

### BoardActivity 를 생성한다.

### UserActivity를 종료하고 BoardActivity를 실행한다

[fragment./LoginFragment.kt]
```kt
            // BoardActivity를 실행하고 현재 Activity를 종료한다.
            val boardIntent = Intent(userActivity, BoardActivity::class.java)
            startActivity(boardIntent)
            userActivity.finish()
```

--- 

# 11_BoardActivity 기본 코드 작성

### layout에 기본 코드를 작성한다
[res/layout/activity_board.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:id="@+id/main"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".BoardActivity">

        <androidx.fragment.app.FragmentContainerView
            android:id="@+id/fragmentContainerViewBoard"
            android:layout_width="0dp"
            android:layout_height="0dp"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />
    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>

```

### 필요한 프로퍼티를 정의해준다.

[BoardActivity.kt]
```kt
    lateinit var activityBoardBinding: ActivityBoardBinding

    // 현재 Fragment와 다음 Fragment를 담을 변수(애니메이션 이동 때문에...)
    var newFragment: Fragment? = null
    var oldFragment: Fragment? = null
```


### Binding 객체를 생성한다.

[BoardActivity.kt - onCreateView()]
```kt
activityBoardBinding = DataBindingUtil.setContentView(this@BoardActivity, R.layout.activity_board)
```

### Fragment 이름들을 관리하기 위한 enum class를 작성해준다.

[BoardActivity.kt]
```kt
// 프래그먼트들을 나타내는 값들
enum class BoardFragmentName(var number:Int, var str:String){

}
```

### 필요한 메서드들을 구현한다.

```kt
    // 프래그먼트를 교체하는 함수
    fun replaceFragment(fragmentName: BoardFragmentName, isAddToBackStack:Boolean, animate:Boolean, dataBundle: Bundle?){
        // newFragment가 null이 아니라면 oldFragment 변수에 담아준다.
        if(newFragment != null){
            oldFragment = newFragment
        }
        // 프래그먼트 객체
        newFragment = when(fragmentName){

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
```
---

# 12_게시판 메인 화면 구성

### Fragment와 ViewModel을 만들어준다.
- BoardMainFragment
- BoardMainViewModel

### layout 파일에 기본 코드를 작성한다.

[res/layout/fragment_board_main.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="boardMainViewModel"
            type="com.lion.boardproject.viewmodel.BoardMainViewModel" />
    </data>


    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".fragment.BoardMainFragment">

        <!-- TODO: Update blank fragment layout -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:text="@string/hello_blank_fragment" />

    </FrameLayout>
</layout>

```

### ViewModel의 기본 코드를 작성한다

[viewmodel/BoardMainViewModel.kt]
```kt
class BoardMainViewModel(val boardMainFragment: BoardMainFragment) : ViewModel() {
}
```

### Fragment의 기본 코드를 작성한다.

[fragment/BoardMainFragment.kt]
```kt
class BoardMainFragment : Fragment() {

    lateinit var fragmentBoardMainBinding:FragmentBoardMainBinding
    lateinit var boardActivity: BoardActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentBoardMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_board_main, container, false)
        fragmentBoardMainBinding.boardMainViewModel = BoardMainViewModel(this@BoardMainFragment)
        fragmentBoardMainBinding.lifecycleOwner = this@BoardMainFragment

        boardActivity = activity as BoardActivity

        return fragmentBoardMainBinding.root
    }

}
```

### Fragment의 이름을 정의한다.

[BoardActivity.kt]
```kt
    // 게시판 메인 화면
    BOARD_MAIN_FRAGMENT(1, "BoardMainFragment"),
```

### Fragment 객체를 생성한다.

[BoardActivity.kt - replaceFragment()]
```kt
            // 게시판 메인 화면
            BoardFragmentName.BOARD_MAIN_FRAGMENT -> BoardMainFragment()
```

### 첫 프래그먼트를 보여준다.

[BoardActivity.kt - onCreate()]
```kt
        // 첫 프래그먼트를 보여준다.
        replaceFragment(BoardFragmentName.BOARD_MAIN_FRAGMENT, false, false, null)
```

### fragment_board_main을 수정해준다.
[res/layout/fragment_board_main.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="boardMainViewModel"
            type="com.lion.boardproject.viewmodel.BoardMainViewModel" />
    </data>


    <androidx.drawerlayout.widget.DrawerLayout
        android:id="@+id/drawerLayoutBoardMain"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:transitionGroup="true">

        <androidx.coordinatorlayout.widget.CoordinatorLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

                <androidx.fragment.app.FragmentContainerView
                    android:id="@+id/fragmentContainerViewBoardMain"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent" />
            </LinearLayout>
        </androidx.coordinatorlayout.widget.CoordinatorLayout>

        <com.google.android.material.navigation.NavigationView
            android:id="@+id/navigationViewBoardMain"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
    </androidx.drawerlayout.widget.DrawerLayout>
</layout>

```

### Fragment 들의 이름을 관리할 enum class를 정의해준다.

[fragment/BoardMainFragment.kt]
```kt
// 하위 프래그먼트들의 이름
enum class BoardSubFragmentName(var number:Int, var str:String){

}
```

### 프래그먼트를 담을 변수를 선언해준다.

[fragment/BoardMainFragment.kt]
```kt
    // 현재 Fragment와 다음 Fragment를 담을 변수(애니메이션 이동 때문에...)
    var newFragment: Fragment? = null
    var oldFragment: Fragment? = null
```

### 프래그먼트 관리를 위한 메서드를 구현해준다.
[fragment/BoardMainFragment.kt]
```kt
    // 프래그먼트를 교체하는 함수
    fun replaceFragment(fragmentName: BoardSubFragmentName, isAddToBackStack:Boolean, animate:Boolean, dataBundle: Bundle?){
        // newFragment가 null이 아니라면 oldFragment 변수에 담아준다.
        if(newFragment != null){
            oldFragment = newFragment
        }
        // 프래그먼트 객체
        newFragment = when(fragmentName){

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

```

---

# 13_네비게이션 뷰 구성

### 나중에 사용할 Fragment와 ViewModel을 만들어준다.
- BoardListFragment
- BoardListViewModel

### layout 파일을 작성해준다.(임시)
[res/layout/fragment_board_list.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="boardListViewModel"
            type="com.lion.boardproject.viewmodel.BoardListViewModel" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:transitionGroup="true" >

        <Button
            android:id="@+id/button"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Button" />
    </LinearLayout>
</layout>

```

### ViewModel의 기본 코드를 작성한다.
[viewmodel/BoardListViewModel.kt]
```kt
class BoardListViewModel(val boardListFragment: BoardListFragment) : ViewModel() {
}
```

### Fragment 기본 코드를 작성한다

[fragment/BoardListFragment.kt]
```kt
class BoardListFragment(val boardMainFragment: BoardMainFragment) : Fragment() {

    lateinit var fragmentBoardListBinding: FragmentBoardListBinding
    lateinit var boardActivity: BoardActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentBoardListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_board_list, container, false)
        fragmentBoardListBinding.boardListViewModel = BoardListViewModel(this@BoardListFragment)
        fragmentBoardListBinding.lifecycleOwner = this@BoardListFragment

        boardActivity = activity as BoardActivity

        return fragmentBoardListBinding.root
    }
}
```

### 프래그먼트 이름을 정의해준다.

[fragment/BoardMainFragment.kt - BoardSubFragmentName]
```kt
    // 게시글 목록 화면
    BOARD_LIST_FRAGMENT(1, "BoardListFragment"),
```

### 프래그먼트 객체를 생성한다.

[fragment/BoardMainFragment.kt - replaceFragment()]
```kt
            // 게시글 목록 화면
            BoardSubFragmentName.BOARD_LIST_FRAGMENT -> BoardListFragment(this@BoardMainFragment)
```

### 첫 프래그먼트를 설정한다.

[fragment/BoardMainFragment.kt - onCreateView()]
```kt
        // 첫 프래그먼트 설정
        replaceFragment(BoardSubFragmentName.BOARD_LIST_FRAGMENT, false, false, null)
```

### DrawerLayout을 펼쳐줄 수 있도록 버튼의 리스너를 구성한다(삭제 예정)
[fragment/BoardListFragment.kt - onCreateView()]
```kt
        fragmentBoardListBinding.button.setOnClickListener {
            boardMainFragment.fragmentBoardMainBinding.drawerLayoutBoardMain.open()
        }
```

### Navigation View의 헤더 부분을 구성하기 위한 레이아웃 파일과 ViewModel을 만들어준다.
- viewmodel/NavigationBoardMainHeaderViewModel.kt
- res/layout/navigation_board_main_header.xml

### ViewModel의 기본 코드와 LiveData를 작성한다.

[viewmodel/NavigationBoardMainHeaderViewModel.kt]
```kt
class NavigationBoardMainHeaderViewModel(val boardMainFragment: BoardMainFragment) : ViewModel() {
    // textViewNavigationBoardMainHeaderNickName - text
    val textViewNavigationBoardMainHeaderNickNameText = MutableLiveData("")
}
```

### 레이아웃을 작성해준다.

[res/layout/navigation_board_main_header.xml]]
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable
            name="navigationBoardMainHeaderViewModel"
            type="com.lion.boardproject.viewmodel.NavigationBoardMainHeaderViewModel" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="20dp"
        android:transitionGroup="true">

        <TextView
            android:id="@+id/textViewNavigationBoardMainHeaderNickName"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{navigationBoardMainHeaderViewModel.textViewNavigationBoardMainHeaderNickNameText}"
            android:textAppearance="@style/TextAppearance.AppCompat.Large" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:text="환영합니다." />
    </LinearLayout>
</layout>

```

### 네비게이션뷰를 구성하는 메서드를 구현해준다.
[fragment/BoardMainFragment.kt]
```kt
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
            navigationBoardMainHeaderBinding.navigationBoardMainHeaderViewModel?.textViewNavigationBoardMainHeaderNickNameText?.value = "닉네임님"

            navigationViewBoardMain.addHeaderView(navigationBoardMainHeaderBinding.root)
        }
    }
```

### NavigationView에서 사용할 메뉴를 만들어준다.
[res/menu/menu_board_main_navigation.xml]

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <item android:title="게시판" >
        <menu >
            <item
                android:id="@+id/menuItemBoardNavigationAll"
                android:checkable="true"
                android:icon="@drawable/local_shipping_24px"
                android:title="전체게시판"
                app:showAsAction="ifRoom" />
            <item
                android:id="@+id/menuItemBoardNavigation1"
                android:checkable="true"
                android:icon="@drawable/post_add_24px"
                android:title="자유게시판"
                app:showAsAction="ifRoom" />
            <item
                android:id="@+id/menuItemBoardNavigation2"
                android:checkable="true"
                android:icon="@drawable/compost_24px"
                android:title="유머게시판"
                app:showAsAction="ifRoom" />
            <item
                android:id="@+id/menuItemBoardNavigation3"
                android:checkable="true"
                android:icon="@drawable/package_24px"
                android:title="시사게시판"
                app:showAsAction="ifRoom" />
            <item
                android:id="@+id/menuItemBoardNavigation4"
                android:checkable="true"
                android:icon="@drawable/approval_24px"
                android:title="운동게시판"
                app:showAsAction="ifRoom" />
        </menu>
    </item>
    <item
        android:id="@+id/menuItemBoardNavigationModifyUserInfo"
        android:checkable="true"
        android:icon="@drawable/person_24px"
        android:title="사용자 정보 수정"
        app:showAsAction="ifRoom" />
    <item
        android:id="@+id/menuItemBoardNavigationLogout"
        android:icon="@drawable/logout_24px"
        android:title="로그아웃"
        app:showAsAction="ifRoom" />
    <item
        android:id="@+id/menuItemBoardNavigationSignOut"
        android:icon="@drawable/move_item_24px"
        android:title="회원탈퇴"
        app:showAsAction="ifRoom" />
</menu>
```

### 메뉴를 구성하는 코드를 넣어준다.

[fragment/BoardMainFragment.kt]
```kt
            // 메뉴 구성
            navigationViewBoardMain.inflateMenu(R.menu.menu_board_main_navigation)
            navigationViewBoardMain.setCheckedItem(R.id.menuItemBoardNavigationAll)
            navigationViewBoardMain.setNavigationItemSelectedListener {

                drawerLayoutBoardMain.close()
                true
            }
```

---

# 14_게시글 목록 화면 구성

### 테스트 용 코드 삭제

[fragment/BoardListFragment.kt]
```kt
        fragmentBoardListBinding.button.setOnClickListener {
            boardMainFragment.fragmentBoardMainBinding.drawerLayoutBoardMain.open()
        }
```

### 화면 구성
- Material 가이드에 나오는 검색 기능
- SearchBar를 누르면 화면이 바뀌면서 검색할 수 있도록 한다.
- CoordinatorLayout 안에 SearchBar와 SearchView를 배치해야 한다
- SearchView의 layout_anchor 속성에 SearchBar의 id를 지정하여 둘을 연결시켜줘야 한다.

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="boardListViewModel"
            type="com.lion.boardproject.viewmodel.BoardListViewModel" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:transitionGroup="true" >

        <com.google.android.material.appbar.MaterialToolbar
            android:id="@+id/toolbarBoardList"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@android:color/transparent"
            android:minHeight="?attr/actionBarSize"
            android:theme="?attr/actionBarTheme"
            app:navigationIcon="@drawable/menu_24px" />

        <androidx.coordinatorlayout.widget.CoordinatorLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <com.google.android.material.search.SearchBar
                android:id="@+id/searchBarBoardList"
                android:layout_width="match_parent"
                android:layout_height="wrap_content" />

            <androidx.recyclerview.widget.RecyclerView
                android:id="@+id/recyclerViewBoardListMain"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:layout_marginTop="90dp" />

            <com.google.android.material.search.SearchView
                android:id="@+id/searchViewBoardList"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                app:layout_anchor="@id/searchBarBoardList">

                <androidx.recyclerview.widget.RecyclerView
                    android:id="@+id/recyclerViewBoardListResult"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent" />
            </com.google.android.material.search.SearchView>
        </androidx.coordinatorlayout.widget.CoordinatorLayout>
    </LinearLayout>
</layout>

```

### Recyclerview의 항목에서 사용할 ViewModel 클래스와 layout 파일을 만들어준다.
- viewmodel/RowBoardListViewModel.kt
- res/layout/row_board_list.xml

### ViewModel 클래스를 작성한다.
[viewmodel/RowBoardListViewModel.kt]
```kt
class RowBoardListViewModel(val boardListFragment: BoardListFragment) : ViewModel(){
    // textViewRowBoardListTitle - text
    val textViewRowBoardListTitleText = MutableLiveData("")
    // textViewRowBoardListNickName - text
    val textViewRowBoardListNickNameText = MutableLiveData("")
}
```

### layout을 작성해준다.
[res/layout/row_board_list.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android">

    <data>
        <variable
            name="rowBoardListViewModel"
            type="com.lion.boardproject.viewmodel.RowBoardListViewModel" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="10dp"
        android:transitionGroup="true">

        <TextView
            android:id="@+id/textViewRowBoardListTitle"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{rowBoardListViewModel.textViewRowBoardListTitleText}"
            android:textAppearance="@style/TextAppearance.AppCompat.Large" />

        <TextView
            android:id="@+id/textViewRowBoardListNickName"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:text="@{rowBoardListViewModel.textViewRowBoardListNickNameText}" />
    </LinearLayout>
</layout>


```

### Toolbar에 title 속성과 연결할 LiveData를 정의한다.

[viewmodel/BoardListViewModel.kt]
```kt
    // toolbarBoardList - title
    val toolbarBoardListTitle = MutableLiveData("")
```

### Toolbar의  title 속성에 LiveData를 연결해준다.

[res/layout/fragment_board_list.xml]
```xml
app:title="@{boardListViewModel.toolbarBoardListTitle}"
```

### Toolbar를 구성하는 메서드를 구현한다.

[fragment/BoardListFragment.kt]
```kt
    // 툴바를 구성하는 메서드
    fun settingToolbar(){
        // 타이틀
        fragmentBoardListBinding.boardListViewModel?.toolbarBoardListTitle?.value = "임시게시판이름"
    }
```

### 메서드를 호출한다.

[fragment/BoardListFragment.kt - onCreateView()]
```kt
        // 툴바를 구성하는 메서드를 호출한다.
        settingToolbar()
```

### 네비게이션 뷰를 보여주는 메서드를 구현한다.

[fragment/BoardMainFragment.kt]
```kt
    // NavigationView를 보여주는 메서드
    fun showNavigationView(){
        fragmentBoardMainBinding.drawerLayoutBoardMain.open()
    }
```

### ViewModel에 메서드를 구현해준다.
[viewmodel/BoardListViewModel.kt]
```kt
    companion object{
        // toolbarBoardList - onNavigationClickBoardList
        @JvmStatic
        @BindingAdapter("onNavigationClickBoardList")
        fun onNavigationClickBoardList(materialToolbar: MaterialToolbar, boardListFragment: BoardListFragment){
            materialToolbar.setNavigationOnClickListener {
                boardListFragment.boardMainFragment.showNavigationView()
            }
        }
    }
```

### 툴바에 적용해준다.

[res/layout/fragment_board_list.xml]
```xml
        <com.google.android.material.appbar.MaterialToolbar
            android:id="@+id/toolbarBoardList"
            ...
            app:onNavigationClickBoardList="@{boardListViewModel.boardListFragment}"/>
```

### SearchBar에서 사용할 메뉴 파일을 만들어준다.
[res/menu/menu_board_list_searchbar.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/menuItemBoardListAdd"
        android:icon="@drawable/add_24px"
        android:title="글쓰기"
        app:showAsAction="always" />
</menu>
```

### SearchBar에 hint와 menu를 설정한다
[res/layout/fragment_board_list.xml]
```xml
            <com.google.android.material.search.SearchBar
                android:id="@+id/searchBarBoardList"
                ...
                android:hint="검색어를 입력해주세요"
                app:menu="@menu/menu_board_list_searchbar" />
```

### SearchView에 hint를 설정해준다.
[res/layout/fragment_board_list.xml]
```xml

            <com.google.android.material.search.SearchView
                ...
                android:hint="검색어를 입력해주세요">
```

### 메인 RecyclerView의 어뎁터를 작성한다.
[fragment/BoardListFragment.kt]
```kt
    // 메인 RecyclerView의 어뎁터
    inner class MainRecyclerViewAdapter : RecyclerView.Adapter<MainRecyclerViewAdapter.MainViewHolder>(){
        inner class MainViewHolder(val rowBoardListBinding: RowBoardListBinding) : RecyclerView.ViewHolder(rowBoardListBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val rowBoardListBinding = DataBindingUtil.inflate<RowBoardListBinding>(layoutInflater, R.layout.row_board_list, parent, false)
            rowBoardListBinding.rowBoardListViewModel = RowBoardListViewModel(this@BoardListFragment)
            rowBoardListBinding.lifecycleOwner = this@BoardListFragment

            val mainViewHolder = MainViewHolder(rowBoardListBinding)
            return mainViewHolder
        }

        override fun getItemCount(): Int {
            return tempList1.size
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            holder.rowBoardListBinding.rowBoardListViewModel?.textViewRowBoardListTitleText?.value = tempList1[position]
            holder.rowBoardListBinding.rowBoardListViewModel?.textViewRowBoardListNickNameText?.value = tempList2[position]
        }
    }
```

### 검색 결과 RecyclerView를 작성해준다.

[fragment/BoardListFragment.kt]
```kt
    // 검색결과 RecyclerView의 어뎁터
    inner class ResultRecyclerViewAdapter : RecyclerView.Adapter<ResultRecyclerViewAdapter.ResultViewHolder>(){
        inner class ResultViewHolder(val rowBoardListBinding: RowBoardListBinding) : RecyclerView.ViewHolder(rowBoardListBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
            val rowBoardListBinding = DataBindingUtil.inflate<RowBoardListBinding>(layoutInflater, R.layout.row_board_list, parent, false)
            rowBoardListBinding.rowBoardListViewModel = RowBoardListViewModel(this@BoardListFragment)
            rowBoardListBinding.lifecycleOwner = this@BoardListFragment

            val resultViewHolder = ResultViewHolder(rowBoardListBinding)
            return resultViewHolder
        }

        override fun getItemCount(): Int {
            return tempList1.size
        }

        override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
            holder.rowBoardListBinding.rowBoardListViewModel?.textViewRowBoardListTitleText?.value = tempList1[position]
            holder.rowBoardListBinding.rowBoardListViewModel?.textViewRowBoardListNickNameText?.value = tempList2[position]
        }
    }
```

### RecyclerView를 구성하는 메서드를 작성해준다.
[fragment/BoardListFragment.kt]
```kt
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
```

### 메서드를 호출한다.
[fragment/BoardListFragment.kt - onCreateView()]
```kt
        // 메인 RecyclerView 구성 메서드를 호출한다.
        settingMainRecyclerView()
        // 검색 결과 RecyclerView 구성 메서드를 호출한다.
        settingResultRecyclerView()
```

---

# 15_글 작성 화면 구성하기

### 글 작성을 위한 Fragment와 ViewModel을 만들어준다.
- fragment/BoardWriteFragment.kt
- viewmodel/BoardWriteViewModel.kt

### ViewModel에 기본 코드를 작성한다.

[viewmodel/BoardWriteViewModel.kt]
```kt
class BoardWriteViewModel(val boardWriteFragment: BoardWriteFragment) : ViewModel() {
}
```

### layout 코드를 작성한다.
[res/layout/fragment_board_write.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
    <data>
        <variable
            name="boardWriteViewModel"
            type="com.lion.boardproject.viewmodel.BoardWriteViewModel" />
    </data>

    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".fragment.BoardWriteFragment">

        <!-- TODO: Update blank fragment layout -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:text="@string/hello_blank_fragment" />

    </FrameLayout>
</layout>

```

### Fragment의 기본 코드를 작성한다.

[fragment/BoardWriteFragment.kt]
```kt

class BoardWriteFragment(val boardMainFragment: BoardMainFragment) : Fragment() {

    lateinit var fragmentBoardWriteBinding: FragmentBoardWriteBinding
    lateinit var boardActivity: BoardActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentBoardWriteBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_board_write, container, false)
        fragmentBoardWriteBinding.boardWriteViewModel = BoardWriteViewModel(this@BoardWriteFragment)
        fragmentBoardWriteBinding.lifecycleOwner = this@BoardWriteFragment

        boardActivity = activity as BoardActivity

        return fragmentBoardWriteBinding.root
    }
}
```

### Fragment 이름을 작성해준다.
[fragment/BoardMainFragment.kt - BoardSubFragmentName]
```kt
    // 게시글 작성 화면
    BOARD_WRITE_FRAGMENT(2, "BoardWriteFragment"),
```

### Fragment 객체를 생성한다.
[fragment/BoardMainFragment.kt]
```kt
    // 프래그먼트를 교체하는 함수
    fun replaceFragment(fragmentName: BoardSubFragmentName, isAddToBackStack:Boolean, animate:Boolean, dataBundle: Bundle?){
       ...
        newFragment = when(fragmentName){
        ...
            // 게시글 작성 화면
            BoardSubFragmentName.BOARD_WRITE_FRAGMENT -> BoardWriteFragment(this@BoardMainFragment)
        }
        ...
    }
```

### + 메뉴를 누르면 BoardWriteFragment로 이동하는 메서드를 만든다.
[fragment/BoardListFragment.kt]
```kt
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
```

### 메서드를 호출한다.

[fragment/BoardListFragment.kt - onCreateView()]
```kt
        // SearchBar를 구성하는 메서드
        settingSearchBar()
```

### Toolbar에 배치할 메뉴 파일을 작성해준다.
[res/menu/menu_board_write_toolbar.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <item android:title="카메라"
        android:id="@+id/menuItemBoardWriteCamera"
        android:icon="@drawable/photo_camera_24px"
        app:showAsAction="always"/>

    <item android:title="앨범"
        android:id="@+id/menuItemBoardWriteAlbum"
        android:icon="@drawable/photo_album_24px"
        app:showAsAction="always"/>

    <item android:title="초기화"
        android:id="@+id/menuItemBoardWriteReset"
        android:icon="@drawable/restart_alt_24px"
        app:showAsAction="always"/>

    <item android:title="완료"
        android:id="@+id/menuItemBoardWriteDone"
        android:icon="@drawable/done_24px"
        app:showAsAction="always"/>

</menu>
```

### 이전화면으로 돌아가는 메서드를 구현한다.
[fragment/BoardWriteFragment.kt]
```kt
    // 이전 화면으로 돌아간다.
    fun movePrevFragment(){
        boardMainFragment.removeFragment(BoardSubFragmentName.BOARD_WRITE_FRAGMENT)
    }
```

### ViewModel에서 navigation icon과 연결할 속성을 만들어준다.
[viewmodel/BoardWriteViewModel.kt]
```kt
    companion object{
        // toolbarBoardWrite - onNavigationClickBoardWrite
        @JvmStatic
        @BindingAdapter("onNavigationClickBoardWrite")
        fun onNavigationClickBoardWrite(materialToolbar: MaterialToolbar, boardWriteFragment: BoardWriteFragment){
            materialToolbar.setNavigationOnClickListener {
                boardWriteFragment.movePrevFragment()
            }
        }
    }
```

### 화면을 구성해준다.
[res/layout/fragment_board_write.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">
    <data>
        <variable
            name="boardWriteViewModel"
            type="com.lion.boardproject.viewmodel.BoardWriteViewModel" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:transitionGroup="true" >

        <com.google.android.material.appbar.MaterialToolbar
            android:id="@+id/toolbarBoardWrite"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@android:color/transparent"
            android:minHeight="?attr/actionBarSize"
            android:theme="?attr/actionBarTheme"
            app:menu="@menu/menu_board_write_toolbar"
            app:navigationIcon="@drawable/arrow_back_24px"
            app:title="글 작성"
            app:onNavigationClickBoardWrite="@{boardWriteViewModel.boardWriteFragment}"/>

        <ScrollView
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical"
                android:padding="10dp" >

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldBoardWriteTitle"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:hint="제목"
                    app:endIconMode="clear_text"
                    app:startIconDrawable="@drawable/subject_24px">

                    <com.google.android.material.textfield.TextInputEditText
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:singleLine="true"
                        android:textAppearance="@style/TextAppearance.AppCompat.Large" />
                </com.google.android.material.textfield.TextInputLayout>

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldBoardWriteText"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:hint="내용"
                    app:endIconMode="clear_text"
                    app:startIconDrawable="@drawable/description_24px"
                    android:layout_marginTop="10dp">

                    <com.google.android.material.textfield.TextInputEditText
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:textAppearance="@style/TextAppearance.AppCompat.Large" />
                </com.google.android.material.textfield.TextInputLayout>

                <com.google.android.material.button.MaterialButtonToggleGroup
                    android:id="@+id/buttonGroupBoardWriteType"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    app:checkedButton="@id/buttonBoardWriteType1"
                    app:selectionRequired="true"
                    app:singleSelection="true">

                    <Button
                        android:id="@+id/buttonBoardWriteType1"
                        style="@style/Widget.Material3.Button.OutlinedButton"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:text="자유" />

                    <Button
                        android:id="@+id/buttonBoardWriteType2"
                        style="@style/Widget.Material3.Button.OutlinedButton"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:text="유머" />

                    <Button
                        android:id="@+id/buttonBoardWriteType3"
                        style="@style/Widget.Material3.Button.OutlinedButton"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:text="시사" />

                    <Button
                        android:id="@+id/buttonBoardWriteType4"
                        style="@style/Widget.Material3.Button.OutlinedButton"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:text="운동" />
                </com.google.android.material.button.MaterialButtonToggleGroup>

                <ImageView
                    android:id="@+id/imageViewBoardWrite"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    android:adjustViewBounds="true"
                    android:src="@drawable/panorama_24px" />

            </LinearLayout>
        </ScrollView>
    </LinearLayout>
</layout>

```

### 화면 요소의 속성과 연결할 LiveData를 정의해준다.
[viewmodel/BoardWriteViewModel.kt]
```kt
    // textFieldBoardWriteTitle - text
    val textFieldBoardWriteTitleText = MutableLiveData("")
    // textFieldBoardWriteText - text
    val textFieldBoardWriteTextText = MutableLiveData("")\
```

### Live데이터와 뷰의 속성을 연결해준다.
[res/layout/fragment_board_write.xml]
```xml
                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldBoardWriteTitle"
                   ...
                    <com.google.android.material.textfield.TextInputEditText
                      ...
                        android:text="@={boardWriteViewModel.textFieldBoardWriteTitleText}"/>
                </com.google.android.material.textfield.TextInputLayout>

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldBoardWriteText"
                    ...
                    <com.google.android.material.textfield.TextInputEditText
                        ...
                        android:text="@={boardWriteViewModel.textFieldBoardWriteTextText}"/>
                </com.google.android.material.textfield.TextInputLayout>

```

---

# 16_카메라와 앨범에서 사진 선택 구성하기

### 툴바를 구성하는 메서드를 작성한다.

[fragment/BoardWriteFragment.kt]
```kt
    // Toolbar를 구성하는 메서드
    fun settingToolbar(){
        fragmentBoardWriteBinding.apply {
            // 메뉴의 항목을 눌렀을 때
            toolbarBoardWrite.setOnMenuItemClickListener {
                when(it.itemId){
                    // 카메라
                    R.id.menuItemBoardWriteCamera -> {

                    }
                    // 앨범
                    R.id.menuItemBoardWriteAlbum -> {

                    }
                    // 초기화
                    R.id.menuItemBoardWriteReset -> {

                    }
                    // 완료
                    R.id.menuItemBoardWriteDone -> {

                    }
                }
                true
            }
        }
    }
```

### 호출한다.

[fragment/BoardWriteFragment.kt - onCreateView()]
```kt
        // Toolbar를 구성하는 메서드를 호출한다.
        settingToolbar()
```

### xml 폴더에 xml 파일을 만들어준다.
[res/xml/file_path.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-path
        name="storage/emulated/0"
        path="."/>
</paths>
```

### AndroidManifest.xml 에 provider를 등록한다.
[AndroidManifest.xml]
```xml
        <!-- 촬영된 사진을 가져올 수 있는 프로바이더 -->
        <provider
            android:authorities="com.lion.boardproject.camera"
            android:name="androidx.core.content.FileProvider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_path"/>
        </provider>
```

### 사진 데이터를 수정하는 메서드를 추가해준다.
[BoardActivity.kt]
```kt

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
```

### 필요한 변수들을 정의해준다.

```kt
    // 카메라나 앨범을 사용하는 Fragment를 받을 변수
    var pictureFragment:Fragment? = null
    // 촬영된 사진이 위치할 경로
    lateinit var filePath:String
    // 저장된 파일에 접근하기 위한 Uri
    lateinit var contentUri:Uri
    // 사진 촬영을 위한 런처
    lateinit var cameraLauncher:ActivityResultLauncher<Intent>
```

###  외부 저장소까지의 경로를 가져온다.
[BoardActivity.kt - onCreate()]
```kt
        // 외부 저장소 경로를 가져온다.
        filePath = getExternalFilesDir(null).toString()
```

### 카메라 실행을 위한 런처를 구성하는 메서드를 구현한다.
[BoardActivity.kt]
```kt
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
                    }
                }
    
                // 사진 파일은 삭제한다.
                val file = File(contentUri.path!!)
                file.delete()
            }
        }
    }
```

### 메서드를 호출한다.
[BoardActivity.kt - onCreate()]
```kt
        
        // 카메라 실행을 위한 런처를 구성하는 메서드를 호출한다.
        settingCameraLauncher()
```

### 카메라 런처를 실행하는 메서드를 구현한다.
[BoardActivity.kt]
```kt

    // 카메라를 실행시키는 메서드
    fun startCameraLauncher(fragment:Fragment){
        pictureFragment = fragment

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // 촬영한 사진이 저장될 파일 이름
        val fileName = "/temp_${System.currentTimeMillis()}.jpg"
        // 경로 + 파일이름
        val picPath = "${filePath}${fileName}"
        val file = File(picPath)

        // 사진이 저장될 위치를 관리하는 Uri 객체를 생성ㅎ
        contentUri = FileProvider.getUriForFile(this@BoardActivity, "com.lion.boardproject.camera", file)

        // Activity를 실행한다.
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
        cameraLauncher.launch(cameraIntent)
    }
```

### 카메라 화면을 실행시킨다.
[fragment/BoardWriteFragment.kt - settingToolbar()]
```kt
                    // 카메라
                    R.id.menuItemBoardWriteCamera -> {
                        boardMainFragment.boardActivity.startCameraLauncher(this@BoardWriteFragment)
                    }
```

### 앨범을 위한 런처를 선언한다.
[BoardActivity.kt]
```kt
    // 앨범을 위한 런처
    lateinit var albumLauncher:ActivityResultLauncher<PickVisualMediaRequest>
```

### 런처를 구성하는 메서드를 구현한다.
[BoardActivity.kt]
```kt

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
                // 이미지의 회전 각도값을 가져온다.
                // val degree = getDegree(it)
                // 회전 값을 이용해 이미지를 회전시킨다.
                // val rotateBitmap = rotateBitmap(bitmap!!, degree)
                // 크기를 조정한 이미지를 가져온다.
                // val resizeBitmap = resizeBitmap(1024, rotateBitmap)

                // 현재 프래그먼트가 무엇인지 분기한다.
                if(pictureFragment != null){
                    // 글을 작성하는 Fragment라면
                    if(pictureFragment is BoardWriteFragment){
                        val f1 = pictureFragment as BoardWriteFragment
                        // 이미지 뷰에 설정해준다.
                        f1.fragmentBoardWriteBinding.imageViewBoardWrite.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }
```

### 런처를 가동시키는 메서드를 구현한다.
[BoardActivity.kt]
```kt
    fun startAlbumLauncher(fragment:Fragment){
        pictureFragment = fragment

        // Activity를 실행한다.
        val pickVisualMediaRequest = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        albumLauncher.launch(pickVisualMediaRequest)
    }
```

### 메서드를 호출한다.
[BoardActivity - onCreate()]
```kt
        // 앨범에서 사진을 가져오는 런처 구성하는 메서드를 호출한다.
        settingAlbumLauncher()
```

### 런처를 가동시키는 메서드를 호출한다.
[fragment/BoardWriteFragment.kt - settingToolbar()]

```kt
                    // 앨범
                    R.id.menuItemBoardWriteAlbum -> {
                        boardMainFragment.boardActivity.startAlbumLauncher(this@BoardWriteFragment)
                    }
```

---

# 17_글 읽는 화면 구성

### Frgment와 ViewModel을 생성한다.
- fragment/BoardReadFragment.kt
- viewmodel/BoardReadViewModel.kt

### layout 파일을 작성한다.
[res/layout/fragment_board_read.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
    <data>
        <variable
            name="boardReadViewModel"
            type="com.lion.boardproject.viewmodel.BoardReadViewModel" />
    </data>

    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".fragment.BoardReadFragment">

        <!-- TODO: Update blank fragment layout -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:text="@string/hello_blank_fragment" />

    </FrameLayout>
</layout>

```

### ViewModel 기본 코드를 작성한다.
[viewmodel/BoardReadViewModel.kt]
```kt
class BoardReadViewModel(val boardReadFragment: BoardReadFragment) : ViewModel() {
}
```

### Fragment 기본 코드를 작성한다
[fragment/BoardReadFragment.kt]
```kt
class BoardReadFragment(val boardMainFragment: BoardMainFragment) : Fragment() {

    lateinit var fragmentBoardReadBinding: FragmentBoardReadBinding
    lateinit var boardActivity: BoardActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentBoardReadBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_board_read, container, false)
        fragmentBoardReadBinding.boardReadViewModel = BoardReadViewModel(this@BoardReadFragment)
        fragmentBoardReadBinding.lifecycleOwner = this@BoardReadFragment

        boardActivity = activity as BoardActivity

        return fragmentBoardReadBinding.root
    }
}
```

### Fragment 이름을 정의해준다.
[fragment/BoardMainFragment.kt - BoardSubFragmentName]
```kt
    // 게시글 읽기  화면
    BOARD_READ_FRAGMENT(3, "BoardReadFragment"),
```

### Fragment 객체를 생성한다.
[fragment/BoardReadFragment.kt - replaceFragment()]
```kt
            // 게시글 읽는 화면
            BoardSubFragmentName.BOARD_READ_FRAGMENT -> BoardReadFragment(this@BoardMainFragment)
```

### BoardReadFragment를 띄워준다.
[fragment/BoardWriteFragment.kt - settingToolbar()]
```kt
                    // 완료
                    R.id.menuItemBoardWriteDone -> {
                        boardMainFragment.replaceFragment(BoardSubFragmentName.BOARD_READ_FRAGMENT, true, true, null)
                    }
```

### 화면을 구성해준다.
[res/layout/fragment_board_read.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <data>
        <variable
            name="boardReadViewModel"
            type="com.lion.boardproject.viewmodel.BoardReadViewModel" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:transitionGroup="true">

        <com.google.android.material.appbar.MaterialToolbar
            android:id="@+id/toolbarBoardRead"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@android:color/transparent"
            android:minHeight="?attr/actionBarSize"
            android:theme="?attr/actionBarTheme"
            app:navigationIcon="@drawable/arrow_back_24px"
            app:title="글 읽기"/>

        <ScrollView
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical"
                android:padding="10dp" >

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldBoardReadTitle"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:hint="제목"
                    app:startIconDrawable="@drawable/subject_24px">

                    <com.google.android.material.textfield.TextInputEditText
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:singleLine="true"
                        android:textAppearance="@style/TextAppearance.AppCompat.Large"
                        android:enabled="false"
                        android:textColor="@color/black"
                        android:text="ㅁㅁㅁㅁㅁㅁ"/>
                </com.google.android.material.textfield.TextInputLayout>

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldBoardReadType"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:layout_marginTop="10dp"
                    android:hint="게시판"
                    app:startIconDrawable="@drawable/format_list_bulleted_24px">

                    <com.google.android.material.textfield.TextInputEditText
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:singleLine="true"
                        android:textAppearance="@style/TextAppearance.AppCompat.Large"
                        android:enabled="false"
                        android:textColor="@color/black"
                        android:text="ㅁㅁㅁㅁㅁㅁ" />
                </com.google.android.material.textfield.TextInputLayout>

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldBoardReadText"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:layout_marginTop="10dp"
                    android:hint="내용"
                    app:startIconDrawable="@drawable/description_24px">

                    <com.google.android.material.textfield.TextInputEditText
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:textAppearance="@style/TextAppearance.AppCompat.Large"
                        android:enabled="false"
                        android:textColor="@color/black"
                        android:text="ㅁㅁㅁㅁㅁㅁ" />
                </com.google.android.material.textfield.TextInputLayout>

                <ImageView
                    android:id="@+id/imageViewBoardRead"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    android:adjustViewBounds="true"
                    android:src="@drawable/panorama_24px" />

            </LinearLayout>
        </ScrollView>
    </LinearLayout>
</layout>

```

### LiveData를 정의해준다.
[viewmodel/BoardReadViewModel.kt]
```kt
    // textFieldBoardReadTitle - text
    val textFieldBoardReadTitleText = MutableLiveData(" ")
    // textFieldBoardReadType - text
    val textFieldBoardReadTypeText = MutableLiveData(" ")
    // textFieldBoardReadText - text
    val textFieldBoardReadTextText = MutableLiveData(" ")
```

### view의 속성에 LiveData를 설정해준다.
[res/layout/fragment_board_read.xml]
```xml
                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldBoardReadTitle"
                    ...

                    <com.google.android.material.textfield.TextInputEditText
                        ...
                        android:text="@{boardReadViewModel.textFieldBoardReadTitleText}"/>
                </com.google.android.material.textfield.TextInputLayout>

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldBoardReadType"
                   ...
                    <com.google.android.material.textfield.TextInputEditText
                        ...
                        android:text="@{boardReadViewModel.textFieldBoardReadTypeText}" />
                </com.google.android.material.textfield.TextInputLayout>

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldBoardReadText"
                    ...
                    <com.google.android.material.textfield.TextInputEditText
                        ...
                        android:text="@{boardReadViewModel.textFieldBoardReadTextText}" />
                </com.google.android.material.textfield.TextInputLayout>

```

### 이전으로 돌아가는 메서드를 구현해준다.

[fragment/BoardReadFragment.kt]
```kt
    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        boardMainFragment.removeFragment(BoardSubFragmentName.BOARD_WRITE_FRAGMENT)
    }
```

### ViewModel에 Toolbar의 Navigation Icon을 눌렀을 때의 속성을 정의해준다.
[viewmodel/BoardReadViewModel.kt]
```kt
    companion object{
        // toolbarBoardRead - onNavigationClickBoardRead
        @JvmStatic
        @BindingAdapter("onNavigationClickBoardRead")
        fun onNavigationClickBoardRead(materialToolbar: MaterialToolbar, boardReadFragment: BoardReadFragment){
            materialToolbar.setNavigationOnClickListener {
                boardReadFragment.movePrevFragment()
            }
        }
    }
```

### Toolbar에 속성을 추가해준다.
[res/layout/fragment_board_read.xml]
```xml
        <com.google.android.material.appbar.MaterialToolbar
            android:id="@+id/toolbarBoardRead"
            ...
            app:onNavigationClickBoardRead="@{boardReadViewModel.boardReadFragment}"/>
```

### 메뉴 파일을 작성해준다.
[res/menu/menu_board_read_toolbar.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <item
        android:id="@+id/menuItemBoardReadModify"
        android:icon="@drawable/edit_24px"
        android:title="수정하기"
        app:showAsAction="always"/>

    <item
        android:id="@+id/menuItemBoardReadDelete"
        android:icon="@drawable/delete_24px"
        android:title="삭제하기"
        app:showAsAction="always"/>
</menu>
```

### 메뉴를 적용해준다.
```xml
        <com.google.android.material.appbar.MaterialToolbar
            android:id="@+id/toolbarBoardRead"
            ...
            app:menu="@menu/menu_board_read_toolbar"/>
```

---

# 18_글 수정 화면 구성

### Fragment와 ViewModel을 만들어준다.
- fragment/BoardModifyFragment.kt
- viewmodel/BoardModifyViewModel.kt

### Fragment의 이름을 정의해준다.
[fragment/BoardMainFragment.kt - BoardSubFragmentName]
```kt
    // 게시글 수정 화면
    BOARD_MODIFY_FRAGMENT(4, "BoardModifyFragment"),
```

### Fragment 객체를 생성한다.
[fragment/BoardMainFragment.kt - replaceFragment()]
```kt
            // 게시글 수정 화면
            BoardSubFragmentName.BOARD_MODIFY_FRAGMENT -> BoardModifyFragment(this@BoardMainFragment)
```

### layout 기본 코드를 작성한다.
[res/layout/fragment_board_modify.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>

<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
    <data>
        <variable
            name="boardModifyViewModel"
            type="com.lion.boardproject.viewmodel.BoardModifyViewModel" />
    </data>


    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".fragment.BoardModifyFragment">

        <!-- TODO: Update blank fragment layout -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:text="@string/hello_blank_fragment" />

    </FrameLayout>
</layout>

```

### ViewModel 기본 코드를 작성한다.
[viewmodel/BoardModifyViewModel.kt]
```kt
class BoardModifyViewModel(val boardModifyFragment: BoardModifyFragment) : ViewModel() {
}
```

### Fragment 기본 코드를 작성한다.
[fragment/BoardModifyFragment.kt]
```kt
class BoardModifyFragment(val boardMainFragment: BoardMainFragment) : Fragment() {

    lateinit var fragmentBoardModifyBinding: FragmentBoardModifyBinding
    lateinit var boardActivity: BoardActivity
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentBoardModifyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_board_modify, container, false)
        fragmentBoardModifyBinding.boardModifyViewModel = BoardModifyViewModel(this@BoardModifyFragment)
        fragmentBoardModifyBinding.lifecycleOwner = this@BoardModifyFragment
        
        boardActivity = activity as BoardActivity
        
        return fragmentBoardModifyBinding.root
    }

}
```

### ViewModel 코드를 작성한다.
[viewmodel/BoardModifyViewModel.kt]
```kt
    
    // textFieldBoardModifyTitle - text
    val textFieldBoardModifyTitleText = MutableLiveData(" ")
    // textFieldBoardModifyText - text
    val textFieldBoardModifyTextText = MutableLiveData(" ")


    companion object{
        // toolbarBoardModify - onNavigationClickBoardModify
        @JvmStatic
        @BindingAdapter("onNavigationClickBoardModify")
        fun onNavigationClickBoardModify(materialToolbar: MaterialToolbar, boardModifyFragment: BoardModifyFragment){
            materialToolbar.setNavigationOnClickListener {
                boardModifyFragment.movePrevFragment()
            }
        }
    }
```

### 메뉴 파일을 작성한다.
[res/menu/menu_board_modify_toolbar.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <item android:title="카메라"
        android:id="@+id/menuItemBoardModifyCamera"
        android:icon="@drawable/photo_camera_24px"
        app:showAsAction="always"/>

    <item android:title="앨범"
        android:id="@+id/menuItemBoardModifyAlbum"
        android:icon="@drawable/photo_album_24px"
        app:showAsAction="always"/>

    <item android:title="초기화"
        android:id="@+id/menuItemBoardModifyReset"
        android:icon="@drawable/restart_alt_24px"
        app:showAsAction="always"/>

    <item android:title="완료"
        android:id="@+id/menuItemBoardModifyDone"
        android:icon="@drawable/done_24px"
        app:showAsAction="always"/>
</menu>
```

### layout 파일을 구성해준다.
[res/layout/fragment_board_modify.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>

<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <data>
        <variable
            name="boardModifyViewModel"
            type="com.lion.boardproject.viewmodel.BoardModifyViewModel" />
    </data>


    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:transitionGroup="true" >

        <com.google.android.material.appbar.MaterialToolbar
            android:id="@+id/toolbarBoardModify"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@android:color/transparent"
            android:minHeight="?attr/actionBarSize"
            android:theme="?attr/actionBarTheme"
            app:menu="@menu/menu_board_modify_toolbar"
            app:navigationIcon="@drawable/arrow_back_24px"
            app:title="글 수정"
            app:onNavigationClickBoardModify="@{boardModifyViewModel.boardModifyFragment}"/>

        <ScrollView
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical"
                android:padding="10dp" >

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldBoardModifyTitle"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:hint="제목"
                    app:endIconMode="clear_text"
                    app:startIconDrawable="@drawable/subject_24px">

                    <com.google.android.material.textfield.TextInputEditText
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:singleLine="true"
                        android:textAppearance="@style/TextAppearance.AppCompat.Large"
                        android:text="@={boardModifyViewModel.textFieldBoardModifyTitleText}"/>
                </com.google.android.material.textfield.TextInputLayout>

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldBoardModifyText"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:hint="내용"
                    app:endIconMode="clear_text"
                    app:startIconDrawable="@drawable/description_24px"
                    android:layout_marginTop="10dp">

                    <com.google.android.material.textfield.TextInputEditText
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:textAppearance="@style/TextAppearance.AppCompat.Large"
                        android:text="@={boardModifyViewModel.textFieldBoardModifyTextText}"/>
                </com.google.android.material.textfield.TextInputLayout>

                <com.google.android.material.button.MaterialButtonToggleGroup
                    android:id="@+id/buttonGroupBoardModifyType"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    app:checkedButton="@id/buttonBoardModifyType1"
                    app:selectionRequired="true"
                    app:singleSelection="true">

                    <Button
                        android:id="@+id/buttonBoardModifyType1"
                        style="@style/Widget.Material3.Button.OutlinedButton"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:text="자유" />

                    <Button
                        android:id="@+id/buttonBoardModifyType2"
                        style="@style/Widget.Material3.Button.OutlinedButton"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:text="유머" />

                    <Button
                        android:id="@+id/buttonBoardModifyType3"
                        style="@style/Widget.Material3.Button.OutlinedButton"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:text="시사" />

                    <Button
                        android:id="@+id/buttonBoardModifyType4"
                        style="@style/Widget.Material3.Button.OutlinedButton"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:text="운동" />
                </com.google.android.material.button.MaterialButtonToggleGroup>

                <ImageView
                    android:id="@+id/imageViewBoardModify"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    android:adjustViewBounds="true"
                    android:src="@drawable/panorama_24px" />
            </LinearLayout>
        </ScrollView>
    </LinearLayout>
</layout>

```

### Fragment의 코드를 작성한다.
[fragment/BoardModifyFragment.kt]
```kt

class BoardModifyFragment(val boardMainFragment: BoardMainFragment) : Fragment() {

    lateinit var fragmentBoardModifyBinding: FragmentBoardModifyBinding
    lateinit var boardActivity: BoardActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentBoardModifyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_board_modify, container, false)
        fragmentBoardModifyBinding.boardModifyViewModel = BoardModifyViewModel(this@BoardModifyFragment)
        fragmentBoardModifyBinding.lifecycleOwner = this@BoardModifyFragment

        boardActivity = activity as BoardActivity

        // Toolbar를 구성하는 메서드를 호출한다.
        settingToolbar()

        return fragmentBoardModifyBinding.root
    }


    // Toolbar를 구성하는 메서드
    fun settingToolbar(){
        fragmentBoardModifyBinding.apply {
            // 메뉴의 항목을 눌렀을 때
            toolbarBoardModify.setOnMenuItemClickListener {
                when(it.itemId){
                    // 카메라
                    R.id.menuItemBoardModifyCamera -> {
                        boardMainFragment.boardActivity.startCameraLauncher(this@BoardModifyFragment)
                    }
                    // 앨범
                    R.id.menuItemBoardModifyAlbum -> {
                        boardMainFragment.boardActivity.startAlbumLauncher(this@BoardModifyFragment)
                    }
                    // 초기화
                    R.id.menuItemBoardModifyReset -> {

                    }
                    // 완료
                    R.id.menuItemBoardModifyDone -> {
                        boardMainFragment.removeFragment(BoardSubFragmentName.BOARD_MODIFY_FRAGMENT)
                    }
                }
                true
            }
        }
    }

    // 이전 화면으로 돌아간다.
    fun movePrevFragment(){
        boardMainFragment.removeFragment(BoardSubFragmentName.BOARD_MODIFY_FRAGMENT)
    }
}
```

### 사진 촬영 런처 실행 메서드를 수정한다

[BoardActivity.kt - settingCameraLauncher()]
```kt
                    // 글을 수정하는 Fragment 라면
                    else if(pictureFragment is BoardModifyFragment){
                        val f1 = pictureFragment as BoardModifyFragment
                        f1.fragmentBoardModifyBinding.imageViewBoardModify.setImageBitmap(resizeBitmap)
                    }
```

### 엘범 런처 실행 메서드를 수정한다

[BoardActivity.kt - settingAlbumLauncher()]
```kt
                    // 글을 수정하는 Fragment 라면
                    else if(pictureFragment is BoardModifyFragment){
                        val f1 = pictureFragment as BoardModifyFragment
                        f1.fragmentBoardModifyBinding.imageViewBoardModify.setImageBitmap(resizeBitmap)
                    }
```

### BoardReadFragment에서 툴바를 구성하는 메서드를 구현한다.
[fragment/BoardReadFragment.kt]
```kt
    // 툴바를 구성하는 메서드
    fun settingToolbar(){
        fragmentBoardReadBinding.apply {
            toolbarBoardRead.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.menuItemBoardReadModify -> {
                        boardMainFragment.replaceFragment(BoardSubFragmentName.BOARD_MODIFY_FRAGMENT, true, true, null)
                    }
                    R.id.menuItemBoardReadDelete -> {
                        
                    }
                }
                true
            }
        }
    }
```

### 메서드를 호출한다.
[fragment/BoardReadFragment - onCreateView()]
```kt
        // 툴바를 구성하는 메서드를 호출한다.
        settingToolbar()
```

---

# 19_글 목록 화면에서 항목을 눌렀을 때 처리

### Recyclerview의 항목으로 사용하는 layout에 배경을 지정한다

```xml

    <LinearLayout
        ...
        android:background="?attr/selectableItemBackground">
```

### 어뎁터에서 항목을 눌렀을때를 구현해준다.

[fragment/BoardListFragment.kt - MainRecyclerViewAdapter]
[fragment/BoardListFragment.kt - ResultRecyclerViewAdapter]
```kt
            rowBoardListBinding.root.setOnClickListener { 
                boardMainFragment.replaceFragment(BoardSubFragmentName.BOARD_READ_FRAGMENT, true, true, null)
            }
```

### BoardReadFragment에서 뒤로가기 메서드를 수정한다.

[fragment/BoardReadFragment.kt]
```kt
    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        boardMainFragment.removeFragment(BoardSubFragmentName.BOARD_WRITE_FRAGMENT)
        boardMainFragment.removeFragment(BoardSubFragmentName.BOARD_READ_FRAGMENT)
    }
```

---

# 20_회원 정보 수정 화면 구성

### NavigationView의 메뉴를 눌렀을 때를 수정한다.
[fragment/BoardMainFragment.kt - settingNavigationView()]
```kt
                when(it.itemId){
                    // 전체 게시판
                    R.id.menuItemBoardNavigationAll -> replaceFragment(BoardSubFragmentName.BOARD_LIST_FRAGMENT, false, false, null)
                    // 자유 게시판
                    R.id.menuItemBoardNavigation1 -> replaceFragment(BoardSubFragmentName.BOARD_LIST_FRAGMENT, false, false, null)
                    // 유머 게시판
                    R.id.menuItemBoardNavigation2 -> replaceFragment(BoardSubFragmentName.BOARD_LIST_FRAGMENT, false, false, null)
                    // 시사 게시판
                    R.id.menuItemBoardNavigation3 -> replaceFragment(BoardSubFragmentName.BOARD_LIST_FRAGMENT, false, false, null)
                    // 운동 게시판
                    R.id.menuItemBoardNavigation4 -> replaceFragment(BoardSubFragmentName.BOARD_LIST_FRAGMENT, false, false, null)
                    // 사용자 정보 수정
                    R.id.menuItemBoardNavigationModifyUserInfo -> {

                    }
                    // 로그 아웃
                    R.id.menuItemBoardNavigationLogout -> {

                    }
                    // 회원 탈퇴
                    R.id.menuItemBoardNavigationSignOut -> {
                        
                    }
                }
```

### Fragment와 ViewModel을 만들어준다.
- fragment/UserModifyFragment
- viewmodel/UserModifyViewModel

### layout 기본 코드를 작성한다.
[res/layoutfragment_user_modify.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>

<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="userModifyViewModel"
            type="com.lion.boardproject.viewmodel.UserModifyViewModel" />
    </data>

    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".fragment.UserModifyFragment">

        <!-- TODO: Update blank fragment layout -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:text="@string/hello_blank_fragment" />

    </FrameLayout>

</layout>
```

### ViewModel 기본 코드를 작성한다.
[viewmodel/UserModifyViewModel.kt]
```kt
class UserModifyViewModel(val userModifyFragment: UserModifyFragment) : ViewModel() {
}
```

### Fragment 기본 코드를 작성한다
[fragment/UserModifyFragment.kt]
```kt

class UserModifyFragment(val boardMainFragment: BoardMainFragment) : Fragment() {

    lateinit var fragmentUserModifyBinding: FragmentUserModifyBinding
    lateinit var boardActivity: BoardActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        
        fragmentUserModifyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_modify, container, false)
        fragmentUserModifyBinding.userModifyViewModel = UserModifyViewModel(this@UserModifyFragment)
        fragmentUserModifyBinding.lifecycleOwner = this@UserModifyFragment
        
        boardActivity = activity as BoardActivity
        
        return fragmentUserModifyBinding.root
    }
}
```

### Fragment의 이름을 정의해준다.
[fragment/BoardMainFragment.kt]
```kt
    // 사용자 정보 수정 화면
    USER_MODIFY_FRAGMENT(5, "UserModifyFragment"),
```

### Fragment 객체를 생성한다.
[fragment/UserModifyFragment.kt]
```kt
            // 사용자 정보 수정 화면
            BoardSubFragmentName.USER_MODIFY_FRAGMENT -> UserModifyFragment(this@BoardMainFragment)
```

### NavigationView에서 정보수정 메뉴를 누르면 정보 수정화면이 나오도록 한다.
[fragment/BoardMainFragment.kt - settingNavigationView()]
```kt
                    // 사용자 정보 수정
                    R.id.menuItemBoardNavigationModifyUserInfo -> replaceFragment(BoardSubFragmentName.USER_MODIFY_FRAGMENT, false, false, null)
```

### 메뉴를 만들어준다.

[res/menu/menu_user_modify_toolbar.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <item android:title="완료"
        android:id="@+id/menuItemUserModifyDone"
        android:icon="@drawable/done_24px"
        app:showAsAction="always"/>
</menu>
```


### ViewModel의 코드를 작성한다.
[viewmodel/UserMOdifyViewModel.kt]
```kt
class UserModifyViewModel(val userModifyFragment: UserModifyFragment) : ViewModel() {
    
    // textFieldUserModifyPw1 - text
    val textFieldUserModifyPw1Text = MutableLiveData("")
    // textFieldUserModifyPw2 - text
    val textFieldUserModifyPw2Text = MutableLiveData("")
    // textFieldUserModifyNickName - text
    val textFieldUserModifyNickNameText = MutableLiveData(" ")
    // textFieldUserModifyAge - text
    val textFieldUserModifyAgeText = MutableLiveData(" ")

    // checkBoxUserModifyHobbyAll - checkedState
    val checkBoxUserModifyHobbyAllCheckedState = MutableLiveData(MaterialCheckBox.STATE_UNCHECKED)
    // checkBoxUserModifyHobbyAll - checked
    val checkBoxUserModifyHobbyAllChecked = MutableLiveData(false)
    // checkBoxUserModifyHobby1 - checked
    val checkBoxUserModifyHobby1Checked = MutableLiveData(false)
    // checkBoxUserModifyHobby2 - checked
    val checkBoxUserModifyHobby2Checked = MutableLiveData(false)
    // checkBoxUserModifyHobby3 - checked
    val checkBoxUserModifyHobby3Checked = MutableLiveData(false)
    // checkBoxUserModifyHobby4 - checked
    val checkBoxUserModifyHobby4Checked = MutableLiveData(false)
    // checkBoxUserModifyHobby5 - checked
    val checkBoxUserModifyHobby5Checked = MutableLiveData(false)
    // checkBoxUserModifyHobby6 - checked
    val checkBoxUserModifyHobby6Checked = MutableLiveData(false)

    // checkBoxUserModifyHobbyAll - onClick
    fun checkBoxUserModifyHobbyAllOnClick(){
        // 체크되어 있다면
        if(checkBoxUserModifyHobbyAllChecked.value == true){
            checkBoxUserModifyHobby1Checked.value = true
            checkBoxUserModifyHobby2Checked.value = true
            checkBoxUserModifyHobby3Checked.value = true
            checkBoxUserModifyHobby4Checked.value = true
            checkBoxUserModifyHobby5Checked.value = true
            checkBoxUserModifyHobby6Checked.value = true
        } else {
            checkBoxUserModifyHobby1Checked.value = false
            checkBoxUserModifyHobby2Checked.value = false
            checkBoxUserModifyHobby3Checked.value = false
            checkBoxUserModifyHobby4Checked.value = false
            checkBoxUserModifyHobby5Checked.value = false
            checkBoxUserModifyHobby6Checked.value = false
        }
    }

    // 하위 취미 체크박스들
    fun checkBoxUserModifyHobbyOnClick(){
        // 체크되어 있는 체크박스 개수
        var checkedCount = 0
        if(checkBoxUserModifyHobby1Checked.value == true){
            checkedCount++
        }
        if(checkBoxUserModifyHobby2Checked.value == true){
            checkedCount++
        }
        if(checkBoxUserModifyHobby3Checked.value == true){
            checkedCount++
        }
        if(checkBoxUserModifyHobby4Checked.value == true){
            checkedCount++
        }
        if(checkBoxUserModifyHobby5Checked.value == true){
            checkedCount++
        }
        if(checkBoxUserModifyHobby6Checked.value == true){
            checkedCount++
        }

        if(checkedCount == 0){
            // 체크된 것이 없으면
            checkBoxUserModifyHobbyAllChecked.value = false
            checkBoxUserModifyHobbyAllCheckedState.value = MaterialCheckBox.STATE_UNCHECKED
        } else if(checkedCount == 6){
            // 모두 체크되어 있다면
            checkBoxUserModifyHobbyAllChecked.value = true
            checkBoxUserModifyHobbyAllCheckedState.value = MaterialCheckBox.STATE_CHECKED
        } else {
            checkBoxUserModifyHobbyAllCheckedState.value = MaterialCheckBox.STATE_INDETERMINATE
        }
    }

    companion object{
        // toolbarUserModify - onNavigationClickUserModify
        @JvmStatic
        @BindingAdapter("onNavigationClickUserModify")
        fun onNavigationClickUserModify(materialToolbar: MaterialToolbar, userModifyFragment: UserModifyFragment){
            materialToolbar.setNavigationOnClickListener {
                userModifyFragment.boardMainFragment.showNavigationView()
            }
        }
    }
}
```


### 화면을 구성해준다.

[res/layout/fragment_user_modify.xml]

```xml
<?xml version="1.0" encoding="utf-8"?>

<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <data>
        <variable
            name="userModifyViewModel"
            type="com.lion.boardproject.viewmodel.UserModifyViewModel" />
    </data>

    <LinearLayout
        android:layout_width="match_parent" "
        android:orientation="vertical"
        android:transitionGroup="true" >

        <com.google.android.material.appbar.MaterialToolbar
            android:id="@+id/toolbarUserModify"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@android:color/transparent"
            android:minHeight="?attr/actionBarSize"
            android:theme="?attr/actionBarTheme"
            app:menu="@menu/menu_user_modify_toolbar"
            app:navigationIcon="@drawable/menu_24px"
            app:title="회원 정보 수정"
            app:onNavigationClickUserModify="@{userModifyViewModel.userModifyFragment}"/>

        <ScrollView
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:padding="10dp" >

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldUserModifyPw1"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="비밀번호"
                    app:endIconMode="password_toggle"
                    app:startIconDrawable="@drawable/key_24px">

                    <com.google.android.material.textfield.TextInputEditText
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:digits="@string/digit_value"
                        android:singleLine="true"
                        android:inputType="text|textPassword"
                        android:textAppearance="@style/TextAppearance.AppCompat.Large"
                        android:text="@={userModifyViewModel.textFieldUserModifyPw1Text}"/>
                </com.google.android.material.textfield.TextInputLayout>

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldUserModifyPw2"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="비밀번호 확인"
                    app:endIconMode="password_toggle"
                    app:startIconDrawable="@drawable/key_24px"
                    android:layout_marginTop="10dp">

                    <com.google.android.material.textfield.TextInputEditText
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:digits="@string/digit_value"
                        android:singleLine="true"
                        android:inputType="text|textPassword"
                        android:textAppearance="@style/TextAppearance.AppCompat.Large"
                        android:text="@={userModifyViewModel.textFieldUserModifyPw2Text}"/>
                </com.google.android.material.textfield.TextInputLayout>

                <com.google.android.material.divider.MaterialDivider
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    app:dividerColor="@color/black"/>

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldUserModifyNickName"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="닉네임"
                    app:endIconMode="clear_text"
                    app:startIconDrawable="@drawable/person_add_24px"
                    android:layout_marginTop="10dp">

                    <com.google.android.material.textfield.TextInputEditText
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:singleLine="true"
                        android:textAppearance="@style/TextAppearance.AppCompat.Large"
                        android:text="@={userModifyViewModel.textFieldUserModifyNickNameText}"/>
                </com.google.android.material.textfield.TextInputLayout>

                <Button
                    android:id="@+id/buttonUserModifyCheckNickName"
                    style="@style/Widget.Material3.Button.OutlinedButton"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    android:text="닉네임 중복 확인"
                    android:textAppearance="@style/TextAppearance.AppCompat.Large"/>

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldUserModifyAge"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="나이"
                    app:endIconMode="clear_text"
                    app:startIconDrawable="@drawable/face_24px"
                    android:layout_marginTop="10dp">

                    <com.google.android.material.textfield.TextInputEditText
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:singleLine="true"
                        android:inputType="number|numberDecimal"
                        android:textAppearance="@style/TextAppearance.AppCompat.Large"
                        android:text="@={userModifyViewModel.textFieldUserModifyAgeText}"/>
                </com.google.android.material.textfield.TextInputLayout>

                <com.google.android.material.divider.MaterialDivider
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    app:dividerColor="@color/black"/>

                <com.google.android.material.checkbox.MaterialCheckBox
                    android:id="@+id/checkBoxUserModifyHobbyAll"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    android:text="취미"
                    android:textAppearance="@style/TextAppearance.AppCompat.Large"
                    app:checkedState="@{userModifyViewModel.checkBoxUserModifyHobbyAllCheckedState}"
                    android:checked="@={userModifyViewModel.checkBoxUserModifyHobbyAllChecked}"
                    android:onClick="@{(view) -> userModifyViewModel.checkBoxUserModifyHobbyAllOnClick()}"/>

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    android:orientation="vertical">

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="20dp"
                        android:layout_marginTop="10dp"
                        android:orientation="horizontal">

                        <CheckBox
                            android:id="@+id/checkBoxUserModifyHobby1"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:layout_weight="1"
                            android:text="게임"
                            android:checked="@={userModifyViewModel.checkBoxUserModifyHobby1Checked}"
                            android:onClick="@{(view) -> userModifyViewModel.checkBoxUserModifyHobbyOnClick()}"/>

                        <CheckBox
                            android:id="@+id/checkBoxUserModifyHobby2"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:layout_weight="1"
                            android:text="독서"
                            android:checked="@={userModifyViewModel.checkBoxUserModifyHobby2Checked}"
                            android:onClick="@{(view) -> userModifyViewModel.checkBoxUserModifyHobbyOnClick()}"/>

                        <CheckBox
                            android:id="@+id/checkBoxUserModifyHobby3"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:layout_weight="1"
                            android:text="요리"
                            android:checked="@={userModifyViewModel.checkBoxUserModifyHobby3Checked}"
                            android:onClick="@{(view) -> userModifyViewModel.checkBoxUserModifyHobbyOnClick()}"/>
                    </LinearLayout>

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="20dp"
                        android:layout_marginTop="10dp"
                        android:orientation="horizontal">

                        <CheckBox
                            android:id="@+id/checkBoxUserModifyHobby4"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:layout_weight="1"
                            android:text="낚시"
                            android:checked="@={userModifyViewModel.checkBoxUserModifyHobby4Checked}"
                            android:onClick="@{(view) -> userModifyViewModel.checkBoxUserModifyHobbyOnClick()}"/>

                        <CheckBox
                            android:id="@+id/checkBoxUserModifyHobby5"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:layout_weight="1"
                            android:text="영화감상"
                            android:checked="@={userModifyViewModel.checkBoxUserModifyHobby5Checked}"
                            android:onClick="@{(view) -> userModifyViewModel.checkBoxUserModifyHobbyOnClick()}"/>

                        <CheckBox
                            android:id="@+id/checkBoxUserModifyHobby6"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:layout_weight="1"
                            android:text="기타"
                            android:checked="@={userModifyViewModel.checkBoxUserModifyHobby6Checked}"
                            android:onClick="@{(view) -> userModifyViewModel.checkBoxUserModifyHobbyOnClick()}"/>
                    </LinearLayout>
                </LinearLayout>

            </LinearLayout>
        </ScrollView>

    </LinearLayout>

</layout>


```
---

# 21_FireBase 사용 설정

### firebase 프로젝트 생성
1. firebase 서비스에 접속한다.
 - https://firebase.google.com/

2. 로그인을 하고 우측 상단에 "콘솔로 가기(go to console)" 를 눌러준다

3. 다음 화면에서 "프로젝트 만들기" 를 눌러준다.

4. 다음 화면에서 프로젝트 이름을 설정해주고 "계속"을 누른다.

5. 다음 화면에서 "계속"을 눌러준다.

6. 다음 화면에서 "Google 애널리틱스 계정"을 골라주고 "프로젝트 만들기"를 눌러준다.

7. 기다린다...

8. 잠시 후 완료가 되면 "계속"을 눌러준다.


### 안드로이드 설정

1. 생성된 프로젝트를 클릭해서 프로젝트 관리 화면으로 들어온다.

2. 안드로이드 아이콘을 눌러준다.

3. 정보 입력 칸에 다음과 같이 입력해준다.
- Android 패키지 이름 : 안드로이드 프로젝트에서 build.gradle.kts 파일에 있는 applicationId
- 앱 닉네임 : 생략해도 된다.
- 디버그 서명 인증서 SHA-1 : sha-1 값 확인시 확인사항.txt 파일 참고
- 입력 후 "앱 등록" 을 눌러준다.

4. "google-services.json 다운로드"를 눌러 파일을 내려받는다.

5. Android Student에서 좌측 "패키지 익스플로러"상단의 "Android"를 "Proejct"로 변경한다

6. "app" 폴더 안에 "google-services.json" 파일을 넣어주고 다시 "Android"로 변경한다.

7. "다음" 을 눌러준다.

8. Project 수준의 build.gradle.kts 파일에 다음과 같이 넣어준다.

- 반드시 문서에 있는 것을 복사해서 넣어주세요
```kt
plugins {
    ...
    id("com.google.gms.google-services") version "4.4.2" apply false
}
```

9. Mudule 수준의 build.gradle.kts 파일에 다음과 같이 넣어준다.

```kt
plugins {
    ...
    id("com.google.gms.google-services")
}
    
...

dependencies {
    ...
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore:25.1.1")
    implementation("com.google.firebase:firebase-storage:21.0.1")
}
```

10. sync now 까지 완료하였다면 사이트에서 "다음"을 눌러준다.

11. "콘솔로 이동"을 눌러준다.

12. 프로젝트 화면에서 좌측의 "빌드 > Firestore Database"를 클릭한다.

13. 다음 화면에서 "데이터 베이스 만들기"를 클릭한다.

14. 팝업창에서  "위치"를 "Seoul"로 바꾼 다음 "다음" 을 눌러준다.

15. 다음 화면에서 "테스트 모드에서 시작" 을 선택하고 "만들기"를 눌러준다.

16. 데이터 베이스 관리 화면에서 상단에 있는 "규칙" 탭을 눌러준다.

17. 다음 부분을 수정하고 "게시"를 눌러준다.,

```text
allow read, write: if request.time < timestamp.date(년, 월, 일);

->

allow read, write: if true;
```

18. 프로젝트 화면에서 좌측의 "빌드 > Storage"를 클릭한다. 만약 되어 있다면 23번으로 넘어가세요

19. 만약 요금제 계정이 설정이 안되어 있다면 "프로젝트 업그레이드"를 눌러준다.

20. 팝업에서 "Cloud Billing 계정 만들기"를 눌러준다.

21. 다음 화면에서 결제 정보를 입력해주고 "계속"을 눌러준다.

22. "Card Billing 계정 연결"을 눌러준다.

23. Storage 사이트에서 "시작하기"를 눌러준다

24. 팝업창에서  "위치"를 기본으로 두고 "계속" 을 눌러준다.

25. 다음 화면에서 "테스트 모드에서 시작" 을 선택하고 "만들기"를 눌러준다.

26. Storage 관리 화면에서 상단에 있는 "규칙" 탭을 눌러준다.

27. 다음 부분을 수정하고 "게시"를 눌러준다.,

```text
allow read, write: if request.time < timestamp.date(년, 월, 일);

->

allow read, write: if true;
```

---

# 데이터 구조 정의

[사용자]
문서 ID
아이디
비밀번호
자동 로그인 토큰
닉네임
나이
게임
독서
요리
낚시
영화감상
기타
시간
상태값

[게시판]
게시판 구분값
게시판 이름

[게시글]
문서 ID
게시판 구분값
게시글 제목
작성자 구분값
게시글 내용
게시판 구분 값
첨부 사진 파일 이름
댓글
시간
상태

[댓글]
문서 ID
댓글 작성자 닉네임
댓글 내용
댓글이 달린 글 구분 값
시간
상태

---

# 필요한 패키지 폴더
- model
- vo
- repository
- service
- util

---

# 22_FireStore랑 연동하기 위한 기본 클래스들을 작성한다.

### vo
- UserVO
- TypeVO
- BoardVO
- replyVO

[vo/UserVO.kt]
```kt

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
}
```

[vo/TypeVO.kt]
```kt
class TypeVO {
    // 게시판 구분값
    var typeValue = 0
    // 게시판 이름
    var typeName = ""
}
```

[vo/ReplyVO.kt]
```kt
class ReplyVO {
    // 댓글 작성자 닉네임
    var replyNickName = ""
    // 댓글 내용
    var replyText = ""
    // 댓글이 달린 글 구분 값
    var replyBoardId = ""
    // 시간
    var replyTimeStamp = 0L
    // 상태
    var replyState = 0
}
```

[vo/BoardVO.kt]

```kt
class BoardVO {
    // 게시판 구분값
    var boardTypeValue = 0
    // 게시글 제목
    var boardTitle = ""
    // 작성자 구분값
    var boardWriteId = ""
    // 게시글 내용
    var boardText = ""
    // 첨부 사진 파일 이름
    var boardFileName = ""
    // 댓글
    var boardReply = mutableListOf<ReplyVO>()
    // 시간
    var boardTimeStamp = 0L
    // 상태
    var boardState = 0
}
```

### 필요한 값들을 정의한다.
- util/Values.kt 파일을 만들어준다.
```kt

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
```

### model
- BoardModel
- ReplyModel
- TypeModel
- UserModel

[model/TypeModel.kt]
```kt
class TypeModel {
    // 게시판 구분값
    var typeValue = BoardType.BOARD_TYPE_1
    // 게시판 이름
    var typeName = ""
}
```

[model/UserModel.kt]
```kt

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
}
```

[model/ReplyModel.kt]
```kt
class ReplyModel {
    // 댓글 문서 id
    var replyDocumentId = ""
    // 댓글 작성자 닉네임
    var replyNickName = ""
    // 댓글 내용
    var replyText = ""
    // 댓글이 달린 글 구분 값
    var replyBoardId = ""
    // 시간
    var replyTimeStamp = 0L
    // 상태
    var replyState = ReplyState.REPLY_STATE_NORMAL
}
```

[model/BoardModel.kt]
```kt
class BoardModel {
    // 게시글 문서 id
    var boardDocumentId = ""
    // 게시판 구분값
    var boardTypeValue = BoardType.BOARD_TYPE_1
    // 게시글 제목
    var boardTitle = ""
    // 작성자 구분값
    var boardWriteId = ""
    // 게시글 내용
    var boardText = ""
    // 첨부 사진 파일 이름
    var boardFileName = ""
    // 댓글
    var boardReply = mutableListOf<ReplyModel>()
    // 시간
    var boardTimeStamp = 0L
    // 상태
    var boardState = BoardState.BOARD_STATE_NORMAL
}
```

### Model과 VO 간에 서로 변환하는 메서드를 구현한다.

[model/TypeModel.kt]

```kt
    fun toTypeVO() : TypeVO{
        val typeVO = TypeVO()
        typeVO.typeValue = typeValue.number
        typeVO.typeName = typeName
        
        return typeVO
    }
```

[model/UserModel.kt]

```kt

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
```

[model/ReplyModel.kt]
```kt
    fun toReplyVO() : ReplyVO{
        val replyVO = ReplyVO()

        replyVO.replyNickName = replyNickName
        replyVO.replyText = replyText
        replyVO.replyBoardId = replyBoardId
        replyVO.replyTimeStamp = replyTimeStamp
        replyVO.replyState = replyState.number

        return replyVO
    }
```

[model/BoardModel.kt]
```kt
    fun toBoardVO():BoardVO{
        val boardVO = BoardVO()
        boardVO.boardTypeValue = boardTypeValue.number
        boardVO.boardTitle = boardTitle
        boardVO.boardWriteId = boardWriteId
        boardVO.boardText = boardText
        boardVO.boardFileName = boardFileName
        boardVO.boardTimeStamp = boardTimeStamp
        boardVO.boardState = boardState.number

        boardVO.boardReply = mutableListOf()
        boardReply.forEach {
            val replyVO = it.toReplyVO()
            boardVO.boardReply.add(replyVO)
        }

        return boardVO
    }
```

[vo/ReplyVO.kt]
```kt
    fun toReplyModel(replyDocumentId:String) : ReplyModel{
        val replyModel = ReplyModel()
        replyModel.replyDocumentId = replyDocumentId
        replyModel.replyNickName = replyNickName
        replyModel.replyText = replyText
        replyModel.replyBoardId = replyBoardId
        replyModel.replyTimeStamp = replyTimeStamp
        when(replyState){
            ReplyState.REPLY_STATE_NORMAL.number -> {
                replyModel.replyState = ReplyState.REPLY_STATE_NORMAL
            }
            ReplyState.REPLY_STATE_DELETE.number -> {
                replyModel.replyState = ReplyState.REPLY_STATE_DELETE
            }
        }
        return replyModel
    }
```

[vo/BoardVO.kt]
```kt
    fun toBoardModel(boardDocumentId:String) : BoardModel {
        val boardModel = BoardModel()

        boardModel.boardDocumentId = boardDocumentId
        boardModel.boardTitle = boardTitle
        boardModel.boardWriteId = boardWriteId
        boardModel.boardText = boardText
        boardModel.boardFileName = boardFileName
        boardModel.boardTimeStamp = boardTimeStamp

        when(boardTypeValue){
            BoardType.BOARD_TYPE_1.number -> boardModel.boardTypeValue = BoardType.BOARD_TYPE_1
            BoardType.BOARD_TYPE_2.number -> boardModel.boardTypeValue = BoardType.BOARD_TYPE_2
            BoardType.BOARD_TYPE_3.number -> boardModel.boardTypeValue = BoardType.BOARD_TYPE_3
            BoardType.BOARD_TYPE_4.number -> boardModel.boardTypeValue = BoardType.BOARD_TYPE_4
        }

        when(boardState){
            BoardState.BOARD_STATE_NORMAL.number -> boardModel.boardState = BoardState.BOARD_STATE_NORMAL
            BoardState.BOARD_STATE_DELETE.number -> boardModel.boardState = BoardState.BOARD_STATE_DELETE
        }

        boardModel.boardReply = mutableListOf()

        boardReply.forEach {
            val replyModel = it.toReplyModel("")
            boardModel.boardReply.add(replyModel)
        }

        return boardModel
    }
```

[vo/typeVO.kt]
```kt
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
```

[vo/UserVO.kt]
```kt

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
```

### Repository에 클래스를 만들어준다.
- UserRepository
- BoardRepository

### service
- UserService
- BoardService

---

# 23_회원가입처리

### 편의성을 위해 입력 요소가 아닌 다른 곳을 누르면 키보드가 내려가는 코드를 작성해준다.

[UserActivity.kt]
[BoardActivity.kt]
```kt
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
```

### 사용자가 입력한 내용을 번들에 담아 전달한다.
[fragment/UserJoinStep1Fragment.kt]
```kt
        fragmentUserJoinStep1Binding.apply {
            // 사용자가 입력한 데이터를 가져온다.
            val userId = userJoinStep1ViewModel?.textFieldUserJoinStep1IdEditTextText?.value!!
            val userPw = userJoinStep1ViewModel?.textFieldUserJoinStep1Pw1EditTextText?.value!!
            // Log.d("test100", userId)
            // Log.d("test100", userPw)
            // 데이터를 담는다.
            val dataBundle = Bundle()
            dataBundle.putString("userId", userId)
            dataBundle.putString("userPw", userPw)
            userActivity.replaceFragment(UserFragmentName.USER_JOIN_STEP2_FRAGMENT, true, true, dataBundle)
        }
```

### 번들을 통해 전달 받은 데이터를 담을 변수를 선언한다.
[fragment/UserJoinStep2Fragment.kt]
```kt
    // 번들로 전달된 데이터를 담을 변수
    lateinit var userId:String
    lateinit var userPw:String
```

### 번들로 전달되는 데이터를 변수에 담아주는 메서드를 구현한다.
[fragment/UserJoinStep2Fragment.kt]
```kt
    // 번들에 담겨져있는 데이터를 변수에 담아준다.
    fun gettingArguments(){
        userId = arguments?.getString("userId")!!
        userPw = arguments?.getString("userPw")!!

//        Log.d("test100", userId)
//        Log.d("test100", userPw)
    }
```

### 메서드를 호출한다.
[fragment/UserJoinStep2Fragment.kt - onCreateView()]
```kt
        // 번들에 담겨져있는 데이터를 변수에 담아주는 메서드를 호출한다.
        gettingArguments()
```

### 사용자 정보를 저장하는 메서드를 만들어준다.
[repository/UserRepository.kt]
```kt
    companion object{
        // 사용자 정보를 추가하는 메서드
        fun addUserData(userVO: UserVO){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            collectionReference.add(userVO)
        }
    }
```

### 서비스에 메서드를 구현한다.
[service/UserService.kt]
```kt
    companion object{
        // 사용자 정보를 추가하는 메서드
        fun addUserData(userModel: UserModel){
            // 데이터를 VO에 담아준다.
            val userVO = userModel.toUserVO()
            // 저장하는 메서드를 호출한다.
            UserRepository.addUserData(userVO)
        }
    }
```

### 회원 가입 처리 메서드를 수정한다.
[fragment/UserJoinStep2Fragment.kt - proUserJoin()]
```kt

            // 저장할 데이터를 추출한다.
            val userNickname = userJoinStep2ViewModel?.textFieldUserJoinStep2NickNameEditTextText?.value!!
            val userAge = userJoinStep2ViewModel?.textFieldUserJoinStep2AgeEditTextText?.value!!.toInt()
            var userHobby1 = userJoinStep2ViewModel?.checkBoxUserJoinStep2Hobby1Checked?.value!!
            var userHobby2 = userJoinStep2ViewModel?.checkBoxUserJoinStep2Hobby2Checked?.value!!
            var userHobby3 = userJoinStep2ViewModel?.checkBoxUserJoinStep2Hobby3Checked?.value!!
            var userHobby4 = userJoinStep2ViewModel?.checkBoxUserJoinStep2Hobby4Checked?.value!!
            var userHobby5 = userJoinStep2ViewModel?.checkBoxUserJoinStep2Hobby5Checked?.value!!
            var userHobby6 = userJoinStep2ViewModel?.checkBoxUserJoinStep2Hobby6Checked?.value!!
            var userTimeStamp = System.nanoTime()
            var userState = UserState.USER_STATE_NORMAL

            val userModel = UserModel().also {
                it.userId = userId
                it.userPw = userPw
                it.userNickName = userNickname
                it.userAge = userAge
                it.userHobby1 = userHobby1
                it.userHobby2 = userHobby2
                it.userHobby3 = userHobby3
                it.userHobby4 = userHobby4
                it.userHobby5 = userHobby5
                it.userHobby6 = userHobby6
                it.userTimeStamp = userTimeStamp
                it.userState = userState
            }

            // 저장한다.
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    // 서비스의 저장 메서드를 호출한다.
                    UserService.addUserData(userModel)
                }
                work1.join()
                userActivity.showMessageDialog("가입 완료", "가입이 완료되었습니다\n로그인해주세요", "확인"){
                    userActivity.removeFragment(UserFragmentName.USER_JOIN_STEP1_FRAGMENT)
                }
            }
```

---

# 24_중복확인처리

### 추가할 버튼과 연결할 메서드를 ViewModel에 만들어준다.
[viewmodel/UserJoinStep1ViewModel.kt]
```kt
    // buttonUserJoinStep1CheckId - onClick
    fun buttonUserJoinStep1CheckIdOnClick(){

    }
```

### layout 파일에 버튼을 추가한다.
[res/layout/fragment_user_join_step1.xml]
```xml
                <Button
                    android:id="@+id/buttonUserJoinStep1CheckId"
                    style="@style/Widget.Material3.Button.OutlinedButton"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    android:text="중복확인"
                    android:textAppearance="@style/TextAppearance.AppCompat.Large"
                    android:onClick="@{(view) -> userJoinStep1ViewModel.buttonUserJoinStep1CheckIdOnClick()}"/>
```

### 사용자 아이디를 통해 데이터를 가져오는 메서드를 만들어준다.
[repository/UserRepository.kt]
```kt
        // 사용자 아이디를 통해 사용자 데이터를 가져오는 메서드
        suspend fun selectUserDataByUserId(userId:String) : MutableList<UserVO>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val result = collectionReference.whereEqualTo("userId", userId).get().await()
            val userVoList = result.toObjects(UserVO::class.java)
            return userVoList
        }
```

### 아이디가 존재하는지 검사하는 메서드를 만들어준다.
[service/UserService.kt]
```kt
        // 가입하려는 아이디가 존재하는지 확인하는 메서드
        suspend fun checkJoinUserId(userId:String) : Boolean{
            // 아이디를 통해 사용자 정보를 가져온다.
            val userVoList = UserRepository.selectUserDataByUserId(userId)
            // 가져온 데이터가 있다면
            if(userVoList.isNotEmpty()){
                return false
            }
            // 가져온 데이터가 없다면
            else {
                return true
            }
        }
```

### 중복확인을 했는지 검사하기 위한 아이디를 선언해준다.
[fragment/UsrJoinStep1Fragment.kt]
```kt
    // 아이디 중복 확인 검사를 했는지 확인하는 변수
    var isCheckJoinUserIdExist = false
```

### 아이디를 변경했을 경우 중복확인을 했지는지 검사하기 위한 변수에 false를 넣어준다.
[viewmodel/UserJoinStep1ViewModel.kt]
```kt
    // textFieldUserJoinStep1Id - onTextChanged
    fun textFieldUserJoinStep1IdOnTextChanged(){
        userJoinStep1Fragment.isCheckJoinUserIdExist = false
    }
```

### layout 파일에 리스너를 설정한다.
[res/layout/fragment_user_join_step1.xml]
```xml
                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldUserJoinStep1Id"
                    ...
                    <com.google.android.material.textfield.TextInputEditText
                        ...
                        android:onTextChanged="@{() -> userJoinStep1ViewModel.textFieldUserJoinStep1IdOnTextChanged()}"/>
                </com.google.android.material.textfield.TextInputLayout>
```

### 중복확인 처리하는 메서드를 구현한다.
[fragment/UserJoinStep1Fragment.kt]
```kt
    // 아이디 중복 확인처리
    fun checkJoinUserId(){
        // 사용자가 입력한 아이디
        val userId = fragmentUserJoinStep1Binding.userJoinStep1ViewModel?.textFieldUserJoinStep1IdEditTextText?.value!!
        // 입력한 것이 없다면
        if(userId.isEmpty()){
            userActivity.showMessageDialog("아이디 입력", "아이디를 입력해주세요", "확인"){
                userActivity.showSoftInput(fragmentUserJoinStep1Binding.textFieldUserJoinStep1Id)
            }
            return
        }
        // 사용할 수 있는 아이디인지 검사한다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                UserService.checkJoinUserId(userId)
            }
            val chk = work1.await()

            // 사용할 수 있는 아이디인지 여부값을 담아준다.
            isCheckJoinUserIdExist = chk

            if(chk){
                userActivity.showMessageDialog("중복확인", "사용할 수 있는 아이디 입니다", "확인"){

                }
            } else{
                userActivity.showMessageDialog("중복확인", "이미 존재하는 아이디 입니다", "확인"){
                    fragmentUserJoinStep1Binding.userJoinStep1ViewModel?.textFieldUserJoinStep1IdEditTextText?.value = ""
                    userActivity.showSoftInput(fragmentUserJoinStep1Binding.textFieldUserJoinStep1Id)

                }
            }
        }
    }
```

### 중복확인을 했을 때 다음 단계로 넘어가도록 처리해준다.
[viewmodel/UserJoinStep1ViewModel.kt]
```kt

            // 중복확인을 안했다면..
            if(userJoinStep1Fragment.isCheckJoinUserIdExist == false){
                userActivity.showMessageDialog("아이디 중복 확인", "아이디 중복 확인을 해주세요", "확인"){
                    
                }
                return
            }
```

--- 

# 25_닉네임 중복 확인 처리

### 닉네임 확인 여부를 검사하기 위한 변수를 선언한다.
[fragment/UserJoinStep2Fragment.kt]
```kt
    // 닉네임 중복 확인을 했는지 확인하기 위한 변수
    var isCheckUserNickExist = false
```

### 사용자 닉네임을 통해 데이터를 가져오는 메서드를 구현한다.
[repository/UserRepository.kt]
```kt
        // 사용자 닉네임을 통해 사용자 데이터를 가져오는 메서드
        suspend fun selectUserDataByUserNickName(userNickName:String) : MutableList<UserVO>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val result = collectionReference.whereEqualTo("userNickName", userNickName).get().await()
            val userVoList = result.toObjects(UserVO::class.java)
            return userVoList
        }
```

### 사용자 닉네임을 사용할 수 있는지 검사하는 메서드를 구현한다.
[service/UserService.kt]
```kt

        // 사용하려는 닉네임이 존재하는지 확인하는 메서드
        suspend fun checkJoinUserNickName(userNickName:String) : Boolean{
            // 닉네임을 통해 사용자 정보를 가져온다.
            val userVoList = UserRepository.selectUserDataByUserNickName(userNickName)
            // 가져온 데이터가 있다면
            if(userVoList.isNotEmpty()){
                return false
            }
            // 가져온 데이터가 없다면
            else {
                return true
            }
        }
```

### 중복확인 하는 메서드를 구현해준다.
[fragment/UserJoinStep2Fragment.kt]
```kt
    // 닉네임 중복 확인처리
    fun checkJoinNickName(){
        // 사용자가 입력한 닉네임
        val userNickName = fragmentUserJoinStep2Binding.userJoinStep2ViewModel?.textFieldUserJoinStep2NickNameEditTextText?.value!!
        // 입력한 것이 없다면
        if(userNickName.isEmpty()){
            userActivity.showMessageDialog("닉네임 입력", "닉네임을 입력해주세요", "확인"){
                userActivity.showSoftInput(fragmentUserJoinStep2Binding.textFieldUserJoinStep2NickName)
            }
            return
        }
        // 사용할 수 있는 닉네임인지 검사한다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                UserService.checkJoinUserNickName(userNickName)
            }
            val chk = work1.await()

            // 사용할 수 있는 닉네임인지 여부값을 담아준다.
            isCheckUserNickExist = chk

            if(chk){
                userActivity.showMessageDialog("중복확인", "사용할 수 있는 닉네임 입니다", "확인"){

                }
            } else{
                userActivity.showMessageDialog("중복확인", "이미 존재하는 닉네임 입니다", "확인"){
                    fragmentUserJoinStep2Binding.userJoinStep2ViewModel?.textFieldUserJoinStep2NickNameEditTextText?.value = ""
                    userActivity.showSoftInput(fragmentUserJoinStep2Binding.textFieldUserJoinStep2NickName)

                }
            }
        }
    }
```

### 중복확인 버튼과 닉네임 입력요소에 연결할 메서드를 구현한다.
[viewmodel/UserJoinStep2ViewModel.kt]
```kt
    // buttonUserJoinStep2CheckNickName - onClick
    fun buttonUserJoinStep2CheckNickNameOnClick(){
        userJoinStep2Fragment.checkJoinNickName()
    }

    // textFieldUserJoinStep2NickName - onTextChanged
    fun textFieldUserJoinStep2NickNameOnTextChanged(){
        userJoinStep2Fragment.isCheckUserNickExist = false
    }
```

### 닉네임 입력요소와 중복 확인 버튼에 메서드를 연결해준다.
[res/layout/fragment_user_join_step2.xml]
```xml
                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldUserJoinStep2NickName"
                    ...
                    <com.google.android.material.textfield.TextInputEditText
                        ...
                        android:onTextChanged="@{() -> userJoinStep2ViewModel.textFieldUserJoinStep2NickNameOnTextChanged()}"/>
                </com.google.android.material.textfield.TextInputLayout>

                <Button
                    android:id="@+id/buttonUserJoinStep2CheckNickName"
                    ...
                    android:onClick="@{() -> userJoinStep2ViewModel.buttonUserJoinStep2CheckNickNameOnClick()}"/>
```

### 가입 처리 메서드에 코드를 추가해준다.
[fragment/UserJoinStep2Fragment.kt - proUserJoin()]
```kt

            // 닉네임 중복 확인
            if(isCheckUserNickExist == false){
                userActivity.showMessageDialog("중복확인", "닉네임 중복 확인을 해주세요", "확인"){
                    userActivity.showSoftInput(textFieldUserJoinStep2NickName.editText!!)
                }
                return
            }
```

# 26_로그인 처리

### 사용자 아이디를 통해 사용자 정보와 문서 아이디를 반환하는 메서드를 만들어준다.
[repository/UserRepository.kt]
```kt
        // 사용자 아이디와 동일한 사용자의 정보 하나를 반환하는 메서드
        suspend fun selectUserDataByUserIdOne(userId:String) : MutableMap<String, *>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val result = collectionReference.whereEqualTo("userId", userId).get().await()
            val userVoList = result.toObjects(UserVO::class.java)

            val userMap = mutableMapOf(
                "user_document_id" to result.documents[0].id,
                "user_vo" to userVoList[0]
            )
            return userMap
        }
```

### 사용자 정보와 문서 아이디를 반환하는 메서드를 만든다.
[service/UserSerivce.kt]
```kt
        // 사용자 아이디를 통해 문서 id와 사용자 정보를 가져온다.
        // 사용자 아이디와 동일한 사용자의 정보 하나를 반환하는 메서드
        suspend fun selectUserDataByUserIdOne(userId:String) : UserModel{
            val tempMap = UserRepository.selectUserDataByUserIdOne(userId)
            val loginUserVo = tempMap["user_vo"] as UserVO
            val loginUserDocumentId = tempMap["user_document_id"] as String

            val loginUserModel = loginUserVo.toUserModel(loginUserDocumentId)

            return loginUserModel
        }
```

### 로그인 성공 여부값을 나타내는 값을 정의한다.
[util/Values.kt]
```kt
// 로그인 결과
enum class LoginResult(val number:Int, val str:String){
    LOGIN_RESULT_SUCCESS(1, "로그인 성공"),
    LOGIN_RESULT_ID_NOT_EXIST(2, "존재하지 않는 아이디"),
    LOGIN_RESULT_PASSWORD_INCORRECT(3, "잘못된 비밀번호")
}
```

### 로그인 성공 여부를 판단하는 메서드를 만들어준다.
[service/UserService.kt]
```kt
        // 로그인 처리 메서드
        suspend fun checkLogin(loginUserId:String, loginUserPw:String) : LoginResult{
            // 로그인 결과
            var result = LoginResult.LOGIN_RESULT_SUCCESS

            // 입력한 아이디로 사용자 정보를 가져온다.
            val userVoList = UserRepository.selectUserDataByUserId(loginUserId)
            // 가져온 사용자 정보가 없다면
            if(userVoList.isEmpty()){
                result = LoginResult.LOGIN_RESULT_ID_NOT_EXIST
            } else {
                if(loginUserPw != userVoList[0].userPw){
                    // 비밀번호가 다르다면
                    result = LoginResult.LOGIN_RESULT_PASSWORD_INCORRECT
                }
            }
            return result
        }
```

### 로그인 성공 여부에 따라 처리하는 부분을 구현해준다.

[fragment/LoginFragment.kt - proLogin()]
```kt

            // 사용자가 입력한 아이디와 비밀번호
            val loginUserId = loginViewModel?.textFieldUserLoginIdEditTextText?.value!!
            val loginUserPw = loginViewModel?.textFieldUserLoginPwEditTextText?.value!!

            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    UserService.checkLogin(loginUserId, loginUserPw)
                }
                // 로그인 결과를 가져온다.
                val loginResult = work1.await()
                // Log.d("test100", loginResult.str)
                // 로그인 결과로 분기한다.
                when(loginResult){
                    LoginResult.LOGIN_RESULT_ID_NOT_EXIST -> {
                        userActivity.showMessageDialog("로그인 실패", "존재하지 않는 아이디 입니다", "확인"){
                            loginViewModel?.textFieldUserLoginIdEditTextText?.value = ""
                            loginViewModel?.textFieldUserLoginPwEditTextText?.value = ""
                            userActivity.showSoftInput(textFieldUserLoginId.editText!!)
                        }
                    }
                    LoginResult.LOGIN_RESULT_PASSWORD_INCORRECT -> {
                        userActivity.showMessageDialog("로그인 실패", "잘못된 비밀번호 입니다", "확인"){
                            loginViewModel?.textFieldUserLoginPwEditTextText?.value = ""
                            userActivity.showSoftInput(textFieldUserLoginPw.editText!!)
                        }
                    }
                    LoginResult.LOGIN_RESULT_SUCCESS -> {
                        // 로그인한 사용자 정보를 가져온다.
                        val work2 = async(Dispatchers.IO){
                            UserService.selectUserDataByUserIdOne(loginUserId)
                        }
                        val loginUserModel = work2.await()

                        // BoardActivity를 실행하고 현재 Activity를 종료한다.
                        val boardIntent = Intent(userActivity, BoardActivity::class.java)
                        boardIntent.putExtra("user_document_id", loginUserModel.userDocumentId)
                        boardIntent.putExtra("user_nick_name", loginUserModel.userNickName)
                        startActivity(boardIntent)
                        userActivity.finish()
                    }
                }
            }
```

---

# 27_네비게이션 뷰 처리

### 사용자 문서 아이디와 닉네임을 받을 변수를 선언한다.
[BoardActivity.kt]
```kt
    // 사용자 문서 id와 닉네임을 받을 변수
    var loginUserDocumentId = ""
    var loginUserNickName = ""
```

### 로그인한 사용자의 문서 아이디와 닉네임을 변수에 담아준다.
[BoardActivity.kt - onCreate()]
```kt
        // 사용자 문서 id와 닉네임을 받는다.
        loginUserDocumentId = intent.getStringExtra("user_document_id")!!
        loginUserNickName = intent.getStringExtra("user_nick_name")!!
```

### 네비게이션의 닉네임을 변경해준다.
[fragment/BoardMainFragment.kt - settingNavigationView()]
```kt
            // 닉네임 설정
            navigationBoardMainHeaderBinding.navigationBoardMainHeaderViewModel?.textViewNavigationBoardMainHeaderNickNameText?.value = "${boardActivity.loginUserNickName}님"
```

--- 

# 28_글쓰기 처리

### 이미지 뷰에 있는 이미지 데이터를 추출해 파일로 저장하는 메서드를 만들어준다.
[BoardActivity.kt]
```kt
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
```

### 이미지 데이터를 업로드하는 메서드를 구현한다.

[repository/BoardRepository.kt]
```kt
    companion object{
        // 이미지 데이터를 서버로 업로드 하는 메서드
        fun uploadImage(sourceFilePath:String, serverFilePath:String){
            // 저장되어 있는 이미지의 경로
            val file = File(sourceFilePath)
            val fileUri = Uri.fromFile(file)
            // 업로드 한다.
            val firebaseStorage = FirebaseStorage.getInstance()
            val childReference = firebaseStorage.reference.child("image/$serverFilePath")
            childReference.putFile(fileUri).await()
        }
    }
```

[service/BoardService.kt]
```kt
    companion object{
        // 이미지 데이터를 서버로 업로드 하는 메서드
        fun uploadImage(sourceFilePath:String, serverFilePath:String){
            BoardRepository.uploadImage(sourceFilePath, serverFilePath)
        }
    }
```

### 레이아웃에 이미지 삭제 버튼을 추가한다.
[res/layout/fragment_board_write.xml]
```xml
                <Button
                    android:id="@+id/buttonBoardWriteImageDelete"
                    style="@style/Widget.Material3.Button.OutlinedButton"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    android:text="이미지 삭제" />
```

### 카메라나 앨범에서 이미지를 가져왔는지 확인하기 위한 변수를 선언한다.
[fragment/BoardWriteFragment.kt]
```kt
    // 카메라나 앨범에서 이미지를 가져왔는지...
    var isSetImageView = false
```

### 이미지를 설정하면 변수에 true를 넣어준다.
[BoardActivity - settingCameraLauncher()]
```kt
                    if(pictureFragment is BoardWriteFragment){
                        ...
                        f1.isSetImageView = true
}
```

[BoardActivity -settingAlbumLauncher()]
```kt
                    if(pictureFragment is BoardWriteFragment){
                        ...
                        f1.isSetImageView = true
                    }
```

### 이미지 뷰를 초기화 하는 메서드를 만들어준다.
[fragment/BoardWriteFragment.kt]
```kt
    // 이미지 뷰 초기화
    fun resetImageView(){
        fragmentBoardWriteBinding.imageViewBoardWrite.setImageResource(R.drawable.panorama_24px)
        isSetImageView = false
    }
```

### 버튼과 연결할 메서드를 구현해준다.
[viewmodel/BoardWriteViewModel.kt]
```kt
    // buttonBoardWriteImageDelete - onClick
    fun buttonBoardWriteImageDeleteOnClick(){
        boardWriteFragment.resetImageView()
    }
```

### 이미지 삭제 버튼에 메서드를 연결해준다.
[res/layout/fragment_board_write.xml]
```xml
                <Button
                    android:id="@+id/buttonBoardWriteImageDelete"
                    ...
                    android:onClick="@{() -> boardWriteViewModel.buttonBoardWriteImageDeleteOnClick()}"/>
```

### 게시판 종류값을 담을 MutableLive 데이터를 정의한다.
[viewmodel/BoardWriteViewModel.kt]
```kt
    // buttonGroupBoardWriteType
    val buttonGroupBoardWriteType = MutableLiveData(BoardType.BOARD_TYPE_1)
```

### MutableLive 데이터에 감시자를 설정한다.
[viewmodel/BoardWriteViewModel.kt]
```kt
    init{
        //  buttonGroupBoardWriteType의 감시자
        buttonGroupBoardWriteType.observe(boardWriteFragment){
            // 게시판 종류에 따라서 버튼을 선택해준다.
            val buttonId = when(it){
                BoardType.BOARD_TYPE_1 -> R.id.buttonBoardWriteType1
                BoardType.BOARD_TYPE_2 -> R.id.buttonBoardWriteType2
                BoardType.BOARD_TYPE_3 -> R.id.buttonBoardWriteType3
                BoardType.BOARD_TYPE_4 -> R.id.buttonBoardWriteType4
                BoardType.BOARD_TYPE_ALL -> 0
            }
            // 버튼의 id 값을 설정한다.
            boardWriteFragment.fragmentBoardWriteBinding.buttonGroupBoardWriteType.check(buttonId)
        }
    }
```

### 입력 요소를 초기화 하는 메서드를 구현한다.

[fragment/BoardWriteFragment.kt]
```kt
    // 입력요소 초기화
    fun resetInput(){
        fragmentBoardWriteBinding.apply {
            boardWriteViewModel?.textFieldBoardWriteTitleText?.value = ""
            boardWriteViewModel?.textFieldBoardWriteTextText?.value = ""

            fragmentBoardWriteBinding.boardWriteViewModel?.buttonGroupBoardWriteType?.value = BoardType.BOARD_TYPE_1

            resetImageView()
        }
    }
```

### 초기화 아이콘을 누르면 메서드를 호출한다.
[fragment/BoardWriteFragment.kt - settingToolbar()]
```kt
                    // 초기화
                    R.id.menuItemBoardWriteReset -> {
                        resetInput()
                    }
```

### button toggle group 에 설정할 속성을 정의해준다.
[viewmodel/BoardWriteViewModel.kt]
```kt
        // buttonGroupBoardWriteType - onButtonCheckedChange
        @JvmStatic
        @BindingAdapter("onButtonCheckedChange")
        fun onButtonCheckedChange(materialButtonToggleGroup: MaterialButtonToggleGroup, boardWriteFragment: BoardWriteFragment){
            // 리스너를 설정한다.
            materialButtonToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
                boardWriteFragment.fragmentBoardWriteBinding.boardWriteViewModel?.buttonGroupBoardWriteType?.apply {
                    if(isChecked){
                        when(checkedId){
                            R.id.buttonBoardWriteType1 -> {
                                value = BoardType.BOARD_TYPE_1
                            }
                            R.id.buttonBoardWriteType2 -> {
                                value = BoardType.BOARD_TYPE_2
                            }
                            R.id.buttonBoardWriteType3 -> {
                                value = BoardType.BOARD_TYPE_3
                            }
                            R.id.buttonBoardWriteType4 -> {
                                value = BoardType.BOARD_TYPE_4
                            }
                        }
                    }
                }

            }
        }
```

### ButtonToggleGroup에 속성을 추가해준다.
[res/layout/fragment_board_write.xml]
```xml
                <com.google.android.material.button.MaterialButtonToggleGroup
                    android:id="@+id/buttonGroupBoardWriteType"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    app:checkedButton="@id/buttonBoardWriteType1"
                    app:selectionRequired="true"
                    app:singleSelection="true"
                    app:onButtonCheckedChange="@{boardWriteViewModel.boardWriteFragment}">
```

### 새로운 글 정보를 저장하는 메서드를 구현한다.

[repository/BoardRepository.kt]
```kt
        // 글 데이터를 저장하는 메서드
        // 새롭게 추가된 문서의 id를 반환한다.
        suspend fun addBoardData(boardVO:BoardVO) : String{
            val fireStore = FirebaseFirestore.getInstance()
            val collectionReference = fireStore.collection("BoardData")
            val documentReference = collectionReference.add(boardVO).await()
            return documentReference.id
        }
```

[service/BoardService.kt]
```kt

        // 글 데이터를 저장하는 메서드
        // 새롭게 추가된 문서의 id를 반환한다.
        suspend fun addBoardData(boardModel: BoardModel) : String{
            // VO 객체를 생성한다.
            val boardVO = boardModel.toBoardVO()
            // 저장한다.
            val documentId = BoardRepository.addBoardData(boardVO)
            return documentId
        }
```

### 글 작성 처리하는 메서드를 구현한다.
[fragment/BoardWriteFragment.kt]
```kt
    // 글 작성 완료 처리 메서드
    fun proBoardWriteSubmit(){
        fragmentBoardWriteBinding.apply {
            // 게시판 구분값
            var boardTypeValue = boardWriteViewModel?.buttonGroupBoardWriteType?.value!!
            // 게시글 제목
            var boardTitle = boardWriteViewModel?.textFieldBoardWriteTitleText?.value!!
            // 작성자 구분값
            var boardWriteId = boardActivity.loginUserDocumentId
            // 게시글 내용
            var boardText = boardWriteViewModel?.textFieldBoardWriteTextText?.value!!
            // 첨부 사진 파일 이름
            var boardFileName = "none"
            // 시간
            var boardTimeStamp = System.nanoTime()

            if(boardTitle.isEmpty()){
                boardActivity.showMessageDialog("입력 오류", "제목을 입력해주세요", "확인"){
                    boardActivity.showSoftInput(textFieldBoardWriteTitle.editText!!)
                }
                return
            }

            if(boardText.isEmpty()){
                boardActivity.showMessageDialog("입력 오류", "내용을 입력해주세요", "확인"){
                    boardActivity.showSoftInput(textFieldBoardWriteText.editText!!)
                }
                return
            }

            // 업로드
            CoroutineScope(Dispatchers.Main).launch {
                // 이미지가 첨부되어 있다면
                if(isSetImageView){
                    // 서버상에서의 파일 이름
                    boardFileName = "image_${System.currentTimeMillis()}.jpg"
                    // 로컬에 ImageView에 있는 이미지 데이터를 저장한다.
                    boardActivity.saveImageView(fragmentBoardWriteBinding.imageViewBoardWrite)

                    val work1 = async(Dispatchers.IO){
                        BoardService.uploadImage("${boardActivity.filePath}/uploadTemp.jpg", boardFileName)
                    }
                    work1.join()
                }

                // 서버에 저장할 글 데이터
                val boardModel = BoardModel()
                boardModel.boardTypeValue = boardTypeValue
                boardModel.boardTitle = boardTitle
                boardModel.boardWriteId = boardWriteId
                boardModel.boardText = boardText
                boardModel.boardFileName = boardFileName
                boardModel.boardTimeStamp = boardTimeStamp
                // 저장한다.
                val work2 = async(Dispatchers.IO){
                    BoardService.addBoardData(boardModel)
                }
                val documentId = work2.await()
                Log.d("test100", documentId)
                // 글을 보는 화면으로 이동한다.
                boardMainFragment.replaceFragment(BoardSubFragmentName.BOARD_READ_FRAGMENT, true, true, null)
            }
        }
    }
```

### 완료 메뉴를 눌렀을 때 메서드를 호출한다
[fragment/BoardWriteFragment.kt - settingToolbar()]
```kt
                    // 완료
                    R.id.menuItemBoardWriteDone -> {
                        // boardMainFragment.replaceFragment(BoardSubFragmentName.BOARD_READ_FRAGMENT, true, true, null)
                        proBoardWriteSubmit()
                    }
```

---

# 29_글 읽기 처리

### 문서 id를 지정하여 글 데이터를 가져오는 메서드를 구현한다.

[repository/BoardRepository.kt]
```kt
        // 글의 문서 id를 통해 글 데이터를 가져온다.
        suspend fun selectBoardDataOneById(documentId:String) : BoardVO{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("BoardData")
            val documentReference = collectionReference.document(documentId)
            val documentSnapShot = documentReference.get().await()
            val boardVO = documentSnapShot.toObject(BoardVO::class.java)!!
            return boardVO
        }
```

[service/BoardService.kt]
```kt
        // 글의 문서 id를 통해 글 데이터를 가져온다.
        suspend fun selectBoardDataOneById(documentId:String) : BoardModel{
            // 글 데이터를 가져온다.
            val boardVO = BoardRepository.selectBoardDataOneById(documentId)
            // BoardModel객체를 생성한다.
            val boardModel = boardVO.toBoardModel(documentId)
            return boardModel
        }
```

### 문서 id를 글 읽는 화면으로 전달해준다.
[fragment/BoardWriteFragment.kt]
```kt
                // Log.d("test100", documentId)
                // 글을 보는 화면으로 이동한다.
                // 문서의 아이디를 전달한다.
                val dataBundle = Bundle()
                dataBundle.putString("boardDocumentId", documentId)
                boardMainFragment.replaceFragment(BoardSubFragmentName.BOARD_READ_FRAGMENT, true, true, dataBundle)
```

### 문서의 id를 담을 변수를 선언한다.
[fragment/BoardReadFragment.kt]
```kt
    // 현재 글의 문서 id를 담을 변수
    lateinit var boardDocumentId:String
```

### 번들을 통해 전달되는 데이터를 변수에 담는 메서드를 만들어준다.
[fragment/BoardReadFragment.kt]
```kt
    // arguments의 값을 변수에 담아준다.
    fun gettingArguments(){
        boardDocumentId = arguments?.getString("boardDocumentId")!!
    }
```

### 메서드를 호출한다.
[fragment/BoardReadFragment.kt - onCreateView()]
```kt
        // arguments의 값을 변수에 담아주는 메서드를 호출한다.
        gettingArguments()
```

### 글 데이터를 가져와 보여주는 메서드를 구현한다.
[fragment/BoardReadFragment.kt]
```kt
    // 글 데이터를 가져와 보여주는 메서드
    fun settingBoardData(){
        // 서버에서 데이터를 가져온다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                BoardService.selectBoardDataOneById(boardDocumentId)
            }
            val boardModel = work1.await()

            fragmentBoardReadBinding.apply {
                boardReadViewModel?.textFieldBoardReadTitleText?.value = boardModel.boardTitle
                boardReadViewModel?.textFieldBoardReadTextText?.value = boardModel.boardText
                boardReadViewModel?.textFieldBoardReadTypeText?.value = boardModel.boardTypeValue.str

                if(boardModel.boardFileName == "none"){
                    imageViewBoardRead.isVisible = false
                }
            }
            
        }
    }
```

### 메서드를 호출한다.
[fragment/BoardReadFragment.kt - onCreateView()]
```kt
        // 글 데이터를 가져와 보여주는 메서드를 호출한다.
        settingBoardData()
```

### 이미지 URI를 가져오는 메서드를 구현한다.
[repository/BoardRepository.kt]
```kt
        // 이미지 데이터를 가져온다.
        suspend fun gettingImage(imageFileName:String) : Uri{
            val storageReference = FirebaseStorage.getInstance().reference
            // 파일명을 지정하여 이미지 데이터를 가져온다.
            val childStorageReference = storageReference.child("image/$imageFileName")
            val imageUri = childStorageReference.downloadUrl.await()
            return imageUri
        }
```

[service/BoardService.kt]
```kt
        // 이미지 데이터를 가져온다.
        suspend fun gettingImage(imageFileName:String) : Uri {
            val imageUri = BoardRepository.gettingImage(imageFileName)
            return imageUri
        }
```

### Glide 라이브러리를 추가한다.
[build.gradle.kts]
```kt
implementation("com.github.bumptech.glide:glide:4.16.0")
```

### 서버에서 이미지를 가져와 보여주는 메서드를 구현한다.

```kt
    // 서버에 있는 이미지를 가져와 ImageView에 보여준다.
    fun showServiceImage(imageUri:Uri, imageView: ImageView){

        Glide.with(this@BoardActivity).load(imageUri).into(imageView)
    }
```

### 데이터를 가져와 글 내용을 보여주는 메서드를 구현한다
```kt
    // 글 데이터를 가져와 보여주는 메서드
    fun settingBoardData(){
        // 서버에서 데이터를 가져온다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                BoardService.selectBoardDataOneById(boardDocumentId)
            }
            val boardModel = work1.await()

            fragmentBoardReadBinding.apply {
                boardReadViewModel?.textFieldBoardReadTitleText?.value = boardModel.boardTitle
                boardReadViewModel?.textFieldBoardReadTextText?.value = boardModel.boardText
                boardReadViewModel?.textFieldBoardReadTypeText?.value = boardModel.boardTypeValue.str

                if(boardModel.boardFileName == "none"){
                    imageViewBoardRead.isVisible = false
                }
            }

            // 첨부 이미지가 있다면
            if(boardModel.boardFileName != "none"){
                val work1 = async(Dispatchers.IO) {
                    // 이미지에 접근할 수 있는 uri를 가져온다.
                    BoardService.gettingImage(boardModel.boardFileName)
                }

                val imageUri = work1.await()
                boardActivity.showServiceImage(imageUri, fragmentBoardReadBinding.imageViewBoardRead)
            }
        }
    }
```

---
# 30_글 목록 처리하기

### 글 정보를 가져오는 메서드를 구현한다.
[repository/BoardRepository.kt]
```kt
        // 글 목록을 가져오는 메서드
        suspend fun gettingBoardList() : MutableList<Map<String, *>>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("BoardData")
            // 데이터를 가져온다.
            val result = collectionReference.orderBy("boardTimeStamp", Query.Direction.DESCENDING).get().await()
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
```
### 사용자 정보 전체를 가져오는 메서드를 구현한다.
[repository/UserRepository.kt]
```kt
        // 사용자 정보 전체를 가져오는 메서드
        suspend fun selectUserDataAll() : MutableList<MutableMap<String, *>>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val result = collectionReference.get().await()
            val userList = mutableListOf<MutableMap<String, *>>()
            result.forEach {
                val userMap = mutableMapOf(
                    "user_document_id" to it.id,
                    "user_vo" to it.toObject(UserVO::class.java)
                )
                userList.add(userMap)
            }
            return userList
        }
```

[service/BoardService.kt]
```kt
        // 글 목록을 가져오는 메서드
        suspend fun gettingBoardList() : MutableList<BoardModel>{
            // 글정보를 가져온다.
            val boardList = mutableListOf<BoardModel>()
            val resultList = BoardRepository.gettingBoardList()
            // 사용자 정보를 가져온다.
            val userList = UserRepository.selectUserDataAll()
            // 사용자 정보를 맵에 담는다.
            val userMap = mutableMapOf<String, String>()
            userList.forEach {
                val userDocumentId = it["user_document_id"] as String
                val userVO = it["user_vo"] as UserVO
                userMap[userDocumentId] = userVO.userNickName
            }
        
            resultList.forEach {
                val boardVO = it["boardVO"] as BoardVO
                val documentId = it["documentId"] as String
                val boardModel = boardVO.toBoardModel(documentId)
                boardModel.boardWriterNickName = userMap[boardModel.boardWriteId]!!
                boardList.add(boardModel)
            }
        
            return boardList
        }
```

### 사용자 닉네임을 받을 프로퍼티를 정의해준다.
[model/BoardModel.kt]
```kt
    // 작성자 닉네임
    var boardWriterNickName = ""
```

### RecyclerView 구성을 위한 리스트를 선언한다.
[fragment/BoardListFragment.kt]
```kt
    // 메인 RecyclerView를 구성하기 위해 사용할 리스트
    var recyclerViewList = mutableListOf<BoardModel>()
```

### RecyclerViewMain의 어뎁터를 수정한다.
[fragment/BoardListFragment.kt - MainRecyclerViewAdapter]
```kt
        override fun getItemCount(): Int {
            return recyclerViewList.size
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            holder.rowBoardListBinding.rowBoardListViewModel?.textViewRowBoardListTitleText?.value = recyclerViewList[position].boardTitle
            holder.rowBoardListBinding.rowBoardListViewModel?.textViewRowBoardListNickNameText?.value = recyclerViewList[position].boardWriterNickName
        }
```

### 데이터를 읽어와 RecyclerView를 갱신하는 메서드를 구현한다.
[frgment/BoardListFragment.kt]
```kt
    // 데이터를 가져와 MainRecyclerView를 갱신하는 메서드
    fun refreshMainRecyclerView(){
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                BoardService.gettingBoardList()
            }
            recyclerViewList = work1.await()

            fragmentBoardListBinding.recyclerViewBoardListMain.adapter?.notifyDataSetChanged()
        }
    }
```

### 항목을 누르면 이동하는 부분을 수정해준다.
[fragment/BoardListFragment.kt - MainRecyclerViewAdapter]
```kt
            rowBoardListBinding.root.setOnClickListener {
                // 사용자가 누른 항목의 게시글 문서 번호를 담아서 전달한다.
                val dataBundle = Bundle()
                dataBundle.putString("boardDocumentId", recyclerViewList[mainViewHolder.adapterPosition].boardDocumentId)
                boardMainFragment.replaceFragment(BoardSubFragmentName.BOARD_READ_FRAGMENT, true, true, dataBundle)
            }
```

### 다른 화면 갔다 돌아왔을 때를 대비해 리스트를 초기화 해준다.
[fragment/BoardListFragment.kt - onCreateView()]
```kt
        // RecyclerView 구성을 위한 리스트를 초기화한다.
        recyclerViewList.clear()
```

---

# 31_글 읽는 화면에 닉네임 추가
- 앞서 처리했어야 하는 내용인데 빠져서 추가합니다.

### 닉네임 요소와 연결할 live data를 정의해준다.
[viewmodel/BoardReadViewModel.kt]

```kt
    // textFieldBoardReadNickName - text
    val textFieldBoardReadNickName = MutableLiveData(" ")
```

### layout 파일에 입력 요소를 추가한다.
[res/layout/fragment_board_read.xml]
```xml
                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldBoardReadNickName"
                    ...

                    <com.google.android.material.textfield.TextInputEditText
                        ...
                        android:text="@{boardReadViewModel.textFieldBoardReadNickName}"/>
                </com.google.android.material.textfield.TextInputLayout>
```

### 사용자 문서 id를 통해 사용자 정보를 가져오는 메서드를 구현한다.
[repository/UserRepository.kt]
```kt
        // 사용자 문서 아이디를 통해 사용자 정보를 가져온다.
        suspend fun selectUserDataByUserDocumentIdOne(userDocumentId:String) : UserVO{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val result = collectionReference.document(userDocumentId).get().await()
            val userVO = result.toObject(UserVO::class.java)!!
            return userVO
        }
```

### 글 하나의 정보를 가져오는 메서드를 수정한다.
[service/BoardService.kt - selectBoardDataOneById()]
```kt
            // 사용자 정보를 가져온다.
            val userVO = UserRepository.selectUserDataByUserDocumentIdOne(boardModel.boardWriteId)
            val userModel = userVO.toUserModel(boardModel.boardWriteId)
            // 닉네임을 담아준다.
            boardModel.boardWriterNickName = userModel.userNickName
            
```

### 글 정보를 출력하는 부분을 수정한다.
[fragment/frgment/BoardListFragment.kt - settingBoardData()]
```kt
                boardReadViewModel?.textFieldBoardReadNickName?.value = boardModel.boardWriterNickName
```

---

# 32_글 수정 삭제 메뉴 노출 처리

### 메뉴를 보이지 않게 설정한다.
[frgment/BoardListFragment.kt - settingToolbar()]
```kt
            // 메뉴를 보이지 않게 설정한다.
            toolbarBoardRead.menu.children.forEach {
                it.isVisible = false
            }
```

### 작성자와 로그인한 사람이 같으면 메뉴를 보여준다.
[frgment/BoardListFragment.kt - settingBoardData()]
```kt
                // 작성자와 로그인한 사람이 같으면 메뉴를 보기에 한다.
                if(boardModel.boardWriteId == boardActivity.loginUserDocumentId){
                    toolbarBoardRead.menu.children.forEach {
                        it.isVisible = true
                    }
                }
```

---

# 33_글 수정 처리

### 버튼 두 개를 추가한다.

[res/layout/fragment_board_modify.xml]
```xml
                <Button
                    android:id="@+id/buttonBoardModifyRemoveImage"
                    style="@style/Widget.Material3.Button.OutlinedButton"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    android:text="이미지삭제"
                    android:textAppearance="@style/TextAppearance.AppCompat.Large"/>

                <Button
                    android:id="@+id/buttonBoardModifyResetImage"
                    style="@style/Widget.Material3.Button.OutlinedButton"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    android:text="이미지 초기화"
                    android:textAppearance="@style/TextAppearance.AppCompat.Large"/>
```

### 수정 메뉴를 누르면 글의 문서 번호를 전달하면서 이동한다.
[fragment/BoardReadFragment.kt - settingToolbar()]
```kt
                    R.id.menuItemBoardReadModify -> {
                        // 글의 문서 번호를 전달한다.
                        val dataBundle = Bundle()
                        dataBundle.putString("boardDocumentId", boardDocumentId)
                        boardMainFragment.replaceFragment(BoardSubFragmentName.BOARD_MODIFY_FRAGMENT, true, true, dataBundle)
                    }
```

### 문서 id를 담아놓을 변수를 선언한다.
[frgment/BoardModifyFragment.kt]
```kt
    // 현재 글의 문서 id를 담을 변수
    lateinit var boardDocumentId:String
```

### 문서 번호를 추출해 변수에 담아준다.
[fragment/BoardModifyFragment.kt]
```kt
    // arguments의 값을 변수에 담아준다.
    fun gettingArguments(){
        boardDocumentId = arguments?.getString("boardDocumentId")!!
    }
```

### 메서드를 호출한다.
[fragment/BoardModifyFragment.kt - onCreateView()]
```kt
        // arguments의 값을 변수에 담아주는 메서드를 호출한다.
        gettingArguments()
```

### ViewModel에 버튼 토글 그룹과 연결할 LiveData를 선언해준다.
[viewmodel/BoardModifyViewModel.kt]
```kt
    // buttonGroupBoardModifyType
    val buttonGroupBoardModifyType = MutableLiveData(BoardType.BOARD_TYPE_1)
```

### Live Data에 감시자를 붙혀준다.
[viewmodel/BoardModifyViewModel.kt]
```kt
    init{
        //  buttonGroupBoardModifyType의 감시자
        buttonGroupBoardModifyType.observe(boardModifyFragment){
            // 게시판 종류에 따라서 버튼을 선택해준다.
            val buttonId = when(it){
                BoardType.BOARD_TYPE_1 -> R.id.buttonBoardModifyType1
                BoardType.BOARD_TYPE_2 -> R.id.buttonBoardModifyType2
                BoardType.BOARD_TYPE_3 -> R.id.buttonBoardModifyType3
                BoardType.BOARD_TYPE_4 -> R.id.buttonBoardModifyType4
                BoardType.BOARD_TYPE_ALL -> 0
            }
            // 버튼의 id 값을 설정한다.
            boardModifyFragment.fragmentBoardModifyBinding.buttonGroupBoardModifyType.check(buttonId)
        }
    }
```

### View에 설정할 속성을 만들어준다.
[viewmodel/BoardModifyViewModel.kt]
```kt
        // buttonGroupBoardModifyType - onButtonCheckedChangeModify
        @JvmStatic
        @BindingAdapter("onButtonCheckedChangeModify")
        fun onButtonCheckedChangeModify(materialButtonToggleGroup: MaterialButtonToggleGroup, boardModifyFragment: BoardModifyFragment) {
            // 리스너를 설정한다.
            materialButtonToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
                boardModifyFragment.fragmentBoardModifyBinding.boardModifyViewModel?.buttonGroupBoardModifyType?.apply {
                    if (isChecked) {
                        when (checkedId) {
                            R.id.buttonBoardModifyType1 -> {
                                value = BoardType.BOARD_TYPE_1
                            }

                            R.id.buttonBoardModifyType2 -> {
                                value = BoardType.BOARD_TYPE_2
                            }

                            R.id.buttonBoardModifyType3 -> {
                                value = BoardType.BOARD_TYPE_3
                            }

                            R.id.buttonBoardModifyType4 -> {
                                value = BoardType.BOARD_TYPE_4
                            }
                        }
                    }
                }

            }
        }
```

### layout 파일에 속성을 적용해준다.
[res/layout/fragment_board_modify.xml]
```xml
<com.google.android.material.button.MaterialButtonToggleGroup
    android:id="@+id/buttonGroupBoardModifyType"
    ...
    app:onButtonCheckedChangeModify="@{boardModifyViewModel.boardModifyFragment}">
```

### 변수를 선언한다.
[fragment/BoardModifyFragment.kt]
```kt
    // 서버에서 받아온 데이터를 담을 변수
    lateinit var boardModel: BoardModel
    lateinit var boardBitmap: Bitmap
    // 원본 글에 이미지가 있는가..
    var isHasBitmap = false
    // 이미지를 사용자가 삭제하였는가..
    var isRemoveBitmap = false
```

### 서버에서 데이터를 받아와 담는 변수의 val을 제거한다.
[fragment/BoardModifyFragment.kt - settingBoardData()]
```kt
            boardModel = work1.await()
```

### 이미지와 관련된 버튼을 안보이게 한다.
[fragment/BoardModifyFragment.kt - onCreateView()]
```kt
        // 이미지 관련 버튼 두 개를 안보이게 한다.
        fragmentBoardModifyBinding.apply { 
            buttonBoardModifyRemoveImage.isVisible = false
            buttonBoardModifyResetImage.isVisible = false
        }
```

### 이미지 뷰에서 이미지를 추출해 변수에 담아준다.
[fragment/BoardModifyFragment.kt - settingBoardData()]
```kt
                // 이미지 뷰에 있는 이미지를 추출하여 변수에 담아준다.
//                val bitmapDrawable = fragmentBoardModifyBinding.imageViewBoardModify.drawable as BitmapDrawable
//                boardBitmap = bitmapDrawable.bitmap
                
                // 글에 이미지가 있는지...
                isHasBitmap = true
                
                // 이미지 삭제 버튼을 보여준다.
                fragmentBoardModifyBinding.buttonBoardModifyRemoveImage.isVisible = true
```

### 이미지 초기화 버튼을 보여준다.
[fragment/BoardModifyFragment.kt - settingBoardData()]
```kt
            // 이미지 초기화 버튼을 보여준다.
            fragmentBoardModifyBinding.buttonBoardModifyResetImage.isVisible = true
```

### 이미지뷰에 담긴 이미지를 변수에 담아준다.
[fragment/BoardModifyFragment.kt]
```kt
    // 이미지 뷰에 있는 이미지를 변수에 담아준다.
    fun getBitmapFromImageView(){
        if(::boardBitmap.isInitialized == false){
            val bitmapDrawable = fragmentBoardModifyBinding.imageViewBoardModify.drawable as BitmapDrawable
            boardBitmap = bitmapDrawable.bitmap
        }
    }
```

### 카메라나 앨범을 누르면 이미지를 담아주는 메서드를 호출한다.
[fragment/BoardModifyFragment.kt - settingToolbar()]
```kt
                    // 카메라
                    R.id.menuItemBoardModifyCamera -> {
                        if(isHasBitmap){
                            // 이미지 뷰에 있는 이미지를 변수에 담아준다.
                            getBitmapFromImageView()
                        }
                        boardMainFragment.boardActivity.startCameraLauncher(this@BoardModifyFragment)
                    }
                    // 앨범
                    R.id.menuItemBoardModifyAlbum -> {
                        if(isHasBitmap){
                            // 이미지 뷰에 있는 이미지를 변수에 담아준다.
                            getBitmapFromImageView()
                        }
                        boardMainFragment.boardActivity.startAlbumLauncher(this@BoardModifyFragment)
                    }
```

### 초기화 메뉴를 눌렀을 때 호출될 메서드를 구현한다.
[fragment/BoardModifyFragment.kt - resetBoardData()]
```kt
    // 글 정보를 초기화 하는 메서드
    fun resetBoardData(){
        fragmentBoardModifyBinding.apply {
            boardModifyViewModel?.textFieldBoardModifyTitleText?.value = boardModel.boardTitle
            boardModifyViewModel?.textFieldBoardModifyTextText?.value = boardModel.boardText
            boardModifyViewModel?.buttonGroupBoardModifyType?.value = boardModel.boardTypeValue

            if(isHasBitmap){
                imageViewBoardModify.setImageBitmap(boardBitmap)
                isRemoveBitmap = false
            } else {
                imageViewBoardModify.setImageResource(R.drawable.panorama_24px)
            }
        }
    }
```

### 초기화 메뉴를 누르면 메서드를 호출한다.
[fragment/BoardModifyFragment.kt - settingToolbar()]
```kt
                    // 초기화
                    R.id.menuItemBoardModifyReset -> {
                        // 글 정보를 초기화 하는 메서드를 호출한다.
                        resetBoardData()
                    }
```

### 초기화 메뉴를 누르면 호출될 메서드를 구현한다.
[fragment/BoardModifyFragment.kt - resetImageView()]
```kt
    // 이미지뷰를 초기화 하는 메서드
    fun resetImageView(){
        fragmentBoardModifyBinding.apply {
            if(isHasBitmap){
                if(::boardBitmap.isInitialized) {
                    imageViewBoardModify.setImageBitmap(boardBitmap)
                    isRemoveBitmap = false
                }
            } else {
                imageViewBoardModify.setImageResource(R.drawable.panorama_24px)
            }
        }
    }
```

### 이미지 초기화 버튼을 누르면 호출되는 메서드를 구현한다.
[viewmodel/BoardModifyViewModel.kt]
```kt
    // buttonBoardModifyResetImage - onClick
    fun buttonBoardModifyResetImageOnClick(){
        boardModifyFragment.resetImageView()
    }
```

### 이미지 초기화 버튼에 적용해준다.
[res/layout/fragment_board_modify.xml]
```xml
<Button
    android:id="@+id/buttonBoardModifyResetImage"
    ...
    android:onClick="@{() -> boardModifyViewModel.buttonBoardModifyResetImageOnClick()}"/>
```

### 이미지 뷰의 이미지를 삭제처리하는 메서드를 구현한다.
[fragment/BoardModifyFragment.kt]
```kt
    // 이미지뷰의 이미지를 삭제하는 메서드
    fun removeImageView(){
        fragmentBoardModifyBinding.apply {
            // 이미지를 추출에 변수에 담아준다.
            getBitmapFromImageView()

            imageViewBoardModify.setImageResource(R.drawable.panorama_24px)
            isRemoveBitmap = true
        }
    }
```

### 이미지 삭제 버튼에 연결할 메서드를 구현해준다.
[viewmodel/BoardModifyViewModel.kt]
```kt
    // buttonBoardModifyRemoveImage - onClick
    fun buttonBoardModifyRemoveImageOnClick(){
        boardModifyFragment.removeImageView()
    }
```

### 버튼에 메서드를 연결해준다.
[res/layout/fragment_board_modify.xml]
```xml
                <Button
                    android:id="@+id/buttonBoardModifyRemoveImage"
                    ...
                    android:onClick="@{() -> boardModifyViewModel.buttonBoardModifyRemoveImageOnClick()}"/>
```

### 글을 수정하는 메서드를 작성해준다.
[repository/BoardRepository.kt]
```kt
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
```

[service/BoardService.kt]
```kt
        // 글정보를 수정하는 메서드
        suspend fun updateBoardData(boardModel: BoardModel){
            // vo 객체에 담아준다.
            val boardVO = boardModel.toBoardVO()
            // 수정하는 메서드를 호출한다.
            BoardRepository.updateBoardData(boardVO, boardModel.boardDocumentId)
        }
```

### 이미지를 수정했는가 변수를 선언한다.
[fragment/BoardModifyFragment.kt]
```kt
    // 이미지를 수정하였는가..
    var isModifyBitmap = false
```

### 사진을 찍고 돌아왔을 때 변수에 true를 넣어준다.
[BoardActivity.kt - settingCameraLauncher()]
```kt

                    // 글을 수정하는 Fragment 라면
                    else if(pictureFragment is BoardModifyFragment){
                        ...
                        f1.isModifyBitmap = true
                    }

```

### 앨범에서 사진을 선택하고 돌아왔을 때 변수에 true를 넣어준다.
[BoardActivity.kt - settingAlbumLauncher()]
```kt
  
                    else if(pictureFragment is BoardModifyFragment){
                        ...
                        f1.isModifyBitmap = true
                    }
```

### 초기화 관련된 곳에는 false를 넣어준다.
[fragment/BoardModifyFragment.kt]
```kt
    // 글 정보를 초기화 하는 메서드
    fun resetBoardData(){
            ...

            isModifyBitmap = false
        }
    }

    // 이미지뷰를 초기화 하는 메서드
    fun resetImageView(){
        fragmentBoardModifyBinding.apply {
            if(isHasBitmap){
                if(::boardBitmap.isInitialized) {
                    ...
                    isModifyBitmap = false
                }
            } else {
                ...
                isModifyBitmap = false
            }
        }
    }
```

### Storage에서 이미지 파일을 삭제하는 메서드를 만들어준다.
[repository/BoardRepository.kt]
```kt
        // 서버에서 이미지 파일을 삭제한다.
        suspend fun removeImageFile(imageFileName:String){
            val imageReference = FirebaseStorage.getInstance().reference.child("image/$imageFileName")
            imageReference.delete().await()
        }
```

[service/BoardService.kt]
```kt

        // 서버에서 이미지 파일을 삭제한다.
        suspend fun removeImageFile(imageFileName:String){
            BoardRepository.removeImageFile(imageFileName)
        }
```

### 글 수정처리 하는 메서드를 구현한다.
[fragment/BoardModifyFragment.kt]
```kt
    // 글 수정 처리 메서드(입력칸 검사는 생략)
    fun proBoardUpdate(){
        fragmentBoardModifyBinding.apply {
            // boardModel에 새로운 글 내용을 넣어준다.
            boardModel.boardTitle = boardModifyViewModel?.textFieldBoardModifyTitleText?.value!!
            boardModel.boardText = boardModifyViewModel?.textFieldBoardModifyTextText?.value!!
            boardModel.boardTypeValue = boardModifyViewModel?.buttonGroupBoardModifyType?.value!!

            CoroutineScope(Dispatchers.Main).launch {
                if(isHasBitmap) {
                    // 만약 이미지를 삭제했다면
                    if (isRemoveBitmap) {
                        // 이미지 파일을 삭제한다.
                        val work1 = async(Dispatchers.IO) {
                            BoardService.removeImageFile(boardModel.boardFileName)
                        }
                        work1.join()
                        boardModel.boardFileName = "none"
                    }
                }
                // 이미지를 수정한적이 있다면
                if(isModifyBitmap){
                    if(boardModel.boardFileName != "none") {
                        // 이미지 파일을 삭제한다.
                        val work1 = async(Dispatchers.IO) {
                            BoardService.removeImageFile(boardModel.boardFileName)
                        }
                        work1.join()
                    }

                    // 서버상에서의 파일 이름
                    boardModel.boardFileName = "image_${System.currentTimeMillis()}.jpg"
                    // 로컬에 ImageView에 있는 이미지 데이터를 저장한다.
                    boardActivity.saveImageView(fragmentBoardModifyBinding.imageViewBoardModify)

                    val work2 = async(Dispatchers.IO){
                        BoardService.uploadImage("${boardActivity.filePath}/uploadTemp.jpg", boardModel.boardFileName)
                    }
                    work2.join()
                }
                
                // 글 데이터를 업로드한다.
                val work3 = async(Dispatchers.IO){
                    BoardService.updateBoardData(boardModel)
                }
                work3.join()

                boardMainFragment.removeFragment(BoardSubFragmentName.BOARD_MODIFY_FRAGMENT)
            }
        }
    }
```

### 메서드를 호출한다.
[fragment/BoardModifyFragment.kt - settingToolbar()]
```kt
                    R.id.menuItemBoardModifyDone -> {
                        // boardMainFragment.removeFragment(BoardSubFragmentName.BOARD_MODIFY_FRAGMENT)
                        val builder = MaterialAlertDialogBuilder(boardActivity)
                        builder.setTitle("글 수정")
                        builder.setMessage("수정시 복구할 수 없습니다")
                        builder.setNegativeButton("취소", null)
                        builder.setPositiveButton("수정"){ dialogInterface: DialogInterface, i: Int ->
                            proBoardUpdate()
                        }
                        builder.show()
                    }
```
---
# 34_글 삭제 처리

### 글 삭제 처리 하는 메서드를 구현해준다.
[repository/BoardRepository.kt]
```kt
        // 서버에서 글을 삭제한다.
        suspend fun deleteBoardData(boardDocumentId:String){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("BoardData")
            val documentReference = collectionReference.document(boardDocumentId)
            documentReference.delete().await()
        }
```

[service/BoardService.kt]
```kt
        // 서버에서 글을 삭제한다.
        suspend fun deleteBoardData(boardDocumentId:String){
            BoardRepository.deleteBoardData(boardDocumentId)
        }
```

### 글 데이터를 담을 변수를 선언해준다.
[fragment/BoardReadFragment.kt]
```kt
    // 글 데이터를 담을 변수
    lateinit var boardModel:BoardModel
```

### 서버에서 글 정보를 받는 부분에서 val 을 삭제한다.
[fragment/BoardReadFragment.kt - settingBoardData]
```kt
    boardModel = work1.await()
```

### 글을 삭제하는 메서드를 구현한다.
[fragment/BaordReadFragment.kt]
```kt
    // 글 삭제 처리 메서드
    fun proBoardDelete(){
        CoroutineScope(Dispatchers.Main).launch {
            // 만약 첨부 이미지가 있다면 삭제한다.
            if(boardModel.boardFileName != "none"){
                val work1 = async(Dispatchers.IO){
                    BoardService.removeImageFile(boardModel.boardFileName)
                }
                work1.join()
            }
            // 글 정보를 삭제한다.
            val work2 = async(Dispatchers.IO){
                BoardService.deleteBoardData(boardDocumentId)
            }
            work2.join()
            // 글 목록 화면으로 이동한다.
            boardMainFragment.removeFragment(BoardSubFragmentName.BOARD_READ_FRAGMENT)
        }
    }
```

### 메서드를 호출한다.
[fragment/BaordReadFragment.kt - settingToolbar()]
```kt
                    R.id.menuItemBoardReadDelete -> {
                        val builder = MaterialAlertDialogBuilder(boardActivity)
                        builder.setTitle("글 삭제")
                        builder.setMessage("삭제시 복구할 수 없습니다")
                        builder.setNegativeButton("취소", null)
                        builder.setPositiveButton("삭제"){ dialogInterface: DialogInterface, i: Int ->
                            proBoardDelete()
                        }
                        builder.show()
                    }
```

---

# 35_게시판 타입별 처리

### NavigationView의 메뉴를 눌렀을 때 게시판 타입값을 전달할 수 있도록 수정한다.
[fragment/BoardMainFragment.kt - settingNavigationView()]
```kt
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
```

### 게시판 타입 값을 담을 변수를 선언한다.
[fragment/BoardListFragment.kt]
```kt
    // 게시판 타입값을 담을 변수
    lateinit var boardType:BoardType
```

### 게시판 타입을 담는 메서드를 구현한다.
[fragment/BoardListFragment.kt]
```kt
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
```

### 메서드를 호출한다.
[fragment/BoardListFragment.kt - onCreateView()]
```kt
        // 게시판 타입 값을 담는 메서드를 호출한다.
        settingBoardType()
```

### 툴바의 title 설정부분을 수정한다.
[fragment/BoardListFragment.kt - settingToolbar()]
```kt
    // 툴바를 구성하는 메서드
    fun settingToolbar(){
        // 타이틀
        fragmentBoardListBinding.boardListViewModel?.toolbarBoardListTitle?.value = boardType.str
    }
```

### 첫 Fragment를 설정하는 부분을 수정해준다.
[fragment/BoardMainFragment.kt - onCreateView()]
```kt
        // 첫 프래그먼트 설정
        val dataBundle = Bundle()
        dataBundle.putInt("BoardType", BoardType.BOARD_TYPE_ALL.number)
        replaceFragment(BoardSubFragmentName.BOARD_LIST_FRAGMENT, false, false, dataBundle)
```

### 게시글 목록을 가져오는 메서드를 수정한다.
[repository/BoardRepository.kt]
```kt
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
```

[service/BoardService.kt]

```kt
        // 글 목록을 가져오는 메서드
        suspend fun gettingBoardList(boardType: BoardType) : MutableList<BoardModel>{
            // 글정보를 가져온다.
            val boardList = mutableListOf<BoardModel>()
            val resultList = BoardRepository.gettingBoardList(boardType)
```

[fragment/BoardListFragment.kt ]

```kt

    // 데이터를 가져와 MainRecyclerView를 갱신하는 메서드
    fun refreshMainRecyclerView(){
        ...
                BoardService.gettingBoardList(boardType)
            }
```


---
# 36_글 목록 검색 기능 구현

### RecyclerView 구성을 위한 임시 데이터를 주석처리 한다.

[fragment/BoardListFragment.kt]
```kt
//    // ReyclerView 구성을 위한 임시 데이터
//    val tempList1 = Array(100) {
//        "글제목 ${it + 1}"
//    }
//    val tempList2 = Array(100){
//        "닉네임 ${it + 1}"
//    }
```

### 검색용 RecyclerView 구성을 위한 리스트를 선언해준다.
[fragment/BoardListFragment.kt]
```kt
    // 검색 RecyclerView를 구성하기 위해 사용할 리스트
    var recyclerViewSearchList = mutableListOf<BoardModel>()
```

### 검색용 RecyclerView의 어뎁터를 수정한다.

[fragment/BoardListFragment.kt - ResultRecyclerViewAdapter()]
```kt
        override fun getItemCount(): Int {
            return recyclerViewSearchList.size
        }

        override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
            holder.rowBoardListBinding.rowBoardListViewModel?.textViewRowBoardListTitleText?.value = recyclerViewSearchList[position].boardTitle
            holder.rowBoardListBinding.rowBoardListViewModel?.textViewRowBoardListNickNameText?.value = recyclerViewSearchList[position].boardWriterNickName
        }
```

### 게시판 글 데이터에서 검색어를 포함하는 것만 골라 반환하는 메서드를 만들어준다.
[service/BoardService.kt]
```kt
        // 검색 결과를 반환하는 메서드
        suspend fun gettingBoardSearchList(boardType: BoardType, keyword:String) : MutableList<BoardModel>{
            // 해당 게시판에 해당하는 글 데이터를 가져온다.
            val boardList = gettingBoardList(boardType)
            // 검색 결과를 담을 리스트
            val resultList = mutableListOf<BoardModel>()
            // 게시글의 수 만큼 반복한다.
            boardList.forEach { 
                // 제목이나 내용에 검색어가 포함되어 있다면
                if(it.boardTitle.contains(keyword) or it.boardText.contains(keyword)){
                    // 객체를 리스트에 담는다.
                    resultList.add(it)
                }
            }
            // 반환한다.
            return resultList
        }
```

### 검색어에 해당하는 글 정보를 가져와 검색 결과 목록을 갱신하는 메서드를 구현한다.
[fragment/BoardListFragment.kt]
```kt
    // 검색 결과를 가져와 RecyclerView를 갱신하는 메서드
    fun refreshSearchRecyclerView(){
        fragmentBoardListBinding.apply {
            // 검색어
            val keyword = searchViewBoardList.editText.text.toString()
            // 데이터를 가져온다.
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    BoardService.gettingBoardSearchList(boardType, keyword)
                }
                recyclerViewSearchList = work1.await()
                recyclerViewBoardListResult.adapter?.notifyDataSetChanged()
            }
        }
    }
```

### SearchView를 구성하는 메서드를 구현한다.

[fragment/BoardListFragment.kt]
```kt

// SearchView 셋팅 메서드
fun settingSearchView(){
    fragmentBoardListBinding.apply {
        // SerachView의 입력 창에 대한 엔터키 이벤트
        searchViewBoardList.editText.setOnEditorActionListener { v, actionId, event ->
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
```

### 메서드를 호출한다.
[fragment/BoardListFragment.kt - onCreateView()]
```kt
        // SearchView 셋팅 메서드
        settingSearchView()
```

### 항목을 눌렀을 때 처리 부분을 수정해준다.
[fragment/BoardListFragment.kt - ResultRecyclerViewAdapter]
```kt
            rowBoardListBinding.root.setOnClickListener {
                // 사용자가 누른 항목의 게시글 문서 번호를 담아서 전달한다.
                val dataBundle = Bundle()
                dataBundle.putString("boardDocumentId", recyclerViewSearchList[resultViewHolder.adapterPosition].boardDocumentId)
                boardMainFragment.replaceFragment(BoardSubFragmentName.BOARD_READ_FRAGMENT, true, true, dataBundle)
            }
```

### SearchView가 보여지고 있는 상태인지를 담을 변수를 선언한다.
[fragment/BoardListFragment.kt]
```kt
    // 현재 SearchView가 보여지고 있는지.
    var isShownSearchView = false
```

### 검색어를 담을 변수를 선언해준다.
[fragment/BoardListFragment.kt]
```kt
    // 검색어를 담을 변수
    var searchKeyword = ""
```

### 검색어 처리 메서드를 수정한다.
[fragment/BoardListFragment.kt]
```kt
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
                ....
            }
        }
    }
```

### 검색창의 엔터키를 눌렀을 때 동작하는 부분을 수정한다.
```kt
    // SearchView 셋팅 메서드
    fun settingSearchView(){
        fragmentBoardListBinding.apply {
            // SerachView의 입력 창에 대한 엔터키 이벤트
            searchViewBoardList.editText.setOnEditorActionListener { v, actionId, event ->
                // 사용자가 입력한 검색어를 가져와 변수에 담아둔다.
                searchKeyword = searchViewBoardList.editText.text.toString()
```
---

# 37_회원정보 수정

### 사용자 정보를 가져와 반환하는 메서드를 만들어준다.
[service/UserService.kt]
```kt
        // 사용자 문서 아이디를 통해 사용자 정보를 가져온다.
        suspend fun selectUserDataByUserDocumentIdOne(userDocumentId:String) : UserModel{
            val userVO = UserRepository.selectUserDataByUserDocumentIdOne(userDocumentId)
            val userModel = userVO.toUserModel(userDocumentId)
            return userModel
        }
```

### 사용자 정보 객체를 담을 변수를 선언한다.
[fragment/UserModifyFragment.kt]
```kt
    // 사용자 정보를 담을 변수
    lateinit var userModel:UserModel
```

### 서버에서 데이터를 읽어와 입력 요소에 채워준다.
[fragment/UserModifyFragment.kt]
```kt
    fun settingInputData(){
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                UserService.selectUserDataByUserDocumentIdOne(boardActivity.loginUserDocumentId)
            }
            userModel = work1.await()

            fragmentUserModifyBinding.apply {
                userModifyViewModel?.textFieldUserModifyNickNameText?.value = userModel.userNickName
                userModifyViewModel?.textFieldUserModifyAgeText?.value = userModel.userAge.toString()
                userModifyViewModel?.checkBoxUserModifyHobby1Checked?.value = userModel.userHobby1
                userModifyViewModel?.checkBoxUserModifyHobby2Checked?.value = userModel.userHobby2
                userModifyViewModel?.checkBoxUserModifyHobby3Checked?.value = userModel.userHobby3
                userModifyViewModel?.checkBoxUserModifyHobby4Checked?.value = userModel.userHobby4
                userModifyViewModel?.checkBoxUserModifyHobby5Checked?.value = userModel.userHobby5
                userModifyViewModel?.checkBoxUserModifyHobby6Checked?.value = userModel.userHobby6

                userModifyViewModel?.checkBoxUserModifyHobbyOnClick()
            }
        }
    }
```

### 메서드를 호출한다.
[fragment/UserModifyFragment.kt - onCreateView()]
```kt
        // 데이터를 읽어와 입력 요소를 채워준다.
        settingInputData()
```

### 정보를 수정하는 메서드를 만들어준다.
[repository/UserRepository.kt]
```kt
        // 사용자 데이터를 수정한다.
        suspend fun updateUserData(userVO: UserVO, userDocumentId: String){
            // 수정할 데이터를 담을 맵
            val userMap = mapOf(
                "userPw" to userVO.userPw,
                "userNickName" to userVO.userNickName,
                "userAge" to userVO.userAge,
                "userHobby1" to userVO.userHobby1,
                "userHobby2" to userVO.userHobby2,
                "userHobby3" to userVO.userHobby3,
                "userHobby4" to userVO.userHobby4,
                "userHobby5" to userVO.userHobby5,
                "userHobby6" to userVO.userHobby6,
            )
            // 수정할 문서에 접근할 수 있는 객체를 가져온다.
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val documentReference = collectionReference.document(userDocumentId)
            documentReference.update(userMap).await()
        }
```

[service/UserService.kt]
```kt
        // 사용자 데이터를 수정한다.
        suspend fun updateUserData(userModel: UserModel){
            val userVO = userModel.toUserVO()
            UserRepository.updateUserData(userVO, userModel.userDocumentId)
        }
```

### 닉네임을 검사하는 메서드를 구현한다.
[fragment/UserModifyFragment.kt]
```kt
    // 닉네임 중복 확인 버튼을 누르면 호출되는 메서드
    fun checkUserNickName(){
        fragmentUserModifyBinding.apply {
            val nickName = userModifyViewModel?.textFieldUserModifyNickNameText?.value!!

            if(nickName.isEmpty()){
                boardActivity.showMessageDialog("닉네임 입력 오류", "닉네임을 입력해주세요", "확인"){
                    boardActivity.showSoftInput(textFieldUserModifyNickName.editText!!)
                }
                return
            }

            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    UserService.checkJoinUserNickName(nickName)
                }
                isCheckUserNickName = work1.await()

                if(isCheckUserNickName) {
                    boardActivity.showMessageDialog("닉네임", "사용할 수 있는 닉네임 입니다", "확인"){

                    }
                } else {
                    boardActivity.showMessageDialog("닉네임", "사용할 수 없는 닉네임 입니다", "확인"){
                        
                    }
                }
            }
        }
    }
```

### Button의 onclick 속성에 설정할 메서드를 구현해준다.
[viewmodel/UserModifyViewModel.kt]
```kt
    // buttonUserModifyCheckNickName - onClick
    fun buttonUserModifyCheckNickNameOnClick(){
        userModifyFragment.checkUserNickName()
    }

```

### 닉네임 중복 확인 버튼의 onClick 속성에 메서드를 연결해준다.
[res/layout/fragment_user_modidfy.xml]
```xml
                <Button
                    android:id="@+id/buttonUserModifyCheckNickName"
                      ...
                    android:onClick="@{() -> userModifyViewModel.buttonUserModifyCheckNickNameOnClick()}"/>
```

### 수정 처리 메서드를 구현해준다.

[fragment/UserModifyFragment.kt]
```kt
// 수정 처리 메서드
    fun proModifyUserData(){
        fragmentUserModifyBinding.apply {
            // 입력에 대한 검사
            
            // 비밀번호
            // 둘 중 하나라도 입력이 되어 있는 경우 서로 다르면 입력 오류로 처리한다.
            val pw1 = userModifyViewModel?.textFieldUserModifyPw1Text?.value!!
            val pw2 = userModifyViewModel?.textFieldUserModifyPw2Text?.value!!

            if(pw1.isNotEmpty() or pw2.isNotEmpty()){
                if(pw1 != pw2){
                    boardActivity.showMessageDialog("비밀번호 입력 오류", "비밀번호가 다릅니다", "확인"){
                        userModifyViewModel?.textFieldUserModifyPw1Text?.value = ""
                        userModifyViewModel?.textFieldUserModifyPw2Text?.value = ""
                        boardActivity.showSoftInput(textFieldUserModifyPw1.editText!!)
                    }
                    return
                }
            }

            // 원래의 닉네임과 입력 요소에 입력된 닉네임이 서로 다르고 중복 검사를 안했을 경우 입력 오류로 처리
            val nickName = userModifyViewModel?.textFieldUserModifyNickNameText?.value!!
            if(userModel.userNickName != nickName){
                if(isCheckUserNickName == false){
                    boardActivity.showMessageDialog("닉네임 입력 오류", "닉네임 중복 확인을 해주세요", "확인"){

                    }
                    return
                }
            }
            // 나이 입력 요소에 입력이 되어 있지 않다면 입력 오류 처리
            val age = userModifyViewModel?.textFieldUserModifyAgeText?.value!!
            if(age.isEmpty()){
                boardActivity.showMessageDialog("나이 입력 오류", "나이를 입력해주세요", "확인"){
                    boardActivity.showSoftInput(textFieldUserModifyAge.editText!!)
                }
                return
            }

            // 수정할 데이터를 담아 준다.
            if(pw1.isNotEmpty()){
                userModel.userPw = pw1
            }
            userModel.userNickName = nickName
            userModel.userAge = age.toInt()
            userModel.userHobby1 = userModifyViewModel?.checkBoxUserModifyHobby1Checked?.value!!
            userModel.userHobby2 = userModifyViewModel?.checkBoxUserModifyHobby2Checked?.value!!
            userModel.userHobby3 = userModifyViewModel?.checkBoxUserModifyHobby3Checked?.value!!
            userModel.userHobby4 = userModifyViewModel?.checkBoxUserModifyHobby4Checked?.value!!
            userModel.userHobby5 = userModifyViewModel?.checkBoxUserModifyHobby5Checked?.value!!
            userModel.userHobby6 = userModifyViewModel?.checkBoxUserModifyHobby6Checked?.value!!

            // 수정한다.
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    UserService.updateUserData(userModel)
                }
                work1.join()

                val snackbar = Snackbar.make(boardActivity.activityBoardBinding.root, "수정이 완료되었습니다", Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
        }
    }
```

### 툴바를 구성해주는 메서드를 구현해준다.
[fragment/UserModifyFragment.kt]
```kt
    // Toolbar를 구성하는 메서드
    fun settingToolbar(){
        fragmentUserModifyBinding.apply {
            toolbarUserModify.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.menuItemUserModifyDone -> {
                        // 수정 처리 메서드를 호출한다.
                        proModifyUserData()
                    }
                }
                true
            }
        }
    }
```

### 메서드를 호출한다.
[fragment/UserModifyFragment.kt - onCreateView()]
```kt
        // Toolbar를 구성하는 메서드를 호출한다.
        settingToolbar()
```

### 닉네임 입력 요소에 입력을 하면 호출될 메서드를 구현한다.
[viewmodel/UserModifyViewModel.kt]
```kt
    // buttonUserModifyCheckNickName - onTextChanged
    fun buttonUserModifyCheckNickNameOnTextChanged(){
        userModifyFragment.isCheckUserNickName = false
    }
```

### 닉네임 입력 요소에 적용해준다.
[res/layout/fragment_user_modify.xml]
```xml
                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/textFieldUserModifyNickName"
                    ...

                    <com.google.android.material.textfield.TextInputEditText
                        ...
                        android:onTextChanged="@{() -> userModifyViewModel.buttonUserModifyCheckNickNameOnTextChanged()}"/>
                </com.google.android.material.textfield.TextInputLayout>
```

---

# 38_자동로그인 처리

### 자동 로그인 토큰값을 수정하는 메서드를 구현한다.
[repository/UserRepository.kt]
```kt
        // 자동로그인 토큰값을 갱신하는 메서드
        suspend fun updateUserAutoLoginToken(userDocumentId:String, newToken:String){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val documentReference = collectionReference.document(userDocumentId)
            val tokenMap = mapOf(
                "userAutoLoginToken" to newToken
            )
            documentReference.update(tokenMap).await()
        }
```

[service/UserService.kt]
```kt
        // 자동로그인 토큰값을 갱신하는 메서드
        suspend fun updateUserAutoLoginToken(context:Context, userDocumentId:String){
            // 새로운 토큰값을 발행한다.
            val newToken = "${userDocumentId}${System.nanoTime()}"
            // SharedPreference에 저장한다.
            val pref = context.getSharedPreferences("LoginToken", Context.MODE_PRIVATE)
            pref.edit {
                putString("token", newToken)
            }
            // 서버에 저장한다.
            UserRepository.updateUserAutoLoginToken(userDocumentId, newToken)
        }
```

### 자동 로그인 처리 코드를 작성해준다.
[fragment/LoginFragment.kt - proLogin()]
```kt
                        // 만약 자동로그인이 체크되어 있다면
                        if(loginViewModel?.checkBoxUserLoginAutoChecked?.value!!){
                            CoroutineScope(Dispatchers.Main).launch{
                                val work1 = async(Dispatchers.IO){
                                    UserService.updateUserAutoLoginToken(userActivity, loginUserModel.userDocumentId)
                                }
                                work1.join()
                            }
                        }
```

### 사용자 로그인 토큰 값을 통해 사용자 정보를 가져오는 메서드를 구현한다.
[repository/UserRepository.kt]
```kt
        // 자동 로그인 토큰 값으로 사용자 정보를 가져오는 메서드
        suspend fun selectUserDataByLoginToken(loginToken:String) : Map<String, *>?{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val resultList = collectionReference.whereEqualTo("userAutoLoginToken", loginToken).get().await()
            val userVOList = resultList.toObjects(UserVO::class.java)
            if(userVOList.isEmpty()){
                return null
            } else {
                val userDocumentId = resultList.documents[0].id
                val returnMap = mapOf(
                    "userDocumentId" to userDocumentId,
                    "userVO" to userVOList[0]
                )
                return returnMap
            }
        }
```

[service/UserService.kt]

```kt
        // 자동 로그인 토큰 값으로 사용자 정보를 가져오는 메서드
        suspend fun selectUserDataByLoginToken(loginToken:String) : UserModel?{
            val loginMap = UserRepository.selectUserDataByLoginToken(loginToken)
            if(loginMap == null){
                return null
            } else {
                val userDocumentId = loginMap["userDocumentId"] as String
                val userVO = loginMap["userVO"] as UserVO

                val userModel = userVO.toUserModel(userDocumentId)
                return userModel
            }
        }
```

### 자동 로그인 처리 메서드를 구현한다.
[UserActivity.kt]
```kt
    // 자동 로그인 처리 메서드
    // 자동 로그인에 실패하면 LoginFragment를 띄우고
    // 자동 로그인에 성공하면 현재 Activity를 종료하고 BoardActivity를 실행킨다.
    fun userAutoLoginProcessing(){
        // Preference에 login token이 있는지 확인한다.
        val pref = getSharedPreferences("LoginToken", Context.MODE_PRIVATE)
        val loginToken = pref.getString("token", null)
        // Log.d("test100", "$loginToken")

        CoroutineScope(Dispatchers.Main).launch {
            if(loginToken != null){
                // 사용자 정보를 가져온다.
                val work1 = async(Dispatchers.IO){
                    UserService.selectUserDataByLoginToken(loginToken)
                }
                val loginUserModel = work1.await()
                // 가져온 사용자 데이터가 있다면
                if(loginUserModel != null){
                    // BoardActivity를 실행하고 현재 Activity를 종료한다.
                    val boardIntent = Intent(this@UserActivity, BoardActivity::class.java)
                    boardIntent.putExtra("user_document_id", loginUserModel.userDocumentId)
                    boardIntent.putExtra("user_nick_name", loginUserModel.userNickName)
                    startActivity(boardIntent)
                    finish()
                } else {
                    // 첫번째 Fragment를 설정한다.
                    replaceFragment(UserFragmentName.USER_LOGIN_FRAGMENT, false, false, null)
                }
            } else {
                // 첫번째 Fragment를 설정한다.
                replaceFragment(UserFragmentName.USER_LOGIN_FRAGMENT, false, false, null)
            }
        }
    }
```

### 메서드를 호출한다.
[UserActivity.kt - onCreate()]
```kt
        // 자동 로그인 처리 메서드 호출
        userAutoLoginProcessing()

        // 첫번째 Fragment를 설정한다.
        // replaceFragment(UserFragmentName.USER_LOGIN_FRAGMENT, false, false, null)
```

---

# 39_로그아웃 처리

### 로그아웃 처리 메서드를 구현한다.
[fragment/BoardMainFragment.kt]
```kt
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
```

### 메서드를 호출한다.
[fragment/BoardMainFragment.kt - settingNavigationView()]
```kt
                    // 로그 아웃
                    R.id.menuItemBoardNavigationLogout -> {
                        logoutProcess()
                    }
```

---

# 40_회원탈퇴 처리

### 회원 탈퇴에 대한 로그인 결과 값을 추가해준다.
[util/Values.kt - LoginResult]
```kt
    LOGIN_RESULT_SIGNOUT_MEMBER(4, "탈퇴한 회원"),
```

### 탈퇴한 회원이라면 로그인 결과값을 탈퇴로 설정해준다.
[service/UserService.kt - checkLogin()]
```kt
                // 탈퇴한 회원이라면
                if(userVoList[0].userState == UserState.USER_STATE_SIGNOUT.number){
                    result = LoginResult.LOGIN_RESULT_SIGNOUT_MEMBER
                }
```

### 탈퇴한 회원에 대한 처리를 추가해준다.
[fragment/Fragment.kt - proLogin()]
```kt
                    LoginResult.LOGIN_RESULT_SIGNOUT_MEMBER -> {
                        userActivity.showMessageDialog("로그인 실패", "탈퇴한 회원입니다", "확인"){
                            loginViewModel?.textFieldUserLoginIdEditTextText?.value = ""
                            loginViewModel?.textFieldUserLoginPwEditTextText?.value = ""
                            userActivity.showSoftInput(textFieldUserLoginId.editText!!)
                        }
                    }
```

### 사용자의 상태를 수정하는 메서드를 구현한다.
[repository/UserRepository.kt]
```kt
        // 사용자의 상태를 변경하는 메서드
        suspend fun updateUserState(userDocumentId:String, newState:UserState){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val documentReference = collectionReference.document(userDocumentId)

            val updateMap = mapOf(
                "userState" to newState.number
            )

            documentReference.update(updateMap).await()
        }
```

[service/UserService.kt]
```kt

        // 사용자의 상태를 변경하는 메서드
        suspend fun updateUserState(userDocumentId:String, newState:UserState){
            UserRepository.updateUserState(userDocumentId, newState)
        }
```

### 탈퇴 처리 메서드를 구현한다.
[fragment/BoardMainFragment.kt]

```kt
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
```

### 메서드를 호출한다.
[fragment/BoardMainFragment.kt - settingNavigationView()]
```kt
                    // 회원 탈퇴
                    R.id.menuItemBoardNavigationSignOut -> {
                        signOutProcess()
                    }
```