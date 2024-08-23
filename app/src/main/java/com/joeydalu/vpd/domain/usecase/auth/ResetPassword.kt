package com.joeydalu.vpd.domain.usecase.auth

import com.joeydalu.vpd.domain.usecase.CallbackUseCase
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import java.util.function.Consumer


/**
 * Use-case triggering firebase to reset the users password.
 * @author Joseph Dalughut
 */
class ResetPassword: CallbackUseCase<String, Unit>() {

    private var firAuth: FirebaseAuth = Firebase.auth

    /**
     * Triggers a reset of the users password.
     *
     * @param onSuccessListener Will receive unit callback on success.
     * @param onFailureListener Will receive any thrown errors
     */
    override fun execute(params: String, onSuccessListener: Consumer<Unit>, onFailureListener: Consumer<Throwable>) {
        firAuth.sendPasswordResetEmail(params)
            .addOnSuccessListener {
                onSuccessListener.accept(Unit)
            }.addOnFailureListener { throwable ->
                throwable.printStackTrace()
                onFailureListener.accept(throwable)
            }
    }
}