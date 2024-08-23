package com.joeydalu.vpd.view.dashboard.ui.transactions.data

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.joeydalu.vpd.data.model.Transaction
import com.joeydalu.vpd.databinding.ItemTransactionViewBinding
import java.text.NumberFormat
import java.util.Currency

class TransactionViewHolder(itemView: View): ViewHolder(itemView) {

    val binding = ItemTransactionViewBinding.bind(itemView)
    @SuppressLint("SetTextI18n")
    fun bind(transaction: Transaction) {
        binding.txtAccount1.text = "${transaction.sourceAccountName} - ${transaction.sourceAccountNumber}"
        binding.txtAccount2.text = "${transaction.destAccountName} - ${transaction.destAccountNumber}"

        // show currencies
        val currency = Currency.getInstance(transaction.currencyCode)
        val format = NumberFormat.getCurrencyInstance()
        format.currency = currency
        binding.txtBalance.text = format.format(transaction.amount / 100.0)
    }

}