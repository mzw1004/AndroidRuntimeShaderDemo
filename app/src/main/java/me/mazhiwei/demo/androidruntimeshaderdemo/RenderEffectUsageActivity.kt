package me.mazhiwei.demo.androidruntimeshaderdemo

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class RenderEffectUsageActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val rootView = LayoutInflater.from(this).inflate(R.layout.activity_render_effect_usage, null)
    setContentView(rootView)
    applyRuntimeShader(rootView)
  }

  private fun applyRuntimeShader(view: View) {
    val shader = RuntimeShader(
      """
      uniform shader uContent;
      
      vec4 main(vec2 coords)
      {
        return uContent.eval(coords);
      }
    """.trimIndent()
    )
    val effect = RenderEffect.createRuntimeShaderEffect(shader, "uContent")
    view.setRenderEffect(effect)
  }
}
