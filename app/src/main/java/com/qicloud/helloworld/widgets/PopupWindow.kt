package com.qicloud.helloworld.widgets

import android.content.Context
import com.qicloud.helloworld.R
import com.qicloud.helloworld.base.BasePopupWindow

class PopupWindow(mContext: Context) : BasePopupWindow(mContext) {

    init {
        setPopWindowBgAlpha(0.5f)

    }

    override val viewId: Int
        get() = R.layout.dialog_share
    override val animId: Int
        get() = R.style.AnimationPopupWindow_bottom_to_up
}