package com.example.crudproduk.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.crudproduk.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var b: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(b.root)

        val nama = intent.getStringExtra("NAMA") ?: ""
        val harga = intent.getDoubleExtra("HARGA", 0.0)
        val foto = intent.getStringExtra("FOTO")

        b.tvNama.text = nama
        b.tvHarga.text = "Rp %,d".format(harga.toInt())
        Glide.with(b.img).load(foto ?: "").into(b.img)

        b.btnBack.setOnClickListener { finish() }
    }
}
