package com.example.projectutsmobile

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projectutsmobile.databinding.ActivityKaryawanBinding

class KaryawanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKaryawanBinding
    private lateinit var karyawanViewModel: KaryawanViewModel
    private lateinit var adapter: KaryawanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKaryawanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        val layoutManager = GridLayoutManager(this, 2) // 2 kolom dalam grid
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    KaryawanAdapter.VIEW_TYPE_HEADER -> 2 // Header mencakup 2 kolom
                    KaryawanAdapter.VIEW_TYPE_ITEM -> 1 // Setiap item mencakup 1 kolom
                    else -> 1
                }
            }
        }
        binding.recyclerviewKaryawan.layoutManager = layoutManager
        adapter = KaryawanAdapter(
            onEditClick = { karyawan -> showEditDialog(karyawan) },
            onDeleteClick = { karyawan -> showDeleteDialog(karyawan) }
        )
        binding.recyclerviewKaryawan.adapter = adapter

        // Inisialisasi ViewModel dan observasi LiveData dari Firebase
        karyawanViewModel = ViewModelProvider(this).get(KaryawanViewModel::class.java)
        karyawanViewModel.firebaseKaryawan.observe(this) { karyawanList ->
            karyawanList?.let {
                adapter.submitList(it)
            }
        }

        // Ambil data dari Firebase
        karyawanViewModel.fetchKaryawanFromFirebase()

        // Set up add button untuk menampilkan dialog add
        binding.buttonSaveKaryawan.setOnClickListener {
            showAddDialog()
        }

        // Set up SearchView
        binding.searchViewKaryawan.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchKaryawan(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchKaryawan(it) }
                return false
            }
        })
    }

    private fun searchKaryawan(query: String) {
        val filteredList = karyawanViewModel.firebaseKaryawan.value?.filter {
            it.nama_karyawan.contains(query, ignoreCase = true)
        }
        filteredList?.let {
            adapter.submitList(it)
        }
    }

    private fun showToast(message: String, length: Int) {
        Toast.makeText(this, message, length).show()
    }

    private fun createDialog(layoutResId: Int): Dialog {
        val dialog = Dialog(this)
        dialog.setContentView(layoutResId)
        dialog.setCancelable(true)
        return dialog
    }

    private fun setupGenderSpinner(spinner: Spinner) {
        ArrayAdapter.createFromResource(
            this,
            R.array.jenis_kelamin_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    private fun isValidInput(nama: String, jenisKelamin: String, alamat: String): Boolean {
        return nama.isNotEmpty() && jenisKelamin.isNotEmpty() && alamat.isNotEmpty()
    }

    private fun showAddDialog() {
        val dialog = createDialog(R.layout.dialog_add_karyawan)

        val editTextNama = dialog.findViewById<EditText>(R.id.editTextNamaKaryawan)
        val spinnerJenisKelamin = dialog.findViewById<Spinner>(R.id.spinnerJenisKelamin)
        val editTextAlamat = dialog.findViewById<EditText>(R.id.editTextAlamatKaryawan)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSaveKaryawan)

        setupGenderSpinner(spinnerJenisKelamin)

        buttonSave.setOnClickListener {
            val nama = editTextNama.text.toString().trim()
            val jenisKelamin = spinnerJenisKelamin.selectedItem?.toString().orEmpty()
            val alamat = editTextAlamat.text.toString().trim()

            if (isValidInput(nama, jenisKelamin, alamat)) {
                val karyawanBaru = Karyawan(0, nama, jenisKelamin, alamat)
                karyawanViewModel.insert(listOf(karyawanBaru))
                dialog.dismiss()
                showToast("Karyawan berhasil ditambahkan", Toast.LENGTH_SHORT)
            } else {
                showToast("Harap isi semua bagian dengan data yang valid", Toast.LENGTH_LONG)
            }
        }

        dialog.show()
    }

    private fun showEditDialog(karyawan: Karyawan) {
        val dialog = createDialog(R.layout.dialog_edit_karyawan)

        val editTextNama = dialog.findViewById<EditText>(R.id.editTextNamaKaryawan)
        val spinnerJenisKelamin = dialog.findViewById<Spinner>(R.id.spinnerJenisKelaminKaryawan)
        val editTextAlamat = dialog.findViewById<EditText>(R.id.editTextAlamatKaryawan)
        val buttonUpdate = dialog.findViewById<Button>(R.id.buttonEdit)

        setupGenderSpinner(spinnerJenisKelamin)

        editTextNama.setText(karyawan.nama_karyawan)
        editTextAlamat.setText(karyawan.alamat_karyawan)

        val genderOptions = resources.getStringArray(R.array.jenis_kelamin_options)
        val spinnerPosition = genderOptions.indexOf(karyawan.jenis_kelamin)
        spinnerJenisKelamin.setSelection(spinnerPosition)

        buttonUpdate.setOnClickListener {
            val nama = editTextNama.text.toString().trim()
            val jenisKelamin = spinnerJenisKelamin.selectedItem?.toString().orEmpty()
            val alamat = editTextAlamat.text.toString().trim()

            if (isValidInput(nama, jenisKelamin, alamat)) {
                val updatedKaryawan = karyawan.copy(
                    nama_karyawan = nama,
                    jenis_kelamin = jenisKelamin,
                    alamat_karyawan = alamat
                )
                karyawanViewModel.update(listOf(updatedKaryawan))
                dialog.dismiss()
                showToast("Karyawan berhasil diperbarui", Toast.LENGTH_SHORT)
            } else {
                showToast("Harap isi semua bagian dengan data yang valid", Toast.LENGTH_LONG)
            }
        }

        dialog.show()
    }

    private fun showDeleteDialog(karyawan: Karyawan) {
        val dialog = AlertDialog.Builder(this)
            .setMessage("Apakah Anda yakin ingin menghapus data karyawan ini?")
            .setPositiveButton("Ya") { _, _ ->
                karyawanViewModel.delete(karyawan)
                showToast("Karyawan berhasil dihapus", Toast.LENGTH_SHORT)
            }
            .setNegativeButton("Tidak", null)
            .create()

        dialog.show()
    }
}
