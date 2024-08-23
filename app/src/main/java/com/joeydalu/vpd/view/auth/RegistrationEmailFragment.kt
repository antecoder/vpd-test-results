package com.joeydalu.vpd.view.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import com.joeydalu.vpd.R
import com.joeydalu.vpd.databinding.FragmentRegistrationEmailBinding
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


/**
 * [Fragment] which collects the users name and email
 *
 * Use the [RegistrationEmailFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 * @author Joseph Dalughut
 */
class RegistrationEmailFragment : BaseAuthFragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment RegistrationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = RegistrationEmailFragment()
    }

    lateinit var binding: FragmentRegistrationEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRegistrationEmailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachActions()
        observeModel()
    }

    private fun attachActions() {
        binding.btnClose.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnNext.setOnClickListener {
            submitSignupDetails()
        }
        binding.edtEmailAddress.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submitSignupDetails()
                true
            } else false
        }
    }

    private fun submitSignupDetails() {
        hideKeyboard()
        val authViewModel = getViewModel()
        authViewModel?.onSubmitRegistrationNameEmail(binding.edtFullName.text.toString().trim(), binding.edtEmailAddress.text.toString().trim())
    }

    private fun observeModel() {
        val authViewModel = getViewModel();

        // observe and handle events
        authViewModel?.events?.observe(viewLifecycleOwner) { event ->
            when (event) {
                AuthViewModel.Event.navBack -> requireActivity().onBackPressedDispatcher.onBackPressed()
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
                }
            }
        }

        // observe and handle errors
        authViewModel?.errorState?.observe(viewLifecycleOwner) { state ->
            when (state) {
                AuthViewModel.ErrorState.nameEmpty -> {
                    hideKeyboard()
                    showSnackbar(R.string.error_name_empty)
                }
                AuthViewModel.ErrorState.nameShort -> {
                    hideKeyboard()
                    showSnackbar(R.string.error_name_short)
                }
                AuthViewModel.ErrorState.emailEmpty -> {
                    hideKeyboard()
                    showSnackbar(R.string.error_email_empty)
                }
                AuthViewModel.ErrorState.emailInvalid -> {
                    hideKeyboard()
                    showSnackbar(R.string.error_email_invalid)
                }
                else -> {
                }
            }
        }

    }

    private fun navigateToDashboard() {
        // TODO
    }

    private fun showWorkingIndicator(isWorking: Boolean) {
        if (isWorking) {

            // Disable interaction
            binding.edtEmailAddress.isEnabled = false
            binding.edtFullName.isEnabled = false
            binding.btnNext.isEnabled = false
            binding.btnClose.isEnabled = false

            binding.btnNext.showProgress {
                buttonTextRes = R.string.working
                progressColorRes = R.color.white
            }
        } else {

            // Enable interaction
            binding.edtEmailAddress.isEnabled = true
            binding.edtFullName.isEnabled = true
            binding.btnNext.isEnabled = true
            binding.btnClose.isEnabled = true

            binding.btnNext.hideProgress(R.string.next)
        }
    }

}