package dev.fummicc1.lit.bookshelf.datas

import io.realm.RealmObject
import java.util.*

open class Book(
        var title: String = "",
        var author: String = "",
        var price: Int = 0,
        var description: String = "",
        var createdAt: Date = Date()
): RealmObject()