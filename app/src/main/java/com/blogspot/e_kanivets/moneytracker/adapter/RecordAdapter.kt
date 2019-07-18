package com.blogspot.e_kanivets.moneytracker.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.blogspot.e_kanivets.moneytracker.MtApp
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.controller.FormatController
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import com.blogspot.e_kanivets.moneytracker.report.record.IRecordReport
import com.blogspot.e_kanivets.moneytracker.ui.presenter.ShortSummaryPresenter
import kotlinx.android.synthetic.main.view_record.view.*
import kotlinx.android.synthetic.main.view_summary_records.view.*

class RecordAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private var formatController: FormatController

    private var itemClickListener: OnItemClickListener? = null

    private var whiteRed: Int
    private var whiteGreen: Int
    private var red: Int
    private var green: Int

    private var records: List<Record>
    private var context: Context

    private var summaryPresenter: ShortSummaryPresenter
    private var isHeaderViewNeeded: Boolean = false
    private lateinit var headerViewHolder: HeaderViewHolder

    constructor(context: Context, records: List<Record>, formatController: FormatController, isHeaderViewNeeded: Boolean) {
        this.context = context
        this.records = records

        MtApp.get().appComponent.inject(this)

        whiteRed = ContextCompat.getColor(context, R.color.white_red)
        whiteGreen = ContextCompat.getColor(context, R.color.white_green)
        red = ContextCompat.getColor(context, R.color.red)
        green = ContextCompat.getColor(context, R.color.green)

        summaryPresenter = ShortSummaryPresenter(context)

        if (isHeaderViewNeeded) {
            headerViewHolder = HeaderViewHolder(LayoutInflater.from(context).inflate(R.layout.view_summary_records, null), itemClickListener)
            summaryPresenter.create(true, headerViewHolder)
        }

        this.isHeaderViewNeeded = isHeaderViewNeeded
        this.formatController = formatController
    }

    override fun getItemCount() = records.size

    override fun getItemViewType(position: Int): Int = if (position == 0 && isHeaderViewNeeded) {
        TYPE_HEADER
    } else {
        TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            if (viewType == TYPE_HEADER) {
                headerViewHolder
            } else {
                ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_record, parent, false), itemClickListener)
            }

    override fun onBindViewHolder(rvViewHolder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0 && isHeaderViewNeeded) {
            //adapter already bound to view
            return
        }

        val viewHolder = rvViewHolder as ViewHolder
        val record: Record = records[position - if (isHeaderViewNeeded) 1 else 0]
        viewHolder.container.setBackgroundColor(if (record.isIncome) whiteGreen else whiteRed)
        viewHolder.tvPrice.setTextColor(if (record.isIncome) green else red)

        viewHolder.tvDateAndTime.text = formatController.formatDateAndTime(record.time)
        val price = (if (record.isIncome) record.fullPrice else getNegative(record.fullPrice))
        viewHolder.tvPrice.text = formatController.formatSignedAmount(price)
        viewHolder.tvTitle.text = record.title
        viewHolder.tvCategory.text = record.category?.name
        viewHolder.tvCurrency.text = record.currency
    }

    private fun getNegative(number: Double): Double {
        return -1 * number
    }

    fun setRecords(recordsList: List<Record>, report: IRecordReport?, currency: String, ratesNeeded: List<String>) {
        records = recordsList
        summaryPresenter.update(report, currency, ratesNeeded)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    class ViewHolder : RecyclerView.ViewHolder {

        var container: LinearLayout
        var tvDateAndTime: TextView
        var tvPrice: TextView
        var tvTitle: TextView
        var tvCategory: TextView
        var tvCurrency: TextView

        constructor(view: View, itemClickListener: OnItemClickListener?) : super(view) {
            container = view.container
            tvDateAndTime = view.tvDateAndTime
            tvPrice = view.tvPrice
            tvTitle = view.tvTitle
            tvCategory = view.tvCategory
            tvCurrency = view.tvCurrency

            view.setOnClickListener {
                itemClickListener?.onItemClick(adapterPosition)
            }
        }

    }

    class HeaderViewHolder : RecyclerView.ViewHolder {

        var tvPeriod: TextView
        var tvTotalIncome: TextView
        var tvTotalExpense: TextView
        var tvTotal: TextView

        constructor(view: View, itemClickListener: OnItemClickListener?) : super(view) {
            tvPeriod = view.tvPeriod
            tvTotalIncome = view.tvTotalIncome
            tvTotalExpense = view.tvTotalExpense
            tvTotal = view.tvTotal

            view.setOnClickListener {
                itemClickListener?.onItemClick(0)
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    companion object {

        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1

    }

}
