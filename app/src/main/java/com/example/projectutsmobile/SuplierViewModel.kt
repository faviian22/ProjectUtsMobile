package com.example.projectutsmobile

import android.app.Application
import android.util.Log
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
class SuplierViewModel(application: Application) : AndroidViewModel(application) {

    private val suplierDao = BakeryDatabase.getDatabase(application).suplierDao()
    val allSuplier: LiveData<List<Suplier>> = suplierDao.getAllSuplier()

    // Firebase Database reference
    private val database = FirebaseDatabase.getInstance()
    private val suplierRef: DatabaseReference = database.getReference("suplier")

    // LiveData for Firebase
    private val _firebaseSuplier = MutableLiveData<List<Suplier>>()
    val firebaseSuplier: LiveData<List<Suplier>> get() = _firebaseSuplier

    // Insert Suplier in both Room and Firebase
    fun insert(suplierList: List<Suplier>) = viewModelScope.launch(Dispatchers.IO) {
        // Generate unique IDs for each suplier
        val suplierWithUniqueIds = suplierList.map { suplier ->
            val uniqueId = UUID.randomUUID().mostSignificantBits
            suplier.copy(id_suplier = uniqueId.toInt())
        }

        // Insert into Room database
        suplierDao.insertAll(suplierWithUniqueIds)

        // Insert into Firebase Realtime Database if online
        if (isNetworkAvailable()) {
            suplierWithUniqueIds.forEach { suplier ->
                suplierRef.child(suplier.id_suplier.toString()).setValue(suplier)  // Insert to Firebase
            }
        } else {
            // If offline, store locally for later sync
            storeOffline(suplierWithUniqueIds)
        }
    }

    // Update Suplier in both Room and Firebase
    fun update(suplierList: List<Suplier>) = viewModelScope.launch(Dispatchers.IO) {
        // Update in Room database
        suplierDao.updateAll(suplierList)

        // Update in Firebase Realtime Database if online
        if (isNetworkAvailable()) {
            suplierList.forEach { suplier ->
                suplierRef.child(suplier.id_suplier.toString()).setValue(suplier)  // Update in Firebase
            }
        }
    }

    // Delete Suplier in both Room and Firebase
    fun delete(suplier: Suplier) = viewModelScope.launch(Dispatchers.IO) {
        // Delete from Room database
        suplierDao.delete(suplier)

        // Delete from Firebase Realtime Database
        if (isNetworkAvailable()) {
            suplierRef.child(suplier.id_suplier.toString()).removeValue()  // Remove from Firebase
        }
    }

    // Fetch Suplier data from Firebase
    fun fetchSuplierFromFirebase() {
        suplierRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val suplierList = mutableListOf<Suplier>()
                for (dataSnapshot in snapshot.children) {
                    val suplier = dataSnapshot.getValue(Suplier::class.java)
                    suplier?.let { suplierList.add(it) }
                }
                _firebaseSuplier.value = suplierList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error fetching supliers: ${error.message}")
            }
        })
    }

    // Check network availability (simulated for now)
    private fun isNetworkAvailable(): Boolean {
        return true  // Simulating always connected for now
    }

    // Store data offline if network is unavailable (optional)
    private fun storeOffline(suplierList: List<Suplier>) {
        // Implement offline storage if needed
    }
}