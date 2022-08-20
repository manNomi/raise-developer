package com.example.raise_developer

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class OptionDialog(userId: String): DialogFragment() {
    var id = userId
    lateinit var bgmOnButtonClickListener: BgmOnButtonClickListener
    lateinit var bgmOffButtonClickListener: BgmOffButtonClickListener
    lateinit var soundEffectOffButtonClickListener: SoundEffectOffButtonClickListener
    lateinit var soundEffectOnButtonClickListener: SoundEffectOnButtonClickListener
    lateinit var logOutButtonClickListener: LogOutButtonClickListener
    lateinit var saveAndCloseButtonClickListener: SaveAndCloseButtonClickListener


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.option_dialog,container,false)
        isCancelable = false
        view.findViewById<TextView>(R.id.option_dialog_id).text = id
        initEvent(view)
        return view
    }

    fun initEvent(view: View){
        closeButtonEvent(view)
        view.findViewById<Button>(R.id.option_dialog_bgm_on_button).setOnClickListener {
            Log.d("눌럿냐","눌림?")
            bgmOnButtonClickListener.bgmOnButtonEvent()
            dismiss()
        }
        view.findViewById<Button>(R.id.option_dialog_bgm_off_button).setOnClickListener {
           bgmOffButtonClickListener.bgmOffButtonEvent()
            dismiss()
        }
        view.findViewById<Button>(R.id.option_dialog_sound_effect_on_button).setOnClickListener {
            soundEffectOnButtonClickListener.soundEffectOnButtonEvent()
            dismiss()
        }
        view.findViewById<Button>(R.id.option_dialog_sound_effect_off_button).setOnClickListener {
            soundEffectOffButtonClickListener.soundEffectOffButtonEvent()
            dismiss()
        }
        view.findViewById<Button>(R.id.option_dialog_logout_button).setOnClickListener {
            logOutButtonClickListener.logoutButtonEvent()
            dismiss()
        }
        view.findViewById<Button>(R.id.option_dialog_save_and_close_button).setOnClickListener {
            val tutorialDialog = TutorialDialog()
            tutorialDialog.show(parentFragmentManager,"optionDialog")
        }

    }

    fun closeButtonEvent(view: View) {
        val closeButton = view.findViewById<ImageView>(R.id.option_dialog_close_button)
        closeButton.setOnClickListener {
            dismiss()
        }
    }

    interface BgmOnButtonClickListener{
        fun bgmOnButtonEvent()

    }
    interface BgmOffButtonClickListener{
        fun bgmOffButtonEvent()

    }
    interface SoundEffectOnButtonClickListener{
        fun soundEffectOnButtonEvent()

    }
    interface SoundEffectOffButtonClickListener{
        fun soundEffectOffButtonEvent()

    }
    interface LogOutButtonClickListener{
        fun logoutButtonEvent()

    }
    interface SaveAndCloseButtonClickListener{
        fun saveAndCloseButtonEvent()
    }

    fun setBgmOnButtonEvent(listener: BgmOnButtonClickListener){
        bgmOnButtonClickListener = listener
    }
    fun setBgmOffButtonEvent(listener: BgmOffButtonClickListener){
        bgmOffButtonClickListener = listener
    }
    fun setSoundEffectOnButtonEvent(listener: SoundEffectOnButtonClickListener){
        soundEffectOnButtonClickListener = listener
    }
    fun setSoundEffectOffButtonEvent(listener: SoundEffectOffButtonClickListener){
        soundEffectOffButtonClickListener = listener
    }
    fun setLogoutButtonEvent(listener: LogOutButtonClickListener){
        logOutButtonClickListener = listener
    }
    fun setSaveAndCloseButtonEvent(listener: SaveAndCloseButtonClickListener){
        saveAndCloseButtonClickListener = listener
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 0.95f,0.7f) // 다이알로그 크기 조정
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