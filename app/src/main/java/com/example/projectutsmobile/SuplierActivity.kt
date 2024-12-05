package com.example.projectutsmobile

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectutsmobile.databinding.ActivitySuplierBinding

class SuplierActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuplierBinding
    private lateinit var suplierViewModel: SuplierViewModel
    private lateinit var adapter: SuplierAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuplierBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        adapter = SuplierAdapter(
            onEdit = { suplier -> showEditDialog(suplier) },
            onDelete = { suplier -> showDeleteDialog(suplier) }
        )
        binding.recyclerviewSuplier.layoutManager = LinearLayoutManager(this)
        binding.recyclerviewSuplier.adapter = adapter

        // Initialize ViewModel and observe LiveData
        suplierViewModel = ViewModelProvider(this).get(SuplierViewModel::class.java)
        suplierViewModel.allSuplier.observe(this) { suplierList ->
            suplierList?.let {
                adapter.submitList(it.sortedBy { suplier -> suplier.nama_suplier })
            }
        }

        // Fetch data from Firebase
        suplierViewModel.fetchSuplierFromFirebase()

        // Add button action
        binding.buttonSaveSuplier.setOnClickListener {
            showAddDialog()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun createDialog(layoutResId: Int): Dialog {
        val dialog = Dialog(this)
        dialog.setContentView(layoutResId)
        dialog.setCancelable(true)
        return dialog
    }

    private fun isValidInput(vararg fields: String): Boolean {
        return fields.all { it.isNotEmpty() }
    }

    private fun showAddDialog() {
        val dialog = createDialog(R.layout.dialog_add_suplier)

        val editTextNama = dialog.findViewById<EditText>(R.id.editTextNamaSuplier)
        val editTextNoTlpn = dialog.findViewById<EditText>(R.id.editTextNoTlpn)
        val editTextAlamat = dialog.findViewById<EditText>(R.id.editTextAlamatSuplier)
        val editTextNamaProduk = dialog.findViewById<EditText>(R.id.editTextNamaProduk)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSaveSuplier)

        buttonSave.setOnClickListener {
            val nama = editTextNama.text.toString().trim()
            val noTlpn = editTextNoTlpn.text.toString().trim()
            val alamat = editTextAlamat.text.toString().trim()
            val namaProduk = editTextNamaProduk.text.toString().trim()

            if (isValidInput(nama, noTlpn, alamat, namaProduk)) {
                val newSuplier = Suplier(0, nama, noTlpn, alamat, namaProduk)
                suplierViewModel.insert(listOf(newSuplier))  // Insert as a list
                dialog.dismiss()
                showToast("Suplier added successfully")
            } else {
                showToast("Please fill all fields")
            }
        }

        dialog.show()
    }

    private fun showEditDialog(suplier: Suplier) {
        val dialog = createDialog(R.layout.dialog_edit_suplier)

        val editTextNama = dialog.findViewById<EditText>(R.id.editTextNamaSuplier)
        val editTextNoTlpn = dialog.findViewById<EditText>(R.id.editTextNoTlpn)
        val editTextAlamat = dialog.findViewById<EditText>(R.id.editTextAlamatSuplier)
        val editTextNamaProduk = dialog.findViewById<EditText>(R.id.editTextNamaProduk)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSaveSuplier)

        // Prefill data
        editTextNama.setText(suplier.nama_suplier)
        editTextNoTlpn.setText(suplier.no_Tlpn)
        editTextAlamat.setText(suplier.alamat_suplier)
        editTextNamaProduk.setText(suplier.nama_produk)

        buttonSave.setOnClickListener {
            val nama = editTextNama.text.toString().trim()
            val noTlpn = editTextNoTlpn.text.toString().trim()
            val alamat = editTextAlamat.text.toString().trim()
            val namaProduk = editTextNamaProduk.text.toString().trim()

            if (isValidInput(nama, noTlpn, alamat, namaProduk)) {
                val updatedSuplier = suplier.copy(
                    nama_suplier = nama,
                    no_Tlpn = noTlpn,
                    alamat_suplier = alamat,
                    nama_produk = namaProduk
                )
                suplierViewModel.update(listOf(updatedSuplier))  // Update as a list
                dialog.dismiss()
                showToast("Suplier updated successfully")
            } else {
                showToast("Please fill all fields")
            }
        }

        dialog.show()
    }

    private fun showDeleteDialog(suplier: Suplier) {
        val dialog = createDialog(R.layout.dialog_delete_suplier)

        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)
        val buttonConfirmDelete = dialog.findViewById<Button>(R.id.buttonConfirmDelete)

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        buttonConfirmDelete.setOnClickListener {
            suplierViewModel.delete(suplier)
            dialog.dismiss()
            showToast("Suplier deleted successfully")
        }

        dialog.show()
    }
}