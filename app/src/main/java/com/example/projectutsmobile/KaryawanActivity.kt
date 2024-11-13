package com.example.projectutsmobile

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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

        // Set up adapter with edit and delete click listeners
        adapter = KaryawanAdapter(
            onEditClick = { karyawan -> showEditDialog(karyawan) },
            onDeleteClick = { karyawan -> showDeleteDialog(karyawan) }
        )
        binding.recyclerviewKaryawan.adapter = adapter

        // Set up GridLayoutManager
        val manager = GridLayoutManager(this, 2) // Two columns
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                // Make headers span both columns
                return when (adapter.getItemViewType(position)) {
                    KaryawanAdapter.VIEW_TYPE_HEADER_MALE, KaryawanAdapter.VIEW_TYPE_HEADER_FEMALE -> 2
                    else -> 1
                }
            }
        }
        binding.recyclerviewKaryawan.layoutManager = manager

        // Initialize ViewModel and observe data
        karyawanViewModel = ViewModelProvider(this).get(KaryawanViewModel::class.java)
        karyawanViewModel.allKaryawan.observe(this, { karyawanList ->
            karyawanList?.let { adapter.submitList(it) }
        })

        // Set up add button to show the add dialog
        binding.buttonSaveKaryawan.setOnClickListener {
            showAddDialog()
        }
    }

    private fun showAddDialog() {
        val dialog = createDialog(R.layout.dialog_add_karyawan)

        val editTextNama = dialog.findViewById<EditText>(R.id.editTextNamaKaryawan)
        val editTextJenisKelamin = dialog.findViewById<EditText>(R.id.editTextJenisKelaminKaryawan)
        val editTextAlamat = dialog.findViewById<EditText>(R.id.editTextAlamatKaryawan)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSaveKaryawan)

        buttonSave.setOnClickListener {
            val nama = editTextNama.text.toString().trim()
            val jenisKelamin = editTextJenisKelamin.text.toString().trim()
            val alamat = editTextAlamat.text.toString().trim()
            if (nama.isNotEmpty() && jenisKelamin.isNotEmpty() && alamat.isNotEmpty()) {
                val karyawanBaru = Karyawan(0, nama, jenisKelamin, alamat)
                karyawanViewModel.insert(karyawanBaru)
                dialog.dismiss()
            } else {
                showToast("Please fill all fields", Toast.LENGTH_LONG)
            }
        }

        dialog.show()
    }

    private fun showEditDialog(karyawan: Karyawan) {
        val dialog = createDialog(R.layout.dialog_edit_karyawan)

        val editTextNama = dialog.findViewById<EditText>(R.id.editTextNamaKaryawan)
        val editTextJenisKelamin = dialog.findViewById<EditText>(R.id.editTextJenisKelaminKaryawan)
        val editTextAlamat = dialog.findViewById<EditText>(R.id.editTextAlamatKaryawan)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSaveKaryawan)

        editTextNama.setText(karyawan.nama_karyawan)
        editTextJenisKelamin.setText(karyawan.jenis_kelamin)
        editTextAlamat.setText(karyawan.alamat_karyawan)

        buttonSave.setOnClickListener {
            val updatedKaryawan = karyawan.copy(
                nama_karyawan = editTextNama.text.toString().trim(),
                jenis_kelamin = editTextJenisKelamin.text.toString().trim(),
                alamat_karyawan = editTextAlamat.text.toString().trim()
            )
            if (updatedKaryawan.nama_karyawan.isNotEmpty() && updatedKaryawan.jenis_kelamin.isNotEmpty() && updatedKaryawan.alamat_karyawan.isNotEmpty()) {
                karyawanViewModel.update(updatedKaryawan)
                dialog.dismiss()
            } else {
                showToast("Please fill all fields", Toast.LENGTH_LONG)
            }
        }

        dialog.show()
    }

    private fun showDeleteDialog(karyawan: Karyawan) {
        val dialog = createDialog(R.layout.dialog_delete_karyawan)

        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)
        val buttonConfirmDelete = dialog.findViewById<Button>(R.id.buttonConfirmDelete)

        buttonCancel.setOnClickListener { dialog.dismiss() }
        buttonConfirmDelete.setOnClickListener {
            karyawanViewModel.delete(karyawan)
            dialog.dismiss()
        }
        dialog.show()
    }

    // Helper function to show Toast messages
    private fun showToast(message: String, length: Int) {
        Toast.makeText(this, message, length).show()
    }

    // Helper function to create dialogs
    private fun createDialog(layoutResId: Int): Dialog {
        val dialog = Dialog(this)
        dialog.setContentView(layoutResId)
        return dialog
    }
}
