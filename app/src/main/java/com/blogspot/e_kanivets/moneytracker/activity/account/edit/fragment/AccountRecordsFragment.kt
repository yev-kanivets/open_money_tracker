package com.blogspot.e_kanivets.moneytracker.activity.account.edit.fragment

import android.os.Bundle
import android.view.View
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseFragment
import com.blogspot.e_kanivets.moneytracker.adapter.RecordAdapter
import com.blogspot.e_kanivets.moneytracker.controller.data.RecordController
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import kotlinx.android.synthetic.main.fragment_account_records.*
import javax.inject.Inject

class AccountRecordsFragment : BaseFragment() {

    @Inject
    internal lateinit var recordController: RecordController

    private lateinit var account: Account

    override val contentViewId: Int = R.layout.fragment_account_records

    override fun initData() {
        appComponent.inject(this@AccountRecordsFragment)
        arguments?.let { arguments -> account = arguments.getParcelable(AccountRecordsFragment.KEY_ACCOUNT) }
    }

    override fun initViews(view: View) {
        listView.adapter = RecordAdapter(activity, recordController.getRecordsForAccount(account))
    }

    companion object {

        private const val KEY_ACCOUNT = "key_account"

        fun newInstance(account: Account): AccountRecordsFragment {
            val fragment = AccountRecordsFragment()
            val arguments = Bundle()
            arguments.putParcelable(KEY_ACCOUNT, account)
            fragment.arguments = arguments
            return fragment
        }

    }
}
