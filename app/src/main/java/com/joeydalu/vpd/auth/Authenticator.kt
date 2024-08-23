package com.joeydalu.vpd.auth

import com.joeydalu.vpd.data.model.User

/**
 * An interface defining behavior for a simple *Authenticator* used for accessing the
 * users account data.
 * @author Joseph Dalughut
 */
interface Authenticator {

    /**
     * Fetches any user accounts currently logged in on the device.
     *
     * @return a *nullable* logged-in [User] account.
     */
    fun getUser(): User?

}