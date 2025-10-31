package com.example.crudproduk.model

data class Produk(
    val no_produk: Int,
    val nama_produk: String,
    val harga: Double,
    val foto_produk: String?,
    val foto_url: String?,
    val created_at: String?
)
