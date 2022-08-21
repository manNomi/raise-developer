# raise-developer


### 노션 - https://www.notion.so/a58e724d940f4ff8b96d9b789e62f64f


### 내가 사용한 기술들 

## 코루틴 (coroutine)

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

### 코루틴 사용시 어려웠던 점 

- 코루틴 안에 그냥 넣고 함수 때려박으면 코루틴이 안먹는다 
- 코루틴을 사용할 부분을 확실히 나눠줘야만 코루틴이 먹기 때문에 꼬리 물기식 코딩을 하는 것을 지양해야한다
- 꼬리물기식 코딩을 하거나 아예 코드를 나누거나 둘 중하나로 하는 것이 중요했다.


## 프리퍼런스 (Shared Preference)

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

## 파이어베이스 

- raise developer 파이어베이스 

https://console.firebase.google.com/u/0/project/raisedeveloper-4614f/overview?hl=ko 

- 파이어 베이스를 왜 쓰는가? 
- > 데이터를 저장할 공간이 필요 
- 프리퍼런스로 저장하면 되지 않는가?
- > 온라인 게임 + 로그인을 통해 다른계정에 로그인 할 수 있는 게임이므로 원격에 데이터를 저장해서 불러오는 방식이 옳다

- 파이어베이스를 처음 접할때 두가지 데이터베이스중 많은 고민을 했다 
- realtime DB와 fireStore 였는데 설명상 realtime db는 모바일 앱을 위한 / fireStore는 그 외를 위한 것이어서 
- realtime 쪽을 시도했었다. 허나 리얼타임 DB는 실시간으로 반영되는 즉 채팅기능과 같은 부분에 쓰이면 좋은 기능이었다.
- 따라서 fireStore로 정했다. 

```kotlin 
// 파이어베이스 인스턴스 불러오기 
    val db = FirebaseFirestore.getInstance()
    
// 데이터 저장 
fun setData(userId: String, level: String, jsonString: String, money: String) {
            val user = hashMapOf(
                "uID" to userId,
                "money" to money,
                "level" to level,
                "jsonString" to jsonString
            )
            db.collection("user").document(userId).set(
                user
            )
            tutorialCehck = true
        }
// 데이터 업데이트 
fun updateData(userId: String, level: String, jsonString: String, money: String) {
            val user = hashMapOf(
                "money" to money,
                "level" to level,
                "jsonString" to jsonString
            )
            db.collection("user").document(userId).update(user as Map<String, Any>)
        }
        
// 작업할 컬렉션 불러오기
var dataCollection = FireStore.db.collection("user").document(userId).get()// 작업할 컬렉션

// 도큐먼트로 데이터 불러오기

dataCollection.addOnSuccessListener { result ->
                    var dataSet = result.data.toString()
                    }
                .addOnFailureListener { exception ->
                    // 실패할 경우
                    Log.d("리드", "Error getting documents: $exception")
                }
```
- 파이어베이스는 무조건 비동기화 시켜줘야함 (코루틴 사용해야함)
- 불러오고 가져오는 방법은 다양 
- 불러오는 type이 string이 아니라서 toString() 함수 필요 
https://cloud.google.com/firestore/docs/samples/firestore-data-set-field?hl=ko <- 파이어스토어 참고 

### 파이어스토어 사용하면서 어려운 점 

- 비동기화 시켜주는것이 가장 어려웠음 
- 꼬리에 꼬리를 무는 코딩을 했기에 코루틴이 작동하지 않아 다 쪼갰음 
- 또한 파이어스토어를 나의 프로젝트를 따로파서 연결해주었는데 
- 이미 연결이 되어있는지 팀원들이 만들어 놓은 프로젝트로 데이터가 계속 가서 이 때문에 고생을 했음 
- 또한 스트링으로 저장하기 때문에 데이터를 잘라줘야 했음 
- 스트링으로 온것들을 split과 toList를 통해 잘라주고 다시 붙히는 과정을 반복하면서 원하는 데이터를 끌고오기 위해 고생함 

#### 다음과 같은 방식으로 저장됨 

<img width="1097" alt="firebase image" src="https://user-images.githubusercontent.com/98882987/185796173-40d0bf4b-aebd-4172-ada9-6b866d2f63d2.png">

## 랭킹 페이지 정렬 함수 

``` kotlin
fun selectionSort(data: MutableList<Array<String>>):MutableList<Array<String>> {
        var v =data
        for (i in 0 until v.size - 1) {
            var tmp = i
            for (j in i + 1 until v.size) {
                if (v[tmp][1].toInt() <= v[j][1].toInt()) tmp = j
            }
            var x=v[i]
            var y =v[tmp]
            v[i]=y
            v[tmp]=x
        }
        return v
    }
```
- 2차원 배열에서 인데스 1번에 있는 숫자들을 비교해서 크면 앞으로 보내주는 형식의 선택정렬 알고리즘 채용 
- c++ 코드를 참고했는데 c++ 코드를 입력해도 알아서 kotlin 언어로 바꿔준다 (kotlin이 진짜 사기인 이유)


![244575335708D49925](https://user-images.githubusercontent.com/98882987/185795964-3fdb010e-7c07-46d0-83c6-b1b69915a7d3.gif)



