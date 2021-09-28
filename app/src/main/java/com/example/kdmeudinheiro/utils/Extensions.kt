package com.example.kdmeudinheiro.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.TextUtils
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.suspendCancellableCoroutine
import java.text.DecimalFormat
import java.util.*
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resumeWithException

suspend fun <T> Task<T>.await(): T? {
    if (isComplete) {
        val e = exception
        return if (e == null) {
            if (isCanceled) {
                throw CancellationException(
                    "Task $this was cancelled normally."
                )
            } else {
                result
            }
        } else {
            throw e
        }
    }
    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener {
            val e = exception
            if (e == null) {
                if (isCanceled) cont.cancel() else cont.resume(result, e)
            } else {
                cont.resumeWithException(e)
            }
        }
    }
}

fun String.isValidEmail() =
    !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun Double.formatCurrency(): String {
    val dec = DecimalFormat("#,##0.00")
    return "R$ ${dec.format(this)}"
}


fun Date.adjustYear(): String {
    return this.toString().substring(this.toString().length - 4)
}

fun feedback(parentView: View, @StringRes resId: Int, @ColorRes colorRes: Int) {

    setupSnackBar(parentView, resId, colorRes).apply {
        this.show()
    }
}
fun String.fixApiDate(): String{
    return  this.substring(8, 10) + this.substring(4, 7) + "/" + this.substring(0,4)
}

fun AppCompatActivity.checkConnection(): Boolean{
    val connectivityManager = getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    // In here we return true if network is not null and Network is connected
    if(networkInfo != null && networkInfo.isConnected){
        return true
    }
    return false

}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

private fun setupSnackBar(
    v: View,
    @StringRes resId: Int,
    @ColorRes color: Int
): Snackbar {
    return Snackbar.make(v, resId, Snackbar.LENGTH_LONG).apply {
        view.setBackgroundColor(ContextCompat.getColor(context, color))
        view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).apply {
            gravity = Gravity.CENTER
            textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
    }
}

