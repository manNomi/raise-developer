package com.example.raise_developer

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialog

class GrassShopDialog: DialogFragment() {
    lateinit var linearLayout: LinearLayout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = inflater.inflate(R.layout.grass_shop_dialog,container,false)
        linearLayout = view.findViewById(R.id.grass_shop_dialog_linear_layout)
        addCustomView()
        return view
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 0.90f,0.4f) // 다이알로그 크기 조정
    }


    fun addCustomView(){
        for(index in 0 until 10){
            val customView = layoutInflater.inflate(R.layout.grass_shop_custom_view,linearLayout,false)
            linearLayout.addView(customView)
        }
    }
    fun Context.dialogFragmentResize(dialogFragment: DialogFragment, width: Float, height: Float) { // 다이알로그 크기 설정하는 함수
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