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
import com.blogspot.e_kanivets.moneytracker.entity.RecordAdapterData
import com.blogspot.e_kanivets.moneytracker.entity.HeaderItem
import com.blogspot.e_kanivets.moneytracker.entity.RecordItem
import com.blogspot.e_kanivets.moneytracker.report.record.IRecordReport
import com.blogspot.e_kanivets.moneytracker.ui.presenter.ShortSummaryPresenter
import kotlinx.android.synthetic.main.view_header_date.view.*
import kotlinx.android.synthetic.main.view_record.view.*
import kotlinx.android.synthetic.main.view_record.view.container
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

    private var items: List<RecordAdapterData>
    private var context: Context

    private var summaryPresenter: ShortSummaryPresenter
    private var isSummaryViewNeeded: Boolean = false
    private var summaryViewHolder: SummaryViewHolder

    constructor(context: Context, items: List<RecordAdapterData>, isSummaryViewNeeded: Boolean, itemClickListener: ((Int) -> Unit)?) {
        this.context = context
        this.items = items

        MtApp.get().appComponent.inject(this)

        whiteRed = ContextCompat.getColor(context, R.color.white_red)
        whiteGreen = ContextCompat.getColor(context, R.color.white_green)
        red = ContextCompat.getColor(context, R.color.red)
        green = ContextCompat.getColor(context, R.color.green)

        summaryPresenter = ShortSummaryPresenter(context)

        this.itemClickListener = itemClickListener
        this.isSummaryViewNeeded = isSummaryViewNeeded

        summaryViewHolder = SummaryViewHolder(LayoutInflater.from(context).inflate(R.layout.view_summary_records, null), itemClickListener)
        summaryPresenter.create(true, summaryViewHolder)
    }

    override fun getItemCount() = items.size + if (isSummaryViewNeeded) 1 else 0

    override fun getItemViewType(position: Int): Int = if (position == 0 && isSummaryViewNeeded) {
        TYPE_SUMMARY
    } else if (items[position - if (isSummaryViewNeeded) 1 else 0] is HeaderItem) {
        TYPE_HEADER
    } else {
        TYPE_RECORD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                TYPE_RECORD -> RecordViewHolder(LayoutInflater.from(context).inflate(R.layout.view_record, parent, false), itemClickListener)
                TYPE_HEADER -> HeaderViewHolder(LayoutInflater.from(context).inflate(R.layout.view_header_date, parent, false))
                else -> summaryViewHolder
            }

    override fun onBindViewHolder(rvViewHolder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0 && isSummaryViewNeeded) {
            //adapter already bound to view
            return
        }

        if (rvViewHolder is RecordViewHolder) {
            val record = items[position - if (isSummaryViewNeeded) 1 else 0] as RecordItem
            rvViewHolder.tvPrice.setTextColor(if (record.isIncome) green else red)

            val price = (if (record.isIncome) record.fullPrice else getNegative(record.fullPrice))
            rvViewHolder.tvPrice.text = formatController.formatSignedAmount(price)
            rvViewHolder.tvTitle.text = record.title
            rvViewHolder.tvCategory.text = record.categoryName
            rvViewHolder.tvCurrency.text = record.currency
        } else {
            val headerViewHolder = rvViewHolder as HeaderViewHolder
            val header = items[position - if (isSummaryViewNeeded) 1 else 0] as HeaderItem
            headerViewHolder.tvDate.text = header.date
        }
    }

    private fun getNegative(number: Double): Double {
        return -1 * number
    }

    fun setRecords(itemsList: List<RecordAdapterData>, report: IRecordReport?, currency: String, ratesNeeded: List<String>) {
        items = itemsList
        summaryPresenter.update(report, currency, ratesNeeded)
        notifyDataSetChanged()
    }

    class RecordViewHolder : RecyclerView.ViewHolder {

        var container: LinearLayout
        var tvPrice: TextView
        var tvTitle: TextView
        var tvCategory: TextView
        var tvCurrency: TextView

        constructor(view: View, itemClickListener: ((Int) -> Unit)?) : super(view) {
            container = view.container
            tvPrice = view.tvPrice
            tvTitle = view.tvTitle
            tvCategory = view.tvCategory
            tvCurrency = view.tvCurrency

            view.setOnClickListener {
                itemClickListener?.invoke(adapterPosition)
            }
        }
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.tvDate
    }

    class SummaryViewHolder : RecyclerView.ViewHolder, ShortSummaryPresenter.SummaryViewInterface {

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

            view.cvSummary.setOnClickListener {
                itemClickListener?.invoke(0)
            }
        }
    }

    companion object {
        private const val TYPE_SUMMARY = 0
        private const val TYPE_HEADER = 1
        private const val TYPE_RECORD = 2
    }
}
