package com.example.projectutsmobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class SuplierAdapter(
    private val onEdit: (Suplier) -> Unit,
    private val onDelete: (Suplier) -> Unit
) : ListAdapter<Any, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        const val HEADER_VIEW_TYPE = 0
        const val SUPPLIER_VIEW_TYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.header_suplier, parent, false)
                HeaderViewHolder(view)
            }
            SUPPLIER_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_suplier, parent, false)
                SuplierViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                // Set any data for the header if needed
            }
            is SuplierViewHolder -> {
                val suplier = getItem(position) as Suplier
                holder.bind(suplier)
                holder.buttonEdit.setOnClickListener { onEdit(suplier) }
                holder.buttonDelete.setOnClickListener { onDelete(suplier) }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            HEADER_VIEW_TYPE
        } else {
            SUPPLIER_VIEW_TYPE
        }
    }

    inner class SuplierViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textViewNamaSuplier: TextView = view.findViewById(R.id.textViewNamaSuplier)
        private val textViewNoTlpnSuplier: TextView = view.findViewById(R.id.textViewNoTlpn)
        private val textViewAlamatSuplier: TextView = view.findViewById(R.id.textViewAlamatSuplier)
        private val textViewNamaProduk: TextView = view.findViewById(R.id.textViewNamaProduk)
        val buttonEdit: Button = view.findViewById(R.id.buttonEdit)
        val buttonDelete: Button = view.findViewById(R.id.buttonDelete)

        fun bind(suplier: Suplier) {
            textViewNamaSuplier.text = suplier.nama_suplier
            textViewNoTlpnSuplier.text = suplier.no_Tlpn
            textViewAlamatSuplier.text = suplier.alamat_suplier
            textViewNamaProduk.text = suplier.nama_produk
        }
    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textViewHeader: TextView = view.findViewById(R.id.textViewHeader)

        fun bind() {
            textViewHeader.text = "Supplier List" // You can customize the header text here
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is Suplier && newItem is Suplier) {
                oldItem.id_suplier == newItem.id_suplier
            } else {
                false
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }
    }
}
