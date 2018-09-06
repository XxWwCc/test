package com.qicloud.helloworld

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import com.qcloud.qclib.adapter.recyclerview.CommonRecyclerAdapter
import com.qcloud.qclib.refresh.api.RefreshLayout
import com.qcloud.qclib.refresh.listener.OnLoadMoreListener
import com.qcloud.qclib.refresh.listener.OnRefreshListener
import com.qcloud.qclib.toast.QToast
import com.qcloud.qclib.widget.layoutManager.DividerItemDecoration
import com.qcloud.qclib.widget.layoutManager.RecycleViewDivider
import com.qicloud.helloworld.Presenter.impl.ActivityTestPresenterImpl
import com.qicloud.helloworld.View.IActivityTestView
import com.qicloud.helloworld.adapters.TestAdapter
import com.qicloud.helloworld.base.BaseActivity
import junit.framework.Test
import kotlinx.android.synthetic.main.activity_pull_refresh.*
import timber.log.Timber

/**
 * Description：
 * author：Smoker
 * 2018/9/6 09:23
 */
class TestActivity : BaseActivity<IActivityTestView, ActivityTestPresenterImpl>(), IActivityTestView{

    private var pageNo = 0
    private var mAdapter: TestAdapter? = null

    override val layoutId: Int
        get() = R.layout.activity_pull_refresh

    override fun initPresenter(): ActivityTestPresenterImpl? {
        return ActivityTestPresenterImpl()
    }

    override fun initViewAndData() {
        layout_empty.setErrorImage(R.drawable.ic_launcher_background)
        layout_empty.setErrorText("出错了")
        layout_empty.setRetryText("重新加载")
        layout_empty.setRetryListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                pageNo = 1
                loadData()
            }
        })
        layout_empty.showError()
        initRecyclerView()
//        prl_test.autoRefresh()
    }

    fun initRecyclerView(){
        prl_test.setEnableRefresh(true)
        prl_test.setEnableLoadMore(true)
        prl_test.setOnRefreshListener(object : OnRefreshListener{
            override fun onRefresh(refreshLayout: RefreshLayout) {
                pageNo = 1
                loadData()
            }
        })
        prl_test.setOnLoadMoreListener(object : OnLoadMoreListener{
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                pageNo++
                loadData()
            }
        })
        mAdapter = TestAdapter(this)
        list_test.layoutManager = LinearLayoutManager(this)
//        list_test.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST))
        list_test.addItemDecoration(RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL))
        list_test.adapter = mAdapter
        mAdapter!!.onHolderClick = object : CommonRecyclerAdapter.OnHolderClickListener<String>{
            override fun onHolderClick(view: View, t: String, position: Int) {
                QToast.success(this@TestActivity, t)
            }
        }
        mAdapter!!.onItemClickListener = AdapterView.OnItemClickListener { p0, p1, position, p3 ->
            QToast.error(this@TestActivity,position)
        }
    }

    fun loadData(){
        mPresenter?.initList(pageNo)
    }

    override fun initList(list: MutableList<String>, noMoreDate: Boolean) {
//        prl_test.finishLoadMore()
        prl_test.finishRefresh()
        mAdapter?.replaceList(list)
        layout_empty.showContent()
        QToast.show(this,"noMoreData = $noMoreDate")
        prl_test.finishLoadMoreWithNoMoreData(noMoreDate)
    }

    override fun loadErr(errMsg: String, isShow: Boolean) {
        if (isRunning){
            prl_test.finishRefresh()
            prl_test.finishLoadMore()
        }
    }
}