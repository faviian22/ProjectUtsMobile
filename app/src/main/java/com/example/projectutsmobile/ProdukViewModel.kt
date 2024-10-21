package com.example.projectutsmobile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.projectutsmobile.Produk
import com.example.projectutsmobile.ProdukRepository
import kotlinx.coroutines.launch

class ProdukViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProdukRepository
    val allProduk: LiveData<List<Produk>>

    init {
        val produkDao = BakeryDatabase.getDatabase(application).produkDao()
        repository = ProdukRepository(produkDao)
        allProduk = repository.allProduk
    }

    fun insert(produk: Produk) = viewModelScope.launch {
        repository.insert(produk)
    }

    fun update(produk: Produk) = viewModelScope.launch {
        repository.update(produk)
    }

    fun delete(produk: Produk) = viewModelScope.launch {
        repository.delete(produk)
    }
}