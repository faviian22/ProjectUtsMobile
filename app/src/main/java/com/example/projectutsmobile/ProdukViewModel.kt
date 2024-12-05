package com.example.projectutsmobile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProdukViewModel(application: Application) : AndroidViewModel(application) {

    private val produkDao = BakeryDatabase.getDatabase(application).produkDao()
    val allProduk: LiveData<List<Produk>> = produkDao.getAllProduk()

    fun insert(produk: Produk) = viewModelScope.launch(Dispatchers.IO) {
        produkDao.insert(produk)
    }

    fun update(produk: Produk) = viewModelScope.launch(Dispatchers.IO) {
        produkDao.update(produk)
    }

    fun delete(produk: Produk) = viewModelScope.launch(Dispatchers.IO) {
        produkDao.delete(produk)
    }
}