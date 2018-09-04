package com.qicloud.helloworld

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import com.qicloud.helloworld.R.id.btn

class MainActivity : AppCompatActivity() {

    private var waveShiftAnim: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initWaveView()
        val btn: Button = findViewById(R.id.btn)
        btn.setOnClickListener(View.OnClickListener {
            val pop = PopupWindow(this)
            pop.showAtLocation(btn,Gravity.CENTER,0,0)
        })
    }

    private fun initWaveView(){
        /*val waveView: ChuckWaveView = findViewById(R.id.cwv)
        val mAnimators: MutableList<Animator> = ArrayList()
        waveShiftAnim = ObjectAnimator.ofFloat(waveView, "waveShiftRatio", 0f, 1f)
        waveView.setFrontWaveColor(Color.WHITE)
        if (waveShiftAnim != null){
            waveShiftAnim!!.repeatCount = ValueAnimator.INFINITE
            waveShiftAnim!!.duration = 10000
            waveShiftAnim!!.interpolator = LinearInterpolator()
            mAnimators.add(waveShiftAnim!!)
        }
        waveView.invalidate()
        val mAnimatorSet = AnimatorSet()
        mAnimatorSet.playTogether(mAnimators)
        mAnimatorSet.start()*/
    }

    override fun onStop() {
        super.onStop()
        if (waveShiftAnim != null){
            waveShiftAnim!!.reverse()
        }
    }
}
