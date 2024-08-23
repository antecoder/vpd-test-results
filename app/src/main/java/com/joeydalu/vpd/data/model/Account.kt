package com.joeydalu.vpd.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.joeydalu.vpd.data.database.AppDatabase
import kotlinx.parcelize.Parcelize
import java.math.BigInteger
import java.util.Calendar
import java.util.Random

/**
 * Model class representing a [User]'s account.
 * Contains information about their accounts current status
 *
 * @author Joseph Dalughut
 */
@Entity
@Parcelize
data class Account(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val balance: Int = 0,
    @ColumnInfo(name = "currency_code") val currencyCode: String,
    // Save in [Timestamp] format for firebase
    @ColumnInfo(name = "created_at") val createdAt: Timestamp?,
    @ColumnInfo(name = "updated_at") val updatedAt: Timestamp?,
    @ColumnInfo(name = "account_number") val accountNumber: String
): Parcelable {

    companion object {
        class Factory {
            suspend fun seed(database: AppDatabase) {
                if (database.accountDao().count() != 0) {
                    return
                }
                val now = Calendar.getInstance().time
                val timestamp = Timestamp(now)
                val accounts = mutableListOf(
                    Account(1, "Jeremy Brown",  900000000, "NGN", timestamp, timestamp, "1234593979"),
                    Account(2, "William Blue",  10099090, "NGN", timestamp, timestamp, "2138294829"),
                    Account(3, "Sandra Paul",  10990000, "USD", timestamp, timestamp, "32453858485"),
                    Account(4, "Enigma Choice", 1009000, "NGN", timestamp, timestamp, "4049598485"),
                    Account(5, "Station Ex", 5000000, "NGN", timestamp, timestamp, "58359384459"),
                    Account(6, "Fanny Johnson",  100990000, "NGN", timestamp, timestamp, "28948285848"),
                    Account(7, "Lois Gore",  1009990, "NGN", timestamp, timestamp, "2848503867"),
                    Account(8, "Dussledurf Sally",  1099900, "NGN", timestamp, timestamp, "88472737577"),
                    Account(9, "Carma Grace",  109990000, "USD", timestamp, timestamp, "24377748389"),
                    Account(10, "Joey Dalu",  107786700, "NGN", timestamp, timestamp, "1219593840"),
                )
                database.accountDao().insertAll(accounts)
            }

        }
    }


}