package com.example.raise_developer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity: AppCompatActivity() {

    val provider = OAuthProvider.newBuilder("github.com")

    val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)
        initEvent()
    }

    fun initEvent(){
//        로그인버튼
        val loginBtn=findViewById<TextView>(R.id.login_btn)
        loginBtn.setOnClickListener{
            auth.startActivityForSignInWithProvider(this, provider.build())
                .addOnSuccessListener(
                    OnSuccessListener<AuthResult?> {
                            authResult -> auth.signInWithCredential(authResult.credential!!)
                        .addOnCompleteListener(this@LoginActivity) {task ->
                            if(task.isSuccessful) {
                                val user = Firebase.auth.currentUser
                                val id = user!!.providerData.toString()
                                val userId = authResult.additionalUserInfo?.username.toString() // 유저의 아이디
                                val userEmail = authResult.additionalUserInfo?.profile
                                Log.d("if Login success", userId)
                                val intent= Intent(this,MainActivity::class.java)
                                intent.putExtra("userId",userId) // 유저 아이디 전달
                                startActivity(intent)
                            }
                            else {
                                Toast.makeText(this,"깃허브 로그인 실패", Toast.LENGTH_LONG).show()}
                        }
                    }
                )
                .addOnFailureListener(
                    OnFailureListener {
                        Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
                    }
                )
        }
        }


    }

