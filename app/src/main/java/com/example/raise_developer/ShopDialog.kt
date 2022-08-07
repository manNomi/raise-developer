package com.example.raise_developer

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.text.BoringLayout
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.raise_developer.DataBase.cartList
import com.example.raise_developer.DataBase.cartNameList
import com.example.raise_developer.DataBase.cartPrice
import com.example.raise_developer.DataBase.employLevel
import com.example.raise_developer.DataBase.employName
import com.example.raise_developer.DataBase.employPrice
import com.example.raise_developer.DataBase.employType
import com.example.raise_developer.DataBase.watchList
import com.example.raise_developer.DataBase.watchNameList
import com.example.raise_developer.DataBase.watchPrice
import kotlin.random.Random

class ShopDialog(personalMoney: Int) : DialogFragment() {
    lateinit var linearLayout: LinearLayout
    var myPersonalMoney = personalMoney
    lateinit var customViewClickListener: CustomViewClickListener

    var watchList= mutableListOf<String>()

    var cartList= mutableListOf<String>()


    var watchNameList= mutableListOf<String>()


    var presentType = "employ"

    var employName= arrayListOf(
        "힙합을 즐겨듣는 고등학생",
        "10년째 공무원 준비하는 미대생",
        "게으른 4차원 디자이너",
        "대입 떨어진 재수생",
        "동네 컴퓨터 수리집 사장님",
        "하벌드 AI 박사 학위를 취득한 인마대 김병X 교수님",
        "네카라쿠베”스”의 최만석 CEO",
        "빌게이쯔(마이크로소프트콘 CEO)",
        "일론 마스크(테술라 CEO)",
        "은퇴한 70대 IT 대기업 개발자 할아버지",
        "애니와 애니노래를 좋아하는 백수",
        "하루 왠종일 게임만 하는 내 친구",
        "힙합에 푹빠진 사운드 디렉터",
        "문서작업은 누구보다 자신 있는 사무직의 달인",
        "사회생활을 잘 못하는 해킹 대회 우승자",
        "한국어를 잘못하는 유학파 출신 디자이너",
        "쌀국수를 즐겨먹는 디렉터",
        "포켓몬 로켓단의 프로그래머",
        "무직 백수"
        )
    var employType= arrayListOf(
        "사운드 디렉터",
        "디자이너",
        "디자이너",
        "개발자",
        "엔지니어",
        "교수",
        "CEO",
        "CEO",
        "CEO",
        "슈퍼 개발자",
        "사운드 디렉터",
        "개발자",
        "사운드 디렉터",
        "비서",
        "해커",
        "디자이너",
        "사운드 디렉터",
        "개발자",
        "무직 백수"
    )
    var cartNameList= arrayListOf(
        "기안 모닌",
        "기안 레위",
        "현도 아반테",
        "기안 K33",
        "BWW X33",
        "페리리 489GTC",
        "람머르기니 아빤타도르",
        "롤러로이스 KING"
    )

    var presentLevel= mutableListOf<String>()

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

        addCustomView("employ")

        DataBase.watchInit()
        DataBase.cartInit()
        addCustomView("empoly")

        initEvent(view)
        return view
    }
    lateinit var presentPrice :MutableList<Int>

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
        presentLevel=existEmploy[3]
//        네임 값
        var nameList = mutableListOf<String>()

        var employLevelCanUp= mutableListOf<Boolean>()
//        매개변수로 받은 type에 따라서 갈림길 중복코드 방지용
        if (type == "watch") {
            image = watchList
            presentType = "item"
            nameList = watchNameList
            presentPrice= watchPrice

        } else if (type == "cart") {
            image = cartList
            presentType = "item"
            nameList = cartNameList
            presentPrice= cartPrice

        } else {
            for (index in 1 until 20) {
                image.add("character${index}")
            }
            presentType = "employ"
            nameList = employName
            presentPrice= employPrice
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
        for (index in 0 until image.size) {


            var shopCustomView =layoutInflater.inflate(R.layout.shop_dialog_employ, linearLayout, false)
            var customViewButton =
                shopCustomView.findViewById<TextView>(R.id.custom_shop_btn)
            if(presentType=="employ") {
                 shopCustomView =
                    layoutInflater.inflate(R.layout.shop_dialog_employ, linearLayout, false)
                    customViewButton =
                    shopCustomView.findViewById<TextView>(R.id.custom_shop_btn)
            }
            else{
                 shopCustomView =
                    layoutInflater.inflate(R.layout.shop_custom_view, linearLayout, false)
                customViewButton =
                    shopCustomView.findViewById<TextView>(R.id.shop_custom_view_buy_button)
            }

            val customViewImage =
                shopCustomView.findViewById<ImageView>(R.id.inventory_custom_view_image_view)
            val customViewButtonText =
                shopCustomView.findViewById<TextView>(R.id.shop_custom_view_buy_button)

            val imageChange =
                resources.getIdentifier(image[index], "mipmap", activity?.packageName)
            customViewImage.setImageResource(imageChange)
            var purchaseCheck = false
            var employCheck = false
            customViewButton.text="구매"
            val customViewLevelPresentText =
                shopCustomView.findViewById<TextView>(R.id.shop_employ_level)
            if (presentType=="employ") {
                customViewLevelPresentText.text = "X"
            }
//            아까 넣어둔 이미지 인덱스 값과 비교해서 같은 것 나오면 구매완료 누르고
//            이미 구매했다는 변수 true로 만들기
            customViewButtonText.text = nameList[index]
            for (i in 0 until imageIndex.size) {
                if (index == imageIndex[i]) {
                    purchaseCheck = true
                    customViewButtonText.text="보유중"
                    if (presentType=="employ"){
//                        val customViewLevelText =
//                            shopCustomView.findViewById<TextView>(R.id.shop_custom_level_text)
//                            customViewLevelText.text="보유중 LEVEL"
                        customViewLevelPresentText.text= employLevel[index].toString()
                        customViewButtonText.text = nameList[index]
                        val customViewEmployText =
                            shopCustomView.findViewById<TextView>(R.id.employ_text)
                        customViewEmployText.text= "직원(보유중)"
                        customViewButton.text="레벨업"
                        if (presentLevel[i]==null){
                            presentLevel[i]="0"
                            employLevel[index]=presentLevel[i].toInt()
                            Log.d("구매",presentLevel[i])
                        }
                        else {
                            Log.d("구매","현재")
//                            구매가능 변수
                            employCheck=true
                            employLevel[index] = presentLevel[i].toInt()
                        }
                        customViewLevelPresentText.text= employLevel[index].toString()
                    }
                }
            }
            Log.d("test",index.toString())
            val customViewPriceText =
                shopCustomView.findViewById<TextView>(R.id.price_text)
            customViewPriceText.text=presentPrice[index].toString()

            customViewButton.setOnClickListener {
                if (myPersonalMoney >= presentPrice[index] && !purchaseCheck) {
                    customViewClickListener.purchaseSuccess(
                        customViewPriceText.text.toString(),
                        image[index],
                        presentType
                    ) // 값 전달
                    Log.d("구매여부", "구매성공")
                    myPersonalMoney -= presentPrice[index]
                    Log.d("type", nameList[index])
                    prefs.setString(
                        presentType,
                        image[index],
                        nameList[index],
                        employType[index],
                        employLevel[index].toString()
                    )
                    customViewButtonText.text = "구매완료"
                    purchaseCheck = true
                    Toast.makeText(context, "구매성공", Toast.LENGTH_SHORT).show()
                    customViewLevelPresentText.text= employLevel[index].toString()

                }
                else if (myPersonalMoney >= presentPrice[index] && employCheck) {
                    Log.d("구매", "else if")
                    Log.d("레벨 현재",presentLevel.toString())
                    Log.d("고용 레벨",employLevel.toString())
                    customViewClickListener.purchaseSuccess(
                        customViewPriceText.text.toString(),
                        image[index],
                        presentType
                    ) // 값 전달
                    Log.d("구매여부", "구매성공")
                    myPersonalMoney -= presentPrice[index]
                    Log.d("type", nameList[index])
                    prefs.setString(
                        presentType,
                        image[index],
                        nameList[index],
                        employType[index],
                        employLevel[index].toString()
                    )
                    customViewButtonText.text = "구매완료"
                    purchaseCheck = true
                    Toast.makeText(context, "구매성공", Toast.LENGTH_SHORT).show()
                    customViewLevelPresentText.text= employLevel[index].toString()
                }
                else {
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
//        employBuyBtn(view)
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
        context?.dialogFragmentResize(this,1f,0.9f) // 다이알로그 크기 조정
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

//    fun employBuyBtn(view:View){
//        val shopButton = view.findViewById<ImageView>(R.id.shop_btn)
//        shopButton.setOnClickListener {
//            val shopDialog = ShopDialogEmploy(personalMoney)
//            shopDialog.setDialogListener(object: ShopDialog.CustomViewClickListener{ // 인터페이스 상속받음
//                override fun purchaseSuccess(price: String, menuName:String ,type: String) { // price 라는 아이템의 가격값을 전달 받음
//                    personalMoney -= price.toInt() // 빼주고
//                    findViewById<TextView>(R.id.main_page_text_view_personal_money).text = "${personalMoney}원" //적용
//                    shopDialog.dismiss()
//
//                }
//            })
//        }
}
