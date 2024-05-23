package com.example.one.helper

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream


object MessageHelper {
    init {
        Log.d("init","init MessageHelper")
    }
    /**
     * @since 2025/5/17
     * 向其他应用发送pareText
     */
    fun sendPlainText(context: Context, data: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_TEXT, data)
        shareIntent.type = "text/plain"

        // 检查是否有可以处理分享意图的应用
        val packageManager = context.packageManager
        val activities = packageManager.queryIntentActivities(shareIntent, 0)

        if (activities.isNotEmpty()) {
            // 找到至少一个应用程序可以处理此意图
            try {
                // 检查当前设备是否运行Android Q 或更高版本
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // 在 Android Q 及更高版本上，需要使用专门的 API 启动共享操作
                    val chooserIntent = Intent.createChooser(shareIntent, "分享文本")
                    chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(chooserIntent)
                } else {
                    // 在 Android Q 以下的版本上，直接启动共享意图
                    context.startActivity(shareIntent)
                }
            } catch (ex: ActivityNotFoundException) {
                // 处理找不到活动的情况
                Toast.makeText(context, "没有合适的处理程序", Toast.LENGTH_SHORT).show()
            }
        } else {
            // 没有应用程序可以处理此意图
            Toast.makeText(context, "没有找到可以处理此意图的应用程序", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * @since 2025/5/16
     * 分享图片
     */
    fun shareBitmap(context: Context, bitmap: Bitmap, title: String) {
        // 将 Bitmap 保存到本地存储中，并获取其 URI
        val uri = saveBitmapAndGetUri(context, bitmap)

        // 创建分享意图
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.putExtra(Intent.EXTRA_TITLE, title)

        try {
            // 启动分享意图
            val chooserIntent = Intent.createChooser(shareIntent, "分享图片")
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooserIntent)
        } catch (ex: ActivityNotFoundException) {
            // 处理找不到活动的情况
            Toast.makeText(context, "没有合适的处理程序", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * @since 2025/5/16
     */
    // 将bitmap保存到cache中并且返回uri
    private fun saveBitmapAndGetUri(context: Context, bitmap: Bitmap): Uri {
        // 创建文件输出流，将 Bitmap 保存到本地存储
        val imagesFolder = File(context.cacheDir, "images")
        imagesFolder.mkdirs()
        val file = File(imagesFolder, "shared_image.png")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()

        // 获取保存的文件的 URI
        return FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            file
        )
    }

}