package com.joeydalu.vpd.view.transfers

import android.content.Context
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.joeydalu.vpd.R
import com.joeydalu.vpd.data.database.AppDatabase
import com.joeydalu.vpd.data.model.Account
import com.joeydalu.vpd.databinding.DialogAccountPickerBinding
import com.joeydalu.vpd.view.dashboard.ui.accounts.data.AccountListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * A [BottomSheetDialog] which displays a list of provided accounts.
 *
 * @param context a [Context]
 * @param accounts a list of [Account]'s to display
 * @param onSelectAccount receives the selected [Account] as callback
 *
 * @author Joseph Dalughut
 */
class TransferAccountPickerDialog(context: Context, val database: AppDatabase, onSelectAccount: (Account) -> Unit): BottomSheetDialog(context) {

    private var binding: DialogAccountPickerBinding
    private var adapter: AccountListAdapter

    init {
        binding = DialogAccountPickerBinding.bind(View.inflate(context, R.layout.dialog_account_picker, null))
        setContentView(binding.root)

        adapter = AccountListAdapter {
            onSelectAccount(it)
            dismiss()
        }
        binding.recycler.adapter = adapter
        binding.btnClose.setOnClickListener {
            dismiss()
        }
        loadAccounts()
    }

    private fun loadAccounts() {
        lifecycleScope.launch {
            val accounts = database.accountDao().list()
            showAccounts(accounts)
        }
    }

    private fun showAccounts(accounts: List<Account>) {
        adapter.items = accounts
    }

}