package com.no1ks.madbrains_android_course

import android.util.Base64
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

class StringRequestWithAuth(
    method: Int, url: String?, listener: Response.Listener<String?>?,
    errorListener: Response.ErrorListener?
) : StringRequest(method, url, listener, errorListener) {

    @Throws(AuthFailureError::class)
    override fun getHeaders(): Map<String, String> {
        val headers: MutableMap<String, String> = HashMap()
        val credentials = "${LoggedUser.username}:${LoggedUser.password}"
        val auth = ("Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP))
        headers["Authorization"] = auth
        return headers
    }
}