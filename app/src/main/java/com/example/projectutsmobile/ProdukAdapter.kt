package com.example.projectutsmobile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectutsmobile.databinding.ItemProdukBinding
import com.example.projectutsmobile.Produk


class ProdukAdapter(
    private val onEdit: (Produk) -> Unit,
    private val onDelete: (Produk) -> Unit
) : RecyclerView.Adapter<ProdukAdapter.ProdukViewHolder>() {

    private var produkList = emptyList<Produk>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdukViewHolder {
        val binding = ItemProdukBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProdukViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProdukViewHolder, position: Int) {
        val currentProduk = produkList[position]
        holder.bind(currentProduk, onEdit, onDelete)
    }

    override fun getItemCount(): Int {
        return produkList.size
    }

    fun submitList(produkList: List<Produk>) {
        this.produkList = produkList
        notifyDataSetChanged()
    }
    inner class ProdukViewHolder(private val binding: ItemProdukBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(produk: Produk, onEdit: (Produk) -> Unit, onDelete: (Produk) -> Unit) {
            binding.textViewIdProduk.text = produk.id_produk.toString()
            binding.textViewNamaProduk.text = produk.namaProduk
            binding.textViewStokProduk.text = produk.stokProduk.toString()
            binding.textViewSatuanProduk.text = produk.satuanProduk.toString()
            binding.textViewHargaProduk.text = produk.hargaProduk.toString()

            binding.buttonEdit.setOnClickListener {
                val produkBaru = Produk(
                    produk.id_produk,
                    produk.namaProduk,
                    produk.stokProduk,
                    produk.satuanProduk,
                    produk.hargaProduk
                )
                onEdit(produkBaru)
            }

            binding.buttonDelete.setOnClickListener {
                onDelete(produk)
            }
        }
    }
}