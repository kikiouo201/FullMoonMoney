package com.example.fullmoonmoney.data.room

import android.text.TextUtils
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.reflect.Type

class RoomConverter {
    @TypeConverter
    fun mapToJson(params: Map<String?, String?>?): String? {
        return JSONObject(params).toString()
    }

    @TypeConverter
    fun jsonToMap(json: String?): Map<String?, String?>? {
        if (TextUtils.isEmpty(json)) {
            return emptyMap<String?, String>()
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Map<String?, String?>?>() {}.type
        return gson.fromJson(json, type)
    }
}