package com.example.projectutsmobile

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectutsmobile.Karyawan
import com.example.projectutsmobile.KaryawanAdapter
import com.example.projectutsmobile.KaryawanViewModel
import com.example.projectutsmobile.R
import com.example.projectutsmobile.databinding.ActivityKaryawanBinding

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
            karyawan?.let { adapter.setKaryawan(it) }
        })

        // Tombol untuk menambahkan karyawan baru
        binding.buttonSaveKaryawan.setOnClickListener {
            showAddDialog()
        }
    }

    private fun showAddDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_karyawan)

        val editTextNama = dialog.findViewById<EditText>(R.id.editTextNamaKaryawan)
        val editTextJenisKelamin = dialog.findViewById<EditText>(R.id.editTextJenisKelaminKaryawan)
        val editTextAlamat = dialog.findViewById<EditText>(R.id.editTextAlamatKaryawan)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSaveKaryawan)

        // Menyimpan data karyawan baru ketika tombol Save diklik
        buttonSave.setOnClickListener {
            val nama = editTextNama.text.toString()
            val jenisKelamin = editTextJenisKelamin.text.toString()
            val alamat = editTextAlamat.text.toString()
            val karyawanBaru = Karyawan(0, nama, jenisKelamin, alamat) // ID = 0, akan di-generate otomatis
            karyawanViewModel.insert(karyawanBaru)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showEditDialog(karyawan: Karyawan) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_edit_karyawan)

        val editTextNama = dialog.findViewById<EditText>(R.id.editTextNamaKaryawan)
        val editTextJenisKelamin = dialog.findViewById<EditText>(R.id.editTextJenisKelaminKaryawan)
        val editTextAlamat = dialog.findViewById<EditText>(R.id.editTextAlamatKaryawan)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSaveKaryawan)

        // Set existing details
        editTextNama.setText(karyawan.nama_karyawan)
        editTextJenisKelamin.setText(karyawan.jenis_kelamin)
        editTextAlamat.setText(karyawan.alamat_karyawan)

        buttonSave.setOnClickListener {
            val nama = editTextNama.text.toString()
            val jenisKelamin = editTextJenisKelamin.text.toString()
            val alamat = editTextAlamat.text.toString()
            val karyawanUpdate = Karyawan(karyawan.id_karyawan, nama, jenisKelamin, alamat)
            karyawanViewModel.update(karyawanUpdate)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDeleteDialog(karyawan: Karyawan) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_delete_karyawan)

        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)
        val buttonConfirmDelete = dialog.findViewById<Button>(R.id.buttonConfirmDelete)

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        buttonConfirmDelete.setOnClickListener {
            karyawanViewModel.delete(karyawan)
            dialog.dismiss()
        }

        dialog.show()
    }
}