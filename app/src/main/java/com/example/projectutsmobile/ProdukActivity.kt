package com.example.projectutsmobile

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projectutsmobile.databinding.ActivityProdukBinding

class ProdukActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProdukBinding
    private val produkViewModel: ProdukViewModel by viewModels()
    private lateinit var adapter: ProdukAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        adapter = ProdukAdapter(
            onEditClick = { produk -> showEditDialog(produk) },
            onDeleteClick = { produk -> showDeleteDialog(produk) }
        )
        val layoutManager = GridLayoutManager(this, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (adapter.getItemViewType(position)) {
                        ProdukAdapter.VIEW_TYPE_HEADER_LOW_STOK, ProdukAdapter.VIEW_TYPE_HEADER_SAFE_STOK -> 2 // Header full-width
                        ProdukAdapter.VIEW_TYPE_ITEM -> 1 // Item produk satu kolom
                        else -> 1
                    }
                }
            }
        }
        binding.recyclerviewProduk.layoutManager = layoutManager
        binding.recyclerviewProduk.adapter = adapter

        // Observe ViewModel data
        produkViewModel.allProduk.observe(this) { produkList ->
            produkList?.let { adapter.submitList(it) }
        }

        // Set up add button to show the add dialog
        binding.buttonSaveProduk.setOnClickListener {
            showAddDialog()
        }
    }

    private fun showToast(message: String, length: Int) {
        Toast.makeText(this, message, length).show()
    }

    private fun createDialog(layoutResId: Int): Dialog {
        val dialog = Dialog(this)
        dialog.setContentView(layoutResId)
        dialog.setCancelable(true)
        return dialog
    }

    private fun isValidInput(nama: String, stok: Int?, satuan: String, harga: Int?): Boolean {
        return nama.isNotEmpty() && stok != null && stok >= 0 && satuan.isNotEmpty() && harga != null && harga > 0
    }

    private fun showAddDialog() {
        val dialog = createDialog(R.layout.dialog_add_produk)

        val editTextNama = dialog.findViewById<EditText>(R.id.editTextNamaProduk)
        val editTextStok = dialog.findViewById<EditText>(R.id.editTextStokProduk)
        val editTextSatuan = dialog.findViewById<EditText>(R.id.editTextSatuanProduk)
        val editTextHarga = dialog.findViewById<EditText>(R.id.editTextHargaProduk)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSaveProduk)

        buttonSave.setOnClickListener {
            val nama = editTextNama.text.toString().trim()
            val stok = editTextStok.text.toString().toIntOrNull()
            val satuan = editTextSatuan.text.toString().trim()
            val harga = editTextHarga.text.toString().toIntOrNull()

            if (isValidInput(nama, stok, satuan, harga)) {
                val produkBaru = Produk(0, nama, stok!!, satuan, harga!!)
                produkViewModel.insert(produkBaru)
                dialog.dismiss()
                showToast("Produk added successfully", Toast.LENGTH_SHORT)
            } else {
                showToast("Please fill all fields with valid data", Toast.LENGTH_LONG)
            }
        }

        dialog.show()
    }

    private fun showEditDialog(produk: Produk) {
        val dialog = createDialog(R.layout.dialog_edit_produk)

        val editTextNama = dialog.findViewById<EditText>(R.id.editTextNamaProduk)
        val editTextStok = dialog.findViewById<EditText>(R.id.editTextStokProduk)
        val editTextSatuan = dialog.findViewById<EditText>(R.id.editTextSatuanProduk)
        val editTextHarga = dialog.findViewById<EditText>(R.id.editTextHargaProduk)
        val buttonUpdate = dialog.findViewById<Button>(R.id.buttonSaveProduk)

        editTextNama.setText(produk.namaProduk)
        editTextStok.setText(produk.stokProduk.toString())
        editTextSatuan.setText(produk.satuanProduk)
        editTextHarga.setText(produk.hargaProduk.toString())

        buttonUpdate.setOnClickListener {
            val nama = editTextNama.text.toString().trim()
            val stok = editTextStok.text.toString().toIntOrNull()
            val satuan = editTextSatuan.text.toString().trim()
            val harga = editTextHarga.text.toString().toIntOrNull()

            if (isValidInput(nama, stok, satuan, harga)) {
                val updatedProduk = produk.copy(
                    namaProduk = nama,
                    stokProduk = stok!!,
                    satuanProduk = satuan,
                    hargaProduk = harga!!
                )
                produkViewModel.update(updatedProduk)
                dialog.dismiss()
                showToast("Produk updated successfully", Toast.LENGTH_SHORT)
            } else {
                showToast("Please fill all fields with valid data", Toast.LENGTH_LONG)
            }
        }

        dialog.show()
    }

    private fun showDeleteDialog(produk: Produk) {
        val dialog = AlertDialog.Builder(this)
            .setMessage("Are you sure you want to delete this produk?")
            .setPositiveButton("Yes") { _, _ ->
                produkViewModel.delete(produk)
                showToast("Produk deleted successfully", Toast.LENGTH_SHORT)
            }
            .setNegativeButton("No", null)
            .create()

        dialog.show()
    }
}