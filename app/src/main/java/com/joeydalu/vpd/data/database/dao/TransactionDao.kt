package com.joeydalu.vpd.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.joeydalu.vpd.data.model.Transaction

/**
 * Data-access-object for interacting with [Account]'s saved on the devices local database
 * @author Joseph Dalughut
 */
@Dao
interface TransactionDao {

    /**
     * Counts the list of [Transaction] objects in the database.
     * @return An [Integer] denoting the number of accounts in the database.
     */
    @Query("SELECT COUNT(*) FROM account")
    suspend fun count(): Int

    /**
     * Fetches a list of [Account] objects from the local database.
     * @return a list of [Account] objects.
     */
    @Query("SELECT * FROM `transaction`")
    suspend fun list(): List<Transaction>

    /**
     * Fetches a [LiveData] list of [Account] objects from the database.
     * @return a [LiveData] list of [Account] objects.
     */
    @Query("SELECT * FROM `transaction`")
    fun liveList(): LiveData<List<Transaction>>

    /**
     * Inserts an [Transaction] into the database.
     * @return an [Integer] denoting the number of successful inserts.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: Transaction)

    /**
     * Inserts a list of [Transaction]'s into the database.
     * @return an [Integer] denoting the number of successful inserts.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<Transaction>)

    /**
     * Deletes an [Transaction] from the database.
     * @return an [Integer] denoting the number of successful deletes.
     */
    @Delete
    suspend fun delete(transaction: Transaction)

}