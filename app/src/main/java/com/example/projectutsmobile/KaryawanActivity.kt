package com.example.projectutsmobile

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projectutsmobile.databinding.ActivityKaryawanBinding

class KaryawanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKaryawanBinding
    private lateinit var karyawanViewModel: KaryawanViewModel
    private lateinit var adapter: KaryawanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKaryawanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the adapter with edit and delete click listeners
        adapter = KaryawanAdapter(
            onEditClick = { karyawan -> showEditDialog(karyawan) },
            onDeleteClick = { karyawan -> showDeleteDialog(karyawan) }
        )
        binding.recyclerviewKaryawan.adapter = adapter

        // Set up GridLayoutManager with support for headers
        val manager = GridLayoutManager(this, 2) // Two columns
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    KaryawanAdapter.VIEW_TYPE_HEADER -> 2 // Headers span both columns
                    else -> 1 // Regular items take one column
                }
            }
        }
        binding.recyclerviewKaryawan.layoutManager = manager

        // Initialize ViewModel and observe data
        karyawanViewModel = ViewModelProvider(this).get(KaryawanViewModel::class.java)
        karyawanViewModel.allKaryawan.observe(this) { karyawanList ->
            karyawanList?.let {
                adapter.submitList(it) // Submit the list of karyawan to the adapter
            }
        }

        // Set up add button to show the add dialog
        binding.buttonSaveKaryawan.setOnClickListener {
            showAddDialog()
        }
    }

    // Helper function to show Toast messages
    private fun showToast(message: String, length: Int) {
        Toast.makeText(this, message, length).show()
    }

    // Helper function to create and return a dialog
    private fun createDialog(layoutResId: Int): Dialog {
        val dialog = Dialog(this)
        dialog.setContentView(layoutResId)
        dialog.setCancelable(true)
        return dialog
    }

    // Function to setup gender spinner
    private fun setupGenderSpinner(spinner: Spinner) {
        ArrayAdapter.createFromResource(
            this,
            R.array.jenis_kelamin_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    // Function to validate input fields
    private fun isValidInput(nama: String, jenisKelamin: String, alamat: String): Boolean {
        return nama.isNotEmpty() && jenisKelamin.isNotEmpty() && alamat.isNotEmpty()
    }

    // Show dialog for adding new employee
    // In KaryawanActivity.kt
    private fun showAddDialog() {
        val dialog = createDialog(R.layout.dialog_add_karyawan)

        val editTextNama = dialog.findViewById<EditText>(R.id.editTextNamaKaryawan)
        val spinnerJenisKelamin = dialog.findViewById<Spinner>(R.id.spinnerJenisKelamin)
        val editTextAlamat = dialog.findViewById<EditText>(R.id.editTextAlamatKaryawan)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSaveKaryawan)

        // Ensure spinner setup
        setupGenderSpinner(spinnerJenisKelamin)

        buttonSave.setOnClickListener {
            val nama = editTextNama.text.toString().trim()
            val jenisKelamin = spinnerJenisKelamin.selectedItem?.toString().orEmpty()
            val alamat = editTextAlamat.text.toString().trim()

            if (isValidInput(nama, jenisKelamin, alamat)) {
                // Create new Karyawan object
                val karyawanBaru = Karyawan(0, nama, jenisKelamin, alamat)
                karyawanViewModel.insert(karyawanBaru) // Insert into database
                dialog.dismiss() // Close the dialog after insert
                showToast("Karyawan added successfully", Toast.LENGTH_SHORT)
            } else {
                showToast("Please fill all fields", Toast.LENGTH_LONG)
            }
        }

        dialog.show()
    }

    private fun showEditDialog(karyawan: Karyawan) {
        val dialog = createDialog(R.layout.dialog_edit_karyawan)

        val editTextNama = dialog.findViewById<EditText>(R.id.editTextNamaKaryawan)
        val spinnerJenisKelamin = dialog.findViewById<Spinner>(R.id.spinnerJenisKelaminKaryawan)
        val editTextAlamat = dialog.findViewById<EditText>(R.id.editTextAlamatKaryawan)
        val buttonUpdate = dialog.findViewById<Button>(R.id.buttonEdit)

        setupGenderSpinner(spinnerJenisKelamin)

        // Pre-fill data
        editTextNama.setText(karyawan.nama_karyawan)
        editTextAlamat.setText(karyawan.alamat_karyawan)

        val genderOptions = resources.getStringArray(R.array.jenis_kelamin_options)
        val genderIndex = genderOptions.indexOf(karyawan.jenis_kelamin)
        if (genderIndex >= 0) {
            spinnerJenisKelamin.setSelection(genderIndex)
        }

        buttonUpdate.setOnClickListener {
            val updatedKaryawan = karyawan.copy(
                nama_karyawan = editTextNama.text.toString().trim(),
                jenis_kelamin = spinnerJenisKelamin.selectedItem.toString(),
                alamat_karyawan = editTextAlamat.text.toString().trim()
            )

            if (isValidInput(updatedKaryawan.nama_karyawan, updatedKaryawan.jenis_kelamin, updatedKaryawan.alamat_karyawan)) {
                karyawanViewModel.update(updatedKaryawan)
                dialog.dismiss()
                showToast("Karyawan updated successfully", Toast.LENGTH_SHORT)
            } else {
                showToast("Please fill all fields", Toast.LENGTH_LONG)
            }
        }

        dialog.show()
    }


    // Show dialog for deleting an employee
    private fun showDeleteDialog(karyawan: Karyawan) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Delete Employee")
            .setMessage("Are you sure you want to delete this employee?")
            .setPositiveButton("Delete") { _, _ ->
                karyawanViewModel.delete(karyawan)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}