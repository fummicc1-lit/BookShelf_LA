package dev.fummicc1.lit.bookshelf.datas

import io.realm.RealmObject
import java.util.*

// Open修飾子をつける必要がある
// 変数にデフォルトの値を設定する必要がある
// RealmObjectを継承する必要がある→Data Classは使えない
open class Book(
        var title: String = "",
        var author: String = "",
        var price: Int = 0,
        var description: String = "",
        var createdAt: Date = Date()
): RealmObject()