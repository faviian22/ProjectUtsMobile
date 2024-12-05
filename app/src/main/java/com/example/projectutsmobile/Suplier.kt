package com.example.projectutsmobile

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "suplier_table")
data class Suplier(
    @PrimaryKey(autoGenerate = true)
    val id_suplier: Int = 0,
    val nama_suplier: String = "",
    val no_Tlpn: String = "",
    val alamat_suplier: String = "",
    val nama_produk: String = ""
) {
    // Konstruktor kosong untuk keperluan Firebase
    constructor() : this(0, "", "", "", "")
}