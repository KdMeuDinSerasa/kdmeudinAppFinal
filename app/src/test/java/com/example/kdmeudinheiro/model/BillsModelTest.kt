package com.example.kdmeudinheiro.model

import com.google.common.truth.Truth
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class BillsModelTest : TestCase() {

    private var mIdBill: String = ""
    private var mIdUser: String = ""
    private var mPrice: String = ""
    private var mType: String = ""
    private var mName: String = ""
    private var mStatus: Int = 0


    @Test
    fun `Not Expired should return false `() {
        var calendar = Calendar.getInstance()
        var mDate: Date = calendar.time
        var mBill = BillsModel(mIdBill, mIdUser, mPrice, mType, mName, mDate, mStatus)
        mBill.checkExpired().apply {
            Truth.assertThat(this).isEqualTo(false)
        }
    }

    @Test
    fun `Expired should return true `() {
        var calendar = Calendar.getInstance()
        var mDate: Date = calendar.time
        calendar.add(Calendar.DATE, 1)
        mDate = calendar.time
        var mBill = BillsModel(mIdBill, mIdUser, mPrice, mType, mName, mDate, mStatus)
        mBill.checkExpired().apply {
            Truth.assertThat(this).isEqualTo(true)
        }
    }

    @Test
    fun `Expired but paid should return false `() {
        var calendar = Calendar.getInstance()
        var mDate: Date = calendar.time
        calendar.add(Calendar.DATE, 1)
        mDate = calendar.time
        mStatus = 1
        var mBill = BillsModel(mIdBill, mIdUser, mPrice, mType, mName, mDate, mStatus)
        mBill.checkExpired().apply {
            Truth.assertThat(this).isEqualTo(false)
        }
    }

    @Test
    fun `To expire and not paid should return true`() {
        var calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)
        var mDate: Date = calendar.time
        mStatus = 0
        var mBill = BillsModel(mIdBill, mIdUser, mPrice, mType, mName, mDate, mStatus)
        mBill.checkToExpire().apply {
            Truth.assertThat(this).isEqualTo(true)
        }
    }

    @Test
    fun `To expire and paid should return false`() {
        var calendar = Calendar.getInstance()
        var mDate: Date = calendar.time
        mStatus = 1
        var mBill = BillsModel(mIdBill, mIdUser, mPrice, mType, mName, mDate, mStatus)
        mBill.checkToExpire().apply {
            Truth.assertThat(this).isEqualTo(false)
        }
    }

    @Test
    fun `Days to expire and  not paid should return false`() {
        var calendar = Calendar.getInstance()
        var mDate: Date = calendar.time
        /*here i set the expire date the actual time plus five days.*/
        calendar.add(Calendar.DATE, 5)
        mDate = calendar.time
        var mBill = BillsModel(mIdBill, mIdUser, mPrice, mType, mName, mDate, mStatus)
        mBill.checkToExpire().apply {
            Truth.assertThat(this).isEqualTo(false)
        }
    }

    @Test
    fun `Days expired and not  paid should return false`() {
        var calendar = Calendar.getInstance()
        var mDate: Date = calendar.time
        /*here i set the expire date the actual time minus five days.*/
        calendar.add(Calendar.DATE, -6)
        mDate = calendar.time
        var mBill = BillsModel(mIdBill, mIdUser, mPrice, mType, mName, mDate, mStatus)
        mBill.checkToExpire().apply {
            Truth.assertThat(this).isEqualTo(false)
        }
    }
}