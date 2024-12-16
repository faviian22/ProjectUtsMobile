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

    init {
        // Fetch data from Firebase when ViewModel is created
        fetchProdukFromFirebase()
    }

    // Insert Produk in both Room and Firebase
    fun insert(produk: Produk) = viewModelScope.launch(Dispatchers.IO) {
        // Generate unique ID for the produk if not already set
        val uniqueId = produk.id_produk.takeIf { it != 0 } ?: produkRef.push().key?.hashCode() ?: 0
        val produkWithId = produk.copy(id_produk = uniqueId)

        // Insert into Room database
        produkDao.insert(produkWithId)

        // Insert into Firebase Realtime Database
        produkRef.child(produkWithId.id_produk.toString()).setValue(produkWithId)
    }

    // Update Produk in both Room and Firebase
    fun update(produk: Produk) = viewModelScope.launch(Dispatchers.IO) {
        // Update in Room database
        produkDao.update(produk)

        // Update in Firebase Realtime Database
        produkRef.child(produk.id_produk.toString()).setValue(produk)
    }

    // Delete Produk in both Room and Firebase
    fun delete(produk: Produk) = viewModelScope.launch(Dispatchers.IO) {
        // Delete from Room database
        produkDao.delete(produk)

        // Delete from Firebase Realtime Database
        produkRef.child(produk.id_produk.toString()).removeValue()
    }

    // Fetch Produk data from Firebase
    fun fetchProdukFromFirebase() {
        produkRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val produkList = mutableListOf<Produk>()
                snapshot.children.forEach { dataSnapshot ->
                    val produk = dataSnapshot.getValue(Produk::class.java)
                    produk?.let {
                        produkList.add(it)
                    }
                }
                _firebaseProduk.postValue(produkList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
