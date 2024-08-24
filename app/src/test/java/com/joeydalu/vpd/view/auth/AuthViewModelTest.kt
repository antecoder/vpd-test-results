package com.joeydalu.vpd.view.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.joeydalu.vpd.auth.Authenticator
import com.joeydalu.vpd.data.database.AppDatabase
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var authenticator: Authenticator
    lateinit var database: AppDatabase
    lateinit var authViewModel: AuthViewModel

    @Before
    fun setUp() {
        authenticator = mock(Authenticator::class.java)
        database = mock(AppDatabase::class.java)

        authViewModel = mock(AuthViewModel::class.java)
    }

    @Test
    fun testOnSubmitInvalidLoginCredentials_LoginNeverCalled() {
        //testing validation
        authViewModel.onSubmitLoginDetails("", "")
        verify(authViewModel, never()).beginLogin("", "")

        authViewModel.onSubmitLoginDetails("badEmail", "")
        verify(authViewModel, never()).beginLogin("badEmail", "")

        authViewModel.onSubmitLoginDetails("good@email.com", "")
        verify(authViewModel, never()).beginLogin("good@email.com", "")
    }

    @Test
    fun testOnSubmitInvalidRegistrationCredentials_RegistrationNeverCalled() {
        authViewModel.onSubmitRegistrationNameEmail("Jeremy Maccore", "good@email.com")

        // testing password
        authViewModel.onSubmitRegistrationPasswords("", "")
        verify(authViewModel, never()).beginRegistration("Jeremy Maccore", "good@email.com", "")

        // testing password confirmation
        authViewModel.onSubmitRegistrationPasswords("GoodPassword", "BadPassword")
        verify(authViewModel, never()).beginRegistration("Jeremy Maccore", "good@email.com", "GoodPassword")
    }

}