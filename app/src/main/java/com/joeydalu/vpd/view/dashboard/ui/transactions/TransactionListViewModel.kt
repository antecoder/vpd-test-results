package com.joeydalu.vpd.view.dashboard.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.joeydalu.vpd.auth.Authenticator
import com.joeydalu.vpd.data.database.AppDatabase
import com.joeydalu.vpd.data.model.Account
import com.joeydalu.vpd.data.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * [ViewModel] implementation for displaying the list of [Account]'s on the device.
 *
 * @author Joseph Dalughut
 */
@HiltViewModel
class TransactionListViewModel @Inject constructor(val authenticator: Authenticator, val database: AppDatabase): ViewModel() {


    val transactions: LiveData<List<Transaction>> = database.transactionDao().liveList()

}