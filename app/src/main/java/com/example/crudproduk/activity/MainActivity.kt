package com.example.crudproduk.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crudproduk.adapter.ProdukAdapter
import com.example.crudproduk.api.ApiClient
import com.example.crudproduk.api.ListResp
import com.example.crudproduk.databinding.ActivityMainBinding
import com.example.crudproduk.model.Produk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding
    private val adp = ProdukAdapter(
        onDetail = { p ->
            startActivity(Intent(this, DetailActivity::class.java)
                .putExtra("ID", p.no_produk)
                .putExtra("NAMA", p.nama_produk)
                .putExtra("HARGA", p.harga)
                .putExtra("FOTO", p.foto_url))
        },
        onEdit = { p ->
            startActivity(Intent(this, EditActivity::class.java)
                .putExtra("ID", p.no_produk)
                .putExtra("NAMA", p.nama_produk)
                .putExtra("HARGA", p.harga)
                .putExtra("FOTO", p.foto_url))
        },
        onDelete = { p -> hapus(p.no_produk) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.rv.layoutManager = LinearLayoutManager(this)
        b.rv.adapter = adp

        b.swipe.setOnRefreshListener { load() }
        b.fabAdd.setOnClickListener { startActivity(Intent(this, TambahActivity::class.java)) }
    }

    override fun onResume() {
        super.onResume()
        load()
    }

    private fun load() {
        b.swipe.isRefreshing = true
        ApiClient.service.getProduk().enqueue(object: Callback<ListResp<Produk>> {
            override fun onResponse(call: Call<ListResp<Produk>>, resp: Response<ListResp<Produk>>) {
                b.swipe.isRefreshing = false
                val body = resp.body()
                if (resp.isSuccessful && body?.ok == true) {
                    adp.submit(body.data ?: emptyList())
                } else {
                    Toast.makeText(this@MainActivity, body?.error ?: "Gagal memuat", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ListResp<Produk>>, t: Throwable) {
                b.swipe.isRefreshing = false
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun hapus(id: Int) {
        ApiClient.service.hapus(id).enqueue(object: retrofit2.Callback<com.example.crudproduk.api.ObjResp> {
            override fun onResponse(
                call: retrofit2.Call<com.example.crudproduk.api.ObjResp>,
                resp: retrofit2.Response<com.example.crudproduk.api.ObjResp>
            ) {
                if (resp.isSuccessful && resp.body()?.ok == true) load()
                else Toast.makeText(this@MainActivity, resp.body()?.error ?: "Gagal hapus", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: retrofit2.Call<com.example.crudproduk.api.ObjResp>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
