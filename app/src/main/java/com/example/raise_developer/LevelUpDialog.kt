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
import com.example.raise_developer.DataBase
import com.example.raise_developer.DataBase.levelUpPrice
import com.example.raise_developer.DataBase.levelupCondition

class LevelUpDialog(var userLv: Int, var personalMoney: Int, ) : DialogFragment(){
    private lateinit var onClickListener: DialogInterface.OnClickListener

    lateinit var linearLayout: LinearLayout // 커스텀뷰들을 닮을 레이아웃

    var conditionName = arrayListOf("첫 번 째 이름", "두 번째 이름", "세 번째 이름")
    var conditionImage = arrayListOf("첫 번 째 이미지", "두 번째 이미지", "세 번째 이미지")
    var conditionLevel = arrayListOf(0, 0, 0)

    var effectAnnualMoneyPlus = 0 //레벨업시 추가 연봉
    var effectAnnaulMoneyMultiple = 0 //추가 연봉에 대한 기호
    var effectTouchMoney = 0 //레벨업시 추가 터치수당

    companion object {
        lateinit var prefs: PreferenceInventory
    }

    fun setOnClickListener(listener: DialogInterface.OnClickListener) {
        onClickListener = listener
    }
    // 현재 레벨에 따른 조건 설정
    fun setCondition() {
        // 초기화
        conditionName=arrayListOf()
        conditionImage=arrayListOf()
        conditionLevel=arrayListOf()

        if (userLv == 9) {
            effectAnnualMoneyPlus=0
            effectAnnaulMoneyMultiple=3
            effectTouchMoney=3
            conditionName.add(DataBase.levelupCondition[0][0].name)
            conditionName.add(DataBase.levelupCondition[0][1].name)
            conditionName.add(DataBase.levelupCondition[0][2].name)
            conditionImage.add(DataBase.levelupCondition[0][0].image)
            conditionImage.add(DataBase.levelupCondition[0][1].image)
            conditionImage.add(DataBase.levelupCondition[0][2].image)
            conditionLevel.add(DataBase.levelupCondition[0][0].level)
            conditionLevel.add(DataBase.levelupCondition[0][1].level)
            conditionLevel.add(DataBase.levelupCondition[0][2].level)
        }
        else if (userLv == 19) {
            effectAnnualMoneyPlus=0
            effectAnnaulMoneyMultiple=6
            effectTouchMoney=3
            conditionName.add(DataBase.levelupCondition[1][0].name)
            conditionName.add(DataBase.levelupCondition[1][1].name)
            conditionName.add(DataBase.levelupCondition[1][2].name)
            conditionImage.add(DataBase.levelupCondition[1][0].image)
            conditionImage.add(DataBase.levelupCondition[1][1].image)
            conditionImage.add(DataBase.levelupCondition[1][2].image)
            conditionLevel.add(DataBase.levelupCondition[1][0].level)
            conditionLevel.add(DataBase.levelupCondition[1][1].level)
            conditionLevel.add(DataBase.levelupCondition[1][2].level)
        }
        else if (userLv == 29) {
            effectAnnualMoneyPlus=0
            effectAnnaulMoneyMultiple=9
            effectTouchMoney=3
            conditionName.add(DataBase.levelupCondition[2][0].name)
            conditionName.add(DataBase.levelupCondition[2][1].name)
            conditionName.add(DataBase.levelupCondition[2][2].name)
            conditionImage.add(DataBase.levelupCondition[2][0].image)
            conditionImage.add(DataBase.levelupCondition[2][1].image)
            conditionImage.add(DataBase.levelupCondition[2][2].image)
            conditionLevel.add(DataBase.levelupCondition[2][0].level)
            conditionLevel.add(DataBase.levelupCondition[2][1].level)
            conditionLevel.add(DataBase.levelupCondition[2][2].level)
        }
        else if (userLv == 39) {
            effectAnnualMoneyPlus=0
            effectAnnaulMoneyMultiple=12
            effectTouchMoney=3
            conditionName.add(DataBase.levelupCondition[3][0].name)
            conditionName.add(DataBase.levelupCondition[3][1].name)
            conditionName.add(DataBase.levelupCondition[3][2].name)
            conditionImage.add(DataBase.levelupCondition[3][0].image)
            conditionImage.add(DataBase.levelupCondition[3][1].image)
            conditionImage.add(DataBase.levelupCondition[3][2].image)
            conditionLevel.add(DataBase.levelupCondition[3][0].level)
            conditionLevel.add(DataBase.levelupCondition[3][1].level)
            conditionLevel.add(DataBase.levelupCondition[3][2].level)
        }
        else if (userLv == 49) {
            effectAnnualMoneyPlus=0
            effectAnnaulMoneyMultiple=15
            effectTouchMoney=3
            conditionName.add(DataBase.levelupCondition[4][0].name)
            conditionName.add(DataBase.levelupCondition[4][1].name)
            conditionName.add(DataBase.levelupCondition[4][2].name)
            conditionImage.add(DataBase.levelupCondition[4][0].image)
            conditionImage.add(DataBase.levelupCondition[4][1].image)
            conditionImage.add(DataBase.levelupCondition[4][2].image)
            conditionLevel.add(DataBase.levelupCondition[4][0].level)
            conditionLevel.add(DataBase.levelupCondition[4][1].level)
            conditionLevel.add(DataBase.levelupCondition[4][2].level)
        }
        else if (userLv == 59) {
            effectAnnualMoneyPlus=0
            effectAnnaulMoneyMultiple=18
            effectTouchMoney=3
            conditionName.add(DataBase.levelupCondition[5][0].name)
            conditionName.add(DataBase.levelupCondition[5][1].name)
            conditionName.add(DataBase.levelupCondition[5][2].name)
            conditionImage.add(DataBase.levelupCondition[5][0].image)
            conditionImage.add(DataBase.levelupCondition[5][1].image)
            conditionImage.add(DataBase.levelupCondition[5][2].image)
            conditionLevel.add(DataBase.levelupCondition[5][0].level)
            conditionLevel.add(DataBase.levelupCondition[5][1].level)
            conditionLevel.add(DataBase.levelupCondition[5][2].level)
        }
        else if (userLv == 69){
            effectAnnualMoneyPlus=0
            effectAnnaulMoneyMultiple=21
            effectTouchMoney=3
            conditionName.add(DataBase.levelupCondition[6][0].name)
            conditionName.add(DataBase.levelupCondition[6][1].name)
            conditionName.add(DataBase.levelupCondition[6][2].name)
            conditionImage.add(DataBase.levelupCondition[6][0].image)
            conditionImage.add(DataBase.levelupCondition[6][1].image)
            conditionImage.add(DataBase.levelupCondition[6][2].image)
            conditionLevel.add(DataBase.levelupCondition[6][0].level)
            conditionLevel.add(DataBase.levelupCondition[6][1].level)
            conditionLevel.add(DataBase.levelupCondition[6][2].level)
        }
        else if (userLv == 79){
            effectAnnualMoneyPlus=0
            effectAnnaulMoneyMultiple=24
            effectTouchMoney=3
            conditionName.add(DataBase.levelupCondition[7][0].name)
            conditionName.add(DataBase.levelupCondition[7][1].name)
            conditionName.add(DataBase.levelupCondition[7][2].name)
            conditionImage.add(DataBase.levelupCondition[7][0].image)
            conditionImage.add(DataBase.levelupCondition[7][1].image)
            conditionImage.add(DataBase.levelupCondition[7][2].image)
            conditionLevel.add(DataBase.levelupCondition[7][0].level)
            conditionLevel.add(DataBase.levelupCondition[7][1].level)
            conditionLevel.add(DataBase.levelupCondition[7][2].level)
        }
        else if (userLv == 89){
            effectAnnualMoneyPlus=0
            effectAnnaulMoneyMultiple=27
            effectTouchMoney=3
            conditionName.add(DataBase.levelupCondition[8][0].name)
            conditionName.add(DataBase.levelupCondition[8][1].name)
            conditionName.add(DataBase.levelupCondition[8][2].name)
            conditionImage.add(DataBase.levelupCondition[8][0].image)
            conditionImage.add(DataBase.levelupCondition[8][1].image)
            conditionImage.add(DataBase.levelupCondition[8][2].image)
            conditionLevel.add(DataBase.levelupCondition[8][0].level)
            conditionLevel.add(DataBase.levelupCondition[8][1].level)
            conditionLevel.add(DataBase.levelupCondition[8][2].level)
        }
        else if (userLv == 99){
            effectAnnualMoneyPlus=0
            effectAnnaulMoneyMultiple=3
            effectTouchMoney=3
            conditionName.add(DataBase.levelupCondition[9][0].name)
            conditionName.add(DataBase.levelupCondition[9][1].name)
            conditionName.add(DataBase.levelupCondition[9][2].name)
            conditionImage.add(DataBase.levelupCondition[9][0].image)
            conditionImage.add(DataBase.levelupCondition[9][1].image)
            conditionImage.add(DataBase.levelupCondition[9][2].image)
            conditionLevel.add(DataBase.levelupCondition[9][0].level)
            conditionLevel.add(DataBase.levelupCondition[9][1].level)
            conditionLevel.add(DataBase.levelupCondition[9][2].level)
        }
        else{
            effectAnnualMoneyPlus=100
            effectTouchMoney=1
            conditionName=arrayListOf()
            conditionImage=arrayListOf()
            conditionLevel=arrayListOf()
        }
    }
    // 뷰 설정
    fun setView(view: View){
        setCondition()
        //지움
        linearLayout.removeAllViews()
        //커스텀 뷰 설정
        for (index in 0 until conditionName.size) {
            val customView = layoutInflater.inflate(R.layout.levelup_dialog_custom_view,linearLayout,false)
            val customViewImage=customView.findViewById<ImageView>(R.id.levelup_custom_view_image)
            val customViewName=customView.findViewById<TextView>(R.id.levelup_custom_view_text)
            val customViewLevel=customView.findViewById<TextView>(R.id.condition_level_text)
            val imageChange = resources.getIdentifier(conditionImage[index], "mipmap", activity?.packageName)
            customViewImage.setImageResource(imageChange)
            customViewName.text=conditionName[index]
            customViewLevel.text=conditionLevel[index].toString()
            linearLayout.addView(customView)
        }
        //몇 레벨 업 조건인지 설명하는 텍스트 작성
        val conditionNumText = view.findViewById<TextView>(R.id.levelup_condition_num)
        conditionNumText.text = "LV${userLv+1}에 도달하기 위한 조건"
        //현재 유저의 레벨 설정
        val levelText=view.findViewById<TextView>(R.id.lv_text)
        levelText.text="LV ${userLv}"
        //레벨업 시 효과 및 레벨업 버튼 설정
        val effectAnnualMoneyText=view.findViewById<TextView>(R.id.effect_annual_money)
        val effectTouchMoneyText=view.findViewById<TextView>(R.id.effect_touch_money)
        val levelUpBtn=view.findViewById<Button>(R.id.btn_level_up)
        if(userLv % 10 == 9) {
            effectAnnualMoneyText.text = "연봉 + ${effectAnnaulMoneyMultiple}0%"
            effectTouchMoneyText.text = "터치수당 + ${effectTouchMoney}0000"
            levelUpBtn.text = "LV${userLv} -> LV${userLv+1}\n${DataBase.levelUpPrice[userLv]}만원"
        }
        else if(userLv == 100) {
            effectAnnualMoneyText.text = ""
            effectTouchMoneyText.text = ""
            levelUpBtn.text="최대레벨"
        }
        else {
            effectAnnualMoneyText.text = "연봉 + ${effectAnnualMoneyPlus}0000"
            effectTouchMoneyText.text = "터치수당 + ${effectTouchMoney}0000"
            levelUpBtn.text = "LV${userLv} -> LV${userLv+1}\n${DataBase.levelUpPrice[userLv]}만원"
        }
    }
    //레벨 업 버튼
    fun initBtnEvent(view: View){
        val levelUpBtn=view.findViewById<Button>(R.id.btn_level_up)
        levelUpBtn.setOnClickListener {
            if(userLv % 10 == 9) {
                if(personalMoney >= DataBase.levelUpPrice[userLv]) {
                    existCheck()
                    if (purchaseTrue){
                        personalMoney-=DataBase.levelUpPrice[userLv]
                        userLv+=1
                        val dataInterface = context as LevelUpInterface
                        dataInterface.setUserLvLogic(userLv)
                        dataInterface.setMultipleAnnualMoneyLogic(effectAnnaulMoneyMultiple)
                        dataInterface.plusTouchMoneyLogic(effectTouchMoney)
                        dataInterface.setPersonalMoneyLogic(personalMoney)

                        setView(view)
                    }
                    else{
                        Toast.makeText(context, "레벨업 조건 미충족", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(context, "돈이 부족합니다!", Toast.LENGTH_SHORT).show()
                }
            }
            else if(userLv == 100) {Toast.makeText(context, "최대 레벨입니다!", Toast.LENGTH_SHORT).show()}
            else {
                if(personalMoney >= DataBase.levelUpPrice[userLv]) {
                    personalMoney-=DataBase.levelUpPrice[userLv]
                    userLv+=1
                    val dataInterface = context as LevelUpInterface
                    dataInterface.setUserLvLogic(userLv)
                    dataInterface.plusTouchMoneyLogic(effectTouchMoney)
                    dataInterface.plusAnnualMoneyLogic(effectAnnualMoneyPlus)
                    dataInterface.setPersonalMoneyLogic(personalMoney)

                    setView(view)
                }
                else{
                    Toast.makeText(context, "돈이 부족합니다!", Toast.LENGTH_SHORT).show()
                }
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
        for (index in 0 until conditionName.size) {
            for (i in 0 until existEmpoloy.size) {
                if (conditionName[index] ==existEmpoloy[i]){
                    check+=1
                    if (existEmpoloyLevel[i]!=null) {
                        if (conditionLevel[index] <= existEmpoloyLevel[i].toInt()) {
                            existCheckList.add("구매가능")
                        }
                    }
                }
            }
        }
        if (check==existCheckList.size&& check==conditionName.size){
            purchaseTrue=true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.levelup_dialog,container,false)

        prefs = PreferenceInventory(requireContext())

        linearLayout = view.findViewById(R.id.levelup_dialog_scrollview_linear) // 스크롤 뷰의 linear layout - 여기다 커스텀 뷰를 추가해줌
        initPresentData()
        setView(view)
        initBtnEvent(view)
        return view
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 0.95f,0.7f) // 다이알로그 크기 조정
    }

    //크기 조절
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