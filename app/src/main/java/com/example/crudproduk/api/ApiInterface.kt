package com.example.crudproduk.api

import com.example.crudproduk.model.Produk
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

data class ListResp<T>(val ok: Boolean, val data: List<T>?, val error: String?)
data class ObjResp(val ok: Boolean, val message: String?, val id: Int?, val error: String?)

interface ApiInterface {
    @GET("api.php")
    fun getProduk(@Query("act") act: String = "list"): Call<ListResp<Produk>>

    @GET("api.php")
    fun getDetail(@Query("act") act: String = "detail", @Query("id") id: Int): Call<ObjResp>

    @Multipart
    @POST("api.php?act=tambah")
    fun tambah(
        @Part("nama_produk") nama: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part foto_produk: MultipartBody.Part? = null
    ): Call<ObjResp>

    @Multipart
    @POST("api.php?act=update")
    fun update(
        @Part("id") id: RequestBody,
        @Part("nama_produk") nama: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part foto_produk: MultipartBody.Part? = null
    ): Call<ObjResp>

    @FormUrlEncoded
    @POST("api.php?act=hapus")
    fun hapus(@Field("id") id: Int): Call<ObjResp>
}
