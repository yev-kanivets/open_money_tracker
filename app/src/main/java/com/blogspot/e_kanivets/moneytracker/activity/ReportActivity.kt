package com.blogspot.e_kanivets.moneytracker.activity

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity
import com.blogspot.e_kanivets.moneytracker.adapter.RecordReportAdapter
import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController
import com.blogspot.e_kanivets.moneytracker.controller.FormatController
import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController
import com.blogspot.e_kanivets.moneytracker.controller.data.RecordController
import com.blogspot.e_kanivets.moneytracker.entity.Period
import com.blogspot.e_kanivets.moneytracker.entity.RecordReportItem
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import com.blogspot.e_kanivets.moneytracker.report.ReportMaker
import com.blogspot.e_kanivets.moneytracker.ui.presenter.ShortSummaryPresenter
import kotlinx.android.synthetic.main.activity_report.*
import java.util.*
import javax.inject.Inject

class ReportActivity : BaseBackActivity() {

    @Inject
    lateinit var recordController: RecordController
    @Inject
    lateinit var rateController: ExchangeRateController
    @Inject
    lateinit var currencyController: CurrencyController
    @Inject
    lateinit var formatController: FormatController

    private var recordList: List<Record> = listOf()
    private var period: Period? = null
    private lateinit var adapter: RecordReportAdapter

    private lateinit var shortSummaryPresenter: ShortSummaryPresenter

    override fun getContentViewId() = R.layout.activity_report

    override fun initData(): Boolean {
        super.initData()
        appComponent.inject(this)

        period = intent.getParcelableExtra(KEY_PERIOD)
        if (period == null) return false

        recordList = recordController.getRecordsForPeriod(period)
        shortSummaryPresenter = ShortSummaryPresenter(this)
        adapter = RecordReportAdapter(mutableListOf(), hashMapOf(), this)

        return true
    }

    override fun initViews() {
        super.initViews()

        initSpinnerCurrency()

        adapter.addSummaryView(shortSummaryPresenter.create(false, null))
        recyclerView.adapter = adapter
    }

    private fun update(currency: String) {
        val reportMaker = ReportMaker(rateController)
        val report = reportMaker.getRecordReport(currency, period, recordList)

        val data: HashMap<RecordReportItem.ParentRow, List<RecordReportItem.ChildRow>> = hashMapOf()
        val items: MutableList<RecordReportItem> = mutableListOf()

        if (report != null) {
            for (categoryRecord in report.summary) {

                val parentRow = RecordReportItem.ParentRow(categoryRecord.title, formatController.formatSignedAmount(categoryRecord.amount), false)
                items.add(parentRow)

                val childRows: MutableList<RecordReportItem.ChildRow> = mutableListOf()

                for (summaryRecord in categoryRecord.summaryRecordList) {
                    val childRow = RecordReportItem.ChildRow(summaryRecord.title, formatController.formatSignedAmount(summaryRecord.amount))
                    childRows.add(childRow)
                }

                data[parentRow] = childRows
            }
        }

        adapter.setData(items, data)
        shortSummaryPresenter.update(report, currency, reportMaker.currencyNeeded(currency, recordList))
    }

    private fun initSpinnerCurrency() {
        val currencyList = currencyController.readAll()

        spinnerCurrency.adapter = ArrayAdapter(this, R.layout.view_spinner_item, currencyList)
        spinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) =
                    update(spinnerCurrency.selectedItem.toString())

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        val currency = currencyController.readDefaultCurrency()

        spinnerCurrency.setSelection(currencyList.indexOf(currency))
    }

    companion object {

        const val KEY_PERIOD = "key_period"
    }
}
