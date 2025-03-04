package com.joeydalu.vpd.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.joeydalu.vpd.data.database.dao.AccountDao
import com.joeydalu.vpd.data.database.dao.TransactionDao
import com.joeydalu.vpd.data.model.Account
import com.joeydalu.vpd.data.model.Transaction
import com.joeydalu.vpd.data.model.User

/**
 * Room database implementation for local storage
 * @author Joseph Dalughut
 */
@Database(entities = [Account::class, Transaction::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    /**
     * @return A data-access-object for accessing [Account] objects in the database.
     */
    abstract fun accountDao(): AccountDao

    /***
     * @return A data-access-object for accessing [Transaction] objects in the database.
     */
    abstract fun transactionDao(): TransactionDao

}