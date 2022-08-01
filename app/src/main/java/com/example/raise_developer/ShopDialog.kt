package com.example.raise_developer

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.raise_developer.DataBase.cartList
import com.example.raise_developer.DataBase.cartNameList
import com.example.raise_developer.DataBase.employName
import com.example.raise_developer.DataBase.employType
import com.example.raise_developer.DataBase.watchList
import com.example.raise_developer.DataBase.watchNameList

class ShopDialog(personalMoney: Int) : DialogFragment() {
    lateinit var linearLayout: LinearLayout
    var myPersonalMoney = personalMoney
    lateinit var customViewClickListener: CustomViewClickListener

    var presentType = "empoloy"

    companion object {
        lateinit var prefs: PreferenceInventory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.shop_dialog,container,false)
        isCancelable = false
        linearLayout = view.findViewById(R.id.shop_scroll_view_linear_layout) // 스크롤 뷰의 linear layout - 여기다 커스텀 뷰를 추가해줌
        prefs = PreferenceInventory(requireContext())
        DataBase.watchInit()
        DataBase.cartInit()
        addCustomView("empoly")
        initEvent(view)
        return view
    }

    fun addCustomView(type:String) { // 커스텀 뷰 추가
        linearLayout.removeAllViews()
//        image 변수 아이템 or 고용 둘중 key 에 따라 달라지게 함
//        image 변수에는 image가 들어감
        var image = mutableListOf<String>()
//        imageindex변수 - > 프리퍼런스에서 가져온 데이터가 이미 존재(구매한 데이터일경우)같을경우 index를 저장 해놓는 변수
        var imageIndex = mutableListOf<Int>()
//        존재하는 아이템 고용 값 가져오기 형태는 어레이
        val existItem = prefs.getString("item", "")
        val existEmploy =
            prefs.getString("employ", "") // empoloy로 되어있어서  employ로 바꿨음  preference 값 확인할 것
//        네임 값
        var nameList = mutableListOf<String>()
//        매개변수로 받은 type에 따라서 갈림길 중복코드 방지용
        if (type == "watch") {
            image = watchList
            presentType = "item"
            nameList = watchNameList
        } else if (type == "cart") {
            image = cartList
            presentType = "item"
            nameList = cartNameList
        } else {
            for (index in 1 until 20) {
                image.add("character${index}")
            }
            presentType = "employ"
            nameList = employName
        }
//        아래 두개는 중복 - 이미 구매한 것들 반복문 돌려서 index 넣어두는곳
        if (existItem[0].size != 0) {
            for (index in 0 until image.size) {
                for (i in 0 until existItem[0].size) {
                    if (image[index] == existItem[0][i]) {
                        imageIndex.add(index)
                    }
                }
            }
        }
        Log.d("size", image.size.toString())
        if (existEmploy[0].size != 0) {
            for (index in 0 until image.size) {
                for (i in 0 until existEmploy[0].size) {
                    if (image[index] == existEmploy[0][i]) {
                        imageIndex.add(index)
                    }
                }
            }
        }
        for (index in 1 until image.size) {

            val shopCustomView =
                layoutInflater.inflate(R.layout.shop_custom_view, linearLayout, false)
            val customViewImage =
                shopCustomView.findViewById<ImageView>(R.id.shop_custom_view_image_view)
            val customViewButton =
                shopCustomView.findViewById<Button>(R.id.shop_custom_view_buy_button)

            val imageChange =
                resources.getIdentifier(image[index], "mipmap", activity?.packageName)
            customViewImage.setImageResource(imageChange)
            customViewButton.text = "2000" // 일단은 각각 200000원이라고 했음
            var purchaseCheck = false
//            아까 넣어둔 이미지 인덱스 값과 비교해서 같은 것 나오면 구매완료 누르고
//            이미 구매했다는 변수 true로 만들기
            for (i in 0 until imageIndex.size) {
                if (index == imageIndex[i]) {
                    customViewButton.text = "구매완료"
                    purchaseCheck = true
                }
            }
            customViewButton.setOnClickListener {
                if (myPersonalMoney >= 2000 && !purchaseCheck) {
                    customViewClickListener.purchaseSuccess(
                        customViewButton.text.toString(),
                        image[index],
                        presentType
                    ) // 값 전달
                    Log.d("구매여부", "구매성공")
                    myPersonalMoney -= 2000
                    Log.d("type", nameList[index])
                    prefs.setString(
                        presentType,
                        image[index],
                        nameList[index],
                        employType[index]
                    )

                    customViewButton.text = "구매완료"
                    purchaseCheck = true
                    Toast.makeText(context, "구매성공", Toast.LENGTH_SHORT).show()

                } else {
                    Log.d("구매여부", "구매실패")
                    Toast.makeText(context, "구매할 수 없습니다", Toast.LENGTH_SHORT).show()
                }
            }
            linearLayout.addView(shopCustomView)
        }
    }



    interface CustomViewClickListener{ // 메인 페이지로 값을 전달하기 위한 인터페이스

        fun purchaseSuccess(price: String, id:String, type: String)

    }

    fun initEvent(view: View){
        employShopButtonEvent(view)
        watchShopButtonEvent(view)
        carShopButtonEvent(view)
        closeButtonEvent(view)
    }

    fun setDialogListener(listener: CustomViewClickListener){ // 인터페이스의 함수 초기화
        customViewClickListener = listener
    }

    fun employShopButtonEvent(view: View){
        val employButton = view.findViewById<Button>(R.id.shop_dialog_employ_shop_button)
        val watchButton = view.findViewById<Button>(R.id.shop_dialog_watch_shop_button)
        val carButton = view.findViewById<Button>(R.id.shop_dialog_car_shop_button)
        val scrollView = view.findViewById<ScrollView>(R.id.shop_scroll_view)
        employButton.setOnClickListener {
            employButton.setBackgroundResource(R.drawable.shop_category_selected_button)
            watchButton.setBackgroundResource(R.drawable.shop_category_not_selected_button)
            carButton.setBackgroundResource(R.drawable.shop_category_not_selected_button)
            scrollView.fullScroll(ScrollView.FOCUS_UP) // 스크롤 맨 위로 이동
            addCustomView("empoly")  // 커스텀뷰를 계속 찍어내는 방식이므로 추가해줘야함
        }
    }

    fun watchShopButtonEvent(view: View){
        val employButton = view.findViewById<Button>(R.id.shop_dialog_employ_shop_button)
        val watchButton = view.findViewById<Button>(R.id.shop_dialog_watch_shop_button)
        val carButton = view.findViewById<Button>(R.id.shop_dialog_car_shop_button)
        val scrollView = view.findViewById<ScrollView>(R.id.shop_scroll_view)
        watchButton.setOnClickListener {
            employButton.setBackgroundResource(R.drawable.shop_category_not_selected_button)
            watchButton.setBackgroundResource(R.drawable.shop_category_selected_button)
            carButton.setBackgroundResource(R.drawable.shop_category_not_selected_button)
            scrollView.fullScroll(ScrollView.FOCUS_UP) // 스크롤 맨 위로 이동
            addCustomView("watch") // 커스텀뷰를 계속 찍어내는 방식이므로 추가해줘야함
        }
    }

    fun carShopButtonEvent(view: View){
        val employButton = view.findViewById<Button>(R.id.shop_dialog_employ_shop_button)
        val watchButton = view.findViewById<Button>(R.id.shop_dialog_watch_shop_button)
        val carButton = view.findViewById<Button>(R.id.shop_dialog_car_shop_button)
        val scrollView = view.findViewById<ScrollView>(R.id.shop_scroll_view)
        carButton.setOnClickListener {
            employButton.setBackgroundResource(R.drawable.shop_category_not_selected_button)
            watchButton.setBackgroundResource(R.drawable.shop_category_not_selected_button)
            carButton.setBackgroundResource(R.drawable.shop_category_selected_button)
            scrollView.fullScroll(ScrollView.FOCUS_UP) // 스크롤 맨 위로 이동
            addCustomView("cart") // 커스텀뷰를 계속 찍어내는 방식이므로 추가해줘야함
        }
    }

    fun closeButtonEvent(view: View){
        val closeButton = view.findViewById<ImageButton>(R.id.close_button)
        closeButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this,0.95f,0.7f) // 다이알로그 크기 조정
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
