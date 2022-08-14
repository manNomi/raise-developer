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
import androidx.fragment.app.DialogFragment

class GrassInfoDialog(date: String, contributionCount: String, grassColor: String): DialogFragment() {
    var progressValue = 0
    var date = date
    var contributionCount = contributionCount
    var grassColor = grassColor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = inflater.inflate(R.layout.grass_info_dialog,container,false)
        view.findViewById<TextView>(R.id.grass_info_dialog_date).text = date
        view.findViewById<TextView>(R.id.grass_info_dialog_contribution_count).text = contributionCount
        if(grassColor == "#9be9a8"){
            view.findViewById<ImageView>(R.id.grass_info_dialog_grass_image).setImageResource(R.mipmap.grass_one)
        }

        else if(grassColor == "#40c463"){
            view.findViewById<ImageView>(R.id.grass_info_dialog_grass_image).setImageResource(R.mipmap.grass_two)
        }

        else if(grassColor == "#30a14e"){
            view.findViewById<ImageView>(R.id.grass_info_dialog_grass_image).setImageResource(R.mipmap.grass_three)
        }

        else if(grassColor == "#216e39"){
            view.findViewById<ImageView>(R.id.grass_info_dialog_grass_image).setImageResource(R.mipmap.grass_four)
        }

        else{
            view.findViewById<ImageView>(R.id.grass_info_dialog_grass_image).setImageResource(R.mipmap.empty_grass)

        }
        return view
    }


    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 0.6f,0.4f) // 다이알로그 크기 조정
    }

    fun setProgressBar(i: Int){
        progressValue = i
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