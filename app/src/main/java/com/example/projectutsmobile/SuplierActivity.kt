package com.example.projectutsmobile

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectutsmobile.databinding.ActivitySuplierBinding

class SuplierActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuplierBinding
    private val suplierViewModel: SuplierViewModel by viewModels()
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

        // Observe ViewModel data
        suplierViewModel.firebaseSuplier.observe(this) { suplierList ->
            suplierList?.let {
                val sortedList = it.sortedBy { suplier -> suplier.nama_suplier.lowercase() }
                adapter.submitList(sortedList)
            }
        }

        // Fetch data from Firebase
        suplierViewModel.fetchSuplierFromFirebase()

        // Set up add button to show the add dialog
        binding.buttonSaveSuplier.setOnClickListener {
            showAddDialog()
        }

        // Set up SearchView
        binding.searchViewSuplier.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchSuplier(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchSuplier(it) }
                return false
            }
        })
    }

    private fun searchSuplier(query: String) {
        val filteredList = suplierViewModel.firebaseSuplier.value?.filter {
            it.nama_suplier.contains(query, ignoreCase = true)
        }?.sortedBy { it.nama_suplier.lowercase() }
        filteredList?.let {
            adapter.submitList(it)
        }
    }

    private fun showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, length).show()
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
                suplierViewModel.insert(newSuplier)
                dialog.dismiss()
                showToast("Suplier berhasil ditambahkan", Toast.LENGTH_SHORT)
            } else {
                showToast("Harap isi semua bagian dengan data yang valid", Toast.LENGTH_LONG)
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
                suplierViewModel.update(updatedSuplier)
                dialog.dismiss()
                showToast("Suplier berhasil diperbarui", Toast.LENGTH_SHORT)
            } else {
                showToast("Harap isi semua bagian dengan data yang valid", Toast.LENGTH_LONG)
            }
        }

        dialog.show()
    }

    private fun showDeleteDialog(suplier: Suplier) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_delete_suplier)
        dialog.setCancelable(true)

        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)
        val buttonConfirmDelete = dialog.findViewById<Button>(R.id.buttonConfirmDelete)

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        buttonConfirmDelete.setOnClickListener {
            suplierViewModel.delete(suplier)
            dialog.dismiss()
            showToast("Suplier berhasil dihapus", Toast.LENGTH_SHORT)
        }

        dialog.show()
    }
}
