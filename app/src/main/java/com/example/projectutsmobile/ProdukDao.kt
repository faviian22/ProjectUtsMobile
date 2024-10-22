package com.example.projectutsmobile

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.*

@Dao
interface ProdukDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(produk: Produk)

    @Update
    suspend fun update(produk: Produk)

    @Delete
    suspend fun delete(produk: Produk)

    @Query("SELECT * FROM produk_table ORDER BY id_produk ASC")
    fun getAllProduk(): LiveData<List<Produk>>

    @Query("SELECT * FROM produk_table")
    fun getAll(): Array<Produk>
}