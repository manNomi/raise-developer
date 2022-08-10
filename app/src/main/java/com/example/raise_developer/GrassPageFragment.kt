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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment

class GrassPageFragment(position: Int): Fragment() {
    var fragmentPagePosition = position

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.grass_page_view_pager_layout, container, false)
        gridLayoutSetting(view)
        return view
    }

    fun gridLayoutSetting(view: View) {
        Log.d("gridLayoutSetting","실행됨")
        val gridLayout = view.findViewById<GridLayout>(R.id.gridLayout)
        for (index in 0 until 20) {
            val customView = layoutInflater.inflate(R.layout.grass_page_custom_view,gridLayout,false)
            val grassImage = customView.findViewById<ImageView>(R.id.grass)
            val coinImage = customView.findViewById<ImageView>(R.id.coin)
            val param = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            param.setMargins(3) // 픽셀
            customView.layoutParams = param
//            customView.setOnClickListener { // 클릭하면 잔디 상점 다이알로그 띄움
//                val grassShopDialog = GrassShopDialog()
//                grassShopDialog.show(,"grassShopDialog")
//            }
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
        val harvestCoinButton = view.findViewById<Button>(R.id.harvestCoinButton)
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


    override fun onDestroy() {
        super.onDestroy()
    }
}