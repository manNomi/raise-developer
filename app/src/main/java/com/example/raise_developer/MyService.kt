package com.example.raise_developer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MyService : Service() {

    lateinit var player: MediaPlayer

    val binder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer.create(this,R.raw.peaches)

    }


    inner class LocalBinder : Binder() {
        fun getService(): MyService {
            return this@MyService
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    fun musicTimeSet(time:Int){ //음악 원하는 구간으로 이동
        player.let{
            it.pause()
            it.seekTo(time)
            it.start()
        }
    }

    fun musicStop(){ //음악 멈추기
        player.pause()
    }

    fun musicStart(){ //음악 재생
        Log.d("음악","재생")
        player.start()
    }

    override fun onDestroy() {  //서비스 종료
        super.onDestroy()
        player.stop()

    }


}