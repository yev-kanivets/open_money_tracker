package com.blogspot.e_kanivets.moneytracker.entity

sealed class RecordReportItem {

    data class ChildRow(val title: String, val amount: String) : RecordReportItem()

    data class ParentRow(val category: String, val totalAmount: String, var isOpen: Boolean) : RecordReportItem()

}