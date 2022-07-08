package me.mazhiwei.demo.androidruntimeshaderdemo

import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class RenderEffectUsageActivity : AppCompatActivity() {

  private var rootView: View? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    rootView = LayoutInflater.from(this).inflate(R.layout.activity_render_effect_usage, null)
    setContentView(rootView)
  }

  fun applyEffect(view: View) {
    rootView?.let {
      applyRuntimeShader(it, floatArrayOf(it.width.toFloat(), it.height.toFloat()))
    }
  }

  private fun applyRuntimeShader(view: View, resolution: FloatArray) {
    val shader = RuntimeShader(
      """
      uniform shader uContent;
      
      vec4 main(vec2 coords)
      {
        vec3 col = vec3(0.);
        vec4 origin = uContent.eval(coords);
        float l = vec3(0.299*origin.r+0.578*origin.g+0.144*origin.b).r;
        col += l;
        return vec4(col, origin.a);
      }
    """.trimIndent()
    )
    val effect = RenderEffect.createRuntimeShaderEffect(shader, "uContent")
    view.setRenderEffect(effect)
  }
}
