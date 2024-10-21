package com.example.projectutsmobile

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.app.dao.KaryawanDao
import com.example.app.dao.SuplierDao
import com.example.app.model.Karyawan
import com.example.app.model.Suplier
import com.example.projectutsmobile.Produk
import com.example.projectutsmobile.ProdukDao

@Database(entities = [Produk::class, Karyawan::class, Suplier::class], version = 1, exportSchema = false)
abstract class BakeryDatabase : RoomDatabase() {

    abstract fun produkDao(): ProdukDao
    abstract fun karyawanDao(): KaryawanDao
    abstract fun suplierDao(): SuplierDao

    companion object {
        @Volatile
        private var INSTANCE: BakeryDatabase? = null

        fun getDatabase(context: Context): BakeryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BakeryDatabase::class.java,
                    "bakery_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}