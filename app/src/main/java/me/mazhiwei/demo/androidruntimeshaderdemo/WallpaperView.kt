package me.mazhiwei.demo.androidruntimeshaderdemo

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RuntimeShader
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View

class WallpaperView @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

  private val shader = """
      uniform shader uBitmap;
      uniform vec2 uBitmapSize;
      uniform shader uData;
      uniform vec2 uDataSize;
      uniform vec2 uResolution;
      uniform vec2 mGyro;
      
      const float compression = 10.1;
      const float dmin = (1.0 - compression) / 2.0;
      const float dmax = (1.0 + compression) / 2.0;
      
      vec4 main(vec2 coords)
      {
        vec2 uv = coords / uResolution;
        vec4 col = vec4(0.);
        
        vec2 scale = vec2(0.6, 0.6);
        float depth = uData.eval(uv * uDataSize).r * -2.0 + 0.5;
        depth = clamp(depth, dmin, dmax);
        vec2 coordAdjust = mGyro * vec2(-1.0, -1.0) * depth * scale;
        
        col += uBitmap.eval((uv + coordAdjust) * uBitmapSize).rgba;
        return vec4(col);
      }
    """.trimIndent()

  private val runtimeShader = RuntimeShader(shader)
  private val runtimeShader2 = RuntimeShader(shader)
  private var g = FloatArray(2)

  private val paint = Paint().apply {
    style = Paint.Style.FILL
    color = Color.WHITE
  }

  private val gravitySensor = GravitySensor(context).apply {
    listener = object : GravitySensor.GravityListener {
      override fun onGravityChange(x: Float, y: Float) {
        g[0] = x / 100
        g[1] = y / 100
        postInvalidate()
      }
    }
  }

  init {
    val bitmapBack = BitmapFactory.decodeResource(resources, R.raw.wallpaper_back, BitmapFactory.Options().apply {
      inSampleSize = 2
    })
    val bitmapBackShader = BitmapShader(bitmapBack, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    val bitmapBackData = BitmapFactory.decodeResource(resources, R.raw.wallpaper_data1)
    val bitmapBackDataShader =
      BitmapShader(bitmapBackData, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    val bitmapMiddle = BitmapFactory.decodeResource(resources, R.raw.wallpaper_middle, BitmapFactory.Options().apply {
      inSampleSize = 2
    })
    val bitmapMiddleShader =
      BitmapShader(bitmapMiddle, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    val bitmapMiddleData = BitmapFactory.decodeResource(resources, R.raw.wallpaper_data2)
    val bitmapMiddleDataShader =
      BitmapShader(bitmapMiddleData, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

    runtimeShader.apply {
      setInputShader("uBitmap", bitmapBackShader)
      setFloatUniform(
        "uBitmapSize",
        floatArrayOf(bitmapBack.width.toFloat(), bitmapBack.height.toFloat())
      )
      setInputShader("uData", bitmapBackDataShader)
      setFloatUniform(
        "uDataSize",
        floatArrayOf(bitmapBackData.width.toFloat(), bitmapBackData.height.toFloat())
      )
    }
    runtimeShader2.apply {
      setInputShader("uBitmap", bitmapMiddleShader)
      setFloatUniform(
        "uBitmapSize",
        floatArrayOf(bitmapMiddle.width.toFloat(), bitmapMiddle.height.toFloat())
      )
      setInputShader("uData", bitmapMiddleDataShader)
      setFloatUniform(
        "uDataSize",
        floatArrayOf(bitmapMiddleData.width.toFloat(), bitmapMiddleData.height.toFloat())
      )
    }
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    gravitySensor.onResume()
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    gravitySensor.onPause()
    gravitySensor.onDestroy()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(heightMeasureSpec, heightMeasureSpec)
    runtimeShader.setFloatUniform("uResolution", floatArrayOf(measuredWidth.toFloat(), measuredHeight.toFloat()))
    runtimeShader2.setFloatUniform("uResolution", floatArrayOf(measuredWidth.toFloat(), measuredHeight.toFloat()))
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    val width = width.toFloat()
    val height = height.toFloat()
    // draw back
    runtimeShader.apply {
      setFloatUniform("mGyro", g)
    }
    paint.shader = runtimeShader
    canvas.drawRect(0.0f, 0.0f, width, height, paint)
    // draw middle
    runtimeShader2.apply {
      setFloatUniform("mGyro", g)
    }
    paint.shader = runtimeShader2
    canvas.drawRect(0.0f, 0.0f, width, height, paint)
  }
}
