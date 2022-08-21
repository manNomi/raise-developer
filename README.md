# raise-developer


###노션 - https://www.notion.so/a58e724d940f4ff8b96d9b789e62f64f


### 내가 사용한 기술들 

##코루틴 (coroutine)

- 코루틴은 동기화된 코드를 비동기화 처리해서 가벼운스레드로 돌리는것 
- 코루틴을 왜썼느냐? -> 데이터를 가져온 뒤에 값을 적용시켜줘야 하기 때문 
- 따라서 await() 함수를 사용한것을 볼 수 있다 

 ```kotlin
 CoroutineScope(Dispatchers.Main).launch {
                val data =db.collection("user")
                    .get()
            val dataFirst = async {
                data .addOnSuccessListener { result ->
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
                    }
                    .addOnFailureListener { exception ->
                        Log.w("TAG", "Error getting documents.", exception)
                    }
            }
            dataFirst.await()
            // to do()
```

- 코루틴 스코프 안에 Dispatchers가 있는데 .main .IO . Default 가 있다 
- 주로 main을 씀 
- Main은 말 그대로 메인 쓰레드에 대한 Context이며 UI 갱신이나 Toast 등의 View 작업에 사용됨
- IO는 네트워킹이나 내부 DB 접근 등 백그라운드에서 필요한 작업을 수행할 때 사용됨
- Default는 크기가 큰 리스트를 다루거나 필터링을 수행하는 등 무거운 연산이 필요한 작업에 사용됨

###코루틴 사용시 어려웠던 점 

- 코루틴 안에 그냥 넣고 함수 때려박으면 코루틴이 안먹는다 
- 코루틴을 사용할 부분을 확실히 나눠줘야만 코루틴이 먹기 때문에 꼬리 물기식 코딩을 하는 것을 지양해야한다
- 꼬리물기식 코딩을 하거나 아예 코드를 나누거나 둘 중하나로 하는 것이 중요했다.


##프리퍼런스 (Shared Preference)

- 쉐어드 프리퍼런스는 간단한 string 같은 문자열을 로컬에서 저장하는 것이다 
- (key , value) 형태로 저장하게 된다 
- 무겁지 않고 가벼워서 사용 -> 잦은 통신을 통한 데이터 저장 방식은 속도에 치명적이기 때문 

```kotlin
//  프리퍼런스 정의 
val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)
// 프리퍼런스 삭제 
        prefs.edit().clear().apply()
// 프리퍼런스 저장 
        prefs.edit().putString("inventory", gsonText).apply()
// 프리퍼런스 불러오기
        prefs.getString("item", "")

```

### 프리퍼런스 사용하면서 어려운 점 
- 프리퍼런스가 워낙 가볍고 잘돌아가서 딱히 문제는 없었음
- String으로 저장해야하기에 json 형식의 string으로 데이터들을 변환시켜주는 과정이 까다로웠다 
- 받은 문자열을 짜르는 과정이 있었기 때문 <- (key value의 존재를 인식하지 못한채 코딩해서 발견된 문제 key 값을 상세하게 짰다면 더 쉽게 짤수 있었다)



## 생명주기 service

- 왜 썼느냐 - > 안드로이드에서는 가운데 홈버튼을 누른후 삭제하는 방법을 사용하면 
-> onDestroy() 가 실행되지 않는다. <- 강제종료이기 때문 
- 따라서 이걸 해결해줄 방법이 필요한데 이때 쓰인것이 서비스다 
- 서비스는 프로그램이 종료되어도 사용 가능하므로 
- 프로그램이 꺼질때 서비스는 켜져있다 -> 이때 서비스는 강제종료된것을 확인후 -> stopSelf를 통해 앱을 종료시킨다 -> onDestroy가 실행되는 방식


```kotlin 
// MainActivity.kt 

// onCreate() 에서 해주면 됨 
startService(Intent(this, LifecycleService::class.java))


// LifecycleService.kt
class LifecycleService : Service() {
    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent) { //핸들링 하는 부분
        Log.e("Error", "onTaskRemoved - 강제 종료 $rootIntent")
        Toast.makeText(this, "onTaskRemoved ", Toast.LENGTH_SHORT).show()
        stopSelf() //서비스 종료
    }
}
```

