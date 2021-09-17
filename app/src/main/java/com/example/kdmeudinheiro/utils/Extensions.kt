package com.example.kdmeudinheiro.utils

import android.text.TextUtils
import android.util.Patterns
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import java.text.DecimalFormat
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resumeWithException

suspend fun <T> Task<T>.await(): T? {
    if (isComplete) {
        val e = exception
        return if (e == null) {
            if (isCanceled) {
                throw CancellationException(
                    "Task $this was cancelled normally.")
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

fun Double.formatCurrency():String {
    val dec = DecimalFormat("#,##0.00")
    return "R$ ${dec.format(this)}"
}