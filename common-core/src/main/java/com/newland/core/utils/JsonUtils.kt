package com.newland.mvvmproject.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * @author: leellun
 * @data: 16/6/2021.
 *
 */
object JsonUtils {
    fun objectToJson(data: Any?): String? {
        val gson = Gson()
        return gson.toJson(data)
    }

    fun <T> jsonToObject(str: String?, type: Type?): T {
        val gson = Gson()
        return gson.fromJson(str, type)
    }

    fun <T> jsonToList(str: String?): List<T>? {
        val gson = Gson()
        return gson.fromJson(str, object : TypeToken<List<T>?>() {}.type)
    }
}