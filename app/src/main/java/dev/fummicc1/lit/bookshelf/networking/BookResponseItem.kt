package dev.fummicc1.lit.androidqrcodereader

import java.util.*

data class BookResponse(
    val items: List<BookResponseItem>?
)

data class BookResponseItem(
    val id: String,
    val volumeInfo: VolumeInfo,
    val saleInfo: SaleInfo?
) {
    data class VolumeInfo(
        val title: String,
        val authors: List<String>,
        val description: String?,
        val imageLinks: ImageLinks?,
    ) {
        data class ImageLinks(
            val thumbnail: String
        )
    }


    data class SaleInfo(
        val ratailPrice: RatailPrice?
    ) {
        data class RatailPrice(
            val amount: Float,
            val currencyCode: String
        ) {
            fun getPrice(): Int {
                return when (currencyCode) {
                    ("JPY") -> amount.toInt()
                    ("USD") -> (amount * 106).toInt()
                    ("EUR") -> (amount * 128).toInt()
                    else -> amount.toInt()
                }
            }
        }
    }
}