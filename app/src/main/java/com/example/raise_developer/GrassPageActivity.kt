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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.graphqlsample.queries.GithubCommitQuery
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import kotlinx.coroutines.*
import org.w3c.dom.Text
import java.util.ArrayList

class GrassPageActivity: FragmentActivity() {
    var month = arrayListOf("January","February","March","April","May","June","July","August","September","October","November","December")

    var githubData: List<GithubCommitQuery.Week>? = null

    var myService : MyService? = null
    var isConService = false
    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            Log.d("grass_page_service","실행됨")
            val b = p1 as MyService.LocalBinder
            myService = b.getService()
            isConService = true
            githubData = myService?.githubInfoServiceToGrassPage()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isConService = false
        }
    }


    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Main).launch {
            val bindSer = async { serviceBind() }
            bindSer.await()
            Log.d("커밋정보","${githubData?.get(0)?.contributionDays?.get(0)?.date}")
            val arr = githubData?.get(0)?.contributionDays?.get(0)?.date.toString()
            val arr1 = arr.split("-")
            Log.d("배열","${arr1}")
//            gridLayoutSetting()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.grass_page)
        val viewPager = findViewById<ViewPager2>(R.id.grass_page_view_pager2)
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.adapter = pagerAdapter
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val monthText = findViewById<TextView>(R.id.grass_page_month_text)
                monthText.text = "${position+1}월"
            }
        })

    }
    inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = month.size

        override fun createFragment(position: Int): Fragment = GrassPageFragment(position)

    }

    fun gridLayoutSetting() {
        Log.d("gridLayoutSetting","실행됨")
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        for (index in 0 until 20) {
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
        Log.d("서비스","unbind")
        if (isConService) {
            unbindService(serviceConnection)
            isConService = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceUnBind()
    }

}

