package com.example.crudproduk.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.crudproduk.R
import com.example.crudproduk.databinding.ItemProdukBinding
import com.example.crudproduk.model.Produk

class ProdukAdapter(
    private val onDetail: (Produk) -> Unit,
    private val onEdit: (Produk) -> Unit,
    private val onDelete: (Produk) -> Unit
) : RecyclerView.Adapter<ProdukAdapter.VH>() {

    private val data = mutableListOf<Produk>()

    fun submit(list: List<Produk>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    inner class VH(val b: ItemProdukBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inf = LayoutInflater.from(parent.context)
        return VH(ItemProdukBinding.inflate(inf, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(h: VH, position: Int) {
        val item = data[position]

        h.b.tvNama.text = item.nama_produk
        h.b.tvHarga.text = "Rp %,d".format(item.harga.toInt())

        Glide.with(h.b.img)
            .load(item.foto_url ?: "")
            .placeholder(R.drawable.ic_image_placeholder)
            .into(h.b.img)

        // ✅ gunakan 'item' (Produk), bukan 'it' (View)
        h.b.root.setOnClickListener { onDetail(item) }
        h.b.btnEdit.setOnClickListener { onEdit(item) }
        h.b.btnHapus.setOnClickListener { onDelete(item) }
    }
}
