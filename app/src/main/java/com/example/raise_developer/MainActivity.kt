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

    }
    fun optionDialogButtonEvent(){
        val optionDialogButton = findViewById<Button>(R.id.option_dialog_button)
        optionDialogButton.setOnClickListener {
            val messageDialog = OptionDialog()
            messageDialog.show(supportFragmentManager,"optionDialog")
        }
    }
    fun shopDialogButtonEvent(){
        val shopDialogButton = findViewById<Button>(R.id.shop_dialog_button)
        shopDialogButton.setOnClickListener {
            val messageDialog = ShopDialog()
            messageDialog.show(supportFragmentManager,"shopDialog")
        }

    }
}