package com.example.projectutsmobile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projectutsmobile.databinding.ItemProdukBinding
import extention.toFormattedPrice

class ProdukAdapter(
    private val onEdit: (Produk) -> Unit,
    private val onDelete: (Produk) -> Unit
) : ListAdapter<Produk, ProdukAdapter.ProdukViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdukViewHolder {
        val binding = ItemProdukBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProdukViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProdukViewHolder, position: Int) {
        val currentProduk = getItem(position)
        holder.bind(currentProduk, onEdit, onDelete)
    }

    inner class ProdukViewHolder(private val binding: ItemProdukBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(produk: Produk, onEdit: (Produk) -> Unit, onDelete: (Produk) -> Unit) {
            binding.textViewNamaProduk.text = produk.namaProduk
            binding.textViewStokProduk.text = produk.stokProduk.toString()  // Menampilkan stok produk
            // Menampilkan satuan yang diformat tanpa mengaitkan stok
            binding.textViewSatuanProduk.text = produk.satuanProduk // Menggunakan satuan produk tanpa stok
            // Menampilkan harga yang diformat
            binding.textViewHargaProduk.text = produk.hargaProduk.toFormattedPrice()

            binding.buttonEdit.setOnClickListener { onEdit(produk) }
            binding.buttonDelete.setOnClickListener { onDelete(produk) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Produk>() {
        override fun areItemsTheSame(oldItem: Produk, newItem: Produk): Boolean {
            return oldItem.id_produk == newItem.id_produk
        }

        override fun areContentsTheSame(oldItem: Produk, newItem: Produk): Boolean {
            return oldItem == newItem
        }
    }
}