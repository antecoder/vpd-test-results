package com.joeydalu.vpd.view.dashboard.ui.accounts.data

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.joeydalu.vpd.data.model.Account
import com.joeydalu.vpd.databinding.ItemAccountViewBinding
import java.text.NumberFormat
import java.util.Currency

class AccountViewHolder(itemView: View): ViewHolder(itemView) {

    val binding = ItemAccountViewBinding.bind(itemView)
    @SuppressLint("SetTextI18n")
    fun bind(account: Account) {
        binding.txtTitle.text = "${account.name} - ${account.accountNumber}"

        // show currencies
        val currency = Currency.getInstance(account.currencyCode)
        val format = NumberFormat.getCurrencyInstance()
        format.currency = currency
        binding.txtBalance.text = format.format(account.balance / 100.0)
    }

}