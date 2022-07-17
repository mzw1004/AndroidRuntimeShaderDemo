package me.mazhiwei.demo.androidruntimeshaderdemo

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.RuntimeShader
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class BitmapDisplayActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(DisplayView(this),
        FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER
        )
    )
  }

  private inner class DisplayView(context: Context): BaseShaderView(context) {

    override fun createShader(): RuntimeShader {
      val shader = RuntimeShader(
        """
      uniform shader uBitmap;
      uniform vec2 uBitmapSize;
      uniform vec2 uResolution;
      
      vec4 main(vec2 coords)
      {
        vec2 uv = coords / uResolution;
        vec3 col = vec3(0.);
        col += uBitmap.eval(uv * uBitmapSize).rgb;
        return vec4(col, 1.0);
      }
    """.trimIndent()
      )
      val bitmap = BitmapFactory.decodeResource(resources, R.raw.sample)
      val bitmapShader = BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
      shader.setInputShader("uBitmap", bitmapShader)
      shader.setFloatUniform("uBitmapSize", floatArrayOf(bitmap.width.toFloat(), bitmap.height.toFloat()))
      return shader
    }

    override fun updateShader(shader: RuntimeShader, resolution: FloatArray) {
      shader.setFloatUniform("uResolution", resolution)
    }

  }
}
