package com.example.raise_developer

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
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
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.raise_developer.DataBase.levelUp1to2Image
import com.example.raise_developer.DataBase.levelUp2to3Image
import com.example.raise_developer.DataBase.levelUp3to4Image
import com.example.raise_developer.DataBase.levelUp4to5Image
import com.example.raise_developer.DataBase.levelUp5to6Image
import com.example.raise_developer.DataBase.levelUp6to7Image
import com.example.raise_developer.DataBase.levelUp7to8Image
import com.example.raise_developer.DataBase.levelUp1to2Name

class LevelUpDialog() : DialogFragment(){
    private lateinit var onClickListener: DialogInterface.OnClickListener
    lateinit var linearLayout: LinearLayout
    lateinit var linearCanLayout: LinearLayout

    var presentLv=1
    fun setOnClickListener(listener: DialogInterface.OnClickListener) {
        onClickListener = listener
    }

//    levelXuptoX 는 짝수가 이미지 홀수가 이름입니다
    fun initLevelCondition(view: View){
//        val existEmpoloy = prefs.getString("item", "")
        var presentConditionImage= levelUp1to2Image
        if (presentLv==1) {
            presentConditionImage = levelUp1to2Image
        }
        else if (presentLv==2) {
            presentConditionImage = levelUp2to3Image
        }
        else if (presentLv==3) {
            presentConditionImage = levelUp3to4Image
        }
        else if (presentLv==4) {
            presentConditionImage = levelUp4to5Image
        }
        else if (presentLv==5) {
            presentConditionImage = levelUp5to6Image
        }
        else if (presentLv==6) {
            presentConditionImage = levelUp6to7Image
        }
        else if (presentLv==7){
            presentConditionImage = levelUp7to8Image
        }
    else{
            presentConditionImage= arrayListOf()
        }
    linearLayout.removeAllViews()
    for (index in 0 until  presentConditionImage.size) {
        val customView = layoutInflater.inflate(R.layout.levelup_dialog_custom_view,linearLayout,false)
        val customViewImage=customView.findViewById<ImageView>(R.id.levelup_custom_view_image)
        val imageChange =
            resources.getIdentifier(presentConditionImage[index], "mipmap", activity?.packageName)
        customViewImage.setImageResource(imageChange)
        linearLayout.addView(customView)
    }
    val levelText=view.findViewById<TextView>(R.id.lv_text)
    levelText.text="LV ${presentLv}"
}

    fun initBtnEvent(view: View){
        val levelUpBtn=view.findViewById<Button>(R.id.btn_update)
        levelUpBtn.setOnClickListener {
            if (presentLv!=8){
                presentLv+=1
                initLevelCondition(view)
            }
            else{
                levelUpBtn.text="최대레벨"
            }
        }

    }

    fun addCustomView(){
        for(index in 0 until 10){
            val customView = layoutInflater.inflate(R.layout.levelup_dialog_custom_view,linearLayout,false)
            linearCanLayout.addView(customView)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.levelup_dialog,container,false)
        linearLayout = view.findViewById(R.id.levelup_dialog_scrollview_linear) // 스크롤 뷰의 linear layout - 여기다 커스텀 뷰를 추가해줌
        linearCanLayout = view.findViewById(R.id.levelup_dialog_can_scrollview_linear) // 스크롤 뷰의 linear layout - 여기다 커스텀 뷰를 추가해줌
        initLevelCondition(view)
        initBtnEvent(view)
        addCustomView()
        return view
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