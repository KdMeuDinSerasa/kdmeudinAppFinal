package com.example.kdmeudinheiro.utils

import android.os.IBinder

import android.view.WindowManager
import androidx.test.espresso.Root
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher


class ToastMatcher : TypeSafeMatcher<Root?>() {

    override fun matchesSafely(root: Root?): Boolean {
        val type: Int? = root?.getWindowLayoutParams()?.get()?.type
        if (type == WindowManager.LayoutParams.TYPE_TOAST) {
            val windowToken: IBinder? = root?.getDecorView()?.getWindowToken()
            val appToken: IBinder? = root?.getDecorView()?.getApplicationWindowToken()
            if (windowToken === appToken) {
                //means this window isn't contained by any other windows.
                return true
            }
        }
        return false
    }

    override fun describeTo(description: Description?) {
        description?.appendText("is toast")
    }
}