package com.example.raise_developer

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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.DialogFragment

lateinit var backLogo : ImageView

var backLogoOn=true
class PersonalDialog: DialogFragment() {
    lateinit var linearLayout: LinearLayout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = inflater.inflate(R.layout.personal_information_dialog,container,false)
        backLogo=view.findViewById<ImageView>(R.id.stage_us_logo)

        initEvent(view)

        return view
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 0.90f,0.4f) // 다이알로그 크기 조정
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

    fun bakcClickEvent(){
        if (backLogoOn==true) {
//            backLogo.visibility = View.INVISIBLE
            backLogoOn=false
            backLogo.setBackgroundResource(R.drawable.border_square_blind)
            val imageChange =
                resources.getIdentifier("empty", "mipmap", activity?.packageName)
            backLogo.setImageResource(imageChange)
        }
        else{
//            backLogo.visibility = View.VISIBLE
            backLogo.setBackgroundResource(R.drawable.border_square_white)
            val imageChange =
                resources.getIdentifier("stage_us_logo", "mipmap", activity?.packageName)
            backLogoOn=true
            backLogo.setImageResource(imageChange)
        }
    }

    fun initEvent(view: View){
        backLogo.setOnClickListener{
            bakcClickEvent()
        }
    }
}