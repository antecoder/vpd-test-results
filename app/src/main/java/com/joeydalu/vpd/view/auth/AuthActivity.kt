package com.joeydalu.vpd.view.auth

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.joeydalu.vpd.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity entry-point for authentication.
 *
 * This class handles the authentication flow using a [Fragment] for login and two for Signup.
 * @see LoginFragment
 * @see RegistrationEmailFragment
 * @see RegistrationPasswordFragment
 *
 * @author Joseph Dalughut
 */
@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    companion object {

        // We'll pass *true* if we want to start login and *false* for registration
        const val ARG_LOGIN_MODE = "LOGIN_MODE"

    }

    val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val loginMode = intent.extras?.getBoolean(ARG_LOGIN_MODE, false)

        if (loginMode == true) {
            addLoginFragment()
        } else {
            addRegistrationEmailFragment()
        }
    }

    fun addLoginFragment() {
        val fragment = LoginFragment.newInstance()
        addFragment(fragment, replace = true, addToBackstack = false)
    }

    fun addRegistrationEmailFragment() {
        val fragment = RegistrationEmailFragment.newInstance()
        addFragment(fragment, replace = true, addToBackstack = false)
    }

    fun addRegistrationPasswordFragment(name: String?, email: String?) {
        val fragment = RegistrationPasswordFragment.newInstance(name, email)
        addFragment(fragment, replace = false, addToBackstack = true)
    }

    private fun addFragment(fragment: Fragment, replace: Boolean = false, addToBackstack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
        if (replace) {
            transaction.replace(R.id.vwFragmentContainer, fragment, null)
        } else {
            transaction.add(R.id.vwFragmentContainer, fragment, null)
        }
        if (addToBackstack) transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()
    }

}