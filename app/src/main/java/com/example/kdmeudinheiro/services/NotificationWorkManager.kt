package com.example.kdmeudinheiro.services

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.kdmeudinheiro.enums.KeysShared
import com.example.kdmeudinheiro.repository.BillsRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class NotificationWorkManager(val context: Context, param: WorkerParameters) :
    Worker(context, param) {

    private val mBillsRepository = BillsRepository(FirebaseFirestore.getInstance())

    override fun doWork(): Result {

        checkBills()
        return Result.success()
    }

    fun checkBills() {
        val mSharedPreferences =
            context.getSharedPreferences(KeysShared.APP.key, Context.MODE_PRIVATE)
        val userId = mSharedPreferences.getString(KeysShared.USERID.key, "")
        val calendar = Calendar.getInstance()

        CoroutineScope(Dispatchers.Default).launch {
            var count: Int = 0
            userId?.let { mBillsRepository.getBills(it) }!!.forEach {
                if (it.expire_date.before(calendar.time) && it.status == 0) {
                    count++
                }
            }
            if (count > 0) {
                val mNotificationHandler = NotificationHandler(context)
                mNotificationHandler.createNotification(
                    "Você possui contas vencidas",
                    "Total de contas vencidas: $count"
                ).apply {
                    val notificationManager = NotificationManagerCompat.from(context)
                    notificationManager.notify(1, this)
                }

            }
        }
    }

}



