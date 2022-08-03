package com.example.raise_developer

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.DialogFragment

class InventoryDialog: DialogFragment() {
    lateinit var linearLayout: LinearLayout
    companion object {
        lateinit var prefs: PreferenceInventory
    }
    var existItem = mutableListOf<String>()
    var existItemName = mutableListOf<String>()
    var empoloy = mutableListOf<String>()
    var empoloyName = mutableListOf<String>()
    var empoloyType = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.inventory_dialog, container, false)
        linearLayout = view.findViewById(R.id.inventory_dialog_scroll_view_linear_layout)
        prefs = PreferenceInventory(requireContext())
        existItem = prefs.getString("item", "")[0]
        existItemName = prefs.getString("item", "")[1]

        empoloy= prefs.getString("empoloy", "")[0]
        empoloyName= prefs.getString("empoloy", "")[1]
        empoloyType= prefs.getString("empoloy", "")[2]


//        임플로이 인벤토리 띄우기
        addCustomView("item")
        employButtonEvent(view)
        inventoryButtonEvent(view)
        return view
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this,0.95f,0.7f) // 다이알로그 크기 조정

    }


//    삭제후 만드는 방식
    fun addCustomView(type:String){
        val childCount = linearLayout.childCount // linear layout의 할당된 뷰들의 수
        val viewMenu :MutableList<String>
        val viewName :MutableList<String>
        val viewType :MutableList<String>
        linearLayout.removeAllViews()
        if (type=="item"){
            viewMenu=existItem
            viewName=existItemName
            viewType= mutableListOf("")
        }
        else
        {
            viewMenu=empoloy
            viewName=empoloyName
            viewType=empoloyType
        }
        for(index in 0 until viewMenu.size){ // 나중에 사진이랑 금액 값 넣을거임
            val shopCustomView = layoutInflater.inflate(R.layout.inventory_custom_view,linearLayout,false)
            val customViewImage = shopCustomView.findViewById<ImageView>(R.id.inventory_custom_view_image_view)
            val customViewText =  shopCustomView.findViewById<TextView>(R.id.inventory_custom_view_text_view)
            shopCustomView.findViewById<ImageView>(R.id.inventory_custom_view_image_view)
            val imgaeChange=resources.getIdentifier(viewMenu[index], "mipmap", activity?.packageName)
            customViewImage.setImageResource(imgaeChange)
            if (type=="item"){
                customViewText.text=viewName[index]
            }
            else{
                customViewText.text="(${viewType[index]})\n" + viewName[index]
            }
            linearLayout.addView(shopCustomView)
        }
    }

    fun employButtonEvent(view: View){
        val employButton = view.findViewById<Button>(R.id.inventory_dialog_employ_button)
        val inventoryButton = view.findViewById<Button>(R.id.inventory_dialog_inventory_button)
        val scrollView = view.findViewById<ScrollView>(R.id.inventory_dialog_scroll_view)
        employButton.setOnClickListener {
            employButton.setBackgroundResource(R.drawable.shop_category_selected_button)
            inventoryButton.setBackgroundResource(R.drawable.shop_category_not_selected_button)
            scrollView.fullScroll(ScrollView.FOCUS_UP) // 스크롤 맨 위로 이동
            addCustomView("empoloy")
        }
    }

    fun inventoryButtonEvent(view: View){
        val employButton = view.findViewById<Button>(R.id.inventory_dialog_employ_button)
        val inventoryButton = view.findViewById<Button>(R.id.inventory_dialog_inventory_button)
        val scrollView = view.findViewById<ScrollView>(R.id.inventory_dialog_scroll_view)
        inventoryButton.setOnClickListener {
            inventoryButton.setBackgroundResource(R.drawable.shop_category_selected_button)
            employButton.setBackgroundResource(R.drawable.shop_category_not_selected_button)
            scrollView.fullScroll(ScrollView.FOCUS_UP) // 스크롤 맨 위로 이동
            addCustomView("item")
        }
    }

    fun Context.dialogFragmentResize(dialogFragment: DialogFragment, width: Float, height: Float) {// 다이알로그 크기 설정하는 함수
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