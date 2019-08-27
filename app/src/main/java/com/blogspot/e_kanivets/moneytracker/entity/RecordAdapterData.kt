package com.blogspot.e_kanivets.moneytracker.entity

import com.blogspot.e_kanivets.moneytracker.entity.data.Record

sealed class RecordAdapterData

data class HeaderItem(val date: String) : RecordAdapterData()

data class RecordItem(val title: String, val categoryName: String, val fullPrice: Double, val currency: String, val isIncome: Boolean) : RecordAdapterData() {
    constructor(record: Record) : this(record.title, record.category?.name?.toString().orEmpty(), record.fullPrice, record.currency, record.isIncome)
}