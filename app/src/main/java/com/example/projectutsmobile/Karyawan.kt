package com.example.projectutsmobile

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "karyawan_table")
data class Karyawan(
    @PrimaryKey(autoGenerate = true)
    val id_karyawan: Int = 0,
    val nama_karyawan: String = "",
    val jenis_kelamin: String = "",
    val alamat_karyawan: String = ""
) {
    // Konstruktor kosong untuk keperluan Firebase
    constructor() : this(0, "", "", "")
}
