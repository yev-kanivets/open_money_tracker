package com.blogspot.e_kanivets.moneytracker.activity.account.edit.fragment

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.View
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.R.layout
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseFragment
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import kotlinx.android.synthetic.main.fragment_edit_account.*
import javax.inject.Inject

class EditAccountFragment : BaseFragment() {

    @Inject
    internal lateinit var accountController: AccountController

    private lateinit var account: Account

    override val contentViewId: Int = layout.fragment_edit_account

    override fun initData() {
        appComponent.inject(this@EditAccountFragment)
        arguments?.let { arguments -> account = arguments.getParcelable(KEY_ACCOUNT) }
    }

    override fun initViews(view: View) {
        etTitle.setText(account.title)
        etGoal.setText(account.goal.toString())
        viewColor.setBackgroundColor(account.color)

        val fabDone = view.rootView.findViewById<FloatingActionButton>(R.id.fabDone)
        fabDone.setOnClickListener { done() }
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
            val updated = accountController.update(newAccount) != null
            if (updated) {
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            }
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
