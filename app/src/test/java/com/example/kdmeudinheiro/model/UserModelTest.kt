package com.example.kdmeudinheiro.model

import com.google.common.truth.Truth
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UserModelTest : TestCase() {
    private var email = ""
    private var userName = ""
    private var password = ""

    @Before
    fun setup() {

    }

    /* CheckInsertData Tests */
    @Test
    fun `blank values should return false`() {
        val mUser =
            UserModel(id = "", email = email, password = password, name = userName, img = "")
        mUser.checkInsertData().apply {
            Truth.assertThat(this).isEqualTo(false)
        }

    }

    @Test
    fun `Right values should return true`() {
        email = "edson@gmail.com"
        password = "123456789"
        userName = "edson"
        val mUser =
            UserModel(id = "", email = email, password = password, name = userName, img = "")
        mUser.checkInsertData().apply {
            Truth.assertThat(this).isEqualTo(true)
        }
    }

    @Test
    fun `Missing Values should return false`() {
        email = "edson@gmail.com"
        password = ""
        userName = "edson"
        val mUser =
            UserModel(id = "", email = email, password = password, name = userName, img = "")
        mUser.checkInsertData().apply {
            Truth.assertThat(this).isEqualTo(false)
        }
    }

    @Test
    fun `Missing @ should return false`() {
        email = "edsongmail.com"
        password = "123456789"
        userName = "edson"
        val mUser =
            UserModel(id = "", email = email, password = password, name = userName, img = "")
        mUser.checkInsertData().apply {
            Truth.assertThat(this).isEqualTo(false)
        }
    }

    @Test
    fun `Invalid Email should return false`() {
        email = "edson@gmail"
        password = "123456789"
        userName = "edson"
        val mUser =
            UserModel(id = "", email = email, password = password, name = userName, img = "")
        mUser.checkInsertData().apply {
            Truth.assertThat(this).isEqualTo(false)
        }
    }

    @Test
    fun `email without dot should return false`() {
        email = "edson@gmailcom"
        password = "123456789"
        userName = "edson"
        val mUser =
            UserModel(id = "", email = email, password = password, name = userName, img = "")
        mUser.checkInsertData().apply {
            Truth.assertThat(this).isEqualTo(false)
        }
    }

    @Test
    fun `Missing userName should return false`() {
        email = "edson@gmail.com"
        password = "123456789"
        userName = ""
        val mUser =
            UserModel(id = "", email = email, password = password, name = userName, img = "")
        mUser.checkInsertData().apply {
            Truth.assertThat(this).isEqualTo(false)
        }
    }

    @Test
    fun `username smaller then 4 char should return false`() {
        email = "edson@gmail.com"
        password = "123456789"
        userName = "eds"
        val mUser =
            UserModel(id = "", email = email, password = password, name = userName, img = "")
        mUser.checkInsertData().apply {
            Truth.assertThat(this).isEqualTo(false)
        }
    }
    /* FINISH*/

    /*Check login tests*/
    @Test
    fun `Right values to login should return true`() {
        email = "edson@gmail.com"
        password = "123456789"
        userName = ""
        val mUser =
            UserModel(id = "", email = email, password = password, name = userName, img = "")
        mUser.checkLogin().apply {
            Truth.assertThat(this).isEqualTo(true)
        }
    }

    @Test
    fun `Smaller password to login should return false`() {
        email = "edson@gmail.com"
        password = "1234"
        userName = ""
        val mUser =
            UserModel(id = "", email = email, password = password, name = userName, img = "")
        mUser.checkLogin().apply {
            Truth.assertThat(this).isEqualTo(false)
        }
    }

    @Test
    fun `Blank password to login should return false`() {
        email = "edson@gmail.com"
        password = ""
        userName = ""
        val mUser =
            UserModel(id = "", email = email, password = password, name = userName, img = "")
        mUser.checkLogin().apply {
            Truth.assertThat(this).isEqualTo(false)
        }
    }

    @Test
    fun `Six char long password to login should return true`() {
        email = "edson@gmail.com"
        password = "123456"
        userName = ""
        val mUser =
            UserModel(id = "", email = email, password = password, name = userName, img = "")
        mUser.checkLogin().apply {
            Truth.assertThat(this).isEqualTo(true)
        }
    }

    @Test
    fun `Email without @ password to login should return false`() {
        email = "edsongmail.com"
        password = "1234567"
        userName = ""
        val mUser =
            UserModel(id = "", email = email, password = password, name = userName, img = "")
        mUser.checkLogin().apply {
            Truth.assertThat(this).isEqualTo(false)
        }
    }

    @Test
    fun `Email without dot password to login should return false`() {
        email = "edsongmail@"
        password = "1234567"
        userName = ""
        val mUser =
            UserModel(id = "", email = email, password = password, name = userName, img = "")
        mUser.checkLogin().apply {
            Truth.assertThat(this).isEqualTo(false)
        }
    }


}