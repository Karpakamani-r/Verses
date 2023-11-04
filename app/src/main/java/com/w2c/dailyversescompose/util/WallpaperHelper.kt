package com.w2c.dailyversescompose.util

import android.app.WallpaperManager
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.drawToBitmap
import androidx.core.view.setPadding


fun setAsWallpaper(context: Context, verse: String) {
    val tv = TextView(context)
    tv.width = ViewGroup.LayoutParams.MATCH_PARENT
    tv.height = ViewGroup.LayoutParams.MATCH_PARENT
    tv.setPadding(35)
    tv.gravity = Gravity.CENTER
    tv.textSize = 18f
    tv.text = verse
    tv.setTextColor(Color.parseColor("#64B678"))
    val wallpaperManager = WallpaperManager.getInstance(context)
    wallpaperManager.setBitmap(getGradiantStyleText(tv))
    Toast.makeText(context, "Updated wallpaper successfully", Toast.LENGTH_SHORT).show()
}

fun getGradiantStyleText(tv: TextView): Bitmap {
    val paint = tv.paint
    val width = paint.measureText(tv.text.toString())

    val textShader: Shader = LinearGradient(
        0f,
        0f,
        width,
        tv.textSize,
        intArrayOf(
            Color.parseColor("#64B678"),
            Color.parseColor("#478AEA"),
            Color.parseColor("#8446CC"),
        ),
        null,
        Shader.TileMode.CLAMP
    )
    paint.shader = textShader

    val returnedBitmap =
        Bitmap.createBitmap(tv.getWidth(), tv.getHeight(), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(returnedBitmap)
    val bgDrawable: Drawable = tv.getBackground()
    if (bgDrawable != null) bgDrawable.draw(canvas) else canvas.drawColor(Color.WHITE)
    tv.draw(canvas)
    return returnedBitmap
}