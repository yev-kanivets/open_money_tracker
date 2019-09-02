package com.blogspot.e_kanivets.moneytracker.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.entity.RecordReportItem

class RecordReportAdapter(private var items: List<RecordReportItem>, private val ctx: Context) : RecyclerView.Adapter<RecordReportAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size + 1
    }

    override fun getItemViewType(position: Int): Int = when {
        position == 0 -> TYPE_SUMMARY
        items[position - 1] is RecordReportItem.ChildRow -> TYPE_PARENT
        else -> TYPE_CHILD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            when (viewType) {
                TYPE_PARENT -> ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.view_report_item_exp, parent, false))
                TYPE_CHILD -> ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.view_report_item, parent, false))
                else -> ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.view_report_item, parent, false))
            }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        private const val TYPE_SUMMARY = 0
        private const val TYPE_PARENT = 1
        private const val TYPE_CHILD = 2
    }

}