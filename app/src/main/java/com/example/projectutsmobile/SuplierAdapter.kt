package com.example.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projectutsmobile.Suplier
import com.example.projectutsmobile.databinding.ItemSuplierBinding

class SuplierAdapter(
    private val onEdit: (Suplier) -> Unit,
    private val onDelete: (Suplier) -> Unit
) : ListAdapter<Suplier, SuplierAdapter.SuplierViewHolder>(SuplierDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuplierViewHolder {
        val binding = ItemSuplierBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SuplierViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SuplierViewHolder, position: Int) {
        val currentSuplier = getItem(position)
        holder.bind(currentSuplier, onEdit, onDelete)
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

    // DiffUtil for efficient updates
    class SuplierDiffCallback : DiffUtil.ItemCallback<Suplier>() {
        override fun areItemsTheSame(oldItem: Suplier, newItem: Suplier): Boolean {
            // Replace `id_suplier` with the unique identifier in your Suplier data class
            return oldItem.id_suplier == newItem.id_suplier
        }

        override fun areContentsTheSame(oldItem: Suplier, newItem: Suplier): Boolean {
            // Compare all properties
            return oldItem == newItem
        }
    }
}
