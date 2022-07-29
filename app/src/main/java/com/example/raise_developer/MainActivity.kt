package com.example.raise_developer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var personalMoney = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)

        initEvent()
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