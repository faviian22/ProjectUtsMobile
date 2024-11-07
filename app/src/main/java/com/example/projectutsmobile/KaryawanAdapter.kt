package com.example.projectutsmobile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projectutsmobile.databinding.ItemKaryawanBinding

class KaryawanAdapter(
    private val onEditClick: (Karyawan) -> Unit,
    private val onDeleteClick: (Karyawan) -> Unit
) : ListAdapter<Karyawan, KaryawanAdapter.KaryawanViewHolder>(KaryawanDiffCallback()) {

    class KaryawanViewHolder(val binding: ItemKaryawanBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KaryawanViewHolder {
        val binding = ItemKaryawanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KaryawanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KaryawanViewHolder, position: Int) {
        val currentKaryawan = getItem(position)

        holder.binding.textViewNamaKaryawan.text = currentKaryawan.nama_karyawan
        holder.binding.textViewJenisKelaminKaryawan.text = currentKaryawan.jenis_kelamin
        holder.binding.textViewAlamatKaryawan.text = currentKaryawan.alamat_karyawan

        holder.binding.buttonEdit.setOnClickListener {
            onEditClick(currentKaryawan)
        }

        holder.binding.buttonDelete.setOnClickListener {
            onDeleteClick(currentKaryawan)
        }
    }

    class KaryawanDiffCallback : DiffUtil.ItemCallback<Karyawan>() {
        override fun areItemsTheSame(oldItem: Karyawan, newItem: Karyawan): Boolean {
            // Replace `id_karyawan` with the actual unique identifier in the `Karyawan` class
            return oldItem.id_karyawan == newItem.id_karyawan
        }

        override fun areContentsTheSame(oldItem: Karyawan, newItem: Karyawan): Boolean {
            // Compare the full contents of the item
            return oldItem == newItem
        }
    }
}
