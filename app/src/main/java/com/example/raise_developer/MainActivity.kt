package com.example.raise_developer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initEvent()
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

    fun grassShopDialogButtonEvent(){ // 잔디 상점과 연결하면 됨
        val grassShopDialogButton = findViewById<Button>(R.id.grass_shop_dialog_button)
        grassShopDialogButton.setOnClickListener {
            val grassShopDialog = GrassShopDialog()

            grassShopDialog.show(supportFragmentManager,"grassShopDialg")
        }

    }



}