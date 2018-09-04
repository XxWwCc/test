package com.qicloud.helloworld

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ChuckWaveView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val DEFAULT_AMPLITUDE_RATIO = 0.25f    //振幅：波浪静止时水面距离底部的高度
    private val DEFAULT_WATER_LEVEL_RATIO = 0.5f   //水位：波浪垂直振动时偏离水面的最大距离
    private val DEFAULT_WAVE_LENGTH_RATIO = 1.0f   //波长：一个完整的波浪的水平长度
    private val DEFAULT_WAVE_SHIFT_RATIO = 0.1f    //偏移：波浪相对于初始位置的水平偏移

    private var mWaveShader: BitmapShader? = null
    private var mShaderMatrix: Matrix? = null
    private var mViewPaint: Paint? = null

    private var mDefaultWaterLevel = 0f
    private var mWaterLevelRatio = DEFAULT_WATER_LEVEL_RATIO
    private var mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO

    private var frontWaveColor = 0
    private var afterWaveColor = 0

    fun setFrontWaveColor(frontWaveColor: Int){
        this.afterWaveColor = frontWaveColor
    }

    fun setAfterWaveColor(afterWaveColor: Int) {
        this.afterWaveColor = afterWaveColor
    }

    init {
        mShaderMatrix = Matrix()
        mViewPaint = Paint()
        mViewPaint!!.isAntiAlias = true
    }

    fun getWaveShiftRatio() = mWaveShiftRatio

    //波浪偏移原始位置
    fun setWaveShiftRatio(waveShiftRatio: Float){
        mWaveShiftRatio = waveShiftRatio
        invalidate()
    }

    fun getWaterLevelRatio() = mWaterLevelRatio

    //水位
    fun setWaterLevelRatio(waterLevelRatio: Float){
        mWaterLevelRatio = waterLevelRatio
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        createShader()
    }

    /**
     * 绘制横纵波浪的曲线
     */
    private fun createShader(){
        if (frontWaveColor == 0){
            frontWaveColor = Color.parseColor("#a8e6c8")
        }
        if (afterWaveColor == 0){
            afterWaveColor = Color.parseColor("#8be3a4")
        }
        mDefaultWaterLevel = height * DEFAULT_WATER_LEVEL_RATIO
        val mDefaultWaveLength = width
        val mDefaultAmplitude = height * DEFAULT_AMPLITUDE_RATIO
        val mDefaultAngularFrequency = 2.0f * Math.PI / DEFAULT_WAVE_LENGTH_RATIO

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val wavePaint = Paint()
        wavePaint.strokeWidth = 2.0f
        wavePaint.isAntiAlias = true

        // y=Asin(ωx+φ)+h 正弦曲线
        val endX = width + 1
        val endY = height + 1

        val waveY = FloatArray(endX)
        /*var waveX = IntArray(endX)
        var wave: Array<String> = arrayOf(String())*/
        wavePaint.color = afterWaveColor    //后面的波浪
        for (beginX in 0 until endX){
            val wx = beginX * mDefaultAngularFrequency
            val beginY: Float = (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(wx)).toFloat()
            canvas.drawLine(beginX.toFloat(), beginY, beginX.toFloat(), endY.toFloat(), wavePaint)
            waveY[beginX] = beginY
        }

        wavePaint.color = frontWaveColor    //前面的波浪
        val wave2Shift: Int = (mDefaultWaveLength/4).toInt()
        for (beginX in 0 until endX){
            canvas.drawLine(beginX.toFloat(), waveY[(beginX + wave2Shift) % endX], beginX.toFloat(), endY.toFloat(), wavePaint)
        }

        mWaveShader = BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP)
        mViewPaint?.shader = mWaveShader
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mWaveShader != null){
            if (mViewPaint?.shader == null){
                mViewPaint?.shader = mWaveShader
            }
            mShaderMatrix?.setScale(1f,1f, 0f, mDefaultWaterLevel)
            mShaderMatrix?.postTranslate(mWaveShiftRatio * width, (DEFAULT_WATER_LEVEL_RATIO - mWaterLevelRatio) * height)

            mWaveShader!!.setLocalMatrix(mShaderMatrix)
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), mViewPaint)
        } else {
            mViewPaint?.shader = null
        }
    }

}