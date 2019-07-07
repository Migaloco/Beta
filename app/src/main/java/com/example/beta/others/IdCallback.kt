package com.example.beta.others

import org.json.JSONObject

interface IdCallback {

    fun onUserLogeedIn(token: JSONObject)
}