package com.joeydalu.vpd.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
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
data class Account(
    @PrimaryKey val id: String,
    val name: String?,
    @ColumnInfo(name = "currency_code") val currencyCode: String,
    @ColumnInfo(name = "currency_name") val currencyName: String,
    @ColumnInfo(name = "balance") val creationDate: Int?,

    // Save in [Timestamp] format for firebase
    @ColumnInfo(name = "created_at") val createdAt: Timestamp?,
    @ColumnInfo(name = "updated_at") val updatedAt: Timestamp?,
    @ColumnInfo(name = "user_id") val userId: String
): Parcelable