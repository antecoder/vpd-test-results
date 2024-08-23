package com.joeydalu.vpd.view.auth

import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Abstract [Fragment] providing reusable code for the auth-fragments.
 *
 * @see LoginFragment
 * @see RegistrationEmailFragment
 * @see RegistrationPasswordFragment
 *
 * @author Joseph Dalughut
 */
@AndroidEntryPoint
abstract class BaseAuthFragment: Fragment() {

    /**
     * @return the [AuthViewModel] attached to this fragments parent [AuthActivity]
     */
    fun getViewModel(): AuthViewModel? {
        return getAuthActivity()?.authViewModel
    }

    /**
     * Returns the parent [AuthActivity] to this fragment.
     */
    fun getAuthActivity(): AuthActivity? {
        return (requireActivity() as? AuthActivity)
    }

}