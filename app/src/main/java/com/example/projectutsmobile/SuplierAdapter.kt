package com.example.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectutsmobile.Suplier
import com.example.projectutsmobile.databinding.ItemSuplierBinding

class SuplierAdapter(
    private val onEdit: (Suplier) -> Unit,
    private val onDelete: (Suplier) -> Unit
) : RecyclerView.Adapter<SuplierAdapter.SuplierViewHolder>() {

    private var suplierList = emptyList<Suplier>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuplierViewHolder {
        val binding = ItemSuplierBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SuplierViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SuplierViewHolder, position: Int) {
        val currentSuplier = suplierList[position]
        holder.bind(currentSuplier, onEdit, onDelete)
    }

    override fun getItemCount(): Int {
        return suplierList.size
    }

    fun submitList(suplierList: List<Suplier>) {
        this.suplierList = suplierList
        notifyDataSetChanged()
    }

    inner class SuplierViewHolder(private val binding: ItemSuplierBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(suplier: Suplier, onEdit: (Suplier) -> Unit, onDelete: (Suplier) -> Unit) {
            binding.textViewNamaSuplier.text = suplier.nama_suplier
            binding.textViewNoTlpn.text = suplier.no_Tlpn
            binding.textViewAlamatSuplier.text = suplier.alamat_suplier
            binding.textViewNamaProduk.text = suplier.nama_produk

            binding.buttonEdit.setOnClickListener {
                onEdit(suplier)
            }

            binding.buttonDelete.setOnClickListener {
                onDelete(suplier)
            }
        }
    }
}
