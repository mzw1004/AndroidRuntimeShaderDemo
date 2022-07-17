package me.mazhiwei.demo.androidruntimeshaderdemo

import android.content.Context
import android.graphics.RuntimeShader
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout.LayoutParams
import androidx.appcompat.app.AppCompatActivity

class BasicUsageActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(DisplayView(this),
        LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            Gravity.CENTER
        )
    )
  }

  private inner class DisplayView(context: Context) : BaseShaderView(context) {

    override fun createShader(): RuntimeShader {
      return RuntimeShader(
        """
      uniform vec2 uResolution;
      
      vec4 main(vec2 coords)
      {
        vec2 uv = coords / uResolution;
        vec3 col = vec3(0.);
        col.rg = uv;
        return vec4(col, 1.0);
      }
    """.trimIndent()
      )
    }

    override fun updateShader(shader: RuntimeShader, resolution: FloatArray) {
      shader.setFloatUniform("uResolution", resolution)
    }
  }
}
