package com.example.app.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.projectutsmobile.Suplier

@Dao
interface SuplierDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(suplier: Suplier)

    @Update
    suspend fun update(suplier: Suplier)

    @Delete
    suspend fun delete(suplier: Suplier)

    @Query("SELECT * FROM suplier_table ORDER BY id_suplier ASC")
    fun getAllSuplier(): LiveData<List<Suplier>>

    @Query("SELECT * FROM suplier_table")
    fun getAll(): Array<Suplier>
}