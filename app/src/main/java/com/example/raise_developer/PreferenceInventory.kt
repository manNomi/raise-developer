package com.example.raise_developer

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.raise_developer.FireStore.checkUpdate
import com.google.gson.Gson

class PreferenceInventory(context: Context) {

    var inventory = mutableListOf<String>()
    var inventoryName = mutableListOf<String>()
    var employ = mutableListOf<String>()
    var employName = mutableListOf<String>()
    var employType = mutableListOf<String>()
    var employLevel = mutableListOf<String>()

    val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    data class EmployData(
        val image : String,
        val name : String,
        val type : String,
        val level : String

    )
    data class InventoryData(
        val image : String,
        val name : String,
    )

    data class InventoryCategory(
        val inventory_list : ArrayList<InventoryData>,
        val employ_list : ArrayList<EmployData>
    )
//    프리퍼런스에 Json String 으로 만들어서 저장 하기
//    매개변수로 키값 받고 키값에 따라 저장 형태가 달라짐
//    json 데이터는 수정이 불가하므로 그냥 프리퍼런스 clear 후 다시 만드는 방식

    //    온크리에이트 해줄때
    fun resetJsonString(jsonString:String){
        prefs.edit().clear().apply()
        prefs.edit().putString("inventory", jsonString).apply()
    }

    fun resetMoneyString(money:String){
        prefs.edit().clear().apply()
        prefs.edit().putString("money", money).apply()
    }

    //    꺼질때
    fun sendjsonString(userID:String,level:String,money:String){
        val jsonString=prefs.getString("inventory", "").toString()
        Log.d("센드제이슨",jsonString)
         checkUpdate(userID,level,jsonString,prefs,money)
    }

    fun clearString()
    {
        prefs.edit().clear().apply()
    }

    fun setString(key: String, str: String , name :String , type:String , level:String) {
        prefs.edit().clear().apply()
        Log.d("type",name)
        if(key=="item"){
            inventory.add(str)
            inventoryName.add(name)
            Log.d("type",inventoryName.toString())
        }
        else{
            var check= "비존재"
            for (index in 0 until employ.size) {
                if (employ[index]==str){
                    employLevel[index]=(level.toInt()+1).toString()
                    check="이미존재"
                    break
                }
            }
            if(check== "비존재") {
                employ.add(str)
                employName.add(name)
                employType.add(type)
                employLevel.add(level)
            }
        }
        var gsonText = "{"
        gsonText+="'inventory_list':["
        for (index in 0 until inventory.size) {
            if (index != 0) {
                gsonText += ","
            }
            gsonText += "{"
            gsonText += "'image': '${inventory[index]}',"
            gsonText += "'name': '${inventoryName[index]}' }"
        }
        gsonText += "],"

        gsonText+="'employ_list':["
        for (index in 0 until employ.size) {
            if (index != 0) {
                gsonText += ","
            }
            gsonText += "{"
            gsonText += "'image': '${employ[index]}' ,"
            gsonText += "'type': '${employType[index]}' ,"
            gsonText += "'name': '${employName[index]}' ,"
            gsonText += "'level': '${employLevel[index]}' }"
        }
        gsonText += "]}"

        Log.d("gson", gsonText)
        prefs.edit().putString("inventory", gsonText).apply()
    }

    //    프리퍼런스에 저장된 json String 가져와서 해석 하기
//    이때 매개변수로 key를 받으며 key가 아이템이면 아이템 반환 , 그외의 것이면 고용 반환
    fun getString(key: String, defValue: String): ArrayList<MutableList<String>> {
        val jsonText = prefs.getString("inventory", defValue).toString()
        Log.d("gson", jsonText)
        if (jsonText != "") {
            Log.d("json", jsonText)
            val data = Gson().fromJson(jsonText, InventoryCategory::class.java)
            inventory.clear()
            inventoryName.clear()
            if (data.inventory_list!=null) {
                for (index in 0 until data.inventory_list.size) {
                    inventory.add(data.inventory_list[index].image)
                    inventoryName.add(data.inventory_list[index].name)
                }
            }
            employ.clear()
            employName.clear()
            employType.clear()
            employLevel.clear()
            if (data.employ_list!=null) {
                for (index in 0 until data.employ_list.size) {
                    employ.add(data.employ_list[index].image)
                    employName.add(data.employ_list[index].name)
                    employType.add(data.employ_list[index].type)
                    employLevel.add(data.employ_list[index].level)
                }
            }
        }
        if (key=="item"){
            return arrayListOf(inventory,inventoryName)
        }
        else{
            return arrayListOf(employ,employName,employType,employLevel)
        }
    }
}