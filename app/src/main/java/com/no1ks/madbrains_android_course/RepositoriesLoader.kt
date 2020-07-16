package com.no1ks.madbrains_android_course

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import org.json.JSONArray

class RepositoriesLoader {
    // This interface allows triggering Activity when response downloaded
    interface ResponseListener {
        fun onResponseReady()
        fun onResponseFailed()
    }
    private var mListener: ResponseListener? = null
    fun setCustomListener(listener: ResponseListener?) {
        mListener = listener
    }

    private val url = "https://api.github.com/repositories"
    var queueResult: String = "failed"
    private val repositories: MutableList<Repository> = mutableListOf()
        get() = field

    fun loadRepositoriesFromNetwork(queue: RequestQueue) {
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener { response ->
                parseJson(response)
                mListener?.onResponseReady()
            },
            Response.ErrorListener { error ->
                queueResult = error.message.toString()
                mListener?.onResponseFailed()
            }
        )
        queue.add(stringRequest)
    }

    fun parseJson(responseText: String) {
        repositories.clear()
        val jsonArray = JSONArray(responseText)
        for (index in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(index)
        }
        queueResult = jsonArray.length().toString()
    }
}