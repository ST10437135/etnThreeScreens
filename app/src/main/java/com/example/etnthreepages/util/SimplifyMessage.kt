package com.example.etnthreepages.util

import org.json.JSONException
import org.json.JSONObject

object SimplifyMessage {
    fun get(stringMessage: String): HashMap<String, String> {
        val message = HashMap<String, String>()
        val jsonObject = JSONObject(stringMessage)

        try {
            val jsonMessage = jsonObject.getJSONObject("message")
            jsonMessage.keys().forEach { message[it] = jsonMessage.getString(it) }

        } catch (e: JSONException) {

        }

        return message
    }
}