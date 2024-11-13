package com.example.projectutsmobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProdukAdapter(
    private val onEdit: (Produk) -> Unit,
    private val onDelete: (Produk) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Any>() // Bisa berisi Header atau Produk

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is String -> {
                if (items[position] == "Stok Menipis") VIEW_TYPE_HEADER_LOW_STOCK
                else VIEW_TYPE_HEADER_SAFE_STOCK
            }
            is Produk -> VIEW_TYPE_ITEM
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER_LOW_STOCK, VIEW_TYPE_HEADER_SAFE_STOCK -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.header_produk, parent, false) // Menggunakan header_produk.xml
                HeaderViewHolder(view)
            }
            VIEW_TYPE_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_produk, parent, false)
                ItemViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(items[position] as String)
            is ItemViewHolder -> {
                val produk = items[position] as Produk
                holder.bind(produk)
                holder.buttonEdit.setOnClickListener { onEdit(produk) }
                holder.buttonDelete.setOnClickListener { onDelete(produk) }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(produkList: List<Produk>) {
        items.clear()

        // Pisahkan berdasarkan stok
        val lowStock = produkList.filter { it.stokProduk < 5 }
        val safeStock = produkList.filter { it.stokProduk >= 5 }

        if (lowStock.isNotEmpty()) {
            items.add("Stok Menipis")
            items.addAll(lowStock)
        }

        if (safeStock.isNotEmpty()) {
            items.add("Stok Aman")
            items.addAll(safeStock)
        }

        notifyDataSetChanged()
    }

    // ViewHolder untuk Header
    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textView: TextView = view.findViewById(R.id.textViewHeader) // Menggunakan ID dari header_produk.xml

        fun bind(title: String) {
            textView.text = title
        }
    }

    // ViewHolder untuk Item
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textViewNamaProduk: TextView = view.findViewById(R.id.textViewNamaProduk)
        private val textViewStokProduk: TextView = view.findViewById(R.id.textViewStokProduk)
        private val textViewSatuanProduk: TextView = view.findViewById(R.id.textViewSatuanProduk)
        private val textViewHargaProduk: TextView = view.findViewById(R.id.textViewHargaProduk)
        val buttonEdit: Button = view.findViewById(R.id.buttonEdit)
        val buttonDelete: Button = view.findViewById(R.id.buttonDelete)

        fun bind(produk: Produk) {
            textViewNamaProduk.text = produk.namaProduk
            textViewStokProduk.text = "Stok: ${produk.stokProduk}"
            textViewSatuanProduk.text = produk.satuanProduk
            textViewHargaProduk.text = "Harga: ${produk.hargaProduk}"
        }
    }

    companion object {
        private const val VIEW_TYPE_HEADER_LOW_STOCK = 0
        private const val VIEW_TYPE_HEADER_SAFE_STOCK = 1
        private const val VIEW_TYPE_ITEM = 2
    }
}
