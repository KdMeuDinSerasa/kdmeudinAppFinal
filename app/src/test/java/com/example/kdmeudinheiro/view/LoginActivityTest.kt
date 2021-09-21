package com.example.kdmeudinheiro.view

import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest : TestCase(){

    private lateinit var view : LoginActivity
    private lateinit var emailTyped: String
    private lateinit var passwordTyped: String
    @Before
    fun setup(){
        view = LoginActivity()
        emailTyped = "janeDoe@gmail.com"
        passwordTyped = "DoeJane21"
    }

    @Test
    fun checkFields(){
       view.checkLogin(emailTyped, passwordTyped)

    }
}