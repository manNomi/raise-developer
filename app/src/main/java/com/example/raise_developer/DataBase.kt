package com.example.raise_developer

object DataBase {
    var watchList= mutableListOf<String>()
    var cartList= mutableListOf<String>()
    var watchNameList= mutableListOf<String>()
    var employName= arrayListOf(
        "힙합에 푹 빠진 사운드 디렉터",
        "힙합을 즐겨듣는 고등학생",
        "애니와 애니 노래를 좋아하는 백수",
        "10년째 공무원 준비하는 미대생",
        "하루 온종일 게임만 하는 내 친구",
        "서물대 아깝게 떨어진 재수생",
        "게으른 4차원 디자이너",
        "동네 컴퓨터 수리 집 사장님",
        "쌀국수를 즐겨먹는 디렉터",
        "은퇴한 70대 IT 대기업 개발자 할아버지",
        "문서작업은 누구보다 자신 있는 사무직의 달인",
        "사회생활을 잘 못하는 해킹 대회 우승자",
        "국정원에서 짤린 아저씨",
        "한국어를 잘 못하는 유학파 출신 디자이너",
        "포켓몬 로켓단의 프로그래머",
        "하벌드 AI 박사 학위를 취득한 인마대 김병X 교수님",
        "네카라쿠베”스”의 최만석 CEO",
        "빌게이쯔(마이크로소프트콘 CEO)",
        "일론 마스크(테술라 CEO)",
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
        "개발자",
        "디자이너",
        "사운드 디렉터",
        "개발자"
    )

    var employLevel= arrayListOf(
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0
    )

    var employPrice= arrayListOf(
        0,
        5000,
        12000,
        30000,
        50000,
        90000,
        150000,
        220000,
        380000,
        570000,
        990000,
        2000000,
        3400000,
        6500000,
        10000000,
        31000000,
        52000000,
        80000000,
        100000000
    )

    var cartPrice= arrayListOf(
        100000,
        380000,
        1200000,
        3300000,
        7700000,
        19200000,
        43000000,
        120000000
    )

    var watchPrice= arrayListOf(
        3000,
        5500,
        8100,
        11000,
        13600,
        19900,
        150000,
        190000,
        270000,
        430000,
        620000,
        800000,
        3000000,
        3800000,
        4600000,
        5900000,
        7250000,
        8500000,
        9900000,
        30000000,
        45000000,
        60000000
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
    // 레벨업을 하는 데 필요한 가격 10개 단위로 끊었음
    val levelUpPrice = arrayListOf(
        100,500,900,1600,2700,4000,5500,7000,9000,10000,
        8000,9700,12000,15300,17600,19300,22000,24800,28100,30000,
        33000,39900,51100,60000,71300,84500,98100,106000,128000,145000,
        100000,146000,172000,214000,256000,282000,316000,391000,427000,450000,
        350000,392000,493000,632000,772000,832000,898000,999000,1143000,1300000,
        1020000,1165000,1483000,1831000,2219000,2650000,3132000,3540000,4270000,5000000,
        3500000,3821000,4236000,4770000,5110000,5621000,6260000,6823000,746000,10000000,
        10000000,11500000,13800000,16200000,19900000,23800000,25100000,26400000,28000000,30000000,
        25000000,29400000,32600000,36900000,40700000,43100000,45800000,48200000,51600000,55000000,
        60000000,65000000,70000000,75000000,80000000,85000000,90000000,95000000,100000000,130000000
    )
    //레벨업을 위한 조건 오브젝트
    data class LevelUpConditionObj(
        val name: String,
        val image: String,
        val level: Int
    )
    //레벨업 조건 묶음
    val levelupCondition = arrayListOf(
        arrayListOf(LevelUpConditionObj(employName[0],"character1",10),LevelUpConditionObj(employName[1],"character2",10),LevelUpConditionObj(employName[2],"character3",1)),
        arrayListOf(LevelUpConditionObj(employName[2],"character4",10),LevelUpConditionObj(employName[3],"character4",10),LevelUpConditionObj(employName[4],"character5",1)),
        arrayListOf(LevelUpConditionObj(employName[4],"character5",10),LevelUpConditionObj(employName[5],"character6",10),LevelUpConditionObj(employName[6],"character7",3)),
        arrayListOf(LevelUpConditionObj(employName[6],"character7",10),LevelUpConditionObj(employName[7],"character8",10),LevelUpConditionObj(employName[8],"character9",3)),
        arrayListOf(LevelUpConditionObj(employName[8],"character9",10),LevelUpConditionObj(employName[9],"character10",10),LevelUpConditionObj(employName[10],"character11",5)),
        arrayListOf(LevelUpConditionObj(employName[10],"character11",10),LevelUpConditionObj(employName[11],"character12",5),LevelUpConditionObj(employName[12],"character13",1)),
        arrayListOf(LevelUpConditionObj(employName[11],"character12",10),LevelUpConditionObj(employName[12],"character13",10),LevelUpConditionObj(employName[13],"character14",1)),
        arrayListOf(LevelUpConditionObj(employName[13],"character14",10),LevelUpConditionObj(employName[14],"character15",5),LevelUpConditionObj(employName[15],"character16",1)),
        arrayListOf(LevelUpConditionObj(employName[14],"character15",10),LevelUpConditionObj(employName[15],"character16",10),LevelUpConditionObj(employName[16],"character17",5)),
        arrayListOf(LevelUpConditionObj(employName[16],"character17",10),LevelUpConditionObj(employName[17],"character18",10),LevelUpConditionObj(employName[18],"character19",10)),
    )
    fun watchInit(){
        watchList.clear()
        for (index in 0 until 6) {
            watchList.add("apple_watch_${index+1}")
            watchNameList.add("사과시계 ${index+1}")
        }
        for (index in 0 until 6) {
            watchList.add("gx_watch_${index+1}")
            watchNameList.add("은하시계 ${index+1}")
        }
        for (index in 0 until 7) {
            watchList.add("watch${index+1}")
            watchNameList.add("간쥐시계 ${index+1}")
        }
        for (index in 0 until 3) {
            watchList.add("rolex${index+1}")
            watchNameList.add("로렉스 ${index+1}")
        }
    }
    fun cartInit() {
        cartList.clear()
        for (index in 0 until 8) {
            cartList.add("cart${index + 1}")
        }
    }
}