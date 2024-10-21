package com.example.projectutsmobile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.app.model.Karyawan
import com.example.projectutsmobile.KaryawanRepository
import kotlinx.coroutines.launch
class KaryawanViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: KaryawanRepository
    val allKaryawan: LiveData<List<Karyawan>>

    init {
        val karyawanDao = BakeryDatabase.getDatabase(application).karyawanDao()
        repository = KaryawanRepository(karyawanDao)
        allKaryawan = repository.allKaryawan
    }

    fun insert(karyawan: Karyawan) = viewModelScope.launch {
        repository.insert(karyawan)
    }

    fun update(karyawan: Karyawan) = viewModelScope.launch {
        repository.update(karyawan)
    }

    fun delete(karyawan: Karyawan) = viewModelScope.launch {
        repository.delete(karyawan)
    }
}
