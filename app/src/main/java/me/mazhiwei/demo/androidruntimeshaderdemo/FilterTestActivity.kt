package me.mazhiwei.demo.androidruntimeshaderdemo

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.RuntimeShader
import android.graphics.Shader
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class FilterTestActivity : AppCompatActivity() {

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

    private val filters = intArrayOf(
      R.raw.filter_chrome,
      R.raw.filter_cool,
      R.raw.filter_film,
      R.raw.filter_pop,
    )
    private var index = 0

    init {
      setOnClickListener {
        index += 1
        index %= filters.size
        invalidate()
      }
    }

    override fun createShader(): RuntimeShader {
      val shader = RuntimeShader(
        """
      uniform vec2 uResolution;
      uniform shader uBitmap;
      uniform vec2 uBitmapSize;
      uniform shader uLut;
      uniform vec2 uLutSize;
      
      vec4 main(vec2 coords)
      {
        vec2 uv = coords / uResolution;
        vec4 textureColor = uBitmap.eval(uv * uBitmapSize);
        float blueColor = textureColor.b * 31.0;
        float quad1x = floor(blueColor);
        float quad2x = ceil(blueColor);
        vec2 texPos1;
        texPos1.x = (quad1x * 0.03125) + 0.5/1024.0 + ((0.03125 - 1.0/1024.0) * textureColor.r);
        texPos1.y = 0.5/32.0 + ((1. - 1.0/32.0) * textureColor.g);
        vec2 texPos2;
        texPos2.x = (quad2x * 0.03125) + 0.5/1024.0 + ((0.03125 - 1.0/1024.0) * textureColor.r);
        texPos2.y = 0.5/32.0 + ((1. - 1.0/32.0) * textureColor.g);
        vec4 newColor1 = uLut.eval(texPos1 * uLutSize);
        vec4 newColor2 = uLut.eval(texPos2 * uLutSize);
        vec4 newColor = mix(newColor1, newColor2, fract(blueColor));
        return newColor;
      }
    """.trimIndent()
      )
      val bitmap = BitmapFactory.decodeResource(resources, R.raw.p001)
      val bitmapShader = BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
      shader.setInputShader("uBitmap", bitmapShader)
      shader.setFloatUniform("uBitmapSize", floatArrayOf(bitmap.width.toFloat(), bitmap.height.toFloat()))
      return shader
    }

    override fun updateShader(shader: RuntimeShader, resolution: FloatArray) {
      shader.setFloatUniform("uResolution", resolution)

      val lut = BitmapFactory.decodeResource(resources, filters[index])
      val lutShader = BitmapShader(lut, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
      shader.setInputShader("uLut", lutShader)
      shader.setFloatUniform("uLutSize", floatArrayOf(lut.width.toFloat(), lut.height.toFloat()))
    }
  }
}
