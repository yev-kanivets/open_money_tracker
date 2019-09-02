package com.blogspot.e_kanivets.moneytracker.activity

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity
import com.blogspot.e_kanivets.moneytracker.adapter.ExpandableListReportAdapter
import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController
import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController
import com.blogspot.e_kanivets.moneytracker.controller.data.RecordController
import com.blogspot.e_kanivets.moneytracker.entity.Period
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import com.blogspot.e_kanivets.moneytracker.report.ReportMaker
import com.blogspot.e_kanivets.moneytracker.report.record.RecordReportConverter
import com.blogspot.e_kanivets.moneytracker.ui.presenter.ShortSummaryPresenter
import kotlinx.android.synthetic.main.activity_report.*
import java.util.ArrayList
import java.util.HashMap
import javax.inject.Inject

class ReportActivity : BaseBackActivity() {

    @Inject
    lateinit var recordController: RecordController
    @Inject
    lateinit var rateController: ExchangeRateController
    @Inject
    lateinit var currencyController: CurrencyController

    private var recordList: List<Record> = listOf()
    private var period: Period? = null

    private lateinit var shortSummaryPresenter: ShortSummaryPresenter

    override fun getContentViewId() = R.layout.activity_report

    override fun initData(): Boolean {
        super.initData()
        appComponent.inject(this)

        period = intent.getParcelableExtra(KEY_PERIOD)
        if (period == null) return false

        recordList = recordController.getRecordsForPeriod(period)
        shortSummaryPresenter = ShortSummaryPresenter(this)

        return true
    }

    override fun initViews() {
        super.initViews()

        initSpinnerCurrency()

        expListView.addHeaderView(shortSummaryPresenter.create(false, null))
    }

    private fun update(currency: String) {
        val reportMaker = ReportMaker(rateController)
        val report = reportMaker.getRecordReport(currency, period, recordList)

        var adapter: ExpandableListReportAdapter? = null

        //val childData = ArrayList<List<Map<String, String>>>()

        if (report != null) {
            /*for (categoryRecord in report.summary) {
                val childDataItem = ArrayList<Map<String, String>>()
                for (summaryRecord in categoryRecord.summaryRecordList) {
                    val m = HashMap<String, String>()
                    //m[TITLE_PARAM_NAME] = summaryRecord.title
                    //m[PRICE_PARAM_NAME] = java.lang.Double.toString(summaryRecord.amount)

                    childDataItem.add(m)
                }

                childData.add(childDataItem)
            }*/

            val recordReportConverter = RecordReportConverter(report)
            adapter = ExpandableListReportAdapter(this, recordReportConverter)
        }

        expListView.setAdapter(adapter)
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
