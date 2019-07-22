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
import javax.inject.Inject

class RecordAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Inject
    lateinit var formatController: FormatController

    private var itemClickListener: ((Int) -> Unit)? = null

    private var whiteRed: Int
    private var whiteGreen: Int
    private var red: Int
    private var green: Int

    private var records: List<Record>
    private var context: Context

    private var summaryPresenter: ShortSummaryPresenter
    private var isHeaderViewNeeded: Boolean = false
    private var headerViewHolder: HeaderViewHolder

    constructor(context: Context, records: List<Record>, isHeaderViewNeeded: Boolean, itemClickListener: ((Int) -> Unit)?) {
        this.context = context
        this.records = records

        MtApp.get().appComponent.inject(this)

        whiteRed = ContextCompat.getColor(context, R.color.white_red)
        whiteGreen = ContextCompat.getColor(context, R.color.white_green)
        red = ContextCompat.getColor(context, R.color.red)
        green = ContextCompat.getColor(context, R.color.green)

        summaryPresenter = ShortSummaryPresenter(context)

        this.itemClickListener = itemClickListener
        this.isHeaderViewNeeded = isHeaderViewNeeded

        headerViewHolder = HeaderViewHolder(LayoutInflater.from(context).inflate(R.layout.view_summary_records, null), itemClickListener)
        summaryPresenter.create(true, headerViewHolder)
    }

    override fun getItemCount() = records.size + if (isHeaderViewNeeded) 1 else 0

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

    class ViewHolder : RecyclerView.ViewHolder {

        var container: LinearLayout
        var tvDateAndTime: TextView
        var tvPrice: TextView
        var tvTitle: TextView
        var tvCategory: TextView
        var tvCurrency: TextView

        constructor(view: View, itemClickListener: ((Int) -> Unit)?) : super(view) {
            container = view.container
            tvDateAndTime = view.tvDateAndTime
            tvPrice = view.tvPrice
            tvTitle = view.tvTitle
            tvCategory = view.tvCategory
            tvCurrency = view.tvCurrency

            view.setOnClickListener {
                itemClickListener?.invoke(adapterPosition)
            }
        }
    }

    class HeaderViewHolder : RecyclerView.ViewHolder, ShortSummaryPresenter.SummaryViewInterface {

        private var tvPeriod: TextView
        private var tvTotalIncome: TextView
        private var tvTotalExpense: TextView
        private var tvTotal: TextView

        override fun getTvPeriod(): TextView = tvPeriod

        override fun getTvTotalIncome(): TextView = tvTotalIncome

        override fun getTvTotalExpense(): TextView = tvTotalExpense

        override fun getTvTotal(): TextView = tvTotal

        constructor(view: View, itemClickListener: ((Int) -> Unit)?) : super(view) {
            tvPeriod = view.tvPeriod
            tvTotalIncome = view.tvTotalIncome
            tvTotalExpense = view.tvTotalExpense
            tvTotal = view.tvTotal

            view.setOnClickListener {
                itemClickListener?.invoke(0)
            }
        }
    }

    companion object {

        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }
}
