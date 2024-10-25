package com.example.projectutsmobile

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface KaryawanDao {

    @Insert
    suspend fun insert( karyawan: Karyawan)

    @Update
    suspend fun update(karyawan: Karyawan)

    @Delete
    suspend fun delete(karyawan: Karyawan)

    @Query("SELECT * FROM karyawan_table ORDER BY id_karyawan ASC")
    fun getAllKaryawan(): LiveData<List<Karyawan>>

    @Query("SELECT * FROM karyawan_table")
    fun getAll(): Array<Karyawan>
}