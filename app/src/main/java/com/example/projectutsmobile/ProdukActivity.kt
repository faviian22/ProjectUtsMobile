package com.example.projectutsmobile

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectutsmobile.databinding.ActivityProdukBinding

class ProdukActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProdukBinding
    private lateinit var produkViewModel: ProdukViewModel
    private lateinit var adapter: ProdukAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupViewModel()

        binding.buttonSaveProduk.setOnClickListener {
            showAddDialog()
        }
    }

    private fun setupRecyclerView() {
        adapter = ProdukAdapter(
            onEdit = { produk -> showEditDialog(produk) },
            onDelete = { produk -> showDeleteDialog(produk) }
        )
        binding.recyclerviewProduk.adapter = adapter
        binding.recyclerviewProduk.layoutManager = LinearLayoutManager(this)
    }

    private fun setupViewModel() {
        produkViewModel = ViewModelProvider(this).get(ProdukViewModel::class.java)
        produkViewModel.allProduk.observe(this, { produkList ->
            produkList?.let { adapter.submitList(it) }
        })
    }

    private fun showAddDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_produk)

        val editTextId = dialog.findViewById<EditText>(R.id.editTextIdProduk)
        val editTextNama = dialog.findViewById<EditText>(R.id.editTextNamaProduk)
        val editTextStok = dialog.findViewById<EditText>(R.id.editTextStokProduk)
        val editTextSatuan = dialog.findViewById<EditText>(R.id.editTextSatuanProduk)
        val editTextHarga = dialog.findViewById<EditText>(R.id.editTextHargaProduk)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSaveProduk)

        buttonSave.setOnClickListener {
            val id = editTextId.text.toString().toIntOrNull() ?: 0
            val nama = editTextNama.text.toString().trim()
            val stok = editTextStok.text.toString().toIntOrNull() ?: 0
            val satuan = editTextSatuan.text.toString().toIntOrNull() ?: 0
            val harga = editTextHarga.text.toString().toIntOrNull() ?: 0
            val produk = Produk(id, nama, stok, satuan, harga)
            produkViewModel.insert(produk)
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

        editTextNama.setText(produk.namaProduk)
        editTextStok.setText(produk.stokProduk.toString())
        editTextSatuan.setText(produk.satuanProduk.toString())
        editTextHarga.setText(produk.hargaProduk.toString())

        buttonSave.setOnClickListener {
            val updatedProduk = produk.copy(
                namaProduk = editTextNama.text.toString().trim(),
                stokProduk = editTextStok.text.toString().toIntOrNull() ?: produk.stokProduk,
                satuanProduk = editTextSatuan.text.toString().toIntOrNull() ?: produk.satuanProduk,
                hargaProduk = editTextHarga.text.toString().toIntOrNull() ?: produk.hargaProduk
            )

            produkViewModel.update(updatedProduk)
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
            dialog.dismiss()
        }

        buttonConfirmDelete.setOnClickListener {
            produkViewModel.delete(produk)
            dialog.dismiss()
        }

        dialog.show()
    }
}
