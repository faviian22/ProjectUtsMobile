package com.example.projectutsmobile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class ProdukViewModel(application: Application) : AndroidViewModel(application) {

    private val produkDao = BakeryDatabase.getDatabase(application).produkDao()
    val allProduk: LiveData<List<Produk>> = produkDao.getAllProduk()

    // Firebase Database reference
    private val database = FirebaseDatabase.getInstance()
    private val produkRef: DatabaseReference = database.getReference("produk")

    // LiveData for Firebase
    private val _firebaseProduk = MutableLiveData<List<Produk>>()
    val firebaseProduk: LiveData<List<Produk>> get() = _firebaseProduk

    // Insert Produk in both Room and Firebase
    fun insert(produk: Produk) = viewModelScope.launch(Dispatchers.IO) {
        // Generate unique ID for the produk
        val uniqueId = UUID.randomUUID().mostSignificantBits.toInt()
        val produkWithId = produk.copy(id_produk = uniqueId)

        // Insert into Room database
        produkDao.insert(produkWithId)

        // Insert into Firebase Realtime Database if online
        if (isNetworkAvailable()) {
            produkRef.child(produkWithId.id_produk.toString()).setValue(produkWithId)  // Insert to Firebase
        } else {
            // If offline, store locally for later sync
            storeOffline(listOf(produkWithId))
        }
    }

    // Update Produk in both Room and Firebase
    fun update(produk: Produk) = viewModelScope.launch(Dispatchers.IO) {
        // Update in Room database
        produkDao.update(produk)

        // Update in Firebase Realtime Database if online
        if (isNetworkAvailable()) {
            produkRef.child(produk.id_produk.toString()).setValue(produk)  // Update in Firebase
        }
    }

    // Delete Produk in both Room and Firebase
    fun delete(produk: Produk) = viewModelScope.launch(Dispatchers.IO) {
        // Delete from Room database
        produkDao.delete(produk)

        // Delete from Firebase Realtime Database
        if (isNetworkAvailable()) {
            produkRef.child(produk.id_produk.toString()).removeValue()  // Remove from Firebase
        }
    }

    // Fetch Produk data from Firebase
    fun fetchProdukFromFirebase() {
        produkRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val produkList = mutableListOf<Produk>()
                snapshot.children.forEach {
                    val produk = it.getValue(Produk::class.java)
                    produk?.let { produkList.add(it) }
                }
                _firebaseProduk.postValue(produkList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    // Check if network is available
    private fun isNetworkAvailable(): Boolean {
        // Implement network connectivity check logic here
        return true // Simulate network availability
    }

    // Store data locally when offline
    private fun storeOffline(produkList: List<Produk>) {
        // Implement offline data storage logic (could be done via Room or another local storage mechanism)
    }
}
