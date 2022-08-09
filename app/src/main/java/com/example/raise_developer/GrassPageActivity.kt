package com.example.raise_developer

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.PersistableBundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import androidx.core.view.setMargins
import java.util.ArrayList

class GrassPageActivity: AppCompatActivity() {

    var myService : MyService? = null
    var isConService = false
    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            Log.d("서비스","실행됨")
            val b = p1 as MyService.LocalBinder
            myService = b.getService()
            isConService = true
            val id = intent.getStringExtra("userId") // 로그인 페이지로부터 유저 아이디 받아오기
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isConService = false
        }
    }

    override fun onStart() {
        super.onStart()
        serviceBind()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.grass_page)
        gridLayoutSetting()
    }

    fun gridLayoutSetting() {
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        for (index in 0 until 20) {
            Log.d("호호","하하")
            val customView = layoutInflater.inflate(R.layout.grass_page_custom_view,gridLayout,false)
            val grassImage = customView.findViewById<ImageView>(R.id.grass)
            val coinImage = customView.findViewById<ImageView>(R.id.coin)
            val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            param.setMargins(3) // 픽셀
            customView.layoutParams = param
            customView.setOnClickListener { // 클릭하면 잔디 상점 다이알로그 띄움
                val grassShopDialog = GrassShopDialog()
                grassShopDialog.show(supportFragmentManager,"grassShopDialog")
            }
            ObjectAnimator.ofFloat(coinImage, "translationY", -15f).apply {
                duration = 800
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE

                start()
            }

            if(index %2 == 0){ // 나중에 깃허브 정보를 가져와서 if문으로 배경 이미지를 바꿀거임
                grassImage.setImageResource(R.mipmap.empty_grass)
            }

            else {
                grassImage.setImageResource(R.mipmap.in_grass)
            }

            gridLayout.addView(customView)
        }
        val harvestCoinButton = findViewById<Button>(R.id.harvestCoinButton)
        harvestCoinButton.setOnClickListener {
            for (index in 0 until gridLayout.childCount){
                val child = gridLayout.getChildAt(index)
                val coin = child.findViewById<ImageView>(R.id.coin)
                ObjectAnimator.ofFloat(coin,"translationY", -30f).apply{
                    duration = 1000
                    addListener(object: AnimatorListenerAdapter(){

                        override fun onAnimationEnd(animation: Animator?) {
                            ObjectAnimator.ofFloat(coin,"rotation",360f, 0f).apply{
                                duration = 500
                                addListener(object: AnimatorListenerAdapter(){
                                    override fun onAnimationEnd(animation: Animator?) {
                                        coin.visibility = View.INVISIBLE
                                    }
                                })
                                start()
                            }
                        }
                    })
                    start()
                }
            }
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



}

