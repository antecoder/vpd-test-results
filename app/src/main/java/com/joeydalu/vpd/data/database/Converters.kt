package com.joeydalu.vpd.data.database

import androidx.room.TypeConverter
import com.google.firebase.Timestamp
import java.util.Date

/**
 * Type-converters for converting value-types the database cannot
 * "figure out how to save"
 * @author Joseph Dalughut
 */
class Converters {

    /**
     * Converts a timestamp to a long
     * @param timestamp the [Timestamp] to convert.
     */
    @TypeConverter
    fun fromTimestampToMillis(timestamp: Timestamp): Long {
        return timestamp.toDate().time
    }

    /**
     * Converts a long to a [Timestamp]
     * @param millis the long to convert.
     */
    @TypeConverter
    fun fromMillisToTimestamp(millis: Long): Timestamp {
        return Timestamp(Date(millis))
    }

}