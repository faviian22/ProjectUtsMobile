package com.example.projectutsmobile

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "suplier_table")
data class Suplier(
    @PrimaryKey(autoGenerate = true)
    val id_suplier: Int = 0,
    var nama_suplier: String,
    var no_Tlpn: String,
    var alamat_suplier: String,
    var nama_produk: String

)