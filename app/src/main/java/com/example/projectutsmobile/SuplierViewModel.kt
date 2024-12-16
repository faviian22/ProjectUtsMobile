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

    init {
        fetchSuplierFromFirebase()
    }

    // Insert Suplier in both Room and Firebase
    fun insert(suplier: Suplier) = viewModelScope.launch(Dispatchers.IO) {
        val uniqueId = suplier.id_suplier.takeIf { it != 0 } ?: suplierRef.push().key?.hashCode() ?: 0
        val suplierWithId = suplier.copy(id_suplier = uniqueId)

        suplierDao.insert(suplierWithId)

        if (isNetworkAvailable()) {
            suplierRef.child(suplierWithId.id_suplier.toString()).setValue(suplierWithId)
        }
    }

    // Update Suplier in both Room and Firebase
    fun update(suplier: Suplier) = viewModelScope.launch(Dispatchers.IO) {
        suplierDao.update(suplier)

        if (isNetworkAvailable()) {
            suplierRef.child(suplier.id_suplier.toString()).setValue(suplier)
        }
    }

    // Delete Suplier in both Room and Firebase
    fun delete(suplier: Suplier) = viewModelScope.launch(Dispatchers.IO) {
        suplierDao.delete(suplier)

        if (isNetworkAvailable()) {
            suplierRef.child(suplier.id_suplier.toString()).removeValue()
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
        // Simulate network check, replace with actual network check logic
        return true
    }

    // Store data offline if network is unavailable (optional)
    private fun storeOffline(suplierList: List<Suplier>) {
        // Implement offline storage if needed
    }
}
