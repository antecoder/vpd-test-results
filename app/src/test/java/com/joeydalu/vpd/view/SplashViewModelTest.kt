package com.joeydalu.vpd.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.joeydalu.MainCoroutineRule
import com.joeydalu.vpd.auth.Authenticator
import com.joeydalu.vpd.data.database.AppDatabase
import com.joeydalu.vpd.data.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule(UnconfinedTestDispatcher())

    lateinit var authenticatorUser: Authenticator
    lateinit var authenticatorNoUser: Authenticator
    lateinit var database: AppDatabase

    var splashViewModel: SplashViewModel? = null

    @Before
    fun setUp() {
        authenticatorUser = Mockito.mock(Authenticator::class.java)
        authenticatorNoUser =  Mockito.mock(Authenticator::class.java)
        database = Mockito.mock(AppDatabase::class.java)

        Mockito.`when`(authenticatorUser.getUser()).thenReturn(Mockito.mock(User::class.java))
        Mockito.`when`(authenticatorUser.getUser()).thenReturn(null)
    }

    @After
    fun tearDown() {
        splashViewModel = null
    }

    @Test
    fun test_AuthenticatorWithUser_LoggedInState() = runBlocking  {
        splashViewModel = SplashViewModel(authenticatorUser, database)
//        advanceUntilIdle()
        val authState = splashViewModel!!.authState.value
        assert(authState == SplashViewModel.AuthState.loggedIn)
    }

    @Test
    fun test_AuthenticatorNoUser_LoggedOutState() {
        splashViewModel = SplashViewModel(authenticatorNoUser, database)

        val authState = splashViewModel!!.authState.value
        assert(authState == SplashViewModel.AuthState.loggedOut)
    }

}