package com.absinthe.libchecker.view

import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.absinthe.libchecker.recyclerview.AppAdapter
import com.absinthe.libchecker.view.dialogfragment.EXTRA_PKG_NAME
import com.absinthe.libchecker.view.dialogfragment.NativeLibDialogFragment
import com.absinthe.libchecker.bean.AppItem

class ClassifyDialogView(context: Context) : LinearLayout(context) {

    var adapter = AppAdapter()

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        adapter.setOnItemClickListener { adapter, _, position ->
            NativeLibDialogFragment()
                .apply {
                arguments = Bundle().apply {
                    putString(EXTRA_PKG_NAME, (adapter.getItem(position) as AppItem).packageName)
                }
                (context as AppCompatActivity).apply {
                    show(supportFragmentManager, tag)
                }
            }
        }

        val rvList = RecyclerView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            layoutManager = LinearLayoutManager(context)
            adapter = this@ClassifyDialogView.adapter
        }

        addView(rvList)
    }

}