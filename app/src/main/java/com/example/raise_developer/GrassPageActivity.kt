package com.example.raise_developer

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import androidx.core.view.setMargins
import java.util.ArrayList

class GrassPageActivity: AppCompatActivity() {
    lateinit var array: ArrayList<ImageButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.grass_page)
        gridLayoutSetting()
    }

    fun gridLayoutSetting() {
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        for (index in 0 until 31) {
            Log.d("호호","하하")
            val customView = layoutInflater.inflate(R.layout.grass_page_custom_view,gridLayout,false)
            val imageButton = ImageButton(this) // 이미지 버튼 생성

            val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            param.setMargins(13) // 픽셀 단위임 dp 값은 13 * 160 / 410(dpi) == 약 5dp
            imageButton.layoutParams = param // 마진 값 적용

            imageButton.setOnClickListener { // 클릭하면 잔디 상점 다이알로그 띄움
                val grassShopDialog = GrassShopDialog()
                grassShopDialog.show(supportFragmentManager,"grassShopDialog")
            }

            if(index %2 == 0){ // 나중에 깃허브 정보를 가져와서 i f문으로 배경 이미지를 바꿀거임
                imageButton.setBackgroundResource(R.mipmap.empty_grass)
            }

            else {
                imageButton.setBackgroundResource(R.mipmap.in_grass)
            }

            gridLayout.addView(customView)
        }
    }
}
