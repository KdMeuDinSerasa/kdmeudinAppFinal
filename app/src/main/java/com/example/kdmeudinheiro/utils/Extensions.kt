package com.example.kdmeudinheiro.utils

import android.text.TextUtils
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.kdmeudinheiro.R
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

//fun feedback(parentView: View, @StringRes string: Int){
//    Snackbar.make(parentView, string.toString(), Snackbar.LENGTH_LONG).show()
//}

fun feedback(parentView: View, @StringRes resId: Int, @ColorRes colorRes: Int) {

    setupSnackBar(parentView, resId, colorRes).apply {
        this.show()
    }
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


