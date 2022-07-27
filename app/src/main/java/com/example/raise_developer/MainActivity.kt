package com.example.raise_developer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        grassShopDialogButtonEvent()
        optionDialogButtonEvent()
        shopDialogButtonEvent()
    }
    fun grassShopDialogButtonEvent(){
        val grassShopDialogButton = findViewById<Button>(R.id.grass_shop_dialog_button)
        grassShopDialogButton.setOnClickListener {
            val grassShopDialog = GrassShopDialog()

            grassShopDialog.show(supportFragmentManager,"grassShopDialg")
        }

    }
    fun optionDialogButtonEvent(){
        val optionDialogButton = findViewById<Button>(R.id.option_dialog_button)
        optionDialogButton.setOnClickListener {
            val optionDialog = OptionDialog()
            optionDialog.show(supportFragmentManager,"optionDialog") // 다이알로그 생성
        }
    }
    fun shopDialogButtonEvent(){
        val shopDialogButton = findViewById<Button>(R.id.shop_dialog_button)
        shopDialogButton.setOnClickListener {
            val shopDialog = ShopDialog()
            shopDialog.show(supportFragmentManager,"shopDialog") // 다이알로그 생성
        }

    }
}