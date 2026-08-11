package com.rsdurvasacooling.service

import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.rsdurvasacooling.service.databinding.ActivityInvoiceBinding
import java.io.File

class InvoiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInvoiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name").orEmpty()
        val phone = intent.getStringExtra("phone").orEmpty()
        val address = intent.getStringExtra("address").orEmpty()
        val service = intent.getStringExtra("service").orEmpty()
        val amount = intent.getDoubleExtra("amount", 0.0)

        binding.invoiceText.text = """
            RS Durvasa Cooling Service

            Customer: $name
            Phone: $phone
            Address: $address

            Service:
            $service

            Total: ₹$amount
        """.trimIndent()

        binding.createPdfButton.setOnClickListener {
            createPdf(name, phone, address, service, amount)
        }
    }

    private fun createPdf(name: String, phone: String, address: String, service: String, amount: Double) {
        try {
            val dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: filesDir
            val file = File(dir, "invoice_${System.currentTimeMillis()}.pdf")
            val writer = PdfWriter(file)
            val pdf = PdfDocument(writer)
            val document = Document(pdf)
            document.add(Paragraph("RS Durvasa Cooling Service"))
            document.add(Paragraph("Customer: $name"))
            document.add(Paragraph("Phone: $phone"))
            document.add(Paragraph("Address: $address"))
            document.add(Paragraph("Service: $service"))
            document.add(Paragraph("Total: ₹$amount"))
            document.close()
            Toast.makeText(this, "PDF saved: ${file.name}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "PDF error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
