package com.joeydalu.vpd.domain.usecase.auth

import com.joeydalu.vpd.data.model.User
import com.joeydalu.vpd.domain.usecase.CallbackUseCase
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import java.util.function.Consumer


/**
 * Use-case handling registering a new user
 * @author Joseph Dalughut
 */
class SignupUser: CallbackUseCase<SignupRequest, User>() {

    private var firAuth: FirebaseAuth = Firebase.auth

    /**
     * Begins the process of registering the user using their email & password.
     *
     * @param onSuccessListener Will receive the logged in user when successful.
     * @param onFailureListener Will receive any thrown errors
     */
    override fun execute(params: SignupRequest, onSuccessListener: Consumer<User>, onFailureListener: Consumer<Throwable>) {
        firAuth.createUserWithEmailAndPassword(params.email, params.password)
            .addOnSuccessListener {
                it.user?.let { firUser ->

                    setUserName(params.name, firUser, onSuccessListener, onFailureListener)
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
                onFailureListener.accept(it)
            }

    }

    /**
     * Attaches the users name to his/her account
     * @param name The users provided name
     *
     * @param onSuccessListener Will receive the registered user when successful.
     * @param onFailureListener Will receive any thrown errors
     */
    private fun setUserName(name: String, firUser: FirebaseUser, onSuccessListener: Consumer<User>, onFailureListener: Consumer<Throwable>) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()

        firUser.updateProfile(profileUpdates)
            .addOnSuccessListener {
                val user = User(firUser)
                firUser.sendEmailVerification()
                    .addOnSuccessListener {
                        onSuccessListener.accept(user)
                    }.addOnFailureListener {
                        onSuccessListener.accept(user)
                    }
            }.addOnFailureListener {
                it.printStackTrace()
                // TODO find way to retry
                val user = User(firUser)
                onSuccessListener.accept(user)
            }
    }
}

/**
 * Request object containing the users registration details
 */
data class SignupRequest(val name: String, val email: String, val password: String)