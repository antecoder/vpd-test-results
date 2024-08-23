package com.joeydalu.vpd.view.dashboard.ui.accounts.data

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joeydalu.vpd.R
import com.joeydalu.vpd.data.model.Account

/**
 * A [RecyclerView.Adapter] that displays [Account]'s in a list
 *
 * @see Account
 * @see AccountViewHolder
 */
class AccountListAdapter(val onItemSelect: ((Account) -> Unit)?): RecyclerView.Adapter<AccountViewHolder>() {

    // List of items to display. Once set the list is changed.
    var items: List<Account>? = null
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_account_view, parent, false)
        return AccountViewHolder(view)
    }

    override fun getItemCount(): Int = items?.let {
        return it.count()
    } ?: run {
        return 0
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        // bind item to view
        items?.get(position)?.let { item ->
            holder.bind(item)
        }
        // handle clicks
        holder.binding.root.setOnClickListener {
            val holderPosition = holder.absoluteAdapterPosition
            items?.get(holder.absoluteAdapterPosition)?.let { account ->
                onItemSelect?.let { it1 -> it1(account) }
            }
        }
    }
}