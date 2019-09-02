package com.blogspot.e_kanivets.moneytracker.entity

sealed class RecordReportItem {

    data class ChildRow(val title: String, val amount: Long) : RecordReportItem()

    data class ParentRow(val category: String, val totalAmount: Long, val isOpen: Boolean) : RecordReportItem()

}