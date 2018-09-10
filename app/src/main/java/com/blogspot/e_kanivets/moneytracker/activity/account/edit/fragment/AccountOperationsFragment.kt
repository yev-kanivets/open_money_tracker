package com.blogspot.e_kanivets.moneytracker.activity.account.edit.fragment

import android.os.Bundle
import android.view.View
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseFragment
import com.blogspot.e_kanivets.moneytracker.adapter.RecordAdapter
import com.blogspot.e_kanivets.moneytracker.controller.data.RecordController
import com.blogspot.e_kanivets.moneytracker.controller.data.TransferController
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import com.blogspot.e_kanivets.moneytracker.entity.data.Category
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer
import kotlinx.android.synthetic.main.fragment_account_operations.*
import javax.inject.Inject

class AccountOperationsFragment : BaseFragment() {

    @Inject
    internal lateinit var recordController: RecordController
    @Inject
    internal lateinit var transferController: TransferController

    private lateinit var account: Account

    override val contentViewId: Int = R.layout.fragment_account_operations

    override fun initData() {
        appComponent.inject(this@AccountOperationsFragment)
        arguments?.let { arguments -> account = arguments.getParcelable(AccountOperationsFragment.KEY_ACCOUNT) }
    }

    override fun initViews(view: View) {
        listView.adapter = RecordAdapter(activity, getRecords())
    }

    private fun getRecords(): List<Record> {
        val accountRecords = recordController.getRecordsForAccount(account)
        val accountTransfers = transferController.getTransfersForAccount(account)

        accountRecords += calculateRecordsFromTransfers(accountTransfers)
        accountRecords.sortByDescending { it.time }

        return accountRecords
    }

    private fun calculateRecordsFromTransfers(transfers: List<Transfer>): List<Record> {
        val records = mutableListOf<Record>()

        transfers.forEach {
            val type = if (it.fromAccountId == account.id) Record.TYPE_EXPENSE else Record.TYPE_INCOME
            val title = "Transfer ${if (type == Record.TYPE_EXPENSE) "to" else "from"}"
            val category = Category("transfer")
            val price = if (type == Record.TYPE_EXPENSE) it.fromAmount else it.toAmount
            val decimals = if (type == Record.TYPE_EXPENSE) it.fromDecimals else it.toDecimals

            records += Record(it.id, it.time, type, title, category, price, account, account.currency, decimals)
        }

        return records.toList()
    }

    companion object {

        private const val KEY_ACCOUNT = "key_account"

        fun newInstance(account: Account): AccountOperationsFragment {
            val fragment = AccountOperationsFragment()
            val arguments = Bundle()
            arguments.putParcelable(KEY_ACCOUNT, account)
            fragment.arguments = arguments
            return fragment
        }

    }
}
