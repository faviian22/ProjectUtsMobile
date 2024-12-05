package com.example.projectutsmobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView

class ProdukAdapter(
    private val onEditClick: (Produk) -> Unit,
    private val onDeleteClick: (Produk) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Any>()

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is String -> {
                if (items[position] == "Stok Menipis") VIEW_TYPE_HEADER_LOW_STOK
                else VIEW_TYPE_HEADER_SAFE_STOK
            }
            is Produk -> VIEW_TYPE_ITEM
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER_LOW_STOK, VIEW_TYPE_HEADER_SAFE_STOK -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.header_produk, parent, false)
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
                holder.buttonEdit.setOnClickListener { onEditClick(produk) }
                holder.buttonDelete.setOnClickListener { onDeleteClick(produk) }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(produkList: List<Produk>) {
        items.clear()

        // Filter produk berdasarkan stok
        val lowStokProduk = produkList.filter { it.stokProduk <= 5 }
        val safeStokProduk = produkList.filter { it.stokProduk > 5 }

        if (lowStokProduk.isNotEmpty()) {
            items.add("Stok Menipis")
            items.addAll(lowStokProduk)
        }
        if (safeStokProduk.isNotEmpty()) {
            items.add("Stok Aman")
            items.addAll(safeStokProduk)
        }

        notifyDataSetChanged()
    }

    // ViewHolder untuk Header
    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textView: TextView = view.findViewById(R.id.textViewHeader)
        fun bind(title: String) {
            textView.text = title
        }
    }

    // ViewHolder untuk Item Produk
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textNamaProduk: TextView = view.findViewById(R.id.textViewNamaProduk)
        private val textHargaProduk: TextView = view.findViewById(R.id.textViewHargaProduk)
        private val textStokProduk: TextView = view.findViewById(R.id.textViewStokProduk)
        private val textSatuanProduk: TextView = view.findViewById(R.id.textViewSatuanProduk)
        val buttonEdit: AppCompatImageButton = view.findViewById(R.id.buttonEdit)
        val buttonDelete: AppCompatImageButton = view.findViewById(R.id.buttonDelete)


        fun bind(produk: Produk) {
            textNamaProduk.text = produk.namaProduk
            textHargaProduk.text = "Rp. ${produk.hargaProduk}"
            textStokProduk.text = "${produk.stokProduk}"
            textSatuanProduk.text = produk.satuanProduk // Menampilkan satuan produk
        }
    }

    companion object {
        const val VIEW_TYPE_HEADER_LOW_STOK = 0
        const val VIEW_TYPE_HEADER_SAFE_STOK = 1
        const val VIEW_TYPE_ITEM = 2
    }
}