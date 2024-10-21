package com.example.projectutsmobile

import androidx.lifecycle.LiveData
import com.example.app.dao.SuplierDao
import com.example.app.model.Suplier

class SuplierRepository(private val suplierDao: SuplierDao) {

    val allSuplier: LiveData<List<Suplier>> = suplierDao.getAllSuplier()

    suspend fun insert(suplier: Suplier) {
        suplierDao.insert(suplier)
    }

    suspend fun update(suplier: Suplier) {
        suplierDao.update(suplier)
    }

    suspend fun delete(suplier: Suplier) {
        suplierDao.delete(suplier)
    }
}