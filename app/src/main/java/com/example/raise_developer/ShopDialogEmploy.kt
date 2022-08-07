package com.example.raise_developer

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import kotlin.random.Random

class ShopDialogEmploy(personalMoney: Int,employ: String , price: Int) : DialogFragment() {
    lateinit var linearLayout: LinearLayout
    var myPersonalMoney = personalMoney
    lateinit var customViewClickListener: ShopDialogEmploy.CustomViewClickListener

    var presentType = "empoloy"

    var presentLevel = mutableListOf<String>()

    companion object {
        lateinit var prefs: PreferenceInventory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.shop_dialog, container, false)
        isCancelable = false
        linearLayout =
            view.findViewById(R.id.shop_scroll_view_linear_layout) // 스크롤 뷰의 linear layout - 여기다 커스텀 뷰를 추가해줌
        prefs = PreferenceInventory(requireContext())
        DataBase.watchInit()
        DataBase.cartInit()
        return view
    }

//    fun setDialogListener(listener: ShopDialog.CustomViewClickListener) { // 인터페이스의 함수 초기화
//        customViewClickListener = listener
//    }

    interface CustomViewClickListener{ // 메인 페이지로 값을 전달하기 위한 인터페이스
        fun purchaseSuccess(price: String, id:String, type: String)
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