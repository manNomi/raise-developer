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
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class QuizDialog : DialogFragment() {
    // 퀴즈 목록 (0번 값이 문제, 1번 값이 답)
    val quizList = arrayListOf(
        arrayListOf("명확한 문제를 해결하기 위해 설계된 일련의 명령 또는 규칙","Algorithm"),
        arrayListOf("하드웨어 또는 소프트웨어가 예기치 않은 오류 또는 결함으로 인해 오동작하는 것","Bug"),
        arrayListOf("컴퓨터의 중앙 처리장치","CPU"),
        arrayListOf("소프트웨어 애플리케이션을 구축하기 위한 규칙, 루틴 및 프로토콜 세트","API"),
        arrayListOf("에러가 일어날 가능성을 개발자가 미리 예상하고 프로그램이 비정상적으로 종료되지 않도록 하는 것","Exception"),
        arrayListOf("서버, 시스템, 네트워크 등에서 이상이 생겼을 때 예비 시스템으로 자동 전환되는 것","Failover"),
        arrayListOf("타당성 확인, 검사, 분석의 방법이나 작업 경로 등이 적절한가 확인하는 모든 것","Validation"),
        arrayListOf("프론트엔드 개발과 백엔드 개발 영역을 통틀어 이르는 말, 혹은 모두 다룰 수 있는 개발자","Full-Stack"),
        arrayListOf("프로그램을 개발하기 위한 구조를 제공하는 개발 환경","Framework"),
        arrayListOf("프로그램을 개발하는 데 필요한 여러 기능을 활용할 수 있도록 묶어놓은 함수 또는 기능의 집합","Library"),
        arrayListOf("어떤 특정한 기능을 해결하는 데 쓸 수 있도록 미리 만들어 놓은 코드 및 데이터의 모음","Plug-in"),
        arrayListOf("유저가 입력한 내용이 잘못됐을 때 발생하는 문제","Error"),
        arrayListOf("서버를 통해 전송되는 데이터의 양","Traffic"),
        arrayListOf("누구나 제한 없이 쓸 수 있는 소스 코드 혹은 소프트웨어","Open Source"),
        arrayListOf("소프트웨어나 시스템을 만드는 데 쓰이는 개발 도구 키트, 개발에 필요한 샘플 코드, 코드 편집기 같은 툴이나 콘솔, 안내 문서, API 등이 포함됨","SDK"),
        arrayListOf("프로그램을 개발하는 데 필요한 소스 코드 작성 및 편집, 컴파일, 디버깅 등 모든 작업을 한번에 할 수 있는 통합 개발 환경","IDE"),
        arrayListOf("기계어에 가까운 언어인 어셈블리어로 개발한 컴퓨터 운영 체제","Unix"),
        arrayListOf("유닉스 계열에 속하는 오픈소스 운영체제","Linux"),
        arrayListOf("프로그램을 실행하는 하나의 프로세스 내에서 실제로 작업을 처리하는 주체","Thread"),
        arrayListOf("하나의 프로세스가 두 개 이상의 Thread를 가지는 경우","Multi Thread"),
        arrayListOf("문자로 지정된 인터넷 주소","Domain"),
    )

    var quizNum = 0 //선택된 문제 세트
    var answerNum = 0 //답의 번호
    val wrongAnswerNum = mutableListOf<Int>() //오답으로 쓰일 보기 0번부터 2번 인덱스까지 순서대로 안채워진 객관식의 보기를 채울 예정

    val btnList = mutableListOf<Button>() // 퀴즈의 보기

    fun randomQuizNumSet() {  //퀴즈에 필요한 인덱스 값 셋팅
        quizNum = (0..(quizList.size-1)).random()
        answerNum = (1..4).random()
        while(wrongAnswerNum.size != 3) {
            val randomNum = (0..(quizList.size-1)).random()
            if(randomNum != quizNum) {wrongAnswerNum.add(randomNum)}
        }
    }

    fun btnSet(view: View) { // 버튼을 인덱스로 접근하기 위해 넣는 과정
        val button1 = view.findViewById<Button>(R.id.btn_choice_1)
        val button2 = view.findViewById<Button>(R.id.btn_choice_2)
        val button3 = view.findViewById<Button>(R.id.btn_choice_3)
        val button4 = view.findViewById<Button>(R.id.btn_choice_4)
        btnList.add(button1)
        btnList.add(button2)
        btnList.add(button3)
        btnList.add(button4)
    }

    fun quizTextSet(view: View) {
        val textProblem = view.findViewById<TextView>(R.id.text_problem)
        textProblem.setText(quizList[quizNum][0])
        var wrongAnswerCount = 0
        for(index in 0 until 4) {
            if(index == answerNum) {
                btnList[index].text = quizList[quizNum][1]
            }else {
                btnList[index].text = quizList[wrongAnswerNum[wrongAnswerCount]][1]
                wrongAnswerCount += 1
            }
        }
    }

    fun btnEventResultChoice() {
        for(index in 0 until 4) {
            if(index == answerNum) {
                btnList[index].setOnClickListener {
                    Log.d("result","correct")
                }
            }else {
                btnList[index].setOnClickListener {
                    Log.d("result","incorrect")
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.quiz_dialog_problem,container,false)
        isCancelable = false

        initSet(view)
        initEvent(view)

        return view
    }

    fun initEvent(view: View){
        btnEventResultChoice()
        closeButtonEvent(view)
    }
    fun initSet(view: View) {
        randomQuizNumSet()
        btnSet(view)
        quizTextSet(view)
    }

    fun closeButtonEvent(view: View){
        val closeButton = view.findViewById<ImageButton>(R.id.btn_close)
        closeButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this,0.8f,0.8f) // 다이알로그 크기 조정
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