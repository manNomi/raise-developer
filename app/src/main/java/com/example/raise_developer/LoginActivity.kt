package com.example.raise_developer

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)
        initEvent()
    }

    fun initEvent(){
//        로그인버튼
        val loginBtn=findViewById<TextView>(R.id.login_btn)
        loginBtn.setOnClickListener{
            val intent= Intent(this,MainActivity::class.java)
//            intent.putExtra("id",'id')
            startActivity(intent)
        }

    }

}