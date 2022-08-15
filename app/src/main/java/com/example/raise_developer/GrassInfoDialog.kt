package com.example.raise_developer

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import org.w3c.dom.Text

class GrassInfoDialog(date: String, contributionCount: String, grassColor: String, playTime: Int): DialogFragment() {
    var playTime = playTime
    var maxValue = 0
    var isThreadStop = false
    lateinit var progressBar: ProgressBar
    lateinit var progressBarValue: TextView
    lateinit var harvestButtonClickListener: HarvestButtonClickListener


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
        view.findViewById<TextView>(R.id.grass_info_dialog_contribution_count).text = "contributions=${contributionCount}"

        if (grassColor == "#9be9a8"){
            maxValue = 80
            view.findViewById<ImageView>(R.id.grass_info_dialog_grass_image).setImageResource(R.mipmap.grass_one)
        }

        else if(grassColor == "#40c463"){
            maxValue = 100
            view.findViewById<ImageView>(R.id.grass_info_dialog_grass_image).setImageResource(R.mipmap.grass_two)
        }

        else if(grassColor == "#30a14e"){
            maxValue = 120
            view.findViewById<ImageView>(R.id.grass_info_dialog_grass_image).setImageResource(R.mipmap.grass_three)
        }

        else if(grassColor == "#216e39"){
            maxValue = 140
            view.findViewById<ImageView>(R.id.grass_info_dialog_grass_image).setImageResource(R.mipmap.grass_four)
        }

        else{
            maxValue = 0
            view.findViewById<ImageView>(R.id.grass_info_dialog_grass_image).setImageResource(R.mipmap.empty_grass)

        }
        progressBar = view.findViewById(R.id.grass_info_dialog_progress_bar)
        progressBarValue = view.findViewById(R.id.grass_info_dialog_progress_bar_value)
        progressBar.max = maxValue
        Log.d("grassinfo","${maxValue}")
        if (maxValue != 0){
            val thread = Thread(PlayTime())
            thread.start()
        }
        return view
    }
    interface HarvestButtonClickListener{
        fun harvestMoney(playTime: Int)
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 0.7f,0.5f) // 다이알로그 크기 조정
    }

    fun setDialogListener(listener: HarvestButtonClickListener){ // 인터페이스의 함수 초기화
        harvestButtonClickListener = listener
    }

    inner class PlayTime(): Runnable {
        override fun run() {
            while (!isThreadStop) {
                Log.d("grassInfo","${playTime}")

                activity?.runOnUiThread {
                    if (playTime >= maxValue){
                        progressBarValue.text = "${maxValue} / ${maxValue}만원"
                        progressBar.progress = maxValue
                    }
                    else{
                        progressBarValue.text = "${playTime} / ${maxValue}만원"
                        progressBar.progress = playTime
                    }
                }
                playTime+=1
                Thread.sleep(1000)
            }
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

    override fun onDestroy() {
        super.onDestroy()
        isThreadStop = true
    }
}