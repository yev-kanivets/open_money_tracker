package com.blogspot.e_kanivets.moneytracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.blogspot.e_kanivets.moneytracker.R
import kotlinx.android.synthetic.main.view_backup_item.view.tvTitle

class BackupAdapter(private val context: Context, private val backups: List<String>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val viewHolder: ViewHolder?

        if (view == null) {
            val layoutInflater = LayoutInflater.from(context)

            view = layoutInflater.inflate(R.layout.view_backup_item, parent, false)
            viewHolder = ViewHolder(view)

            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.view.tvTitle.text = getItem(position)

        return view!!
    }

    override fun getItem(position: Int): String = backups[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = backups.size

    private data class ViewHolder(val view: View)

}
