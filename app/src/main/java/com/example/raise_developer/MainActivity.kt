package com.example.raise_developer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var personalMoney = 0  // 개인 자산
    var annualMoney = 2000 // 연봉. 일단 실험식으로 2000원으로 설정함 실행시켜서 10초씩 2000원이 올라가는지 확인해볼것
    lateinit var thread: Thread
    var isThreadStop = false

    val handler = Handler(Looper.getMainLooper()){ // 메시지를 받을 때 마다 plusAnnualMoneyToPersonalMoney() 이 함수 실행
        plusAnnualMoneyToPersonalMoney()
        true
    }

    override fun onStart() { // 일단 시작 할 때 쓰레드를 실행하게 해줬음 잔디 버튼 누르면 쓰레드 종료
        super.onStart()
        thread = Thread(CalculateAnnualMoney())
        thread.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)
        initEvent()
    }

    inner class CalculateAnnualMoney: Runnable{ // 쓰레드 클래스 isThreadStop이 false일 때 돌아가며, 10초마다 handler에 메세지를 보내줌
        override fun run() {
            while(!isThreadStop){
                Thread.sleep(10000)
                handler.sendEmptyMessage(0)
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
            }
        }
        return super.onTouchEvent(event)
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

        val shopButton = findViewById<ImageView>(R.id.shop_btn)
        shopButton.setOnClickListener {
            val shopDialog = ShopDialog()
            shopDialog.show(supportFragmentManager,"shopDialog") // 다이알로그 생성
        }

        val optionButton = findViewById<ImageView>(R.id.main_page_button_option)
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