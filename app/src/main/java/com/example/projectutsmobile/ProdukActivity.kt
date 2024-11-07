package com.example.projectutsmobile

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectutsmobile.databinding.ActivityProdukBinding
import extention.toFormattedPrice // Mengimpor ekstensi untuk memformat harga

class ProdukActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProdukBinding
    private lateinit var produkViewModel: ProdukViewModel
    private lateinit var adapter: ProdukAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()  // Set up RecyclerView
        setupViewModel()     // Set up ViewModel

        // Tombol untuk menambahkan produk
        binding.buttonSaveProduk.setOnClickListener {
            showAddDialog()
        }
    }

    private fun setupRecyclerView() {
        // Menginisialisasi adapter dan layout manager untuk RecyclerView
        adapter = ProdukAdapter(
            onEdit = { produk -> showEditDialog(produk) },
            onDelete = { produk -> showDeleteDialog(produk) }
        )

        // Mengatur layout manager untuk RecyclerView
        binding.recyclerviewProduk.layoutManager = LinearLayoutManager(this)
        binding.recyclerviewProduk.adapter = adapter
    }

    private fun setupViewModel() {
        // Setup ViewModel untuk mengambil data produk
        produkViewModel = ViewModelProvider(this).get(ProdukViewModel::class.java)
        produkViewModel.allProduk.observe(this, { produkList ->
            produkList?.let { adapter.submitList(it) }
        })
    }

    private fun showAddDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_produk)

        val editTextNama = dialog.findViewById<EditText>(R.id.editTextNamaProduk)
        val editTextStok = dialog.findViewById<EditText>(R.id.editTextStokProduk)
        val editTextSatuan = dialog.findViewById<EditText>(R.id.editTextSatuanProduk)
        val editTextHarga = dialog.findViewById<EditText>(R.id.editTextHargaProduk)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSaveProduk)

        buttonSave.setOnClickListener {
            val nama = editTextNama.text.toString().trim()
            val stok = editTextStok.text.toString().toIntOrNull() ?: 0
            val satuan = editTextSatuan.text.toString().trim()
            val harga = editTextHarga.text.toString().toIntOrNull() ?: 0
            val produk = Produk(0, nama, stok, satuan, harga)
            produkViewModel.insert(produk) // Menambahkan produk baru
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showEditDialog(produk: Produk) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_edit_produk)

        val editTextNama = dialog.findViewById<EditText>(R.id.editTextNamaProduk)
        val editTextStok = dialog.findViewById<EditText>(R.id.editTextStokProduk)
        val editTextSatuan = dialog.findViewById<EditText>(R.id.editTextSatuanProduk)
        val editTextHarga = dialog.findViewById<EditText>(R.id.editTextHargaProduk)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSaveProduk)

        // Mengisi EditText dengan data yang ada di produk
        editTextNama.setText(produk.namaProduk)
        editTextStok.setText(produk.stokProduk.toString())
        editTextSatuan.setText(produk.satuanProduk)
        editTextHarga.setText(produk.hargaProduk.toString())

        buttonSave.setOnClickListener {
            // Membuat produk baru dengan data yang telah diubah
            val updatedProduk = produk.copy(
                namaProduk = editTextNama.text.toString().trim(),
                stokProduk = editTextStok.text.toString().toIntOrNull() ?: produk.stokProduk,
                satuanProduk = editTextSatuan.text.toString().trim(),
                hargaProduk = editTextHarga.text.toString().toIntOrNull() ?: produk.hargaProduk
            )

            produkViewModel.update(updatedProduk) // Memperbarui produk
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDeleteDialog(produk: Produk) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_delete_produk)

        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)
        val buttonConfirmDelete = dialog.findViewById<Button>(R.id.buttonConfirmDelete)

        buttonCancel.setOnClickListener {
            dialog.dismiss() // Menutup dialog jika tombol cancel ditekan
        }

        buttonConfirmDelete.setOnClickListener {
            produkViewModel.delete(produk) // Menghapus produk
            dialog.dismiss()
        }

        dialog.show()
    }
}