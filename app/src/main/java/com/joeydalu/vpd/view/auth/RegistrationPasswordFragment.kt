package com.joeydalu.vpd.view.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joeydalu.vpd.R
import com.joeydalu.vpd.databinding.FragmentRegistrationPasswordBinding
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.joeydalu.vpd.util.hideKeyboard
import com.joeydalu.vpd.util.showSnackbar
import com.joeydalu.vpd.view.dashboard.MainActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment handling password collection and starting the registration process.
 *
 * Use the [RegistrationPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 * @author Joseph Dalughut
 */
@AndroidEntryPoint
class RegistrationPasswordFragment : BaseAuthFragment() {

    companion object {

        const val ARG_NAME = "NAME"
        const val ARG_EMAIL = "EMAIL"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment RegistrationFragment.
         */
        @JvmStatic
        fun newInstance(name: String?, email: String?) =
            RegistrationPasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NAME, name)
                    putString(ARG_EMAIL, email)
                }
            }
    }

    private var name: String? = null
    private var email: String? = null

    lateinit var binding: FragmentRegistrationPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(ARG_NAME)
            email = it.getString(ARG_EMAIL)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationPasswordBinding.inflate(layoutInflater)
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
        binding.btnRegister.setOnClickListener {
            authViewModel?.onSubmitRegistrationPasswords(binding.edtPassword.text.toString().trim(), binding.edtConfirmPassword.text.toString().trim())
        }
    }

    private fun observeModel() {
        val authViewModel = getViewModel()
        authViewModel?.events?.observe(viewLifecycleOwner) { event ->
            when (event) {
                AuthViewModel.Event.navBack -> requireActivity().onBackPressedDispatcher.onBackPressed()
                AuthViewModel.Event.registrationBegan -> showWorkingIndicator(true)
                AuthViewModel.Event.registrationSuccess -> {
                    showWorkingIndicator(false)
                }
                AuthViewModel.Event.registrationError -> {
                    hideKeyboard()
                    showWorkingIndicator(false)
                    showSnackbar(R.string.error_tasks_unsuccessful)
                }
                AuthViewModel.Event.navDashboard -> navigateToDashboard()
                else -> {

                }
            }
        }

        authViewModel?.errorState?.observe(viewLifecycleOwner) { state ->
            when (state) {
                AuthViewModel.ErrorState.passwordEmpty -> {
                    hideKeyboard()
                    showSnackbar(R.string.error_password_empty)
                }
                AuthViewModel.ErrorState.passwordShort -> {
                    hideKeyboard()
                    showSnackbar(R.string.error_password_short)
                }
                AuthViewModel.ErrorState.passwordInvalid -> {
                    hideKeyboard()
                    showSnackbar(R.string.error_email_empty)
                }
                AuthViewModel.ErrorState.passwordConfirmEmpty -> {
                    hideKeyboard()
                    showSnackbar(R.string.error_password_confirm_empty)
                }
                AuthViewModel.ErrorState.passwordConfirmInvalid -> {
                    hideKeyboard()
                    showSnackbar(R.string.error_password_confirm_invalid)
                }
                else -> {
                }
            }
        }
    }

    private fun navigateToDashboard() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun showWorkingIndicator(isWorking: Boolean) {
        if (isWorking) {

            // Disable interaction
            binding.edtPassword.isEnabled = false
            binding.edtConfirmPassword.isEnabled = false
            binding.btnRegister.isEnabled = false
            binding.btnClose.isEnabled = false

            binding.btnRegister.showProgress {
                buttonTextRes = R.string.working
                progressColorRes = R.color.white
            }
        } else {

            // Enable interaction
            binding.edtPassword.isEnabled = true
            binding.edtConfirmPassword.isEnabled = true
            binding.btnRegister.isEnabled = true
            binding.btnClose.isEnabled = true

            binding.btnRegister.hideProgress(R.string.register)
        }
    }
}