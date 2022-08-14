package com.example.raise_developer

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object FierStore {
    val db = FirebaseFirestore.getInstance()
    var jsonData=""
    var level=""
    fun readData(userId:String){
            db.collection("user").document(userId)   // 작업할 컬렉션
                .get()      // 문서 가져오기
                .addOnSuccessListener { result ->
                    Log.d("리드", "성공")
                    Log.d("리드테스트",
                        result.data.toString().split("uID=")[1].split(",").toString())
                    var dataSet = result.data.toString().split("uID=")[1].split(",")
                    var presentId = dataSet[0]
                    if (presentId == userId) {
                        jsonData =
                            dataSet[2].replace("\\s".toRegex(), "").split("=")[1].split("}")[0]
                        level = dataSet[1].replace("\\s".toRegex(), "").split("=")[1]
                    }
                    Log.d("id", userId)
                    Log.d("data", jsonData)
                    Log.d("level", level)
                    }
                        .addOnFailureListener { exception ->
                            // 실패할 경우
                            Log.d("리드", "Error getting documents: $exception")
                        }
//        val data =db.collection("user").document(userId).get()
//        Log.d("test",data)
    }
    fun setData(userId:String,level:String,jsonString: String){
        val user = hashMapOf(
            "uID" to userId,
            "level" to level,
            "jsonString" to jsonString
        )
//        db.collection("user")
//            .add(user)
//            .addOnSuccessListener {documentReference->
//                // 성공할 경우
//                Log.d("셋","성공")
//                Log.d("셋", "DocumentSnapshot added with ID: ${documentReference.id}")
//            }
//            .addOnFailureListener { exception ->
//                // 실패할 경우
//                Log.d("셋", "Error getting documents: $exception")
//            }
        db.collection("user").document(userId).set(
            user
        )


    }

    fun updateData(userId:String,level:String,jsonString: String){
        val user = hashMapOf(
            "uID" to userId,
            "level" to level,
            "jsonString" to jsonString
        )
        db.collection("user").document(userId).update(user as Map<String, Any>)
    }

    fun checkData(userId:String,level:String,jsonString: String,prefs:SharedPreferences){
        db.collection("user").document(userId)   // 작업할 컬렉션
            .get()      // 문서 가져오기
            .addOnSuccessListener { result ->
                    Log.d("테스트데이터",result.data.toString())

                if(result.data==null) {
                    setData(userId, level, jsonString)
                    Log.d("체크데이터", "새로만듬")
                    prefs.edit().clear().apply()
                    prefs.edit().putString("inventory", jsonString).apply()
                }
                else{
                    updateData(userId,level,jsonString)
                    Log.d("체크데이터","수정함")
                }
            }
            .addOnFailureListener { exception ->
                // 실패할 경우
                Log.d("리드", "Error getting documents: $exception")
            }
    }
}