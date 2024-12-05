package com.example.projectutsmobile

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProdukDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(produk: Produk)  // For single insertion

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(produkList: List<Produk>)  // For bulk insertion

    @Update
    suspend fun update(produk: Produk)  // For single update

    @Update
    suspend fun updateAll(produkList: List<Produk>)  // For bulk update

    @Delete
    suspend fun delete(produk: Produk)

    @Query("SELECT * FROM produk_table ORDER BY id_produk ASC")
    fun getAllProduk(): LiveData<List<Produk>>  // To get all products

    @Query("SELECT * FROM produk_table")
    fun getAll(): Array<Produk>
}
