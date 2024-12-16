package com.example.projectutsmobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView

class SuplierAdapter(
    private val onEdit: (Suplier) -> Unit,
    private val onDelete: (Suplier) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Any>()

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Char -> VIEW_TYPE_HEADER
            is Suplier -> VIEW_TYPE_ITEM
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.header_suplier, parent, false)
                HeaderViewHolder(view)
            }
            VIEW_TYPE_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_suplier, parent, false)
                SuplierViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind((items[position] as Char).uppercaseChar())
            is SuplierViewHolder -> {
                val suplier = items[position] as Suplier
                holder.bind(suplier)
                holder.buttonEdit.setOnClickListener { onEdit(suplier) }
                holder.buttonDelete.setOnClickListener { onDelete(suplier) }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(suplierList: List<Suplier>) {
        items.clear()

        if (suplierList.isNotEmpty()) {
            // Group suppliers by the first character of their name, ignoring case
            val groupedByInitial = suplierList.groupBy { it.nama_suplier.first().uppercaseChar() }

            // Add headers and suppliers for each group
            groupedByInitial.forEach { (initial, suplierGroup) ->
                items.add(initial) // Add initial as header
                items.addAll(suplierGroup) // Add suppliers in this group
            }
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

        fun bind(header: Char) {
            textViewHeader.text = header.toString()
        }
    }

    inner class SuplierViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textViewNama: TextView = view.findViewById(R.id.textViewNamaSuplier)
        private val textViewNoTlpn: TextView = view.findViewById(R.id.textViewNoTlpn)
        private val textViewAlamat: TextView = view.findViewById(R.id.textViewAlamatSuplier)
        private val textViewNamaProduk: TextView = view.findViewById(R.id.textViewNamaProduk)
        val buttonEdit: AppCompatImageButton = view.findViewById(R.id.buttonEdit)
        val buttonDelete: AppCompatImageButton = view.findViewById(R.id.buttonDelete)

        fun bind(suplier: Suplier) {
            textViewNama.text = suplier.nama_suplier
            textViewNoTlpn.text = suplier.no_Tlpn
            textViewAlamat.text = suplier.alamat_suplier
            textViewNamaProduk.text = suplier.nama_produk
        }
    }
}
