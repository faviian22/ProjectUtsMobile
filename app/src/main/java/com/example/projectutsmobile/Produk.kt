package com.example.projectutsmobile

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produk_table")
data class Produk(
    @PrimaryKey(autoGenerate = true)
    val id_produk: Int = 0,
    val namaProduk: String = "",
    val stokProduk: Int = 0,
    val satuanProduk: String = "",
    val hargaProduk: Int = 0
) {
    // Konstruktor kosong untuk keperluan Firebase
    constructor() : this(0, "", 0, "", 0)
}
