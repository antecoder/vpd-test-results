package com.joeydalu.vpd.view.transfers

import android.content.Context
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.joeydalu.vpd.R
import com.joeydalu.vpd.data.model.Account
import com.joeydalu.vpd.data.model.Transaction
import com.joeydalu.vpd.databinding.DialogAccountPickerBinding
import com.joeydalu.vpd.databinding.DialogConfirmTransactionBinding
import com.joeydalu.vpd.view.dashboard.ui.accounts.data.AccountListAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Currency


/**
 * A [BottomSheetDialog] which asks the user to confirm a [Transaction]
 *
 * @param context a [Context]
 * @param sourceAccount The source account of the transaction.
 * @param destinationAccount The destination account of the transaction.
 * @param amount The amount to transfer.
 * @param onConfirmTransaction Called if the user confirms the transaction
 *
 * @author Joseph Dalughut
 */
class TransferConfirmationDialog(context: Context,
                                 sourceAccount: Account,
                                 destinationAccount: Account,
                                 amount: Int,
                                 onConfirmTransaction: () -> Unit): BottomSheetDialog(context) {

    private var binding: DialogConfirmTransactionBinding
    init {
        binding = DialogConfirmTransactionBinding.bind(View.inflate(context, R.layout.dialog_confirm_transaction, null))
        setContentView(binding.root)

        // Fetch currency, we assume this would never be called with accounts of different currency
        val currency = Currency.getInstance(sourceAccount.currencyCode)
        val format = NumberFormat.getCurrencyInstance()
        format.currency = currency
        val amountText = format.format(amount)

        // Add text parameters
        val string = context.getString(R.string.confirm_transfer_message,
            amountText,
            "${sourceAccount.name} - ${sourceAccount.accountNumber}",
            "${destinationAccount.name} - ${destinationAccount.accountNumber}")
        binding.txtConfirmation.text = string

        binding.btnConfirm.setOnClickListener {
            onConfirmTransaction()
            dismiss()
        }
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

}