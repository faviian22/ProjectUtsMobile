package com.example.projectutsmobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectutsmobile.databinding.ItemKaryawanBinding

class KaryawanAdapter(
    private val onEditClick: (Karyawan) -> Unit,
    private val onDeleteClick: (Karyawan) -> Unit
) : RecyclerView.Adapter<KaryawanAdapter.KaryawanViewHolder>() {

    private var karyawanList = emptyList<Karyawan>()

    class KaryawanViewHolder(val binding: ItemKaryawanBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KaryawanViewHolder {
        val binding = ItemKaryawanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KaryawanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KaryawanViewHolder, position: Int) {
        val currentKaryawan = karyawanList[position]


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

    override fun getItemCount(): Int {
        return karyawanList.size
    }

    fun setKaryawan(karyawans: List<Karyawan>) {
        this.karyawanList = karyawans
        notifyDataSetChanged()
    }
}