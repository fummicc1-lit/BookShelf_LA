package dev.fummicc1.lit.bookshelf.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.fummicc1.lit.bookshelf.datas.Book
import io.realm.Realm

class BookListViewModel: ViewModel() {

    private val _bookList: MutableLiveData<List<Book>> = MutableLiveData()
    val bookList: LiveData<List<Book>>
        get() = _bookList

    private val realm: Realm = Realm.getDefaultInstance()

    init {
        realm.where(Book::class.java).apply {
            val all = this.findAllAsync()
            _bookList.postValue(all)
        }
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }
}