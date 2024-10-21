package com.example.projectutsmobile

import androidx.lifecycle.LiveData

class ProdukRepository(private val produkDao: ProdukDao) {

    val allProduk: LiveData<List<Produk>> = produkDao.getAllProduk()

    suspend fun insert(produk: Produk) {
        produkDao.insert(produk)
    }

    suspend fun update(produk: Produk) {
        produkDao.update(produk)
    }

    suspend fun delete(produk: Produk) {
        produkDao.delete(produk)
    }
}