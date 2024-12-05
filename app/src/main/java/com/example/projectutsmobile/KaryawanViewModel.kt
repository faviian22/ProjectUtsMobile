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

class KaryawanViewModel(application: Application) : AndroidViewModel(application) {

    private val karyawanDao = BakeryDatabase.getDatabase(application).karyawanDao()
    val allKaryawan: LiveData<List<Karyawan>> = karyawanDao.getAllKaryawan()

    // Firebase Database reference
    private val database = FirebaseDatabase.getInstance()
    private val karyawanRef: DatabaseReference = database.getReference("karyawan")

    // LiveData for Firebase
    private val _firebaseKaryawan = MutableLiveData<List<Karyawan>>()
    val firebaseKaryawan: LiveData<List<Karyawan>> get() = _firebaseKaryawan

    // Insert Karyawan in both Room and Firebase
    fun insert(karyawanList: List<Karyawan>) = viewModelScope.launch(Dispatchers.IO) {
        // Generate unique IDs for each karyawan
        val karyawanWithUniqueIds = karyawanList.map { karyawan ->
            val uniqueId = UUID.randomUUID().mostSignificantBits
            karyawan.copy(id_karyawan = uniqueId.toInt())
        }

        // Insert into Room database
        karyawanDao.insertAll(karyawanWithUniqueIds)

        // Insert into Firebase Realtime Database if online
        if (isNetworkAvailable()) {
            karyawanWithUniqueIds.forEach { karyawan ->
                karyawanRef.child(karyawan.id_karyawan.toString()).setValue(karyawan)  // Insert to Firebase
            }
        } else {
            // If offline, store locally for later sync
            storeOffline(karyawanWithUniqueIds)
        }
    }

    // Update Karyawan in both Room and Firebase
    fun update(karyawanList: List<Karyawan>) = viewModelScope.launch(Dispatchers.IO) {
        // Update in Room database
        karyawanDao.updateAll(karyawanList)

        // Update in Firebase Realtime Database if online
        if (isNetworkAvailable()) {
            karyawanList.forEach { karyawan ->
                karyawanRef.child(karyawan.id_karyawan.toString()).setValue(karyawan)  // Update in Firebase
            }
        }
    }

    // Delete Karyawan in both Room and Firebase
    fun delete(karyawan: Karyawan) = viewModelScope.launch(Dispatchers.IO) {
        // Delete from Room database
        karyawanDao.delete(karyawan)

        // Delete from Firebase Realtime Database
        if (isNetworkAvailable()) {
            karyawanRef.child(karyawan.id_karyawan.toString()).removeValue()  // Remove from Firebase
        }
    }

    // Fetch Karyawan data from Firebase
    fun fetchKaryawanFromFirebase() {
        karyawanRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val karyawanList = mutableListOf<Karyawan>()
                snapshot.children.forEach {
                    val karyawan = it.getValue(Karyawan::class.java)
                    karyawan?.let { karyawanList.add(it) }
                }
                _firebaseKaryawan.postValue(karyawanList)
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
    private fun storeOffline(karyawanList: List<Karyawan>) {
        // Implement offline data storage logic (could be done via Room or another local storage mechanism)
    }
}
