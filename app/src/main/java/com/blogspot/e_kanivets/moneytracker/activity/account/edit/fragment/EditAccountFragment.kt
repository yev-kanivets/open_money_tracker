package com.blogspot.e_kanivets.moneytracker.activity.account.edit.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import kotlinx.android.synthetic.main.fragment_edit_account.*

class EditAccountFragment() : Fragment() {

    private lateinit var account: Account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { arguments -> account = arguments.getParcelable(KEY_ACCOUNT) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_edit_account, container, false)
        initViews()
        return rootView
    }

    fun initViews() {
        etTitle.setText(account.title)
        etGoal.setText(account.goal.toString())
        viewColor.setBackgroundColor(account.color)
    }

    private fun done() {
        val title = etTitle.text.toString().trim { it <= ' ' }

        if (title.isEmpty()) {
            tilTitle.error = getString(R.string.field_cant_be_empty)
        } else {
            val newAccount = Account(
                account.id, title, account.curSum.toDouble(),
                account.currency, account.goal, account.isArchived, account.color
            )
        }
    }

    companion object {

        private const val KEY_ACCOUNT = "key_account"

        fun newInstance(account: Account): EditAccountFragment {
            val fragment = EditAccountFragment()
            val arguments = Bundle()
            arguments.putParcelable(KEY_ACCOUNT, account)
            fragment.arguments = arguments
            return fragment
        }

    }

}
