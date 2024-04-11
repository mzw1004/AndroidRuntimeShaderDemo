package me.mazhiwei.demo.androidruntimeshaderdemo

import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WallpaperDemoActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val container = FrameLayout(this)

    setContentView(container)
    lifecycleScope.launch(Dispatchers.IO) {
      val wallpaperView = WallpaperView(this@WallpaperDemoActivity).apply {
        scaleX = 1.4f
        scaleY = 1.4f
      }
      withContext(Dispatchers.Main) {
        container.addView(
          wallpaperView,
          LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER)
        )
      }
    }
  }
}
