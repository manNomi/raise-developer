package com.example.raise_developer
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

object FireStore {
    val db = FirebaseFirestore.getInstance()
    var jsonData = ""
    var level = ""
    var presentMoney = ""
    var tutorialCehck = false

    fun readData(userId: String, prefs: SharedPreferences) {
        CoroutineScope(Dispatchers.Main).launch {
            var data_2 = FireStore.db.collection("user").document(userId).get()// 작업할 컬렉션
            val dataFirst_2 = async {
                data_2 = db.collection("user").document(userId)   // 작업할 컬렉션
                    .get()
            }
            dataFirst_2.await()
            val dataSecond_2 = async {
                data_2.addOnSuccessListener { result ->
                    Log.d("리드", "성공")
                    Log.d("리드데이터셋", result.data.toString())
                    var dataSet = result.data.toString().split("uID=")[1].split(",")
                    var presentId = dataSet[0]
                    Log.d("qwe", dataSet.toString())
                    Log.d("qwe", dataSet[2])
                    var jsonDataSet = result.data.toString().split("uID=")[1].split("jsonString=")
                    Log.d("qwe", jsonDataSet[1])
                    var jsonChanger = jsonDataSet[1].split("}]}")
                    Log.d("1", jsonChanger[0])
                    if (presentId == userId) {
                        if (jsonChanger[0] != "}") {
                            jsonData =
                                jsonChanger[0] + "}]}"
                        }
                        level = dataSet[2].replace("\\s".toRegex(), "").split("=")[1]
                        presentMoney = dataSet[1].replace("\\s".toRegex(), "").split("=")[1]
                    }

                    prefs.edit().putString("inventory", jsonData).apply()
                    prefs.edit().putString("money", presentMoney).apply()
                    prefs.edit().putString("level", level).apply()
                    val user = hashMapOf(
                        "money" to presentMoney,
                        "level" to level,
                        "jsonString" to jsonData
                    )
                    db.collection("user").document(userId).update(user as Map<String, Any>)
                    Log.d("코루틴 테스트", "5")

                }
                    .addOnFailureListener { exception ->
                        // 실패할 경우
                        Log.d("리드", "Error getting documents: $exception")
                    }
            }
        }
        }
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

        fun updateData(userId: String, level: String, jsonString: String, money: String) {
            val user = hashMapOf(
                "money" to money,
                "level" to level,
                "jsonString" to jsonString
            )
            db.collection("user").document(userId).update(user as Map<String, Any>)
        }

        fun checkData(userId: String, level: String, prefs: SharedPreferences) {
            Log.d("체크데이터", "실행")
            CoroutineScope(Dispatchers.Main).launch {
                var data = db.collection("user").document(userId).get()// 작업할 컬렉션
                val data1 = async {
                    data = db.collection("user").document(userId)   // 작업할 컬렉션
                        .get()   // 문서 가져오기
                }
                data1.await()
                val data2 = async {
                    data.addOnSuccessListener { result ->
                        Log.d("테스트데이터", result.data.toString())
                        if (result.data == null) {
                            val jsonString = ""
                            val level = "1"
                            setData(userId, level, jsonString, "0")
                            Log.d("체크데이터", "새로만듬")
                            prefs.edit().clear().apply()
                        } else {
                            readData(userId, prefs)
                            Log.d("체크데이터", "수정함")
                        }
                    }
                }
                data2.await()
                    .addOnFailureListener { exception ->
                        // 실패할 경우
                        Log.d("리드", "Error getting documents: $exception")
                    }
            }
        }

        fun checkUpdate(
            userId: String,
            level: String,
            jsonString: String,
            prefs: SharedPreferences,
            money: String
        ) {
            db.collection("user").document(userId)   // 작업할 컬렉션
                .get()      // 문서 가져오기
                .addOnSuccessListener { result ->
                    Log.d("테스트데이터", result.data.toString())
                    if (result.data == null) {
                        val jsonString = ""
                        val level = "1"
                        setData(userId, level, jsonString, money)
                        Log.d("체크데이터", "새로만듬")
                        prefs.edit().clear().apply()
                    } else {
                        updateData(userId, level, jsonString, money)
                        Log.d("체크데이터업데이트", "수정함")
                    }
                }
                .addOnFailureListener { exception ->
                    // 실패할 경우
                    Log.d("리드", "Error getting documents: $exception")
                }
        }

        fun sendjsonString(userID: String, level: String, money: String, prefs: SharedPreferences) {
            val jsonString = prefs.getString("inventory", "").toString()
            Log.d("센드제이슨", jsonString)
            checkUpdate(userID, level, jsonString, prefs, money)
        }
}
