package com.qicloud.helloworld.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.transition.Explode
import android.transition.Slide
import android.util.TypedValue
import android.view.Window
import com.qcloud.qclib.base.BasePresenter
import com.qcloud.qclib.utils.ApiReplaceUtil
import com.qcloud.qclib.utils.SystemBarUtil
import com.qcloud.qclib.widget.dialog.LoadingDialog
import com.qicloud.helloworld.R
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

/**
 * Description: Activity基类
 * Author: kuzan
 * 2018/9/4 17:32.
 */
abstract class BaseActivity<V, T: BasePresenter<V>>: AppCompatActivity() {
    open var mContext: Context? = null
    open var isRunning: Boolean = false

    open var mPresenter: T? = null

    private var loadingDialog: LoadingDialog? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            val explode = Explode()
            explode.duration = 500
            window.enterTransition = explode  // 进来的动画

            val slide = Slide()
            slide.duration = 500
            window.exitTransition = slide   // 退出的动画
        }
        setContentView(layoutId)

        mContext = this
        BaseApplication.mAppManager?.addActivity(this)
        Timber.i("当前加入Activity: $this")

        if (mPresenter == null) {
            mPresenter = initPresenter()
        }
        mPresenter?.attach(this as V)

        isRunning = true

        initViewAndData()
    }


    protected fun setStatusBarDark() {
        if (SystemBarUtil.isSupportStatusBarDarkFont()) {
            SystemBarUtil.setStatusBarLightMode(this, true)
        } else {
            Timber.e("当前设备不支持状态栏字体变色")
            // 设置状态栏颜色为主题颜色
            SystemBarUtil.setStatusBarColor(this, ApiReplaceUtil.getColor(this, R.color.colorRed), false, false)
        }
    }

    override fun getResources(): Resources {
        val res = super.getResources()
        val config = Configuration()
        config.setToDefaults()
        res.updateConfiguration(config, res.displayMetrics)
        return res
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            EventBus.getDefault().unregister(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mPresenter?.detach()
        BaseApplication.mAppManager?.killActivity(this)
        isRunning = false

    }

    /**
     * 获取主题色
     * */
    fun getColorPrimary(): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
        return typedValue.data
    }

    /**
     * 获取深主题色
     * */
    fun getDarkColorPrimary(): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true)
        return typedValue.data
    }

    /** 模版方法，通过该方法获取该Activity的view的layoutId */
    protected abstract val layoutId: Int

    /** 实例化presenter */
    protected abstract fun initPresenter(): T?

    /** 初始化界面和数据 */
    protected abstract fun initViewAndData()

    fun startLoadingDialog() {
        if (mContext != null) {
            if (loadingDialog == null) {
                loadingDialog = LoadingDialog(mContext!!)
            }
            loadingDialog?.show()
        }
    }

    fun stopLoadingDialog() {
        if (loadingDialog != null && loadingDialog!!.isShowing) {
            loadingDialog?.dismiss()
        }
        loadingDialog = null
    }
}