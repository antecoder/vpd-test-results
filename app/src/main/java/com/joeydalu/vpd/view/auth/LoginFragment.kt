package com.joeydalu.vpd.view.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.joeydalu.vpd.R
import com.joeydalu.vpd.databinding.FragmentLoginBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.joeydalu.vpd.util.hideKeyboard
import com.joeydalu.vpd.util.showSnackbar
import com.joeydalu.vpd.view.accounts.AccountListActivity
import dagger.hilt.android.AndroidEntryPoint


/**
 * [Fragment] handling the UI of the login process.
 *
 * @author Joseph Dalughut
 */
@AndroidEntryPoint
class LoginFragment : BaseAuthFragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment []LoginFragment]
         */
        @JvmStatic
        fun newInstance() = LoginFragment()
    }

    lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachActions()
        observeModel()
    }

    private fun attachActions() {
        val authViewModel = getViewModel()
        binding.btnClose.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnLogin.setOnClickListener {
            authViewModel?.onSubmitLoginDetails(binding.edtEmailAddress.text.toString().trim(), binding.edtPassword.text.toString().trim())
        }

    }

    private fun observeModel() {
        val authViewModel = getViewModel();

        //observe and handle ui-events
        authViewModel?.events?.observe(viewLifecycleOwner) { event ->
            when (event) {
                AuthViewModel.Event.navBack -> requireActivity().onBackPressedDispatcher.onBackPressed()
                AuthViewModel.Event.navRegistrationNameEmail -> getAuthActivity()?.addRegistrationEmailFragment()
                AuthViewModel.Event.navRegistrationPassword -> getAuthActivity()?.addRegistrationPasswordFragment(
                    authViewModel.registrationName,
                    authViewModel.registrationEmail)
                AuthViewModel.Event.loginBegan -> showWorkingIndicator(true)
                AuthViewModel.Event.loginError -> {
                    hideKeyboard()
                    showWorkingIndicator(false)
                    showSnackbar(R.string.error_tasks_unsuccessful)
                }
                AuthViewModel.Event.loginCanceled -> showWorkingIndicator(false)
                AuthViewModel.Event.loginSuccess -> showWorkingIndicator(false)
                AuthViewModel.Event.navDashboard -> navigateToDashboard()
                else -> {
                    // OTHER Events
                }
            }
        }

        // observe and handle errors
        authViewModel?.errorState?.observe(viewLifecycleOwner) { state ->
            when (state) {
                AuthViewModel.ErrorState.emailEmpty -> {
                    showSnackbar(R.string.error_email_empty)
                }
                AuthViewModel.ErrorState.emailInvalid -> {
                    showSnackbar(R.string.error_email_invalid)
                }
                AuthViewModel.ErrorState.passwordEmpty -> {
                    showSnackbar(R.string.error_password_empty)
                }
                AuthViewModel.ErrorState.passwordShort -> {
                    showSnackbar(R.string.error_password_short)
                }
                AuthViewModel.ErrorState.passwordInvalid -> {
                    showSnackbar(R.string.error_email_empty)
                }
                else -> {
                }
            }
        }

    }

    private fun navigateToDashboard() {
        val intent = Intent(requireActivity(), AccountListActivity::class.java)
        startActivity(intent)
        getAuthActivity()?.finish()
    }

    private fun showWorkingIndicator(isWorking: Boolean) {
        if (isWorking) {

            // Disable interaction
            binding.edtEmailAddress.isEnabled = false
            binding.edtPassword.isEnabled = false
            binding.btnLogin.isEnabled = false
            binding.btnForgotPassword.isEnabled = false
            binding.btnClose.isEnabled = false

            binding.btnLogin.showProgress {
                buttonTextRes = R.string.working
                progressColorRes = R.color.white
            }
        } else {

            // Enable interaction
            binding.edtEmailAddress.isEnabled = true
            binding.edtPassword.isEnabled = true
            binding.btnLogin.isEnabled = true
            binding.btnForgotPassword.isEnabled = true
            binding.btnClose.isEnabled = true

            binding.btnLogin.hideProgress(R.string.login)
        }
    }


}