package com.example.projectutsmobile

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectutsmobile.R
import com.example.projectutsmobile.databinding.ActivityKaryawanBinding
import com.example.app.adapter.KaryawanAdapter
import com.example.app.model.Karyawan


class KaryawanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKaryawanBinding
    private lateinit var karyawanViewModel: KaryawanViewModel
    private lateinit var adapter: KaryawanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKaryawanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menginisialisasi Adapter
        adapter = KaryawanAdapter(
            onEditClick = { karyawan -> showEditDialog(karyawan) },
            onDeleteClick = { karyawan -> showDeleteDialog(karyawan) }
        )
        binding.recyclerviewKaryawan.adapter = adapter
        binding.recyclerviewKaryawan.layoutManager = LinearLayoutManager(this)

        // Observasi LiveData dari ViewModel dan update adapter ketika data berubah
        karyawanViewModel = ViewModelProvider(this).get(KaryawanViewModel::class.java)
        karyawanViewModel.allKaryawan.observe(this, { karyawan ->
            karyawan?.let { adapter.setKaryawan(it) }  // Gunakan setKaryawan untuk memperbarui data
        })

        // Tombol untuk menambahkan karyawan baru
        binding.buttonSaveKaryawan.setOnClickListener {
            showAddDialog()
        }
    }

    private fun showAddDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_karyawan) // Use a separate layout for adding products

        val editTextId = dialog.findViewById<EditText>(R.id.editTextIdKaryawan)
        val editTextNama = dialog.findViewById<EditText>(R.id.editTextNamaKaryawan)
        val editTextJenis_Kelamin = dialog.findViewById<EditText>(R.id.editTextJenisKelaminKaryawan)
        val editTextAlamat = dialog.findViewById<EditText>(R.id.editTextAlamatKaryawan)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSaveKaryawan)

        buttonSave.setOnClickListener {
            val id = editTextId.text.toString().toIntOrNull() ?: 0
            val nama = editTextNama.text.toString()
            val jenis_kelamin = editTextJenis_Kelamin.text.toString()
            val alamat = editTextAlamat.text.toString()
            val karyawan = Karyawan(id, nama, jenis_kelamin, alamat)
            karyawanViewModel.insert(karyawan)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showEditDialog(karyawan: Karyawan) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_edit_karyawan) // Use a separate layout for editing products

        val editTextNama = dialog.findViewById<EditText>(R.id.editTextNamaKaryawan)
        val editTextJenisKelamin = dialog.findViewById<EditText>(R.id.editTextJenisKelaminKaryawan)
        val editTextAlamat = dialog.findViewById<EditText>(R.id.editTextAlamatKaryawan)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSaveKaryawan)

        // Set existing product details in the dialog
        editTextNama.setText(karyawan.nama_karyawan)
        editTextJenisKelamin.setText(karyawan.jenis_kelamin)
        editTextAlamat.setText(karyawan.alamat_karyawan)

        buttonSave.setOnClickListener {
            val idKaryawan = karyawan.id_karyawan
            val namaKaryawan = editTextNama.text.toString()
            val jenisKelamin = editTextJenisKelamin.text.toString()
            val alamatKaryawan = editTextAlamat.text.toString()
            val karyawanBaru = Karyawan(idKaryawan, namaKaryawan, jenisKelamin, alamatKaryawan)
            karyawanViewModel.update(karyawanBaru) // Update product
            dialog.dismiss()
        }

        dialog.show()
    }
    private fun showDeleteDialog(karyawan: Karyawan) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_delete_karyawan) // Gunakan layout kustom untuk dialog delete

        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)
        val buttonConfirmDelete = dialog.findViewById<Button>(R.id.buttonConfirmDelete)

        // Ketika tombol "Tidak" diklik, tutup dialog
        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        // Ketika tombol "Ya" diklik, hapus suplier dan tutup dialog
        buttonConfirmDelete.setOnClickListener {
            karyawanViewModel.delete(karyawan)
            dialog.dismiss()
        }

        dialog.show() // Tampilkan dialog
    }

}