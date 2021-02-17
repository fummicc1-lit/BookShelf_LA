package dev.fummicc1.lit.bookshelf.datas

import android.os.Parcel
import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat
import java.util.*

// Open修飾子をつける必要がある
// 変数にデフォルトの値を設定する必要がある
// RealmObjectを継承する必要がある→Data Classは使えない
open class Book (
        @PrimaryKey
        var id: String = UUID.randomUUID().toString(),
        var title: String = "",
        var author: String = "",
        var price: Int = 0,
        var description: String = "",
        var createdAt: Date = Date()
): RealmObject(), Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readInt() ?: 0,
                parcel.readString() ?: "",
                DateFormat.getDateInstance().parse(parcel.readString() ?: "") ?: Date()
        ) {
        }

        override fun describeContents(): Int = 0

        override fun writeToParcel(dest: Parcel?, flags: Int) {
                dest?.writeString(id)
                dest?.writeString(title)
                dest?.writeString(author)
                dest?.writeInt(price)
                dest?.writeString(description)

                val createdAtText = DateFormat.getDateInstance().format(createdAt)
                dest?.writeString(createdAtText)
        }

        companion object CREATOR : Parcelable.Creator<Book> {
                override fun createFromParcel(parcel: Parcel): Book {
                        return Book(parcel)
                }

                override fun newArray(size: Int): Array<Book?> {
                        return arrayOfNulls(size)
                }
        }
}