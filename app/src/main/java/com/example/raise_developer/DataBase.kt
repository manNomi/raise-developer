package com.example.raise_developer

object DataBase {
    var watchList= mutableListOf<String>()
    var cartList= mutableListOf<String>()
    var watchNameList= mutableListOf<String>()
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
        "조성민",
        "한국어를 잘못하는 유학파 출신 디자이너",
        "쌀국수를 즐겨먹는 디렉터",
        "포켓몬 로켓단의 프로그래머",
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
        2000,
        2000,
        2000,
        2000,
        2000,
        2000,
        2000,
        2000,
        2000,
        2000,
        2000,
       2000,
        2000,
        2000,
        2000,
        2000,
        2000,
        2000,
        2000
    )

    var cartPrice= arrayListOf(
        2000,
        2000,
        2000,
        2000,
        2000,
        2000,
        2000,
        2000
    )

    var watchPrice= arrayListOf(
        2000,
        2000,
        2000,
        2000,
        2000,
        2000,
        2000,
        2000,
        2000,
        2000,

        2000,
        2000,
        2000,
        2000,
        2000,
        2000,
        2000,
        2000,
        2000,
        2000,

        2000,
        2000
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
        0,0,0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,0
    )
    //레벨업을 위한 조건 오브젝트
    data class LevelUpConditionObj(
        val name: String,
        val image: String,
        val level: Int
    )
    //레벨업 조건 묶음
    val levelupCondition = arrayListOf(
        arrayListOf(LevelUpConditionObj(employName[0],"character1",0),LevelUpConditionObj(employName[1],"character2",0),LevelUpConditionObj(employName[2],"character3",0)),
        arrayListOf(LevelUpConditionObj(employName[2],"character4",0),LevelUpConditionObj(employName[3],"character4",0),LevelUpConditionObj(employName[4],"character5",0)),
        arrayListOf(LevelUpConditionObj(employName[4],"character5",0),LevelUpConditionObj(employName[5],"character6",0),LevelUpConditionObj(employName[6],"character7",0)),
        arrayListOf(LevelUpConditionObj(employName[6],"character7",0),LevelUpConditionObj(employName[7],"character8",0),LevelUpConditionObj(employName[8],"character9",0)),
        arrayListOf(LevelUpConditionObj(employName[8],"character9",0),LevelUpConditionObj(employName[9],"character10",0),LevelUpConditionObj(employName[10],"character11",0)),
        arrayListOf(LevelUpConditionObj(employName[10],"character11",0),LevelUpConditionObj(employName[11],"character12",0),LevelUpConditionObj(employName[12],"character13",0)),
        arrayListOf(LevelUpConditionObj(employName[11],"character12",0),LevelUpConditionObj(employName[12],"character13",0),LevelUpConditionObj(employName[13],"character14",0)),
        arrayListOf(LevelUpConditionObj(employName[13],"character14",0),LevelUpConditionObj(employName[14],"character15",0),LevelUpConditionObj(employName[15],"character16",0)),
        arrayListOf(LevelUpConditionObj(employName[14],"character15",0),LevelUpConditionObj(employName[15],"character16",0),LevelUpConditionObj(employName[16],"character17",0)),
        arrayListOf(LevelUpConditionObj(employName[16],"character17",0),LevelUpConditionObj(employName[17],"character18",0),LevelUpConditionObj(employName[18],"character19",0)),
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
            watchNameList.add("보통시계 ${index+1}")
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