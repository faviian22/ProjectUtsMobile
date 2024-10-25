package com.example.projectutsmobile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KaryawanViewModel(application: Application) : AndroidViewModel(application) {

    private val karyawanDao = BakeryDatabase.getDatabase(application).karyawanDao()
    val allKaryawan: LiveData<List<Karyawan>> = karyawanDao.getAllKaryawan()

    fun insert(karyawan: Karyawan) = viewModelScope.launch(Dispatchers.IO) {
        karyawanDao.insert(karyawan)
    }

    fun update(karyawan: Karyawan) = viewModelScope.launch(Dispatchers.IO) {
        karyawanDao.update(karyawan)
    }

    fun delete(karyawan: Karyawan) = viewModelScope.launch(Dispatchers.IO) {
        karyawanDao.delete(karyawan)
    }
}
