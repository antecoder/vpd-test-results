package com.joeydalu.vpd.domain.usecase

import java.util.function.Consumer

/**
 * Use-case abstract-implementation, defines default behavior for a use-case to recive a [Params]
 * and return a [Result]
 * @author Joseph Dalughut
 */
abstract class CallbackUseCase<Params, Result> {

    /**
     * Default execution method.
     *
     * @param params the received parameter type
     * @param onSuccessListener should receive the defined [Result] as callback.
     * @param onFailureListener should receive any error as callback.
     */
    abstract fun execute(params: Params, onSuccessListener: Consumer<Result>, onFailureListener: Consumer<Throwable>)

}