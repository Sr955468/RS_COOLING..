package com.rsdurvasacooling.service

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.rsdurvasacooling.service.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "rs_durvasa.db")
            .fallbackToDestructiveMigration()
            .build()

        binding.saveButton.setOnClickListener { saveOffline() }
        binding.syncButton.setOnClickListener { syncFirebase() }
        binding.invoiceButton.setOnClickListener { openInvoice() }
    }

    private fun readRecord(): ServiceRecord? {
        val name = binding.nameInput.text?.toString()?.trim().orEmpty()
        val phone = binding.phoneInput.text?.toString()?.trim().orEmpty()
        val address = binding.addressInput.text?.toString()?.trim().orEmpty()
        val service = binding.serviceInput.text?.toString()?.trim().orEmpty()
        val amount = binding.amountInput.text?.toString()?.toDoubleOrNull()

        if (name.isBlank() || service.isBlank() || amount == null) {
            Toast.makeText(this, "Name, service and amount are required", Toast.LENGTH_SHORT).show()
            return null
        }
        return ServiceRecord(
            customerName = name, phone = phone, address = address,
            service = service, amount = amount
        )
    }

    private fun saveOffline() {
        val record = readRecord() ?: return
        lifecycleScope.launch {
            db.serviceDao().insert(record)
            binding.statusText.text = "Saved offline successfully"
            Toast.makeText(this@MainActivity, "Saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun syncFirebase() {
        lifecycleScope.launch {
            try {
                val firestore = FirebaseFirestore.getInstance()
                val records = db.serviceDao().unsynced()
                for (r in records) {
                    firestore.collection("service_records").document(r.id.toString())
                        .set(r.copy(synced = true)).await()
                    db.serviceDao().markSynced(r.id)
                }
                binding.statusText.text = "Firebase sync complete: ${records.size} record(s)"
            } catch (e: Exception) {
                binding.statusText.text = "Sync failed. Check Firebase setup and internet."
            }
        }
    }

    private fun openInvoice() {
        val record = readRecord() ?: return
        startActivity(Intent(this, InvoiceActivity::class.java).apply {
            putExtra("name", record.customerName)
            putExtra("phone", record.phone)
            putExtra("address", record.address)
            putExtra("service", record.service)
            putExtra("amount", record.amount)
        })
    }
}
