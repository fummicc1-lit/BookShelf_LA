package dev.fummicc1.lit.bookshelf.viewmodels

import android.app.Application
import android.os.Parcelable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.fummicc1.lit.bookshelf.datas.Book
import dev.fummicc1.lit.bookshelf.datas.BookDatabase
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DetailBookViewModel(application: Application): AndroidViewModel(application) {

    enum class Destination {
        COLLECTION,
        EDIT
    }

    @Parcelize
    data class EditDestinationModel(val bookId: Int): Parcelable

    private val database: BookDatabase = BookDatabase.getDataBase(application.applicationContext)

    private val _title: MutableLiveData<String> = MutableLiveData()
    private val _author: MutableLiveData<String> = MutableLiveData()
    private val _description: MutableLiveData<String> = MutableLiveData()
    private val _price: MutableLiveData<Int> = MutableLiveData()

    private val _showConfirmAlert: MutableLiveData<String> = MutableLiveData()

    private val _move: MutableLiveData<Pair<Destination, Parcelable?>> = MutableLiveData()

    var id: Int? = null

    val title: LiveData<String>
        get() = _title
    val author: LiveData<String>
        get() = _author
    val description: LiveData<String>
        get() = _description
    val price: LiveData<Int>
        get() = _price
    val move: LiveData<Pair<Destination, Parcelable?>>
        get() = _move
    val showConfirmAlert: LiveData<String>
        get() = _showConfirmAlert

    fun updateTitle(title: String) {
        this._title.postValue(title)
    }

    fun updateAuthor(author: String) {
        this._author.postValue(author)
    }

    fun updateDescription(description: String) {
        this._description.postValue(description)
    }

    fun updateprice(price: Int) {
        this._price.postValue(price)
    }

    fun confirmDelete() {
        _showConfirmAlert.postValue("${_title.value}を削除しますか？")
    }

    fun delete() {
        id?.let { id ->
            viewModelScope.launch(Dispatchers.IO) {
                val book = database.bookDao().get(id)
                if (book == null) {
                    return@launch
                }
                database.bookDao().delete(book)
                this.launch(Dispatchers.Main) {
                    _move.postValue(Pair(Destination.COLLECTION, null))
                }
            }
        }
    }

    fun edit() {
        id?.let {
            val destinationModel = EditDestinationModel(it)
            _move.postValue(Pair(Destination.EDIT, destinationModel))
        }
    }

    fun fetchBook(id: Int): LiveData<Book?> = runBlocking(Dispatchers.IO) { database.bookDao().observe(id) }
}