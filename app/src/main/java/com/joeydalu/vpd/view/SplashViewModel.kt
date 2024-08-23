package com.joeydalu.vpd.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joeydalu.vpd.util.mutable
import com.joeydalu.vpd.auth.Authenticator
import com.joeydalu.vpd.data.database.AppDatabase
import com.joeydalu.vpd.data.model.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ViewModel] class for handlng checking the auth-state at the start of the application
 */
@HiltViewModel
class SplashViewModel @Inject constructor(val authenticator: Authenticator, val database: AppDatabase):
    ViewModel() {

    val authState: LiveData<AuthState> = MutableLiveData(AuthState.loading)

    init {
        viewModelScope.launch {
            checkUserLogin()
        }
    }

    private fun checkUserLogin() {
        // if we have a user, move to dashboard
        authenticator.getUser()?.let {
            viewModelScope.launch {
                // seed the database
                Account.Companion.Factory().seed(database)
                authState.mutable()?.postValue(AuthState.loggedIn)
            }
        } ?: run {
            authState.mutable()?.postValue(AuthState.loggedOut)
        }
    }

    /**
     * Enum-type representing the state of authentication.
     */
    enum class AuthState {
                         loading,
                         loggedIn,
        loggedOut,
    }

}