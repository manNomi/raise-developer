package com.example.raise_developer

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.DialogFragment

class ShopDialog(personalMoney: Int) : DialogFragment() {
    lateinit var linearLayout: LinearLayout
    var myPersonalMoney = personalMoney
    lateinit var customViewClickListener: CustomViewClickListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.shop_dialog,container,false)
        isCancelable = false
        linearLayout = view.findViewById(R.id.shop_scroll_view_linear_layout) // 스크롤 뷰의 linear layout - 여기다 커스텀 뷰를 추가해줌
        addCustomView(view)
        initEvent(view)

        return view
    }

    fun addCustomView(view:View){ // 커스텀 뷰 추가

        for(index in 1 until 20){
            val shopCustomView = layoutInflater.inflate(R.layout.shop_custom_view,linearLayout,false)
            val customViewImage = shopCustomView.findViewById<ImageView>(R.id.shop_custom_view_image_view)
            val customViewButton = shopCustomView.findViewById<Button>(R.id.shop_custom_view_buy_button)
            //이미지들이 character1,  character2로 되어 있어서 각각의 id를 가져오도록 했음
            val id = resources.getIdentifier("character${index}","mipmap",activity?.packageName)
            customViewImage.setImageResource(id)
            customViewButton.text = "200" // 일단은 각각 200000원이라고 했음
            customViewButton.setOnClickListener {

                if (myPersonalMoney >= 200){

                    customViewClickListener.purchaseSuccess(customViewButton.text.toString(), index) // 값 전달
                    Log.d("구매여부","구매성공")
                    myPersonalMoney -= 200
                }

                else {
                    Log.d("구매여부","구매실패")
                }
            }
            linearLayout.addView(shopCustomView)
        }
    }

    interface CustomViewClickListener{ // 메인 페이지로 값을 전달하기 위한 인터페이스
        fun purchaseSuccess(price: String, id: Int)
    }

    fun initEvent(view: View){
        employShopButtonEvent(view)
        watchShopButtonEvent(view)
        carShopButtonEvent(view)
        closeButtonEvent(view)
    }

    fun setDialogListener(listener: CustomViewClickListener){ // 인터페이스의 함수 초기화
        customViewClickListener = listener
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

    fun closeButtonEvent(view: View){
        val closeButton = view.findViewById<ImageButton>(R.id.close_button)
        closeButton.setOnClickListener {
            dismiss()
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