package com.example.app.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.projectutsmobile.Suplier

@Dao
interface SuplierDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(suplier: Suplier)  // Untuk single insertion

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(suplierList: List<Suplier>)  // Untuk bulk insertion

    @Update
    suspend fun update(suplier: Suplier)  // Untuk single update

    @Update
    suspend fun updateAll(suplierList: List<Suplier>)  // Untuk bulk update

    @Delete
    suspend fun delete(suplier: Suplier)

    @Query("SELECT * FROM suplier_table ORDER BY id_suplier ASC")
    fun getAllSuplier(): LiveData<List<Suplier>>  // Untuk mendapatkan data suplier

    @Query("SELECT * FROM suplier_table")
    fun getAll(): Array<Suplier>
}