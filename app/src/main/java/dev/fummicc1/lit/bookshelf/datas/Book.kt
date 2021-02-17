package dev.fummicc1.lit.bookshelf.datas

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat
import java.util.*

@Entity
data class Book (
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        var title: String,
        var author: String,
        var price: Int,
        var description: String,
        @ColumnInfo(name = "updated_at")
        var updatedAt: Date
)