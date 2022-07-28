package com.example.raise_developer
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text

class GrassCheckActivity: AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.grass_check_page)
        initEvent()
    }
    fun initEvent(){
//        이전버튼
        val prevBtn=findViewById<ImageView>(R.id.prev_btn)
        prevBtn.setOnClickListener{
            prevEvent()
        }
//      다음버튼
        val nextBtn=findViewById<ImageView>(R.id.next_btn)
        nextBtn.setOnClickListener{
            nextEvent()
        }
        var grassBtn : MutableList<Int>


    }

    fun nextEvent(){
        val year_text=findViewById<TextView>(R.id.year_text)
        var text = year_text.text.split("년")[0].toInt()
        text+=1
        year_text.text=text.toString()+"년"
    }

    fun prevEvent(){
        val year_text=findViewById<TextView>(R.id.year_text)
        var text = year_text.text.split("년")[0].toInt()
        text-=1
        year_text.text=text.toString()+"년"

    }


}