package me.mazhiwei.demo.androidruntimeshaderdemo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RuntimeShader
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi

@RequiresApi(33)
class ShaderDisplayView @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

  init {

  }

  private val paint = Paint().apply {
    style = Paint.Style.FILL
    color = Color.RED
  }

  private val runtimeShader = RuntimeShader(
    """
      uniform vec2 u_resolution;
      
      vec4 main(vec2 coords)
      {
        vec2 uv = coords / u_resolution;
        vec3 col = vec3(0.);
        col.rg = uv;
        return vec4(col, 1.0);
      }
    """.trimIndent()
  )

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)
    val width = width.toFloat()
    val height = height.toFloat()
    runtimeShader.setFloatUniform("u_resolution", floatArrayOf(width, height))
    paint.shader = runtimeShader
    canvas?.drawRect(0.0f, 0.0f, width, height, paint)
  }
}
