package com.example.raise_developer

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.icu.lang.UCharacter
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.PointerIcon.load
import android.view.View
import android.view.ViewDebug
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.setMargins
import java.lang.System.load
import java.time.Duration
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    var personalMoney = 0  // 개인 자산
    var annualMoney = 2000 // 연봉. 10분에 한번 씩 올라가는거로 바꾸는게 나을듯
    lateinit var thread: Thread
    lateinit var quizTimeThread: Thread
    var isThreadStop = false
    val handler = Handler(Looper.getMainLooper()){ // 메시지를 받을 때 마다 plusAnnualMoneyToPersonalMoney() 이 함수 실행
        plusAnnualMoneyToPersonalMoney()
        true
    }
    val quizTimeHandler = Handler(Looper.getMainLooper()){
        canSolveQuiz()
        true
    }
    lateinit var soundPool: SoundPool
    var soundId = 0

    var solveQuiz = false // 퀴즈를 풀 수 있는 상태를 나타내는 값

    override fun onStart() { // 일단 시작 할 때 쓰레드를 실행하게 해줬음 잔디 버튼 누르면 쓰레드 종료
        super.onStart()
        thread = Thread(CalculateAnnualMoney())
        thread.start()

        quizTimeThread = Thread(QuizTimer())
        quizTimeThread.start()

        setTypingSound()
        }

    fun setTypingSound()  { // 터치 시 소리 세팅
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .build()
        soundId = soundPool.load(this, R.raw.typing_sound, 1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)
        initEvent()
        characterMove()
//         GridLayout에 addView를 해줄 때는 꼭!! 각 아이템마다 margin을 설정하여 겹치지 않게 할 것!! 겹치면 뷰 지 스스로 삭제함
//         좀더 알아봐야함 뷰 위치 설정
//        val param = GridLayout.LayoutParams(GridLayout.spec(0,5),GridLayout.spec(0,5))
//        param.setMargins(13)
//        characterView.layoutParams = param
    }
    fun characterMove() {
        val character = findViewById<LinearLayout>(R.id.main_page_character)
        val characterNoteMark = findViewById<ImageView>(R.id.music_note)

        ObjectAnimator.ofFloat(character, "translationY", -600f).apply{ // y축 이동
            duration = 700
            interpolator = LinearInterpolator()
            addListener(object: AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) { // 애니메이션이 종료되었을 때

                    ObjectAnimator.ofFloat(character, "translationX", 300f).apply{ // x축 이동
                        duration = 700
                        interpolator = LinearInterpolator()
                        addListener(object: AnimatorListenerAdapter(){

                            override fun onAnimationEnd(animation: Animator?) { // 애니메이션이 종료되었을 때때
                            characterNoteMark.visibility = View.VISIBLE
                                ObjectAnimator.ofFloat(characterNoteMark, "translationY", 15f).apply{
                                    duration = 800
                                    repeatCount = ValueAnimator.INFINITE
                                    repeatMode = ValueAnimator.REVERSE
                                    target = characterNoteMark
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
    }

    fun addCharacterAndMove(name: String, positionX: Float, positionY: Float){
        val frameLayout = findViewById<FrameLayout>(R.id.main_page_character_frame_layout)
        // 캐릭터 커스텀 뷰, 캐릭터 커스텀 뷰를 프레임 레이아웃에다가 넣을거임
        val characterView = layoutInflater.inflate(R.layout.main_page_character_view,frameLayout,false)
        //캐릭터 커스텀뷰 내의 뷰들
        val character = characterView.findViewById<LinearLayout>(R.id.character_linear_layout)
        val characterImage = characterView.findViewById<ImageView>(R.id.character_image)
        val characterName = characterView.findViewById<TextView>(R.id.character_name)
        val characterNoteMark = characterView.findViewById<ImageView>(R.id.character_music_note)

        val id = resources.getIdentifier(name,"mipmap",packageName)
        characterImage.setImageResource(id)

        ObjectAnimator.ofFloat(character, "translationY", positionY).apply{ // y축 이동
            duration = 700 // 애니메이션중 걸리는 시간
            interpolator = LinearInterpolator() // 애니메이션 효과
            addListener(object: AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) { // 애니메이션이 종료되었을 때

                    ObjectAnimator.ofFloat(character, "translationX", positionX).apply{ // x축 이동
                        duration = 700
                        interpolator = LinearInterpolator()
                        addListener(object: AnimatorListenerAdapter(){

                            override fun onAnimationEnd(animation: Animator?) { // 애니메이션이 종료되었을 때때
                                characterNoteMark.visibility = View.VISIBLE
                                ObjectAnimator.ofFloat(characterNoteMark, "translationY", 15f).apply{
                                    duration = 800
                                    repeatCount = ValueAnimator.INFINITE // 무한반복
                                    repeatMode = ValueAnimator.REVERSE
                                    target = characterNoteMark
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

    inner class CalculateAnnualMoney: Runnable{ // 쓰레드 클래스 isThreadStop이 false일 때 돌아가며, 5초마다 handler에 메세지를 보내줌
        override fun run() {
            while(!isThreadStop){
                Thread.sleep(5000)
                handler.sendEmptyMessage(0)
            }
        }
    }

    inner class QuizTimer: Runnable{
        override fun run() {
            while(!isThreadStop){
                Thread.sleep(10000)
                quizTimeHandler.sendEmptyMessage(0)
            }
        }
    }

    fun plusAnnualMoneyToPersonalMoney(){ // 개인 자산에 연봉 값을 더해주는 함수
        personalMoney += annualMoney
        findViewById<TextView>(R.id.main_page_text_view_personal_money).text = "${personalMoney}원"
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean { // 터치할 때마다 개인 자산의 TextView가 만원 씩 증가
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                personalMoney += 10000
                findViewById<TextView>(R.id.main_page_text_view_personal_money).text = "${personalMoney}원"
                Log.d("좌표", "${event.x}, ${event.y}")
                soundPool.play(soundId, 1f, 1f, 1, 0, 1f) // 터치할 때마다 타자 소리
            }
            MotionEvent.ACTION_UP -> {

            }
        }
        return super.onTouchEvent(event)
    }

    fun canSolveQuiz() {
        solveQuiz = true
    }

    fun btnEventQuizLogic() {
        val quizButton = findViewById<ImageButton>(R.id.quiz_btn)
        quizButton.setOnClickListener{
            if(solveQuiz) {
                val quizDialog = QuizDialog()
                quizDialog.show(supportFragmentManager,"quizDialog")
                solveQuiz = false
            }else {
                Toast.makeText(this, "퀴즈가 도착하지 않았습니다!", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun initEvent(){
//        잔디버튼
        val grassBtn=findViewById<ImageButton>(R.id.grass_btn)
        grassBtn.setOnClickListener{
            val intent= Intent(this,GrassCheckActivity::class.java)
            startActivity(intent)
            isThreadStop = true
            Log.d("쓰레드 종료","isThreadStop = ${isThreadStop}")
        }
//        상점 버튼
        val shopButton = findViewById<ImageView>(R.id.shop_btn)
        shopButton.setOnClickListener {
            val shopDialog = ShopDialog(personalMoney)
            shopDialog.setDialogListener(object: ShopDialog.CustomViewClickListener{ // 인터페이스 상속받음

                override fun purchaseSuccess(price: String, menuName:String ,type: String) { // price 라는 아이템의 가격값을 전달 받음

                    personalMoney -= price.toInt() // 빼주고
                    findViewById<TextView>(R.id.main_page_text_view_personal_money).text = "${personalMoney}원" //적용
                    shopDialog.dismiss()
                    if (type == "employ"){
                        addCharacterAndMove(menuName, Random.nextInt(-500,500).toFloat(),Random.nextInt(500).toFloat())
                    }
                    else{}
                }
            })
            shopDialog.show(supportFragmentManager,"shopDialog") // 다이알로그 생성
        }
//         옵션 버튼
        val optionButton = findViewById<ImageButton>(R.id.main_page_button_option)
        optionButton.setOnClickListener {
            val optionDialog = OptionDialog()
            optionDialog.show(supportFragmentManager,"optionDialog") // 다이알로그 생성
        }
//        트로피 버튼
        val trophyBtn=findViewById<ImageView>(R.id.trophy_btn)
        trophyBtn.setOnClickListener{
            val intent= Intent(this,RankingActivity::class.java)
            startActivity(intent)
        }
//        인벤토리 버튼
        val inventoryButton = findViewById<ImageButton>(R.id.inventory_btn)
        inventoryButton.setOnClickListener{
            val inventoryDialog = InventoryDialog()
            inventoryDialog.show(supportFragmentManager,"inventoryDialog")
        }

        btnEventQuizLogic()
    }

//    fun grassShopDialogButtonEvent(){ // 잔디 상점과 연결하면 됨
//        val grassShopDialogButton = findViewById<Button>(R.id.grass_shop_dialog_button)
//        grassShopDialogButton.setOnClickListener {
//            val grassShopDialog = GrassShopDialog()
//
//            grassShopDialog.show(supportFragmentManager,"grassShopDialg")
//        }
//
//    }



}