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
        "한국어를 잘못하는 유학파 출신 디자이너",
        "쌀국수를 즐겨먹는 디렉터",
        "포켓몬 로켓단의 프로그래머",
        "조성민"
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
        "개발자"
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

    val levelUp1to2Name= arrayListOf(
        "대입 떨어진 재수생",
        "동네 컴퓨터 수리집 사장님",
        "하루 왠종일 게임만 하는 내 친구",
        )

    val levelUp1to2Image= arrayListOf(
        "character4",
        "character5",
        "character12",
    )

    val levelUp2to3Name= arrayListOf(
        "힙합을 즐겨듣는 고등학생",
        "10년째 공무원 준비하는 미대생",
        "게으른 4차원 디자이너",
        )

    val levelUp2to3Image= arrayListOf(
        "character1",
        "character2",
        "character3",
    )

    val levelUp3to4Image= arrayListOf(
        "character11",
        "character17",
    )
    val levelUp3to4Name= arrayListOf(
        "애니와 애니노래를 좋아하는 백수",
        "쌀국수를 즐겨먹는 디렉터",
    )

    val levelUp4to5Image= arrayListOf(
        "character13",
        "character15",
        "character17",
    )
    val levelUp4to5Name= arrayListOf(
        "힙합에 푹빠진 사운드 디렉터",
        "사회생활을 잘 못하는 해킹 대회 우승자",
        "포켓몬 로켓단의 프로그래머",
    )

    val levelUp5to6Image= arrayListOf(
        "character14",
        "character16",
    )
    val levelUp5to6Name= arrayListOf(
        "문서작업은 누구보다 자신 있는 사무직의 달인",
        "한국어를 잘못하는 유학파 출신 디자이너",
    )

    val levelUp6to7Image= arrayListOf(
        "character6",
        "character7",
        "character10",
        )

    val levelUp6to7Name= arrayListOf(
        "하벌드 AI 박사 학위를 취득한 인마대 김병X 교수님",
        "네카라쿠베”스”의 최만석 CEO",
        "은퇴한 70대 IT 대기업 개발자 할아버지",
    )

    val levelUp7to8Image= arrayListOf(
        "character8",
        "character9",
    )
    val levelUp7to8Name= arrayListOf(
        "빌게이쯔(마이크로소프트콘 CEO)",
        "일론 마스크(테술라 CEO)"
    )

    fun watchInit(){
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
        for (index in 0 until 8) {
            cartList.add("cart${index + 1}")
        }
    }
}