package com.example.raise_developer

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.*
import android.media.SoundPool
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import com.example.graphqlsample.queries.GithubCommitQuery
import com.example.raise_developer.FireStore.checkData
import com.example.raise_developer.FireStore.presentMoney
import com.example.raise_developer.FireStore.tutorialCehck
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import java.lang.Runnable
import kotlin.random.Random


//퀴즈의 답변에 대한 대답
interface QuizInterface {
    fun quizAnswerLogic(Answer: String) //퀴즈 정답 여부에 대한 함수
}
// 레벨업에 대한 재화 관리
interface LevelUpInterface {
    fun plusAnnualMoneyLogic(PlusValue: Int) //연봉 더하기
    fun plusTouchMoneyLogic(PlusValue: Int) //터치수당 더하기
    fun setMultipleAnnualMoneyLogic(MultipleValue: Int) //연봉에 곱해질 값 셋팅
    fun setUserLvLogic(UserLv: Int)
    fun setPersonalMoneyLogic(PersonalMoney: Int)
}

class MainActivity : AppCompatActivity(), QuizInterface, LevelUpInterface {

    // 개인 자산 관련
    var personalMoney = 0  // 개인 자산
    override fun setPersonalMoneyLogic(PersonalMoney: Int) {
        personalMoney = PersonalMoney
        findViewById<TextView>(R.id.main_page_text_view_personal_money).text = "${personalMoney}만원"
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean { // 터치할 때마다 개인 자산의 TextView가 만원 씩 증가
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                personalMoney += touchMoney
                findViewById<TextView>(R.id.main_page_text_view_personal_money).text =
                    "${personalMoney}만원"
                soundPool.play(soundId, 1f, 1f, 1, 0, 1f) // 터치할 때마다 타자 소리
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return super.onTouchEvent(event)
    }

    var doubleBackToExit: Boolean =false

    override fun onBackPressed() {
        if (doubleBackToExit) {
            finishAffinity()
        } else {
            Toast.makeText(this, "종료하시려면 뒤로가기를 한번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
            doubleBackToExit = true
            runDelayed(1500L) {
                doubleBackToExit = false
            }
        }
    }
    fun runDelayed(millis: Long, function: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(function, millis)
    }

    fun setMoneyText(money:String){
        if (prefs.prefs.getString("money", "")!=null && prefs.prefs.getString("money", "")!=""&& money!="") {
            personalMoney = money.toInt()
            findViewById<TextView>(R.id.main_page_text_view_personal_money).text =
                "${personalMoney}만원"
            Log.d("money",personalMoney.toString())
        }
    }

    // 연봉 관련
    var annualMoneyPlus = 2000 // 연봉. 10분에 한번 씩 올라가는거로 바꾸는게 나을듯
    var annualMoneyMultiple = 1 //연봉에 곱해지는 값
    var saveMoney = 0 // 연봉으로 인해 모아진 돈
    fun setAnnualMoney(): Int { // 연봉 측정 함수
        if(annualMoneyMultiple == 1) {
            return annualMoneyPlus
        }
        else{
            return annualMoneyPlus * (10 + annualMoneyMultiple) / 10
        }
    }
    lateinit var annualMoneyThread: Thread
    val annualMoneyHandler = Handler(Looper.getMainLooper()){
        saveMoneyLogic()
        true
    }
    inner class AnnualMoneyThread: Runnable {
        override fun run() {
            while (!annualMoneyThreadStop) {
                Thread.sleep(10000)
                annualMoneyHandler.sendEmptyMessage(0)
            }
        }
    }
    fun saveMoneyLogic() {
        saveMoney += setAnnualMoney()
    }
    var annualMoneyThreadStop = false
    override fun plusAnnualMoneyLogic(PlusValue: Int) {
        annualMoneyPlus += PlusValue
        findViewById<TextView>(R.id.main_text_annaul_money).text = "연봉\n: ${setAnnualMoney()}만원"
    }
    override fun setMultipleAnnualMoneyLogic(MultipleValue: Int) {
        annualMoneyMultiple = MultipleValue
        findViewById<TextView>(R.id.main_text_annaul_money).text = "연봉\n: ${setAnnualMoney()}만원"
    }

    // 터치수당 관련
    var touchMoney = 10 // 터치수당
    override fun plusTouchMoneyLogic(PlusValue: Int) {
        touchMoney += PlusValue
        findViewById<TextView>(R.id.main_text_touch_money).text = "터치수당: ${touchMoney}만원"
    }

    // 레벨 관련
    var userLv = 0
    override fun setUserLvLogic(UserLv: Int) {
        userLv = UserLv
    }

    fun setLevelText(level:String){
        if (prefs.prefs.getString("level", "")!=null && prefs.prefs.getString("level", "")!=""&& level!="") {
            userLv = level.toInt()
            Log.d("레벨 세팅",level.toString())
        }
    }

    // 퀴즈 관련
    lateinit var quizTimeThread: Thread
    val quizTimeHandler = Handler(Looper.getMainLooper()){
        canSolveQuiz()
        true
    }
    var solveQuiz = false // 퀴즈를 풀 수 있는 상태를 나타내는 값
    fun canSolveQuiz() {
        solveQuiz = true
        val quizButton = findViewById<ImageButton>(R.id.quiz_btn)
        quizButton.visibility = View.VISIBLE
    }
    inner class QuizTimer: Runnable {
        override fun run() {
            while (!quizThreadStop) {
                if(solveQuiz == false) {
                    Thread.sleep(10000) //현재 10초
                    quizTimeHandler.sendEmptyMessage(0)
                }
            }
        }
    }
    var quizThreadStop = false
    override fun quizAnswerLogic(Answer: String) {
        if(Answer == "Right") {

            personalMoney = ((personalMoney * 21) / 20)

            solveQuiz = false

            val quizButton = findViewById<ImageButton>(R.id.quiz_btn)
            quizButton.visibility = View.INVISIBLE

            findViewById<TextView>(R.id.main_page_text_view_personal_money).text = "${personalMoney}만원"
            val quizResultDialog = QuizDialogResultRight()
            quizResultDialog.show(supportFragmentManager, "quizResultDialog")

        }
        else if(Answer == "Wrong") {

            personalMoney = ((personalMoney * 19) / 20)

            solveQuiz = false
            val quizButton = findViewById<ImageButton>(R.id.quiz_btn)
            quizButton.visibility = View.INVISIBLE

            findViewById<TextView>(R.id.main_page_text_view_personal_money).text = "${personalMoney}만원"
            val quizResultDialog = QuizDialogResultWrong()
            quizResultDialog.show(supportFragmentManager, "quizResultDialog")
        }
    }

    // 플레이 시간
    var playTime = 0
    inner class PlayTime: Runnable {
        override fun run() {
            while (!isThreadStop) {
                playTime+=1
                Thread.sleep(1000)
            }
        }
    }
    var isThreadStop = false

//    깃허브 정보
    var githubContributionData: List<GithubCommitQuery.Week>? = null
    fun getGithubContributionInfo(id: String?){
        val token = BuildConfig.GITHUB_TOKEN
        val apolloClient = ApolloClient.builder()
            .addHttpInterceptor(AuthorizationInterceptor("${token}"))
            .serverUrl("https://api.github.com/graphql")
            .build()

        lifecycleScope.launchWhenResumed {
            val response = apolloClient.query(GithubCommitQuery("${id}")).execute()
            //바인드 서비스로 깃허브 정보 데이터 전달
            githubContributionData = response.data?.user?.contributionsCollection?.contributionCalendar?.weeks
            myService?.githubInfoMainActivityToService(response.data?.user?.contributionsCollection?.contributionCalendar?.weeks)
        }
    }
    inner class AuthorizationInterceptor(val token: String) : HttpInterceptor {
        override suspend fun intercept(
            request: HttpRequest,
            chain: HttpInterceptorChain
        ): HttpResponse {
            return chain.proceed(
                request.newBuilder().addHeader("Authorization", "Bearer $token").build()
            )
        }
    }

    //잔디 관련
    var grassMoney = 0
    lateinit var grassPref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var getResultText: ActivityResultLauncher<Intent>
    fun setActivityResultInit(){
        getResultText = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                Log.d("mainActivityresult","result")
                val data = result.data?.getIntExtra("grassMoney",0)
                Log.d("data","${data}")
                grassMoney = data!!
                personalMoney += grassMoney
                findViewById<TextView>(R.id.main_page_text_view_personal_money).text =
                    "${personalMoney}만원"
            }
        }
    }

    //애니메이션
    var characterCustomViewArray = arrayListOf<View>()
    var characterCustomViewNameArray = arrayListOf<String>()
    fun mainCharacterMove(positionX: Float, positionY: Float) { // 내 캐릭터의 애니메이션
        val character = findViewById<LinearLayout>(R.id.main_page_character)
        val characterName = findViewById<TextView>(R.id.main_page_character_name)
        val characterNoteMark = findViewById<ImageView>(R.id.music_note)
        val id = intent.getStringExtra("userId") // 로그인 페이지로부터 유저 아이디 받아오기
        characterName.text = id

        ObjectAnimator.ofFloat(character, "translationY", positionY).apply { // y축 이동
            duration = 700
            interpolator = LinearInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) { // 애니메이션이 종료되었을 때

                    ObjectAnimator.ofFloat(character, "translationX", positionX).apply { // x축 이동
                        duration = 700
                        interpolator = LinearInterpolator()
                        addListener(object : AnimatorListenerAdapter() {

                            override fun onAnimationEnd(animation: Animator?) { // 애니메이션이 종료되었을 때때
                                characterNoteMark.visibility = View.VISIBLE
                                ObjectAnimator.ofFloat(characterNoteMark, "translationY", 15f)
                                    .apply {
                                        duration = 800
                                        repeatCount = ValueAnimator.INFINITE
                                        repeatMode = ValueAnimator.REVERSE
                                        target = characterNoteMark
                                        start()
                                    }
//
                            }
                        })
                        start()
                    }
                }
            })
            start()
        }
    }
    fun soundDirectorCharacterMove(imageName: String, characName: String,  purchaseCheck: Boolean) {
        Log.d("sounddirec","실행됨")
        if(!purchaseCheck){
            Log.d("sounddirec","if문")
            val frameLayout = findViewById<FrameLayout>(R.id.main_page_character_frame_layout)
            val characterView = layoutInflater.inflate(R.layout.main_page_character_view, frameLayout, false)
            //캐릭터 커스텀뷰 내의 뷰들
            val character = characterView.findViewById<LinearLayout>(R.id.character_linear_layout)
            val characterImage = characterView.findViewById<ImageView>(R.id.character_image)
            val characterName = characterView.findViewById<TextView>(R.id.character_name)
            val id = resources.getIdentifier(imageName, "mipmap", packageName)
            characterImage.setImageResource(id)
            val subString1 = characName.substring(0 until characName.length/2)
            val subString2 = characName.substring(characName.length/2 until characName.length)
            characterName.text = "${subString1}\n${subString2}"
            ObjectAnimator.ofFloat(character, "translationY", -650f).apply { // y축 이동
                duration = 700
                interpolator = LinearInterpolator()
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) { // 애니메이션이 종료되었을 때

                        ObjectAnimator.ofFloat(character, "translationX", 300f).apply { // x축 이동
                            duration = 700
                            interpolator = LinearInterpolator()
                            addListener(object : AnimatorListenerAdapter() {

                                override fun onAnimationEnd(animation: Animator?) { // 애니메이션이 종료되었을 때때
                                    val pianoNoteMark = findViewById<ImageView>(R.id.piano_music_note)
                                    pianoNoteMark.visibility = View.VISIBLE
                                    ObjectAnimator.ofFloat(pianoNoteMark, "translationY", 15f)
                                        .apply { // x축 이동
                                            duration = 800
                                            repeatCount = ValueAnimator.INFINITE
                                            repeatMode = ValueAnimator.REVERSE
                                            myService?.musicStart()
                                            start()
                                        }
                                }
                            })
                            start()
                        }
                    }
                })
                start()
            }
            frameLayout.addView(characterView)
        }
    }

    fun loadSavedCharacterAndMove(){
        InventoryDialog.prefs = PreferenceInventory(this)
        val frameLayout = findViewById<FrameLayout>(R.id.main_page_character_frame_layout)
        val employ = InventoryDialog.prefs.getString("empoloy", "")[0]
        val employName = InventoryDialog.prefs.getString("empoloy", "")[1]
        val employLevel = InventoryDialog.prefs.getString("empoloy", "")[3]
        Log.d("loadsavedch","${employ}")
        for (index in 0 until employ.size){
            if (employName[index] == "힙합에 푹 빠진 사운드 디렉터"){
                Log.d("loadsavedch","if문")
                val characterView = layoutInflater.inflate(R.layout.main_page_character_view, frameLayout, false)
                //캐릭터 커스텀뷰 내의 뷰들
                val character = characterView.findViewById<LinearLayout>(R.id.character_linear_layout)
                val characterImage = characterView.findViewById<ImageView>(R.id.character_image)
                val characterName = characterView.findViewById<TextView>(R.id.character_name)
                val id = resources.getIdentifier(employ[index], "mipmap", packageName)
                characterImage.setImageResource(id)
                val subString1 = employName[index].substring(0 until employName[index].length/2)
                val subString2 = employName[index].substring(employName[index].length/2 until employName[index].length)
                characterName.text = "${subString1}\n${subString2}"
                soundDirectorCharacterMove(employ[index], "힙합에 푹 빠진 사운드 디렉터", false)
            }
            else{
                Log.d("loadsavedch","esle문")
                val characterView = layoutInflater.inflate(R.layout.main_page_character_view, frameLayout, false)
                //캐릭터 커스텀뷰 내의 뷰들
                val character = characterView.findViewById<LinearLayout>(R.id.character_linear_layout)
                val characterImage = characterView.findViewById<ImageView>(R.id.character_image)
                val characterName = characterView.findViewById<TextView>(R.id.character_name)
                val id = resources.getIdentifier(employ[index], "mipmap", packageName)
                characterImage.setImageResource(id)
                val subString1 = employName[index].substring(0 until employName[index].length/2)
                val subString2 = employName[index].substring(employName[index].length/2 until employName[index].length)
                characterName.text = "${subString1}\n${subString2}"
                val animationOne = ObjectAnimator.ofFloat(character, "translationY", -300f)
                animationOne.duration = 700
                animationOne.interpolator = LinearInterpolator() // 애니메이션 효과
                animationOne.start()

                val thread = Thread(AnimationThread(character))
                thread.start()

                frameLayout.addView(characterView)
            }
            }

    }

    fun addCharacterAndMove(imageName: String, purchaseCheck: Boolean, characName: String) {
        if(!purchaseCheck){
            val frameLayout = findViewById<FrameLayout>(R.id.main_page_character_frame_layout)
            // 캐릭터 커스텀 뷰, 캐릭터 커스텀 뷰를 프레임 레이아웃에다가 넣을거임
            val characterView = layoutInflater.inflate(R.layout.main_page_character_view, frameLayout, false)
            //캐릭터 커스텀뷰 내의 뷰들
            val character = characterView.findViewById<LinearLayout>(R.id.character_linear_layout)
            val characterImage = characterView.findViewById<ImageView>(R.id.character_image)
            val characterName = characterView.findViewById<TextView>(R.id.character_name)

            val id = resources.getIdentifier(imageName, "mipmap", packageName)
            characterImage.setImageResource(id)
            val subString1 = characName.substring(0 until characName.length/2)
            val subString2 = characName.substring(characName.length/2 until characName.length)
            characterName.text = "${subString1}\n${subString2}"

            val animationOne = ObjectAnimator.ofFloat(character, "translationY", -300f)
            animationOne.duration = 700
            animationOne.interpolator = LinearInterpolator() // 애니메이션 효과
            animationOne.start()

            val thread = Thread(AnimationThread(character))
            thread.start()

            frameLayout.addView(characterView)
        }
    }

    fun xMoveAnimation(character: View){
        var randomNumber = Random.nextInt(-500,500).toFloat()
        val animationOne = ObjectAnimator.ofFloat(character, "translationX", randomNumber)
        animationOne.duration = 700

        animationOne.interpolator = LinearInterpolator() // 애니메이션 효과
        animationOne.addListener(object: AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                var locationArray = IntArray(2)
                character.getLocationInWindow(locationArray)
                if (locationArray[0] < 20 ) {
                    character.x = -80f
                }
                else if (locationArray[0] > 1050) {
                    character.x = 990f
                }
            }
        })
        animationOne.start()
    }

    fun yMoveAnimation(character: View){
        var randomNumber = Random.nextInt(-800,0).toFloat()
        val animationOne = ObjectAnimator.ofFloat(character, "translationY", randomNumber)
        animationOne.duration = 700
        animationOne.interpolator = LinearInterpolator() // 애니메이션 효과
        animationOne.addListener(object: AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                var locationArray = IntArray(2)
                character.getLocationInWindow(locationArray)
                if (locationArray[1] < 710 ) {
                    character.y = 100f
                }
                else if (locationArray[1] > 1530) {
                    character.y = 870f
                }
            }
        })
        animationOne.start()

    }

    fun xyMoveAnimation(character: View){
        var randomX = Random.nextInt(-200,200).toFloat()
        var randomY = Random.nextInt(-800,0).toFloat()
        val animationOne = ObjectAnimator.ofFloat(character, "translationX", randomX)
        animationOne.duration = 700
        animationOne.interpolator = LinearInterpolator()
        animationOne.addListener(object: AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                var locationArray = IntArray(2)
                character.getLocationInWindow(locationArray)
                if (locationArray[0] < 20 ) {
                    character.x = -80f
                }
                else if (locationArray[0] > 1050) {
                    character.x = 990f
                }
            }
        })
        val animationTwo = ObjectAnimator.ofFloat(character, "translationY", randomY)
        animationTwo.duration = 700
        animationTwo.interpolator = LinearInterpolator() // 애니메이션 효과
        animationOne.addListener(object: AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                var locationArray = IntArray(2)
                character.getLocationInWindow(locationArray)
                if (locationArray[1] < 710 ) {
                    character.y = 100f
                }
                else if (locationArray[1] > 1530) {
                    character.y = 870f
                }
            }
        })
        animationOne.start()
        animationTwo.start()
    }

    fun setAnimation(character: View, option: Int){// x: 20~ 1050  y: 710 ~ 1530
        if (option == 0) {
            xMoveAnimation(character)
        }
        else if(option == 1){
            yMoveAnimation(character)
        }
        else if(option == 2){
            xyMoveAnimation(character)
        }
    }

    inner class AnimationThread(character: View): Runnable { //쓰레드 클래스 isAnimationThreadStop가 false일 때 멈춤
        val myCharacter = character
        override fun run() {
            while (!isAnimationThreadStop) {
                var option = Random.nextInt(0, 3)
                Thread.sleep(2000)
                runOnUiThread {
                    setAnimation(myCharacter, option)
                }
            }
        }
    }
    var isAnimationThreadStop = false

    // 터치 시 타자 소리
    lateinit var soundPool: SoundPool
    var soundId = 0
    fun setTypingSound() { // 터치 시 소리 세팅
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .build()
        soundId = soundPool.load(this, R.raw.typing_sound, 1)
    }

    var userID="test qwe123rqw"
    var money = personalMoney.toString()
    var presentLV=0

    // 서비스
    var myService : MyService? = null
    var isConService = false
    val serviceConnection = object : ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            Log.d("서비스","실행됨")
            val b = p1 as MyService.LocalBinder
            myService = b.getService()
            isConService = true
            val id = intent.getStringExtra("userId") // 로그인 페이지로부터 유저 아이디 받아오기
            getGithubContributionInfo(id)
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isConService = false
        }
    }
    fun serviceBind() {
        val bindService = Intent(this, MyService::class.java)
        bindService(bindService, serviceConnection, Context.BIND_AUTO_CREATE)
    }
    fun serviceUnBind() {
        if (isConService) {
            unbindService(serviceConnection)
            isConService = false
        }
    }

    companion object {
        lateinit var prefs: PreferenceInventory
    }

    //버튼 이벤트 정리
    fun initEvent() {
        btnEventGrass()
        btnEventShop()
        btnEventOption()
        btnEventTrophy()
        btnEventInventory()
        btnEventQuiz()
        btnEventSaving()
        btnEventLevelUp()
        btnEventInformation()
    }
    //잔디버튼 이벤트
    fun btnEventGrass() {
        val grassBtn = findViewById<ImageButton>(R.id.grass_btn)
        grassBtn.setOnClickListener {
            val intent= Intent(this,GrassPageActivity::class.java)
            intent.putExtra("playTime",playTime)
            getResultText.launch(intent)
        }
    }
    //상점버튼 이벤트
    fun btnEventShop() {
        val shopButton = findViewById<ImageView>(R.id.shop_btn)
        shopButton.setOnClickListener {
            val shopDialog = ShopDialog(personalMoney)
            shopDialog.setDialogListener(object :
                ShopDialog.CustomViewClickListener { // 인터페이스 상속받음
                override fun purchaseSuccess(
                    price: String,
                    menuName: String,
                    type: String,
                    purchaseCheck: Boolean,
                    characterName: String
                ) { // price 라는 아이템의 가격값을 전달 받음
                    val price=price.split("만원")[0].toInt()
                    personalMoney -= price // 빼주고
                    findViewById<TextView>(R.id.main_page_text_view_personal_money).text =
                        "${personalMoney}만원" //적용
                    Log.d("text","적용")
                    shopDialog.dismiss()
                    if (type == "employ") {
                        Log.d("characterNAmeㅁㄴㅇㄹ","${characterName}")
                        if(characterName == "힙합에 푹 빠진 사운드 디렉터"){
                            Log.d("ifㅁㄴㅇㄹ","${characterName}")
                            soundDirectorCharacterMove(menuName, characterName, purchaseCheck)
                        }
                        else{
                            Log.d("elseㅁㄴㅇㄹ","${characterName}")
                            addCharacterAndMove(menuName, purchaseCheck, characterName)
                        }

                    }
                }
            })
            shopDialog.show(supportFragmentManager, "shopDialog") // 다이알로그 생성
        }
    }
    //옵션버튼 이벤트
    fun btnEventOption() {
        val optionButton = findViewById<ImageButton>(R.id.main_page_button_option)
        optionButton.setOnClickListener {
            val id = intent.getStringExtra("userId") // 로그인 페이지로부터 유저 아이디 받아오기
            val email = intent.getStringExtra("userEmail")
            val optionDialog = OptionDialog(email!!)
            optionDialog.setBgmOnButtonEvent(object : OptionDialog.BgmOnButtonClickListener {
                override fun bgmOnButtonEvent() {
                    val pianoNoteMark = findViewById<ImageView>(R.id.piano_music_note)
                    pianoNoteMark.visibility = View.VISIBLE
                    myService?.musicStart()

                }
            })
            optionDialog.setBgmOffButtonEvent(object: OptionDialog.BgmOffButtonClickListener{
                override fun bgmOffButtonEvent() {
                    val pianoNoteMark = findViewById<ImageView>(R.id.piano_music_note)
                    pianoNoteMark.visibility = View.INVISIBLE
                    myService?.musicStop()

                }
            })
            optionDialog.setSoundEffectOnButtonEvent(object: OptionDialog.SoundEffectOnButtonClickListener{
                override fun soundEffectOnButtonEvent() {
                    setTypingSound()
                }
            })
            optionDialog.setSoundEffectOffButtonEvent(object: OptionDialog.SoundEffectOffButtonClickListener{
                override fun soundEffectOffButtonEvent() {
                    soundPool.release()
                }
            })
            optionDialog.setLogoutButtonEvent(object: OptionDialog.LogOutButtonClickListener{
                override fun logoutButtonEvent() {
                    Log.d("로그아웃 버튼","눌림")
                    val intent = Intent(this@MainActivity,LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            })
            optionDialog.setSaveAndCloseButtonEvent(object: OptionDialog.SaveAndCloseButtonClickListener{
                override fun saveAndCloseButtonEvent() {
                    Log.d("저장하고 나가기 버튼","눌림")
                }
            })

            optionDialog.show(supportFragmentManager, "optionDialog") // 다이알로그 생성

        }
    }
    //랭킹버튼 이벤트
    fun btnEventTrophy() {
        val trophyBtn = findViewById<ImageView>(R.id.trophy_btn)
        trophyBtn.setOnClickListener {
            val intent = Intent(this, RankingActivity::class.java)
            startActivity(intent)
        }
    }
    //인벤토리 버튼
    fun btnEventInventory() {
        val inventoryButton = findViewById<ImageButton>(R.id.inventory_btn)
        inventoryButton.setOnClickListener {
            val inventoryDialog = InventoryDialog()
            inventoryDialog.show(supportFragmentManager, "inventoryDialog")
        }
    }
    //퀴즈버튼 이벤트
    fun btnEventQuiz() {
        val quizButton = findViewById<ImageButton>(R.id.quiz_btn)
        quizButton.setOnClickListener {
            if(solveQuiz) {
                val quizDialog = QuizDialog()
                quizDialog.show(supportFragmentManager,"quizDialog")
            }else {
                Toast.makeText(this, "퀴즈가 도착하지 않았습니다!", Toast.LENGTH_LONG).show()
            }
        }
    }
    //저축버튼 이벤트
    fun btnEventSaving() {
        val savingButton = findViewById<ImageButton>(R.id.saving_btn)
        savingButton.setOnClickListener{
            Toast.makeText(this,"${saveMoney}만원을 획득하셨습니다!",Toast.LENGTH_LONG).show()
            personalMoney += saveMoney
            saveMoney = 0
            findViewById<TextView>(R.id.main_page_text_view_personal_money).text = "${personalMoney}만원"
        }
    }
    //레벨업버튼 이벤트
    fun btnEventLevelUp() {
        val levelButton = findViewById<ImageButton>(R.id.level_btn)
        levelButton.setOnClickListener {
            val levelDialog = LevelUpDialog(userLv,personalMoney)
            levelDialog.show(supportFragmentManager, "levelDialog")
        }
    }
    //정보버튼 이벤트
    fun btnEventInformation() {
        val personalInformationButton = findViewById<ImageButton>(R.id.private_btn)
        personalInformationButton.setOnClickListener {
            val pIDialog = PersonalDialog(setAnnualMoney(),touchMoney,userLv,userID)
            pIDialog.show(supportFragmentManager, "personalInformationDialog")
        }
    }


    //생명주기
    override fun onStart() {
        super.onStart()
        quizTimeThread = Thread(QuizTimer())
        quizTimeThread.start()
        annualMoneyThread = Thread(AnnualMoneyThread())
        annualMoneyThread.start()
        isThreadStop = false
        isAnimationThreadStop = false
        setTypingSound()

        serviceBind()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         startService(Intent(this, LifecycleService::class.java))

        setContentView(R.layout.main_page)
        initEvent()
        mainCharacterMove(470f, -550f)
        val id = intent.getStringExtra("userId") // 로그인 페이지로부터 유저 아이디 받아오기
        userID = id!!
        prefs = PreferenceInventory(this)
        CoroutineScope(Dispatchers.Main).launch {
            val data = async {
                checkData(userID, presentLV.toString(), prefs.prefs)
                delay(3000)
            }
            data.await()
            loadSavedCharacterAndMove()
            if (tutorialCehck) {
                val tutorialDialog = TutorialDialog()
                tutorialDialog.show(supportFragmentManager, "optionDialog")
                tutorialCehck=false
            }
            if (presentMoney=="") {
                presentMoney="0"
            }
            setMoneyText((presentMoney.toInt() + personalMoney).toString())
            setLevelText(FireStore.level)
        }

        val thread = Thread(PlayTime())
        thread.start()
        grassPref = getSharedPreferences("fragmentPlayTime",0)
        editor = grassPref.edit()
        setActivityResultInit()

    }

    override fun onResume() {
        super.onResume()
        isThreadStop = false
        isAnimationThreadStop = false
    }

    override fun onStop() {
        super.onStop()
        serviceUnBind()
        Log.d("activity","onstop")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("activity","destory")
        isThreadStop = true
        isAnimationThreadStop = true
        editor.clear().apply()
        money=personalMoney.toString()
        Log.d("현재머니",money)
        presentLV=userLv

        prefs.sendjsonString(userID,presentLV.toString(),money)
        prefs.prefs.edit().clear().apply()
    }
}






