package com.example.projectutsmobile

import android.content.Context
import androidx.room.Dao
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.projectutsmobile.KaryawanDao
import com.example.projectutsmobile.BakeryDatabase
import com.example.projectutsmobile.Karyawan
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

   private lateinit var karyawanDao: KaryawanDao
    private lateinit var db: BakeryDatabase

    private val karyawan = Karyawan(1, "Ersa", "Perempuan", "Balerejo")

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, BakeryDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        karyawanDao = db.karyawanDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() = db.close()

    @Test
    @Throws(Exception::class)
    fun insertAndRetrieveMatkul() {
        BakeryDatabase.insert(karyawan)
        val result = karyawanDao.getAll()
        assert(result.size == 1)
    }
}