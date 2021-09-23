package com.example.kdmeudinheiro.services

import android.view.View
import androidx.work.*
import java.util.concurrent.TimeUnit

class WorkManagerBuilder(val parentView: View) {

    fun buildService(){
        //create the instance of workManager
        val workManager = WorkManager.getInstance(parentView.context)

        //create the constraints to verify in the user phone
        val consts = Constraints.Builder()
            .setRequiresCharging(false)
            .setRequiredNetworkType(NetworkType.METERED)
            .setRequiresBatteryNotLow(true)
            .build()

        //create the workRequest with the details of the routine
        val mWorkRequest =
            PeriodicWorkRequestBuilder<NotificationWorkManager>(
                15,
                TimeUnit.MINUTES
            ).setConstraints(
                consts
            ).build()

        //start the routine
        workManager.enqueueUniquePeriodicWork(
            "Check_expired_bills",
            ExistingPeriodicWorkPolicy.KEEP,
            mWorkRequest
        )
    }
}