package com.example.raise_developer

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.raise_developer.FireStore.rankData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import java.util.Collections.swap


class RankingActivity: AppCompatActivity() {
    lateinit var linearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ranking_page)
        linearLayout = findViewById(R.id.shop_scroll_view_linear_layout) // 스크롤 뷰의 linear layout - 여기다 커스텀 뷰를 추가해줌
        initEvent()
        ititData()
    }

    var rankLevelData= mutableListOf<Array<String>>()
    var rankMoneyData= mutableListOf<Array<String>>()

    var levelSortData= mutableListOf<Array<String>>()
    var moneySortData= mutableListOf<Array<String>>()

    var btnType="level"

    fun ititData(){
        var userData=FireStore.db.collection("user")
            .get()
        CoroutineScope(Dispatchers.Main).launch {
            val data_1 =async {
                userData=FireStore.db.collection("user")
                    .get()
            }
            data_1.await()

            val data_2 =async {
            userData.addOnSuccessListener { result ->
                        for (document in result) {
                            Log.d("TAG", "${document.id} => ${document.data}")
                            var levelData =
                                document.data.toString().split("level=")[1].split(",")[0]
                            var moneyData =
                                document.data.toString().split("money=")[1].split(",")[0]
                            val rankLevelDefault = arrayOf(document.id, levelData)
                            val rankMoneyDefault = arrayOf(document.id, moneyData)
                            rankMoneyData.add(rankMoneyDefault)
                            rankLevelData.add(rankLevelDefault)
                        }
                        Log.d("과연2","머니 ${rankMoneyData[0][1]}, 랭크 ${rankLevelData[0][1]} ")
                        levelSortData=selectionSort(rankLevelData)
                        moneySortData=selectionSort(rankMoneyData)
                        Log.d("과연","레벨 ${levelSortData}, 머니 ${moneySortData} ")
                        addCustomView(levelSortData,moneySortData)
                        }
                    }
                    userData.addOnFailureListener { exception ->
                        Log.w("TAG", "Error getting documents.", exception)
                    }
            data_2.await()
        }
    }

    fun selectionSort(data: MutableList<Array<String>>):MutableList<Array<String>> {
        var v =data
        for (i in 0 until v.size - 1) {
            var tmp = i
            for (j in i + 1 until v.size) {
                if (v[tmp][1].toInt() <= v[j][1].toInt()) tmp = j
            }
            var x=v[i]
            var y =v[tmp]
            v[i]=y
            v[tmp]=x
        }
        return v
    }

    fun addCustomView(levelData:MutableList<Array<String>>,moneyData:MutableList<Array<String>>){
        linearLayout.removeAllViews()
        var data =levelData
        var type="원"
        if (btnType=="level") {
            data =levelData
            type="레벨"
        }
        else {
            data=moneyData
            type="만원"
        }
        for(index in 0 until data.size){ // 나중에 사진이랑 금액 값 넣을거임
            val rankCustomView = layoutInflater.inflate(R.layout.rank_custom_view,linearLayout,false)
            val customViewText =  rankCustomView.findViewById<TextView>(R.id.shop_custom_view_not_employ_box)
            customViewText.text="${index+1}등 ${data[index][0]}님 ${data[index][1]} $type"
            linearLayout.addView(rankCustomView)
        }
    }

    fun initEvent(){
        assetButtonEvent()
        levelButtonEvent()
    }
    fun assetButtonEvent() {
        val assetButton = findViewById<Button>(R.id.asset_btn)
        val levelButton = findViewById<Button>(R.id.level_btn)
        val scrollView = findViewById<ScrollView>(R.id.rank_scroll_view)
        assetButton.setOnClickListener {
            assetButton.setBackgroundResource(R.drawable.shop_category_selected_button)
            levelButton.setBackgroundResource(R.drawable.shop_category_not_selected_button)
            scrollView.fullScroll(ScrollView.FOCUS_UP) // 스크롤 맨 위로 이동
            btnType="asset"
            addCustomView(levelSortData,moneySortData)
        }
    }

    fun levelButtonEvent() {
        val assetButton = findViewById<Button>(R.id.asset_btn)
        val levelButton = findViewById<Button>(R.id.level_btn)
        val scrollView = findViewById<ScrollView>(R.id.rank_scroll_view)
        levelButton.setOnClickListener {
            levelButton.setBackgroundResource(R.drawable.shop_category_selected_button)
            assetButton.setBackgroundResource(R.drawable.shop_category_not_selected_button)
            scrollView.fullScroll(ScrollView.FOCUS_UP) // 스크롤 맨 위로 이동
            btnType="level"
            addCustomView(levelSortData,moneySortData)
        }
    }
}