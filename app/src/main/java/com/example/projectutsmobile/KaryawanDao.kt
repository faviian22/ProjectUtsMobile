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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(karyawan: Karyawan)  // Untuk single insertion

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(karyawanList: List<Karyawan>)  // Untuk bulk insertion

    @Update
    suspend fun update(karyawan: Karyawan)  // Untuk single update

    @Update
    suspend fun updateAll(karyawanList: List<Karyawan>)  // Untuk bulk update

    @Delete
    suspend fun delete(karyawan: Karyawan)

    @Query("SELECT * FROM karyawan_table ORDER BY id_karyawan ASC")
    fun getAllKaryawan(): LiveData<List<Karyawan>>  // Untuk mendapatkan data karyawan

    @Query("SELECT * FROM karyawan_table")
    fun getAll(): Array<Karyawan>
}
