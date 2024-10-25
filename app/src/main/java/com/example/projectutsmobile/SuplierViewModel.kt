package com.example.projectutsmobile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SuplierViewModel(application: Application) : AndroidViewModel(application) {

    private val suplierDao = BakeryDatabase.getDatabase(application).suplierDao()
    val allSuplier: LiveData<List<Suplier>> = suplierDao.getAllSuplier()

    fun insert(suplier: Suplier) = viewModelScope.launch(Dispatchers.IO) {
        suplierDao.insert(suplier)
    }

    fun update(suplier: Suplier) = viewModelScope.launch(Dispatchers.IO) {
        suplierDao.update(suplier)
    }

    fun delete(suplier: Suplier) = viewModelScope.launch(Dispatchers.IO) {
        suplierDao.delete(suplier)
    }
}
