package com.example.projectutsmobile

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produk_table")
data class Produk(
    @PrimaryKey(autoGenerate = true)
    val id_produk: Int,
    val namaProduk: String,
    val satuanProduk: Int,
    val hargaProduk: Int,
    val stokProduk: Int

)
