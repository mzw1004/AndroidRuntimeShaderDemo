package me.mazhiwei.demo.androidruntimeshaderdemo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RuntimeShader
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi

abstract class BaseShaderView(context: Context) : View(context, null, 0) {

  private val runtimeShader: RuntimeShader

  init {
    runtimeShader = createShader()
  }

  private val paint = Paint().apply {
    style = Paint.Style.FILL
    color = Color.WHITE
  }

  abstract fun createShader(): RuntimeShader

  abstract fun updateShader(shader: RuntimeShader, resolution: FloatArray)

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)
    val width = width.toFloat()
    val height = height.toFloat()
    updateShader(runtimeShader, floatArrayOf(width, height))
    paint.shader = runtimeShader
    canvas?.drawRect(0.0f, 0.0f, width, height, paint)
  }
}
