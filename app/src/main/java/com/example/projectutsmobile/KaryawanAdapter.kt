package com.example.projectutsmobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class KaryawanAdapter(
    private val onEditClick: (Karyawan) -> Unit,
    private val onDeleteClick: (Karyawan) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Any>()  // List that holds headers and employee items

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is String -> VIEW_TYPE_HEADER
            is Karyawan -> VIEW_TYPE_ITEM
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.header_karyawan, parent, false)
                HeaderViewHolder(view)
            }
            VIEW_TYPE_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_karyawan, parent, false)
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

        // Group employees by gender dynamically
        val groupedByGender = karyawanList.groupBy { it.jenis_kelamin }

        // Add headers and employees for each gender group
        groupedByGender.forEach { (gender, karyawanGroup) ->
            items.add(gender) // Add gender as header
            items.addAll(karyawanGroup) // Add employees in this gender group
        }

        notifyDataSetChanged()
    }

    // View Types
    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_ITEM = 1
    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textViewHeader: TextView = view.findViewById(R.id.textViewHeader)

        fun bind(header: String) {
            textViewHeader.text = header
        }
    }

    inner class KaryawanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textViewNama: TextView = view.findViewById(R.id.textViewNamaKaryawan)
        private val textViewJenisKelamin: TextView = view.findViewById(R.id.textViewJenisKelaminKaryawan)
        private val textViewAlamat: TextView = view.findViewById(R.id.textViewAlamatKaryawan)
        val buttonEdit: AppCompatImageButton = view.findViewById(R.id.buttonEdit)
        val buttonDelete: AppCompatImageButton = view.findViewById(R.id.buttonDelete)

        fun bind(karyawan: Karyawan) {
            textViewNama.text = karyawan.nama_karyawan
            textViewJenisKelamin.text = karyawan.jenis_kelamin
            textViewAlamat.text = karyawan.alamat_karyawan
        }
    }
}
