package com.qicloud.helloworld.ui.widget

import android.animation.ObjectAnimator
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TimePicker
import com.qcloud.qclib.AppManager
import com.qcloud.qclib.image.GlideUtil
import com.qcloud.qclib.imageselect.ImageSelectActivity
import com.qcloud.qclib.imageselect.ProcessImageActivity
import com.qcloud.qclib.toast.QToast
import com.qcloud.qclib.widget.customview.wheelview.DateTimePicker
import com.qicloud.helloworld.R
import com.qicloud.helloworld.base.BaseApplication
import com.qicloud.helloworld.widgets.PopupWindow
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_of_test.*
import org.jetbrains.anko.wrapContent

class MainActivity : AppCompatActivity() {

    private var waveShiftAnim: ObjectAnimator? = null
    private var iv_test: ImageView? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initWaveView()
        val btn: Button = findViewById(R.id.btn)

        btn_take_photo.setOnClickListener{
            ProcessImageActivity.openThisActivity(this, 2, 1, false, 2, 100, 100)
        }

        btn_select_image.setOnClickListener{
            ImageSelectActivity.openActivity(this, 1, 3)
        }

        btn_choice_date.setOnClickListener{
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener {
                _, year, month, day -> QToast.show(this@MainActivity, "$year-${month+1}-$day")
            },2018,8,13).show()
        }

        btn_choice_time.setOnClickListener{
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener {
                _, hour, minute -> QToast.show(this, "$hour:$minute")
            }, 8, 0, true).show()
        }

        btn.setOnClickListener{
            val pop = PopupWindow(this)
            pop.showAtLocation(btn,Gravity.CENTER,0,0)
        }
        btn_close.setOnClickListener{
            AppManager.instance.killAllActivity()

        }
        iv_test = findViewById(R.id.iv_test)
        GlideUtil.loadCircleImage(this,iv_test!!,R.drawable.icon_select,R.drawable.icon_no_data, wrapContent, wrapContent)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null){
            if (resultCode == Activity.RESULT_OK){
                if (requestCode == 1){
                    QToast.show(this, "相册")
                } else {
                    QToast.show(this, "照相")
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (waveShiftAnim != null){
            waveShiftAnim!!.reverse()
        }
    }
}
