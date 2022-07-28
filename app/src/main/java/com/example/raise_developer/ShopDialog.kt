package com.example.raise_developer

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.fragment.app.DialogFragment

class ShopDialog : DialogFragment() {
    lateinit var linearLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.shop_dialog,container,false)

        linearLayout = view.findViewById(R.id.shop_scroll_view_linear_layout) // 스크롤 뷰의 linear layout - 여기다 커스텀 뷰를 추가해줌
        addCustomView()
        employShopButtonEvent(view)
        watchShopButtonEvent(view)
        carShopButtonEvent(view)

        return view
    }

    fun addCustomView(){ // 커스텀 뷰 추가
        val childCount = linearLayout.childCount // linear layout의 할당된 뷰들의 수

        if (childCount == 0){
            for(index in 0 until 10){ // 나중에 사진이랑 금액 값 넣을거임
                val shopCustomView = layoutInflater.inflate(R.layout.shop_custom_view,linearLayout,false)
                shopCustomView.findViewById<ImageView>(R.id.shop_custom_view_image_view)
                shopCustomView.findViewById<Button>(R.id.shop_custom_view_buy_button)
                linearLayout.addView(shopCustomView)
            }
        }
    }

    fun employShopButtonEvent(view: View){
        val employButton = view.findViewById<Button>(R.id.shop_dialog_employ_shop_button)
        val watchButton = view.findViewById<Button>(R.id.shop_dialog_watch_shop_button)
        val carButton = view.findViewById<Button>(R.id.shop_dialog_car_shop_button)
        val scrollView = view.findViewById<ScrollView>(R.id.shop_scroll_view)
        employButton.setOnClickListener {
            employButton.setBackgroundResource(R.drawable.shop_category_selected_button)
            watchButton.setBackgroundResource(R.drawable.shop_category_not_selected_button)
            carButton.setBackgroundResource(R.drawable.shop_category_not_selected_button)
            scrollView.fullScroll(ScrollView.FOCUS_UP) // 스크롤 맨 위로 이동
        }
    }

    fun watchShopButtonEvent(view: View){
        val employButton = view.findViewById<Button>(R.id.shop_dialog_employ_shop_button)
        val watchButton = view.findViewById<Button>(R.id.shop_dialog_watch_shop_button)
        val carButton = view.findViewById<Button>(R.id.shop_dialog_car_shop_button)
        val scrollView = view.findViewById<ScrollView>(R.id.shop_scroll_view)
        watchButton.setOnClickListener {
            employButton.setBackgroundResource(R.drawable.shop_category_not_selected_button)
            watchButton.setBackgroundResource(R.drawable.shop_category_selected_button)
            carButton.setBackgroundResource(R.drawable.shop_category_not_selected_button)
            scrollView.fullScroll(ScrollView.FOCUS_UP) // 스크롤 맨 위로 이동
        }
    }

    fun carShopButtonEvent(view: View){
        val employButton = view.findViewById<Button>(R.id.shop_dialog_employ_shop_button)
        val watchButton = view.findViewById<Button>(R.id.shop_dialog_watch_shop_button)
        val carButton = view.findViewById<Button>(R.id.shop_dialog_car_shop_button)
        val scrollView = view.findViewById<ScrollView>(R.id.shop_scroll_view)
        carButton.setOnClickListener {
            employButton.setBackgroundResource(R.drawable.shop_category_not_selected_button)
            watchButton.setBackgroundResource(R.drawable.shop_category_not_selected_button)
            carButton.setBackgroundResource(R.drawable.shop_category_selected_button)
            scrollView.fullScroll(ScrollView.FOCUS_UP) // 스크롤 맨 위로 이동
        }
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this,0.95f,0.7f) // 다이알로그 크기 조정
    }

    fun Context.dialogFragmentResize(dialogFragment: DialogFragment, width: Float, height: Float) {// 다이알로그 크기 설정하는 함수
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT < 30) {

            val display = windowManager.defaultDisplay
            val size = Point()

            display.getSize(size)

            val window = dialogFragment.dialog?.window

            val x = (size.x * width).toInt()
            val y = (size.y * height).toInt()
            window?.setLayout(x, y)

        } else {

            val rect = windowManager.currentWindowMetrics.bounds

            val window = dialogFragment.dialog?.window

            val x = (rect.width() * width).toInt()
            val y = (rect.height() * height).toInt()

            window?.setLayout(x, y)
        }
    }
}