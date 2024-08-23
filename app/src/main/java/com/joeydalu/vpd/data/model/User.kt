package com.joeydalu.vpd.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor
import com.google.firebase.auth.FirebaseUser
import kotlinx.parcelize.Parcelize

/**
 * Model class representing a user of the application.
 * @author Joseph Dalughut
 */
@Entity
@Parcelize
data class User(
    @PrimaryKey val id: String,
    val name: String?,
    val email: String,
    @ColumnInfo(name = "photo_url") val photoUrl: String?,
    val phone: String?
): Parcelable {

    @Constructor
    constructor(firUser: FirebaseUser) : this(
        firUser.uid,
        firUser.displayName,
        firUser.email!!,
        firUser.photoUrl?.path,
        firUser.phoneNumber
    )

}
