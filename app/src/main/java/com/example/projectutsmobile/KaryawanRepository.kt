package com.example.projectutsmobile

import androidx.lifecycle.LiveData
import com.example.app.dao.KaryawanDao
import com.example.app.model.Karyawan

class KaryawanRepository(private val karyawanDao: KaryawanDao) {

    val allKaryawan: LiveData<List<Karyawan>> = karyawanDao.getAllKaryawan()

    suspend fun insert(karyawan: Karyawan) {
        karyawanDao.insert(karyawan)
    }

    suspend fun update(karyawan: Karyawan) {
        karyawanDao.update(karyawan)
    }

    suspend fun delete(karyawan: Karyawan) {
        karyawanDao.delete(karyawan)
    }
}