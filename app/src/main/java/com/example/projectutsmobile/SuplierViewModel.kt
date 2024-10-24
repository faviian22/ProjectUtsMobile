package com.example.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.projectutsmobile.BakeryDatabase
import com.example.projectutsmobile.Suplier
import com.example.projectutsmobile.SuplierRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SuplierViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: SuplierRepository
    val allSuplier: LiveData<List<Suplier>>

    init {
        val suplierDao = BakeryDatabase.getDatabase(application).suplierDao()
        repository = SuplierRepository(suplierDao)
        allSuplier = repository.allSuplier
    }

    fun insert(suplier: Suplier) = viewModelScope.launch{
        repository.insert(suplier)
    }

    fun update(suplier: Suplier) = viewModelScope.launch {
        repository.update(suplier)
    }

    fun delete(suplier: Suplier) = viewModelScope.launch {
        repository.delete(suplier)
    }
}