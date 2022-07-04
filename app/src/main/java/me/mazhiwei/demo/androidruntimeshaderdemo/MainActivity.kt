package me.mazhiwei.demo.androidruntimeshaderdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  fun goBasicUsage(view: View) {
    val intent = Intent(this, BasicUsageActivity::class.java)
    startActivity(intent)
  }

  fun goBitmapDisplay(view: View) {
    val intent = Intent(this, BitmapDisplayActivity::class.java)
    startActivity(intent)
  }
}
