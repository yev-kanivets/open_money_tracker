package com.blogspot.e_kanivets.moneytracker.activity.account.edit.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.blogspot.e_kanivets.moneytracker.activity.account.edit.fragment.EditAccountFragment
import com.blogspot.e_kanivets.moneytracker.entity.data.Account

class EditAccountFragmentPagerAdapter(fragmentManager: FragmentManager, private val account: Account) :
    FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> EditAccountFragment.newInstance(account)
            else -> Fragment()
        }
    }

    override fun getCount(): Int = 1

}
