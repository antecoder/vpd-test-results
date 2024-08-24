package com.joeydalu.vpd.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.joeydalu.vpd.data.model.Account

/**
 * Data-access-object for interacting with [Account]'s saved on the devices local database
 * @author Joseph Dalughut
 */
@Dao
interface AccountDao {

    /**
     * Counts the list of [Account] objects in the database.
     * @return An [Integer] denoting the number of accounts in the database.
     */
    @Query("SELECT COUNT(*) FROM account")
    suspend fun count(): Int

    /**
     * Fetches a list of [Account] objects from the local database.
     * @return a list of [Account] objects.
     */
    @Query("SELECT * FROM account")
    suspend fun list(): List<Account>

    /**
     * Fetches a [LiveData] list of [Account] objects from the database.
     * @return a [LiveData] list of [Account] objects.
     */
    @Query("SELECT * FROM account")
    fun liveList(): LiveData<List<Account>>

    /**
     * Inserts an [Account] into the database.
     * @return an [Integer] denoting the number of successful inserts.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: Account)

    /**
     * Inserts a list of [Account]'s into the database.
     * @return an [Integer] denoting the number of successful inserts.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(accounts: List<Account>)

    /**
     * Updates a list of [Account]'s in the database
     */
    @Update
    fun updateAll(accounts: List<Account>)

    /**
     * Deletes an [Account] from the database.
     * @return an [Integer] denoting the number of successful deletes.
     */
    @Delete
    suspend fun delete(account: Account)

}