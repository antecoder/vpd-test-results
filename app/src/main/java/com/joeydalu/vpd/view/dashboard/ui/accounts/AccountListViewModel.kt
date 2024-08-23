package com.joeydalu.vpd.view.dashboard.ui.accounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.joeydalu.vpd.auth.Authenticator
import com.joeydalu.vpd.data.database.AppDatabase
import com.joeydalu.vpd.data.model.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * [ViewModel] implementation for displaying the list of [Account]'s on the device.
 *
 * @author Joseph Dalughut
 */
@HiltViewModel
class AccountListViewModel @Inject constructor(val authenticator: Authenticator, val database: AppDatabase): ViewModel() {


    val accounts: LiveData<List<Account>> = database.accountDao().liveList()

}