package com.joeydalu.vpd.view.transfers

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.joeydalu.vpd.R
import com.joeydalu.vpd.data.model.Account
import com.joeydalu.vpd.databinding.ActivityTransferBinding
import com.joeydalu.vpd.util.hideKeyboard
import com.joeydalu.vpd.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransferActivity : AppCompatActivity() {

    lateinit var binding: ActivityTransferBinding
    private val viewModel: TransferViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        attachActions()
        observeModel()
    }

    @SuppressLint("SetTextI18n")
    private fun attachActions() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.btnSourceAccount.setOnClickListener {
            hideKeyboard()
            selectAccount { account ->
                binding.btnSourceAccount.text = "${account.name} - ${account.accountNumber}"
                viewModel.onSelectSourceAccount(account)
            }
        }
        binding.btnDestAccount.setOnClickListener {
            hideKeyboard()
            selectAccount { account ->
                binding.btnDestAccount.text = "${account.name} - ${account.accountNumber}"
                viewModel.onSelectDestinationAccount(account)
            }
        }
        binding.btnContine.setOnClickListener {
            hideKeyboard()
            viewModel.onSubmit(binding.edtAmount.text.toString())
        }
    }

    private fun observeModel() {
        viewModel.events.observe(this) { event ->
            when (event) {
                TransferViewModel.Event.navRequestConfirmation -> showConfirmationDialog()
                TransferViewModel.Event.transactionInProgress -> showWorkingIndicator(true)
                TransferViewModel.Event.transactionSuccess -> {
                    showWorkingIndicator(false)
                    showSuccessDialog()
                }
                TransferViewModel.Event.transactionError -> {
                    showWorkingIndicator(false)
                    showSnackbar(R.string.error_tasks_unsuccessful, binding.root)
                }
            }
        }
        viewModel.errors.observe(this) { error ->
            when (error) {
                TransferViewModel.ErrorEvent.errorCurrencyMismatch -> showSnackbar(R.string.transfer_currency_mismatch, binding.root)
                TransferViewModel.ErrorEvent.errorSourceAmountZero -> showSnackbar(R.string.transfer_source_zero, binding.root)
                TransferViewModel.ErrorEvent.errorSourceMissing -> showSnackbar(R.string.transfer_source_missing, binding.root)
                TransferViewModel.ErrorEvent.errorDestinationMissing -> showSnackbar(R.string.transfer_destination_missing, binding.root)
                TransferViewModel.ErrorEvent.errorAmountMissing -> showSnackbar(R.string.transfer_amount_missing, binding.root)
                TransferViewModel.ErrorEvent.errorAmountLess -> showSnackbar(R.string.transfer_amount_less, binding.root)
                TransferViewModel.ErrorEvent.errorSameUser -> showSnackbar(R.string.transfer_same_destination, binding.root)
            }
        }
    }

    private fun selectAccount(consumer: (Account) -> Unit) {
        TransferAccountPickerDialog(this, viewModel.database, consumer).show()
    }

    private fun showConfirmationDialog() {
        TransferConfirmationDialog(this, viewModel.sourceAccount!!, viewModel.destinationAccount!!, viewModel.amount){
            viewModel.onTransactionConfirmed()
        }.show()
    }

    private fun showSuccessDialog() {
        TransferSuccessDialog(this, viewModel.sourceAccount!!, viewModel.destinationAccount!!, viewModel.amount){
            onBackPressedDispatcher.onBackPressed()
        }.show()
    }

    private fun showWorkingIndicator(isWorking: Boolean) {
        if (isWorking) {

            // Disable interaction
            binding.txtAmount.isEnabled = false
            binding.btnSourceAccount.isEnabled = false
            binding.btnDestAccount.isEnabled = false
            binding.btnContine.isEnabled = false

            binding.btnContine.showProgress {
                buttonTextRes = R.string.working
                progressColorRes = R.color.white
            }

        } else {

            // Enable interaction
            binding.txtAmount.isEnabled = true
            binding.btnSourceAccount.isEnabled = true
            binding.btnDestAccount.isEnabled = true
            binding.btnContine.isEnabled = true

            binding.btnContine.hideProgress(R.string.continue_)
        }
    }

}