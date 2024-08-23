package com.joeydalu.vpd.auth

import com.google.firebase.auth.FirebaseAuth
import com.joeydalu.vpd.data.model.User
import javax.annotation.Nullable

/**
 * An [Authenticator] backed by the *Firebase* framework.
 * @author Joseph Dalughut
 */
class FirebaseAuthenticator: Authenticator {

    private var currentUser: User? = null

    /**
     * Fetches any user accounts currently logged in on the device.
     *
     * @return a logged-in [User] account.
     */
    @Nullable
    override fun getUser(): User? {
        currentUser?.let {
            return currentUser
        } ?: run {
            FirebaseAuth.getInstance().currentUser?.let { firUser ->
                this.currentUser = User(firUser)
                return this.currentUser
            } ?: run {
                return null
            }
        }
    }
}