package dev.fummicc1.lit.bookshelf.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import dev.fummicc1.lit.bookshelf.datas.Book
import dev.fummicc1.lit.bookshelf.datas.BookDatabase
import kotlinx.coroutines.runBlocking

class BookListViewModel(application: Application): AndroidViewModel(application) {

    val bookList: LiveData<List<Book>>

    private val database = BookDatabase.getDataBase(application.applicationContext)

    init {
        bookList = runBlocking {
            database.bookDao().observeAll()
        }
    }
}