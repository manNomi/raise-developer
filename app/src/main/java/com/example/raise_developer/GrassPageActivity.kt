package com.example.raise_developer

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.*
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.graphqlsample.queries.GithubCommitQuery
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import kotlinx.coroutines.*
import org.w3c.dom.Text
import java.lang.Runnable
import java.util.ArrayList

class GrassPageActivity: FragmentActivity(),GrassPageFragment.FragmentToActivityGrassMoney {
    var grassMoney = 0
    var playTime = 0
    var isThreadStop = false
    var githubDataArray = arrayListOf<List<String>>()
    var githubData: List<GithubCommitQuery.Week>? = null

    var myService : MyService? = null
    var isConService = false
    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            Log.d("grass_page_service","실행됨")
            val b = p1 as MyService.LocalBinder
            myService = b.getService()
            isConService = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isConService = false
        }
    }
    override fun onStart() {
        super.onStart()
    }

    override fun onReceivedMoney(Money: Int) {
        grassMoney += Money
        findViewById<TextView>(R.id.grass_page_money_value).text = "${grassMoney}만원"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.grass_page)
        findViewById<ImageView>(R.id.imageView3).setOnClickListener {
        }
        CoroutineScope(Dispatchers.Main).launch {
            val bindSer = async { serviceBind() }
            bindSer.await()
            val getGithubData = async{githubData = myService?.githubInfoServiceToGrassPage()}
            getGithubData.await()
            val divide = async { divideGithubDataInfo() }
            divide.await()
        }
    }

    override fun onResume() {
        super.onResume()
        val thread = Thread(PlayTime())
        thread.start()
    }

    inner class PlayTime: Runnable {
        override fun run() {
            playTime = intent.getIntExtra("playTime",0)
            while (!isThreadStop) {
                Log.d("grassActivity","${playTime}")
                playTime+=1
                Thread.sleep(1000)
            }
        }
    }

    fun divideGithubDataInfo(){
        for(index in 0 until githubData?.size!!){
            for (index1 in 0 until githubData?.get(index)?.contributionDays?.size!!){
                var date = githubData?.get(index)?.contributionDays?.get(index1)?.date.toString()
                var dateArray = date.split("-")
                if (index == 0 && index1 == 0){
                    githubDataArray.add(dateArray)
                }
                var tempMonth = dateArray[1]
                val monthInArray = githubDataArray[githubDataArray.size-1][1]
                if (tempMonth != monthInArray){
                    githubDataArray.add(dateArray)
                }
            }
        }
        val viewPager = findViewById<ViewPager2>(R.id.grass_page_view_pager2)
        val pagerAdapter = ScreenSlidePagerAdapter(this@GrassPageActivity)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.adapter = pagerAdapter
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                Log.d("onPageSelected","${position}")
                super.onPageSelected(position)
                val yearText = findViewById<TextView>(R.id.grass_page_year_text)
                val monthText = findViewById<TextView>(R.id.grass_page_month_text)
                yearText.text = "${githubDataArray[position][0]}년"
                monthText.text = "${githubDataArray[position][1]}월"
            }
        })
    }
    inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = githubDataArray.size

        override fun createFragment(position: Int): Fragment = GrassPageFragment(githubDataArray[position], githubData, playTime, position)
    }

    fun serviceBind() {
        val bindService = Intent(this, MyService::class.java)
        bindService(bindService, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    fun serviceUnBind() {
        Log.d("서비스","unbind")
        if (isConService) {
            unbindService(serviceConnection)
            isConService = false
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        Log.d("뒤로가기","버튼")
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("grassMoney", grassMoney)
        setResult(RESULT_OK, intent)
        finish()

//        intent.putExtra("grassMoney",grassMoney)
//        setResult(RESULT_OK,intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceUnBind()
        isThreadStop = true
    }

}

