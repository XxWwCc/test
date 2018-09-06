package com.qicloud.helloworld.adapters

import android.content.Context
import android.widget.Button
import com.qcloud.qclib.adapter.recyclerview.BaseViewHolder
import com.qcloud.qclib.adapter.recyclerview.CommonRecyclerAdapter
import com.qicloud.helloworld.R

/**
 * Description：
 * author：Smoker
 * 2018/9/6 09:47
 */
class TestAdapter(context: Context) : CommonRecyclerAdapter<String>(context){


    override val viewId: Int
        get() = R.layout.item_of_test

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.setText(R.id.tv_test, mList[position])
        holder.get<Button>(R.id.btn_ok).setOnClickListener{
            view ->  onHolderClick?.onHolderClick(view, mList[position], position)
        }
    }
}