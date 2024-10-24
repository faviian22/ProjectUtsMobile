package com.example.projectutsmobile

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectutsmobile.databinding.ActivitySuplierBinding
import com.example.app.adapter.SuplierAdapter
import com.example.app.viewmodel.SuplierViewModel

class SuplierActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuplierBinding
    private lateinit var suplierViewModel: SuplierViewModel
    private lateinit var adapter: SuplierAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuplierBinding.inflate(layoutInflater)
        setContentView(binding.root)


        adapter = SuplierAdapter(
            onEdit = { suplier -> showEditDialog(suplier) },
            onDelete = { suplier -> showDeleteDialog(suplier) }
        )
        binding.recyclerviewSuplier.adapter = adapter
        binding.recyclerviewSuplier.layoutManager = LinearLayoutManager(this)


        suplierViewModel = ViewModelProvider(this).get(SuplierViewModel::class.java)
        suplierViewModel.allSuplier.observe(this, { suplier ->
            suplier?.let { adapter.submitList(it) }  // Gunakan setSuplier untuk memperbarui data
        })

        binding.buttonSaveSuplier.setOnClickListener {
            showAddDialog()
        }
    }

    private fun showAddDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_suplier) // Gunakan layout terpisah untuk menambah suplier

        val editTextNama = dialog.findViewById<EditText>(R.id.editTextNamaSuplier)
        val editTextNoTlpn = dialog.findViewById<EditText>(R.id.editTextNoTlpn)
        val editTextAlamat = dialog.findViewById<EditText>(R.id.editTextAlamatSuplier)
        val editTextNamaProduk = dialog.findViewById<EditText>(R.id.editTextNamaProduk)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSaveSuplier)

        buttonSave.setOnClickListener {
            val nama = editTextNama.text.toString()
            val noTlpn = editTextNoTlpn.text.toString()
            val alamat = editTextAlamat.text.toString()
            val namaProduk = editTextNamaProduk.text.toString()
            val suplier = Suplier(0, nama, noTlpn, alamat, namaProduk)
            suplierViewModel.insert(suplier)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showEditDialog(suplier: Suplier) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_edit_suplier) // Gunakan layout terpisah untuk edit suplier

        val editTextNama = dialog.findViewById<EditText>(R.id.editTextNamaSuplier)
        val editTextNoTlpn = dialog.findViewById<EditText>(R.id.editTextNoTlpn)
        val editTextAlamat = dialog.findViewById<EditText>(R.id.editTextAlamatSuplier)
        val editTextNamaProduk = dialog.findViewById<EditText>(R.id.editTextNamaProduk)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSaveSuplier)


        editTextNama.setText(suplier.nama_suplier)
        editTextNoTlpn.setText(suplier.no_Tlpn)
        editTextAlamat.setText(suplier.alamat_suplier)
        editTextNamaProduk.setText(suplier.nama_produk)

        buttonSave.setOnClickListener {
            val idSuplier = suplier.id_suplier
            val namaSuplier = editTextNama.text.toString()
            val noTlpn = editTextNoTlpn.text.toString()
            val alamatSuplier = editTextAlamat.text.toString()
            val namaProduk = editTextNamaProduk.text.toString()
            val suplierBaru = Suplier(idSuplier, namaSuplier, noTlpn, alamatSuplier, namaProduk)
            suplierViewModel.update(suplierBaru)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDeleteDialog(suplier: Suplier) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_delete_suplier) // Gunakan layout kustom untuk dialog delete

        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)
        val buttonConfirmDelete = dialog.findViewById<Button>(R.id.buttonConfirmDelete)

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

          buttonConfirmDelete.setOnClickListener {
            suplierViewModel.delete(suplier)
            dialog.dismiss()
        }

        dialog.show()
    }

}