package com.joeydalu.vpd.domain.usecase.auth

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.joeydalu.vpd.data.model.User
import com.joeydalu.vpd.domain.usecase.CallbackUseCase
import java.util.function.Consumer

/**
 * Use-case for logging the user into the application.
 * - Handles email & password authentication.
 * - Handles social auth
 *
 * @author Joseph Dalughut
 */
class LoginUser: CallbackUseCase<LoginRequest, User>() {

    private var firAuth: FirebaseAuth = Firebase.auth

    /**
     * Executes the login operation
     *
     * @param params The request containing the users credentials (email & password)
     * @param onSuccessListener Will receive the logged in user when successful.
     * @param onFailureListener Will receive any thrown errors
     */
    override fun execute(params: LoginRequest, onSuccessListener: Consumer<User>, onFailureListener: Consumer<Throwable>) {
        firAuth.signInWithEmailAndPassword(params.email, params.password)
            .addOnSuccessListener {
                it.user?.let { firUser ->
                    val user = User(firUser)
                    onSuccessListener.accept(user)
                }
            }.addOnFailureListener {
                it.printStackTrace()
                onFailureListener.accept(it)
            }
    }

}

/**
 * Request object for login
 */
data class LoginRequest(val email: String, val password: String)