package com.qicloud.helloworld.Presenter.impl

import com.qcloud.qclib.base.BasePresenter
import com.qicloud.helloworld.Presenter.IActivityTestPresenter
import com.qicloud.helloworld.View.IActivityTestView

/**
 * Description：
 * author：Smoker
 * 2018/9/6 09:25
 */
class ActivityTestPresenterImpl : BasePresenter<IActivityTestView>(), IActivityTestPresenter {

    private var list: MutableList<String> = ArrayList()
    private var data: MutableList<String>? = null
    private var noMoreData = true

    init {
        for (i in 0 until 35){
            list.add("test:$i")
        }
    }
    override fun initList(pageNo: Int) {
        data = ArrayList()
        if (pageNo*10 < list.size) {
            for (i in 0 until pageNo*10){
                data!!.add(list[i])
            }
        } else {
            for (i in 0 until list.size){
                data!!.add(list[i])
            }
        }
        noMoreData = list.size <= pageNo*10
        mView?.initList(data!!, noMoreData)
    }
}