package com.example.crudproduk.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.crudproduk.api.ApiClient
import com.example.crudproduk.api.ObjResp
import com.example.crudproduk.databinding.ActivityTambahBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class TambahActivity : AppCompatActivity() {
    private lateinit var b: ActivityTambahBinding
    private var picked: File? = null
    private val PICK = 111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityTambahBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.btnPick.setOnClickListener { pickImage() }
        b.btnSimpan.setOnClickListener { submit() }
        b.btnBack.setOnClickListener { finish() }
    }

    private fun pickImage() {
        val i = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
        startActivityForResult(Intent.createChooser(i, "Pilih Gambar"), PICK)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(req: Int, res: Int, data: Intent?) {
        super.onActivityResult(req, res, data)
        if (req == PICK && res == Activity.RESULT_OK) {
            val uri = data?.data ?: return
            b.imgPreview.setImageURI(uri)
            picked = copyToCache(uri)
        }
    }

    private fun copyToCache(uri: Uri): File? {
        return try {
            val name = queryName(uri) ?: "upload.jpg"
            val input = contentResolver.openInputStream(uri) ?: return null
            val f = File(cacheDir, name)
            FileOutputStream(f).use { out -> input.copyTo(out) }
            f
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show(); null
        }
    }

    private fun queryName(uri: Uri): String? {
        contentResolver.query(uri, null, null, null, null)?.use {
            if (it.moveToFirst()) return it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
        }
        return null
    }

    private fun submit() {
        val nama = b.etNama.text?.toString()?.trim().orEmpty()
        val hargaStr = b.etHarga.text?.toString()?.trim().orEmpty()
        val harga = hargaStr.toDoubleOrNull()
        if (nama.isEmpty() || harga == null || harga < 0) {
            Toast.makeText(this, "Nama/Harga tidak valid", Toast.LENGTH_SHORT).show(); return
        }

        val namaBody = RequestBody.create("text/plain".toMediaTypeOrNull(), nama)
        val hargaBody = RequestBody.create("text/plain".toMediaTypeOrNull(), harga.toString())

        val fotoPart = picked?.let {
            val body = RequestBody.create("image/*".toMediaTypeOrNull(), it)
            MultipartBody.Part.createFormData("foto_produk", it.name, body)
        }

        ApiClient.service.tambah(namaBody, hargaBody, fotoPart).enqueue(object: Callback<ObjResp> {
            override fun onResponse(call: Call<ObjResp>, resp: Response<ObjResp>) {
                if (resp.isSuccessful && resp.body()?.ok == true) finish()
                else Toast.makeText(this@TambahActivity, resp.body()?.error ?: "Gagal", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<ObjResp>, t: Throwable) {
                Toast.makeText(this@TambahActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
