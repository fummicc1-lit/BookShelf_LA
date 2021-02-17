package dev.fummicc1.lit.bookshelf.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.fummicc1.lit.bookshelf.datas.Book
import io.realm.Realm

class DetailBookViewModel(book: Book): ViewModel() {

    private val realm: Realm

    private val _title: MutableLiveData<String> = MutableLiveData()
    private val _description: MutableLiveData<String> = MutableLiveData()
    private val _content: MutableLiveData<String> = MutableLiveData()
    private val _price: MutableLiveData<Int> = MutableLiveData()

    init {
        realm = Realm.getDefaultInstance()
    }

    fun delete(book: Book) {
        realm.executeTransactionAsync {
            it.where(Book::class.java)
                .equalTo("id", book.id)
                .findAll()
                .apply {
                    this.deleteAllFromRealm()
                }
        }
    }
}