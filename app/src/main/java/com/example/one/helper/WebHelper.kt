package com.example.one.helper

import android.util.Log
import androidx.media3.common.util.HandlerWrapper.Message
import com.example.one.data.DiarySurface.DiarySurfaceData
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import org.jsoup.Jsoup

object WebHelper{
    fun fetchContent(url:String): ArrayList<DiarySurfaceData>? {
        Log.d("Web","getting web")
        try {
            val html = fetchHtml(url)
            val content = parseContent(html)
            return content
        }catch (e : IOException)
        {
            Log.d("WebErr","cannot get message")
        }
        return null
    }

    /**
     * 创建html请求并且获取html
     */
    private fun fetchHtml(url:String): String {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        val response = okHttpClient.newCall(request).execute()
        return response.body?.string() ?: ""
    }

    /**
     * 获取html中的信息
     */
    private fun parseContent(html:String): ArrayList<DiarySurfaceData>
    {
        val doc = Jsoup.parse(html)
        val diarySurfaceData:ArrayList<DiarySurfaceData>  = arrayListOf()

        for(item in doc.select(".item"))
        {
            val imageuri = item.select(".fp-one-imagen").attr("src")
            val message = item.select(".fp-one-cita").text()

            diarySurfaceData.add(DiarySurfaceData(imageuri,message))

            Log.d("message",imageuri)
            Log.d("message",message)
        }
        return diarySurfaceData
    }
}