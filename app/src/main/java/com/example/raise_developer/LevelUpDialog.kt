package com.example.raise_developer

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.raise_developer.DataBase.levelUp1to2Image
import com.example.raise_developer.DataBase.levelUp1to2Level
import com.example.raise_developer.DataBase.levelUp1to2Name
import com.example.raise_developer.DataBase.levelUp2to3Image
import com.example.raise_developer.DataBase.levelUp2to3Level
import com.example.raise_developer.DataBase.levelUp2to3Name
import com.example.raise_developer.DataBase.levelUp3to4Image
import com.example.raise_developer.DataBase.levelUp3to4Level
import com.example.raise_developer.DataBase.levelUp3to4Name
import com.example.raise_developer.DataBase.levelUp4to5Image
import com.example.raise_developer.DataBase.levelUp4to5Level
import com.example.raise_developer.DataBase.levelUp4to5Name
import com.example.raise_developer.DataBase.levelUp5to6Image
import com.example.raise_developer.DataBase.levelUp5to6Level
import com.example.raise_developer.DataBase.levelUp5to6Name
import com.example.raise_developer.DataBase.levelUp6to7Image
import com.example.raise_developer.DataBase.levelUp6to7Level
import com.example.raise_developer.DataBase.levelUp6to7Name
import com.example.raise_developer.DataBase.levelUp7to8Image
import com.example.raise_developer.DataBase.levelUp7to8Level
import com.example.raise_developer.DataBase.levelUp7to8Name

class LevelUpDialog() : DialogFragment(){
    private lateinit var onClickListener: DialogInterface.OnClickListener
    lateinit var linearLayout: LinearLayout
    var presentLv=1
    var presentConditionImage= levelUp1to2Image
    var presentConditionText= levelUp1to2Name
    var presentConditionLevel= levelUp1to2Level

    companion object {
        lateinit var prefs: PreferenceInventory
    }

    fun setOnClickListener(listener: DialogInterface.OnClickListener) {
        onClickListener = listener
    }

//    levelXuptoX 는 짝수가 이미지 홀수가 이름입니다
    fun initLevelCondition(view: View){
//        val existEmpoloy = prefs.getString("item", "")

    if (presentLv==1) {
            presentConditionImage = levelUp1to2Image
            presentConditionText= levelUp1to2Name
            presentConditionLevel= levelUp1to2Level
    }
        else if (presentLv==2) {
            presentConditionImage = levelUp2to3Image
            presentConditionText= levelUp2to3Name
            presentConditionLevel= levelUp2to3Level
    }
        else if (presentLv==3) {
            presentConditionImage = levelUp3to4Image
            presentConditionText= levelUp3to4Name
            presentConditionLevel= levelUp3to4Level

    }
        else if (presentLv==4) {
            presentConditionImage = levelUp4to5Image
            presentConditionText= levelUp4to5Name
            presentConditionLevel= levelUp4to5Level

    }
        else if (presentLv==5) {
            presentConditionImage = levelUp5to6Image
            presentConditionText= levelUp5to6Name
            presentConditionLevel= levelUp5to6Level

    }
        else if (presentLv==6) {
            presentConditionImage = levelUp6to7Image
            presentConditionText= levelUp6to7Name
            presentConditionLevel= levelUp6to7Level

    }
        else if (presentLv==7){
            presentConditionImage = levelUp7to8Image
            presentConditionText= levelUp7to8Name
            presentConditionLevel= levelUp7to8Level
    }
    else{
            presentConditionImage= arrayListOf()
            presentConditionText= arrayListOf()
            presentConditionLevel= arrayListOf()
    }
    linearLayout.removeAllViews()
    for (index in 0 until  presentConditionImage.size) {
        val customView = layoutInflater.inflate(R.layout.levelup_dialog_custom_view,linearLayout,false)
        val customViewImage=customView.findViewById<ImageView>(R.id.levelup_custom_view_image)
        val customViewText=customView.findViewById<TextView>(R.id.levelup_custom_view_text)
        val customViewLevelText=customView.findViewById<TextView>(R.id.condition_level_text)
        val imageChange =
            resources.getIdentifier(presentConditionImage[index], "mipmap", activity?.packageName)
        customViewImage.setImageResource(imageChange)
        customViewText.text=presentConditionText[index]
        linearLayout.addView(customView)
        customViewLevelText.text=presentConditionLevel[index].toString()
    }
    val levelText=view.findViewById<TextView>(R.id.lv_text)
    levelText.text="LV ${presentLv}"
}

    fun initBtnEvent(view: View){
        val levelUpBtn=view.findViewById<Button>(R.id.btn_update)
        levelUpBtn.setOnClickListener {
            existCheck()
            if (purchaseTrue){
            if (presentLv<7){
                presentLv+=1
            }
            else if (presentLv==7){
                levelUpBtn.text="최대레벨"
                presentLv+=1
            }
            else{
                levelUpBtn.text="최대레벨"
            }
            initLevelCondition(view)
            Toast.makeText(context, "축하드립니다 레벨업 LV${presentLv}", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, "레벨업 조건 미충족", Toast.LENGTH_SHORT).show()
            }
        }
    }
    var existEmpoloy= mutableListOf<String>()
    var existEmpoloyLevel= mutableListOf<String>()


    fun initPresentData(){
        prefs = PreferenceInventory(requireContext())
        existEmpoloy= prefs.getString("empoloy", "")[1]
        existEmpoloyLevel= prefs.getString("empoloy", "")[3]

    }

    var existCheckList= mutableListOf<String>()
    var purchaseTrue=false

    fun existCheck(){
        existCheckList.clear()
        purchaseTrue=false
        var check= 0
        for (index in 0 until presentConditionText.size) {
            for (i in 0 until existEmpoloy.size) {
                Log.d("test",existEmpoloy[i])
                Log.d("test",existEmpoloyLevel[i])
                if (presentConditionText[index] ==existEmpoloy[i]){
                    check+=1
                    if (existEmpoloyLevel[i]!=null) {
                        if (presentConditionLevel[index] <= existEmpoloyLevel[i].toInt()) {
                            existCheckList.add("구매가능")
                        }
                    }
                }
            }
        }
        if (check==existCheckList.size&& check==presentConditionText.size){
            purchaseTrue=true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.levelup_dialog,container,false)

        prefs = PreferenceInventory(requireContext())

        linearLayout = view.findViewById(R.id.levelup_dialog_scrollview_linear) // 스크롤 뷰의 linear layout - 여기다 커스텀 뷰를 추가해줌
        initPresentData()
        initLevelCondition(view)
        initBtnEvent(view)
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