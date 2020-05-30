package com.absinthe.libchecker.recyclerview

import android.text.format.Formatter
import android.view.View
import com.absinthe.libchecker.R
import com.absinthe.libchecker.bean.LibStringItem
import com.absinthe.libchecker.constant.librarymap.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.chip.Chip

const val MODE_NATIVE = 0
const val MODE_SERVICE = 1
const val MODE_ACTIVITY = 2
const val MODE_RECEIVER = 3
const val MODE_PROVIDER = 4

class LibStringAdapter : BaseQuickAdapter<LibStringItem, BaseViewHolder>(R.layout.item_lib_string) {

    var mode = MODE_NATIVE

    init {
        addChildClickViewIds(R.id.chip)
    }

    override fun convert(holder: BaseViewHolder, item: LibStringItem) {
        holder.setText(R.id.tv_name, item.name)
        if (item.size != 0L) {
            holder.setText(R.id.tv_lib_size, sizeToString(item.size))
            holder.setGone(R.id.tv_lib_size, false)
        } else {
            holder.setGone(R.id.tv_lib_size, true)
        }

        val libIcon = holder.getView<Chip>(R.id.chip)

        val map = when (mode) {
            MODE_NATIVE -> NativeLibMap.MAP
            MODE_SERVICE -> ServiceLibMap.MAP
            MODE_ACTIVITY -> ActivityLibMap.MAP
            MODE_RECEIVER -> ReceiverLibMap.MAP
            MODE_PROVIDER -> ProviderLibMap.MAP
            else -> NativeLibMap.MAP
        }

        map[item.name]?.let {
            libIcon.apply {
                setChipIconResource(it.iconRes)
                text = it.name
                visibility = View.VISIBLE
            }
        } ?: let {
            libIcon.visibility = View.GONE
        }
    }

    private fun sizeToString(size: Long): String {
        return "(${Formatter.formatFileSize(context, size)})"
    }
}