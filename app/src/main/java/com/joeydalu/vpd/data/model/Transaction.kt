package com.joeydalu.vpd.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

/**
 * Model class representing a [User]'s account.
 * Contains information about their accounts current status
 *
 * @author Joseph Dalughut
 */
@Entity
@Parcelize
data class Transaction (
    @PrimaryKey val id: String,
    val amount: Int = 0,

    @ColumnInfo("source_account_id") val sourceAccountId: Int,
    @ColumnInfo("source_account_name") val sourceAccountName: String,
    @ColumnInfo("source_account_number") val sourceAccountNumber: String,

    @ColumnInfo("dest_account_id") val destAccountId: Int,
    @ColumnInfo("dest_account_name") val destAccountName: String,
    @ColumnInfo("dest_account_number") val destAccountNumber: String,

    @ColumnInfo("currency_code") val currencyCode: String,

    // Save in [Timestamp] format for firebase
    @ColumnInfo(name = "created_at") val createdAt: Timestamp?,
    @ColumnInfo(name = "updated_at") val updatedAt: Timestamp?,
    @ColumnInfo(name = "transaction_reference") val transactionReference: String

): Parcelable