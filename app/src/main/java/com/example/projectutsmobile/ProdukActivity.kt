package com.example.projectutsmobile

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
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

        // Inisialisasi adapter dengan callback untuk edit dan delete
        adapter = ProdukAdapter(
            onEditClick = { produk -> showEditDialog(produk) },
            onDeleteClick = { produk -> showDeleteDialog(produk) }
        )
        binding.recyclerviewProduk.adapter = adapter

        // Grid Layout Manager
        val manager = GridLayoutManager(this, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    ProdukAdapter.VIEW_TYPE_HEADER_LOW_STOK, ProdukAdapter.VIEW_TYPE_HEADER_SAFE_STOK -> 2
                    else -> 1
                }
            }
        }
        binding.recyclerviewProduk.layoutManager = manager

        // Observasi data produk dari ViewModel
        produkViewModel.allProduk.observe(this) { produkList ->
            produkList?.let { adapter.submitList(it) }
        }

        // Tombol untuk menambahkan produk baru
        binding.buttonSaveProduk.setOnClickListener {
            showAddDialog()
        }
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

            if (nama.isEmpty()) {
                showToast("Nama produk tidak boleh kosong", Toast.LENGTH_LONG)
                return@setOnClickListener
            }

            if (stok == null || stok < 0) {
                showToast("Stok harus berupa angka valid dan tidak boleh negatif", Toast.LENGTH_LONG)
                return@setOnClickListener
            }

            if (harga == null || harga <= 0) {
                showToast("Harga harus berupa angka valid dan lebih besar dari nol", Toast.LENGTH_LONG)
                return@setOnClickListener
            }

            val produkBaru = Produk(0, nama, stok, satuan, harga)
            produkViewModel.insert(produkBaru)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showEditDialog(produk: Produk) {
        val dialog = createDialog(R.layout.dialog_edit_produk)

        val editTextNama = dialog.findViewById<EditText>(R.id.editTextNamaProduk)
        val editTextStok = dialog.findViewById<EditText>(R.id.editTextStokProduk)
        val editTextSatuan = dialog.findViewById<EditText>(R.id.editTextSatuanProduk)
        val editTextHarga = dialog.findViewById<EditText>(R.id.editTextHargaProduk)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSaveProduk)

        editTextNama.setText(produk.namaProduk)
        editTextStok.setText(produk.stokProduk.toString())
        editTextSatuan.setText(produk.satuanProduk)
        editTextHarga.setText(produk.hargaProduk.toString())

        buttonSave.setOnClickListener {
            val nama = editTextNama.text.toString().trim()
            val stok = editTextStok.text.toString().toIntOrNull()
            val satuan = editTextSatuan.text.toString().trim()
            val harga = editTextHarga.text.toString().toIntOrNull()

            if (nama.isEmpty()) {
                showToast("Nama produk tidak boleh kosong", Toast.LENGTH_LONG)
                return@setOnClickListener
            }

            if (stok == null || stok < 0) {
                showToast("Stok harus berupa angka valid dan tidak boleh negatif", Toast.LENGTH_LONG)
                return@setOnClickListener
            }

            if (harga == null || harga <= 0) {
                showToast("Harga harus berupa angka valid dan lebih besar dari nol", Toast.LENGTH_LONG)
                return@setOnClickListener
            }

            val updatedProduk = produk.copy(namaProduk = nama, stokProduk = stok, satuanProduk = satuan, hargaProduk = harga)
            produkViewModel.update(updatedProduk)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDeleteDialog(produk: Produk) {
        val dialog = createDialog(R.layout.dialog_delete_produk)

        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)
        val buttonConfirmDelete = dialog.findViewById<Button>(R.id.buttonConfirmDelete)

        buttonCancel.setOnClickListener { dialog.dismiss() }
        buttonConfirmDelete.setOnClickListener {
            produkViewModel.delete(produk)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showToast(message: String, length: Int) {
        Toast.makeText(this, message, length).show()
    }

    private fun createDialog(layoutResId: Int): Dialog {
        val dialog = Dialog(this)
        dialog.setContentView(layoutResId)
        dialog.setCancelable(false)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return dialog
    }
}