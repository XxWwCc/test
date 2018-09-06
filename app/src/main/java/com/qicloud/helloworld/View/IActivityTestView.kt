package com.qicloud.helloworld.View

import com.qcloud.qclib.base.BaseView

/**
 * Description：
 * author：Smoker
 * 2018/9/6 09:24
 */
interface IActivityTestView : BaseView {
    fun initList( list: MutableList<String>, noMoreDate: Boolean)
}