package com.example.projectutsmobile

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.app.dao.SuplierDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

   private lateinit var produkDao: ProdukDao
    private lateinit var karyawanDao: KaryawanDao
    private lateinit var suplierDao: SuplierDao

    private lateinit var db: BakeryDatabase

    private val produk = Produk(1, "Gula", 3, "kg", 1000)
    private val karyawan = Karyawan(1, "Ersa", "Perempuan", "Balerejo")
    private val suplier = Suplier(1, "Satria", "0864435434", "Balerejo", "Tepung Kanji")

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, BakeryDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        produkDao = db.produkDao()
        karyawanDao = db.karyawanDao()
        suplierDao = db.suplierDao()

    }

    @After
    @Throws(IOException::class)
    fun closeDb() = db.close()


    @Test
    @Throws(Exception::class)
    fun insertAndRetrieveProduk() {
        produkDao.insert(produk)
        val resultProduk = produkDao.getAll()
        assert(resultProduk.size == 1)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndRetrieveKaryawan() {
        karyawanDao.insert(karyawan)
        val resultKaryawan = karyawanDao.getAll()
        assert(resultKaryawan.size == 1)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndRetrieveSuplier() {
        suplierDao.insert(suplier)
        val resultSuplier = suplierDao.getAll()
        assert(resultSuplier.size == 1)
    }
}