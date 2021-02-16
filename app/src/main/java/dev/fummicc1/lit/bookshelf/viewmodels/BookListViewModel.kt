package dev.fummicc1.lit.bookshelf.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.fummicc1.lit.bookshelf.datas.Book
import io.realm.Realm
import io.realm.RealmResults

class BookListViewModel: ViewModel() {

    val bookList: RealmResults<Book>

    private val realm: Realm = Realm.getDefaultInstance()

    init {
        realm.where(Book::class.java).apply {
             bookList = this.findAllAsync()
        }
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }
}