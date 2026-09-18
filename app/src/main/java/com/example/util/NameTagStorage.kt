package com.example.util

import android.content.Context
import com.example.data.model.SavedNameTag
import org.json.JSONArray
import org.json.JSONObject

object NameTagStorage {
    private const val PREFS_NAME = "hotel_studio_name_tags"
    private const val KEY_SAVED_TAGS = "saved_desk_tents"

    fun getSavedTags(context: Context): List<SavedNameTag> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonStr = prefs.getString(KEY_SAVED_TAGS, null) ?: return emptyList()
        val list = mutableListOf<SavedNameTag>()
        try {
            val jsonArray = JSONArray(jsonStr)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    SavedNameTag(
                        id = obj.optString("id", java.util.UUID.randomUUID().toString()),
                        name = obj.optString("name", ""),
                        designation = obj.optString("designation", ""),
                        salutation = obj.optString("salutation", "Mr."),
                        templateIndex = obj.optInt("templateIndex", 0),
                        pageSize = obj.optString("pageSize", "A5"),
                        fontScale = obj.optDouble("fontScale", 1.0).toFloat(),
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis())
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun saveTag(context: Context, tag: SavedNameTag): List<SavedNameTag> {
        val existing = getSavedTags(context).toMutableList()
        val index = existing.indexOfFirst { 
            it.id == tag.id || (it.name.equals(tag.name, ignoreCase = true) && it.designation.equals(tag.designation, ignoreCase = true)) 
        }
        if (index >= 0) {
            existing[index] = tag
        } else {
            existing.add(0, tag)
        }
        persist(context, existing)
        return existing
    }

    fun deleteTag(context: Context, id: String): List<SavedNameTag> {
        val existing = getSavedTags(context).filterNot { it.id == id }
        persist(context, existing)
        return existing
    }

    fun clearAll(context: Context): List<SavedNameTag> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_SAVED_TAGS).apply()
        return emptyList()
    }

    private fun persist(context: Context, list: List<SavedNameTag>) {
        val jsonArray = JSONArray()
        for (item in list) {
            val obj = JSONObject().apply {
                put("id", item.id)
                put("name", item.name)
                put("designation", item.designation)
                put("salutation", item.salutation)
                put("templateIndex", item.templateIndex)
                put("pageSize", item.pageSize)
                put("fontScale", item.fontScale.toDouble())
                put("timestamp", item.timestamp)
            }
            jsonArray.put(obj)
        }
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_SAVED_TAGS, jsonArray.toString()).apply()
    }
}
