package com.joeydalu.vpd.view.transfers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.joeydalu.vpd.data.database.AppDatabase
import com.joeydalu.vpd.data.model.Account
import com.joeydalu.vpd.data.model.Transaction
import com.joeydalu.vpd.util.mutable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.Callable
import javax.inject.Inject

/**
 * [ViewModel] handling the logic for transferring of funds from one [Account] to another
 */
@HiltViewModel
class TransferViewModel @Inject constructor(val database: AppDatabase): ViewModel() {

    var sourceAccount: Account? = null
    var destinationAccount: Account? = null
    var amount: Int = 0
    val events: LiveData<Event> = MutableLiveData<Event>()
    val errors: LiveData<ErrorEvent> = MutableLiveData<ErrorEvent>()

    /**
     * Sets the source [Account]
     * @param account The *required* source account
     */
    fun onSelectSourceAccount(account: Account) {
        this.sourceAccount = account
    }

    /**
     * Sets the destination [Account]
     * @param account The *required* destination account
     */
    fun onSelectDestinationAccount(account: Account) {
        this.destinationAccount = account
    }

    /**
     * Submits the amount to transfer
     */
    fun onSubmit(amount: String?) {

        // Check if accounts are assigned
        sourceAccount?.let {} ?: run {
            errors.mutable()?.postValue(ErrorEvent.errorSourceMissing)
            return
        }
        destinationAccount?.let {} ?: run {
            errors.mutable()?.postValue(ErrorEvent.errorDestinationMissing)
            return
        }

        // Check if same currency
        if (sourceAccount!!.currencyCode != destinationAccount!!.currencyCode) {
            errors.mutable()?.postValue(ErrorEvent.errorCurrencyMismatch)
            return
        }

        // Check if Source has money
        if (sourceAccount!!.balance == 0) {
            errors.mutable()?.postValue(ErrorEvent.errorSourceAmountZero)
            return
        }

        // Check if Amount is valid
        amount?.let {} ?: run {
            errors.mutable()?.postValue(ErrorEvent.errorAmountMissing)
            return
        }

        // Check if amount is greater than 1
        val amountInt = amount.toInt()
        if (amountInt < 1) {
            errors.mutable()?.postValue(ErrorEvent.errorAmountMissing)
            return
        }

        // Lastly check if the source has the amount while keep in mind our factor of currency is 100
        val finalAmount = amountInt
        if (finalAmount > sourceAccount!!.balance) {
            errors.mutable()?.postValue(ErrorEvent.errorAmountLess)
            return
        }

        // We're good to go, confirm transaction
        this.amount = finalAmount
        events.mutable()?.postValue(Event.navRequestConfirmation)
    }

    fun onTransactionConfirmed() {
        // capture values
        val id = UUID.randomUUID().toString()
        val reference = UUID.randomUUID().toString()
        val now = Timestamp.now()

        // perform calculation
        val newAmount = amount * 100
        val newSourceValue = sourceAccount!!.balance - newAmount
        val newDestValue = destinationAccount!!.balance + newAmount
        sourceAccount!!.balance = newSourceValue
        destinationAccount!!.balance = newDestValue

        val transaction = Transaction(id, newAmount, sourceAccount!!.id, sourceAccount!!.name, sourceAccount!!.accountNumber,
            destinationAccount!!.id, destinationAccount!!.name, destinationAccount!!.accountNumber, sourceAccount!!.currencyCode, now, now, reference)

        CoroutineScope(Dispatchers.IO).launch(CoroutineExceptionHandler { _, exception ->
            exception.printStackTrace()
            events.mutable()?.postValue(Event.transactionError)
        }) {

            // Run Operation in Transaction so that all writes pass or all fail
            events.mutable()?.postValue(Event.transactionInProgress)
            database.runInTransaction(Runnable {
                database.accountDao().updateAll(mutableListOf(sourceAccount!!, destinationAccount!!))
                database.transactionDao().insert(transaction)
            })
            events.mutable()?.postValue(Event.transactionSuccess)
        }
    }



    /**
     * Enum class representing events that occur during the transfer operation
     */
    enum class Event {
        navRequestConfirmation,
        transactionInProgress,
        transactionSuccess,
        transactionError
    }

    /**
     * Enum class representing various errors liable to occur during the transfer
     */
    enum class ErrorEvent {
        errorCurrencyMismatch,
        errorSourceAmountZero,
        errorSourceMissing,
        errorDestinationMissing,
        errorAmountMissing,
        errorAmountLess
    }

}