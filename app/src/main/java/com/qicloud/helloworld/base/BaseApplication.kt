package com.qicloud.helloworld.base

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.qcloud.qclib.AppManager
import com.qcloud.qclib.FrameConfig
import com.qcloud.qclib.utils.SharedUtil
import com.qcloud.qclib.utils.StringUtil
import com.qcloud.qclib.utils.TokenUtil
import com.qicloud.helloworld.R
import com.qicloud.helloworld.constant.AppConstants
import com.qicloud.helloworld.utils.FileLoggingTree
import timber.log.Timber

/**
 * Description: BaseApplication
 * Author: kuzan
 * 2018/9/4 17:27.
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FrameConfig.initSystemConfig(this, R.raw.config)

        mApplication = this
        mAppManager = AppManager.instance

        // 初始化缓存
        SharedUtil.initSharedPreferences(this)

        if (AppConstants.IS_DUBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(FileLoggingTree())
        }
    }

    /**
     * 解决Android 5.0以下方法超限
     * */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        var mApplication: BaseApplication? = null
        var mAppManager: AppManager? = null // Activity 管理器

        /**
         * 判断是否已登录
         *
         * @return true已登录
         */
        fun isLogin(): Boolean {
            return StringUtil.isNotBlank(TokenUtil.getToken())
        }
    }
}