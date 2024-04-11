package me.mazhiwei.demo.androidruntimeshaderdemo

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.SystemClock
import androidx.core.content.ContextCompat
import kotlin.math.abs

class GravitySensor(private val context: Context) : SensorEventListener {

  companion object {
    // 重力加速度
    const val G = 9.80665f

    // 刷新间隔 us
    const val REFRESH_RATE = 16000
  }

  private var sensorManager= ContextCompat.getSystemService(context, SensorManager::class.java)
  var listener: GravityListener? = null
  private var lastX = 0f
  private var lastY = 0f
  private var lastRefreshTime = 0L

   fun onResume() {
    sensorManager?.registerListener(
      this,
      sensorManager?.getDefaultSensor(Sensor.TYPE_GRAVITY),
      REFRESH_RATE,
      REFRESH_RATE
    )
  }

   fun onPause() {
    sensorManager?.unregisterListener(this)
  }

   fun onDestroy() {
    listener = null
    sensorManager?.unregisterListener(this)
  }

  override fun onSensorChanged(event: SensorEvent?) {
    if (event?.sensor?.type == Sensor.TYPE_GRAVITY) {
      var (x, y) = event.values
      //  防抖
      if (abs(-x - lastX) > 0.01 || abs(y - lastY) >= 0.01) {
        if (SystemClock.elapsedRealtime() - lastRefreshTime < REFRESH_RATE / 1000) {
          return
        }
        lastRefreshTime = SystemClock.elapsedRealtime()
        lastX = -x
        lastY = y
        listener?.onGravityChange(lastX, lastY)
      }
    }
  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
  }

  interface GravityListener {
    /**
     * 根据重力返回重力偏移，一个单位重力加速度[G]数值为9.80665f，
     * 每[REFRESH_RATE]毫秒会调用一次，注意不要调用耗时任务
     *
     * @param x 左右角度变化，手机左边朝下为-[G]，右边朝下为[G]，平放为0
     * @param y 上下角度变化，手机上边朝下为-[G]，下边朝下为[G]，平方为0
     */
    fun onGravityChange(x: Float, y: Float)
  }
}