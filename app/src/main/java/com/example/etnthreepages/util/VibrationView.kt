package com.example.etnthreepages.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.animation.AnimationUtils
import com.example.etnthreepages.R

class VibrationView {
    companion object{
        fun vibrate (context: Context, view: View){
            val vibration = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibration.vibrate(
                    VibrationEffect.createOneShot(
                        350, VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            }

            val animation = AnimationUtils.loadAnimation(context, R.anim.vibration)
        }
    }
}