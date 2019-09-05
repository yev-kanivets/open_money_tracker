package com.blogspot.e_kanivets.moneytracker.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.entity.RecordReportItem
import kotlinx.android.synthetic.main.view_report_item_exp.view.*

class RecordReportAdapter(
        private var items: MutableList<RecordReportItem>,
        private var data: HashMap<RecordReportItem.ParentRow, List<RecordReportItem.ChildRow>>,
        private val ctx: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var red: Int = 0
    private var green: Int = 0

    init {
        red = ContextCompat.getColor(ctx, R.color.red)
        green = ContextCompat.getColor(ctx, R.color.green)
    }

    private lateinit var summaryViewHolder: RecyclerView.ViewHolder

    override fun getItemCount(): Int {
        return items.size + 1
    }

    override fun getItemViewType(position: Int): Int = when {
        position == 0 -> TYPE_SUMMARY
        items[getPosWithoutSummary(position)] is RecordReportItem.ChildRow -> TYPE_CHILD
        else -> TYPE_PARENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            when (viewType) {
                TYPE_PARENT -> ParentViewHolder(LayoutInflater.from(ctx).inflate(R.layout.view_report_item_exp, parent, false))
                TYPE_CHILD -> ChildViewHolder(LayoutInflater.from(ctx).inflate(R.layout.view_report_item, parent, false))
                else -> summaryViewHolder
            }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val posWithoutSummary = getPosWithoutSummary(position)
        if (posWithoutSummary < 0) return

        if (holder is ChildViewHolder) {
            val row = items[posWithoutSummary] as RecordReportItem.ChildRow
            holder.tvCategory.text = row.title
            holder.tvTotal.text = row.amount
            holder.tvTotal.setTextColor(if (row.amount.first() != '-') green else red)
        } else {
            val parentViewHolder = holder as ParentViewHolder
            val row = items[posWithoutSummary] as RecordReportItem.ParentRow
            parentViewHolder.tvCategory.text = row.category
            parentViewHolder.tvTotal.text = row.totalAmount
            holder.tvTotal.setTextColor(if (row.totalAmount.first() != '-') green else red)
        }
    }

    fun setData(items: MutableList<RecordReportItem>, data: HashMap<RecordReportItem.ParentRow, List<RecordReportItem.ChildRow>>) {
        this.items = items
        this.data = data
        notifyDataSetChanged()
    }

    fun addSummaryView(summaryView: View) {
        this.summaryViewHolder = summaryView.tag as RecyclerView.ViewHolder
    }

    private fun changeItems(position: Int) {
        if (items[position] is RecordReportItem.ParentRow) {
            val parentRow: RecordReportItem.ParentRow = items[position] as RecordReportItem.ParentRow

            if (parentRow.isOpen) {
                val item = items.filterIndexed { index, _ -> index > position }
                        .find { recordReportItem -> recordReportItem is RecordReportItem.ParentRow }

                val lastChildInd = if (item != null) items.indexOf(item) else items.size

                items.subList(position + 1, lastChildInd).clear()
                val itemCount = lastChildInd - position - 1
                notifyItemRangeRemoved(getPosWithSummary(position + 1), itemCount)

                parentRow.isOpen = false
            } else {
                data[parentRow]?.let { childRows ->
                    var lastChildInd = position + 1
                    for (childRow in childRows) {
                        items.add(lastChildInd, childRow)
                        lastChildInd++
                    }
                    notifyItemRangeInserted(getPosWithSummary(position + 1), childRows.size)
                }

                parentRow.isOpen = true
            }
        }
    }

    private fun getPosWithSummary(position: Int): Int {
        return position + 1
    }

    private fun getPosWithoutSummary(position: Int): Int {
        return position - 1
    }

    inner class ParentViewHolder : RecyclerView.ViewHolder {

        var tvCategory: TextView
        var tvTotal: TextView

        constructor(view: View) : super(view) {
            tvCategory = view.tvCategory
            tvTotal = view.tvTotal

            view.setOnClickListener {
                if (view.lowerDivider.visibility == View.VISIBLE) {
                    view.lowerDivider.visibility = View.GONE
                    view.ivArrow.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_arrow_downward_outline))
                } else {
                    view.lowerDivider.visibility = View.VISIBLE
                    view.ivArrow.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_arrow_upward_outline))
                }

                changeItems(getPosWithoutSummary(adapterPosition))
            }
        }
    }

    class ChildViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvCategory: TextView = view.tvCategory
        val tvTotal: TextView = view.tvTotal

    }

    companion object {

        private const val TYPE_SUMMARY = 0
        private const val TYPE_PARENT = 1
        private const val TYPE_CHILD = 2
    }

}