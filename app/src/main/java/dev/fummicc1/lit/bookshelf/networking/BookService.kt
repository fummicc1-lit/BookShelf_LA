package dev.fummicc1.lit.androidqrcodereader

import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    @GET("volumes")
    suspend fun getBook(@Query("q") query: String): BookResponse
}