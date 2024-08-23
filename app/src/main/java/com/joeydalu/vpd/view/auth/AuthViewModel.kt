package com.joeydalu.vpd.view.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.joeydalu.vpd.domain.usecase.auth.LoginRequest
import com.joeydalu.vpd.domain.usecase.auth.LoginUser
import com.joeydalu.vpd.domain.usecase.auth.ResetPassword
import com.joeydalu.vpd.domain.usecase.auth.SignupRequest
import com.joeydalu.vpd.domain.usecase.auth.SignupUser
import com.joeydalu.vpd.util.isValidEmail
import com.joeydalu.vpd.util.mutable
import com.joeydalu.vpd.auth.Authenticator
import com.joeydalu.vpd.data.database.AppDatabase
import com.joeydalu.vpd.data.model.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ViewModel] handling authentication-UI-related data.
 *
 * @author Joseph Dalughut
 */
@HiltViewModel
class AuthViewModel @Inject constructor(val authenticator: Authenticator, val database: AppDatabase): ViewModel() {

    val errorState: LiveData<ErrorState> = MutableLiveData<ErrorState>()
    val events: LiveData<Event> = MutableLiveData<Event>()

    var registrationName: String? = null
    var registrationEmail: String? = null

    /**
     * Submits the users details for authentication.
     *
     * @param email: The users email address
     * @param password: The users password
     */
    fun onSubmitLoginDetails(email: String?, password: String?) {
        email?.let {
            if (it.isEmpty()) {
                errorState.mutable()?.postValue(ErrorState.emailEmpty)
                return
            }
            if (!it.isValidEmail()) {
                errorState.mutable()?.postValue(ErrorState.emailInvalid)
                return
            }
        } ?: run {
            errorState.mutable()?.postValue(ErrorState.emailEmpty)
            return
        }
        password?.let {
            if (it.isEmpty()) {
                errorState.mutable()?.postValue(ErrorState.passwordEmpty)
                return
            }
            if (it.length < 6) {
                errorState.mutable()?.postValue(ErrorState.passwordShort)
                return
            }
        } ?: run {
            errorState.mutable()?.postValue(ErrorState.passwordEmpty)
            return
        }
        beginLogin(email, password)
    }

    /**
     * Submits the users details to begin registration.
     *
     * @param name: The users full-name
     * @param email: The users email address.
     */
    fun onSubmitRegistrationNameEmail(name: String?, email: String?) {
        name?.let {
            if (it.isEmpty()) {
                errorState.mutable()?.postValue(ErrorState.nameEmpty)
                return
            }
            if (it.length < 3) {
                errorState.mutable()?.postValue(ErrorState.nameShort)
                return
            }
            this.registrationName = it
        } ?: run {
            errorState.mutable()?.postValue(ErrorState.nameEmpty)
            return
        }
        email?.let {
            if (it.isEmpty()) {
                errorState.mutable()?.postValue(ErrorState.emailEmpty)
                return
            }
            if (!it.isValidEmail()) {
                errorState.mutable()?.postValue(ErrorState.emailInvalid)
                return
            }
            this.registrationEmail = it
        } ?: run {
            errorState.mutable()?.postValue(ErrorState.emailEmpty)
            return
        }

        events.mutable()?.postValue(Event.navRegistrationPassword)
    }

    /**
     * Submits the users details for registration
     *
     * @param password: The users selected password
     * @param confirmPassword: The users password confirmation
     */
    fun onSubmitRegistrationPasswords(password: String?, confirmPassword: String?) {
        password?.let {  pit ->
            if (pit.isEmpty()) {
                errorState.mutable()?.postValue(ErrorState.passwordEmpty)
                return
            }
            if (pit.length < 6) {
                errorState.mutable()?.postValue(ErrorState.passwordShort)
                return
            }
            confirmPassword?.let { cit ->
                if (cit.isEmpty()) {
                    errorState.mutable()?.postValue(ErrorState.passwordConfirmEmpty)
                    return
                }
                if (pit != cit) {
                    errorState.mutable()?.postValue(ErrorState.passwordConfirmInvalid)
                    return
                }
            }?: run {
                errorState.mutable()?.postValue(ErrorState.passwordConfirmEmpty)
                return
            }
        } ?: run {
            errorState.mutable()?.postValue(ErrorState.nameEmpty)
            return
        }
        beginRegistration(registrationName!!, registrationEmail!!, password)
    }


    private fun beginLogin(email: String, password: String) {
        events.mutable()?.postValue(Event.loginBegan)
        val loginRequest = LoginRequest(email, password)
        LoginUser().execute(loginRequest, {
            viewModelScope.launch {
                events.mutable()?.postValue(Event.loginSuccess)
                // seed the db
                seedDatabase()
                events.mutable()?.postValue(Event.navDashboard)
            }
        }, {
            it.printStackTrace()
            // TODO: Handle error codes properly
            events.mutable()?.postValue(Event.loginError)
        })
    }

    private fun beginRegistration(name: String, email: String, password: String) {
        events.mutable()?.postValue(Event.registrationBegan)

        val signupRequest = SignupRequest(name, email, password)
        SignupUser().execute(signupRequest, {
            viewModelScope.launch {
                events.mutable()?.postValue(Event.registrationSuccess)
                // seed the db
                seedDatabase()
                events.mutable()?.postValue(Event.navDashboard)
            }
        }, {
            // TODO: Handle error codes properly
            it.printStackTrace()
            events.mutable()?.postValue(Event.registrationError)
        })

    }

    private suspend fun seedDatabase() {
        Account.Companion.Factory().seed(database)
    }

    /**
     * Enum class representing the various error-states
     */
    enum class ErrorState {
        emailEmpty,
        emailInvalid,
        passwordEmpty,
        passwordShort,
        passwordInvalid,
        nameEmpty,
        nameShort,
        passwordConfirmEmpty,
        passwordConfirmInvalid
    }

    /**
     * Enum class triggering ui-events.
     */
    enum class Event {
        navBack,
        navRegistrationNameEmail,
        navRegistrationPassword,
        loginBegan,
        loginSuccess,
        loginError,
        loginCanceled,
        navDashboard,
        registrationBegan,
        registrationSuccess,
        registrationError
    }

}