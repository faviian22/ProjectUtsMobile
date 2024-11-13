package com.example.projectutsmobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class KaryawanAdapter(
    private val onEditClick: (Karyawan) -> Unit,
    private val onDeleteClick: (Karyawan) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Any>() // List for both headers and employee items

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is String -> {
                if (items[position] == "Laki-Laki") VIEW_TYPE_HEADER_MALE
                else if (items[position] == "Perempuan") VIEW_TYPE_HEADER_FEMALE
                else throw IllegalArgumentException("Invalid view type")
            }
            is Karyawan -> VIEW_TYPE_ITEM
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER_MALE, VIEW_TYPE_HEADER_FEMALE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.header_karyawan, parent, false) // Header layout
                HeaderViewHolder(view)
            }
            VIEW_TYPE_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_karyawan, parent, false) // Item layout for employees
                KaryawanViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(items[position] as String)
            is KaryawanViewHolder -> {
                val karyawan = items[position] as Karyawan
                holder.bind(karyawan)
                holder.buttonEdit.setOnClickListener { onEditClick(karyawan) }
                holder.buttonDelete.setOnClickListener { onDeleteClick(karyawan) }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(karyawanList: List<Karyawan>) {
        items.clear()

        // Split into Male and Female categories
        val maleKaryawan = karyawanList.filter { it.jenis_kelamin == "Laki-Laki" }
        val femaleKaryawan = karyawanList.filter { it.jenis_kelamin == "Perempuan" }

        if (maleKaryawan.isNotEmpty()) {
            items.add("Laki-Laki")
            items.addAll(maleKaryawan)
        }

        if (femaleKaryawan.isNotEmpty()) {
            items.add("Perempuan")
            items.addAll(femaleKaryawan)
        }

        notifyDataSetChanged()
    }

    // ViewHolder for Header
    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textView: TextView = view.findViewById(R.id.textViewHeader) // ID for header

        fun bind(title: String) {
            textView.text = title
        }
    }

    // ViewHolder for Item (Employee)
    inner class KaryawanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textViewNama: TextView = view.findViewById(R.id.textViewNamaKaryawan)
        private val textViewJenisKelamin: TextView = view.findViewById(R.id.textViewJenisKelaminKaryawan)
        private val textViewAlamat: TextView = view.findViewById(R.id.textViewAlamatKaryawan)
        val buttonEdit: Button = view.findViewById(R.id.buttonEdit)
        val buttonDelete: Button = view.findViewById(R.id.buttonDelete)

        fun bind(karyawan: Karyawan) {
            textViewNama.text = karyawan.nama_karyawan
            textViewJenisKelamin.text = karyawan.jenis_kelamin
            textViewAlamat.text = karyawan.alamat_karyawan
        }
    }

    companion object {
        private const val VIEW_TYPE_HEADER_MALE = 0
        private const val VIEW_TYPE_HEADER_FEMALE = 1
        private const val VIEW_TYPE_ITEM = 2
    }
}
