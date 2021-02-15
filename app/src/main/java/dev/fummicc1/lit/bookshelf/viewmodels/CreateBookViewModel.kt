package dev.fummicc1.lit.bookshelf.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.fummicc1.lit.bookshelf.datas.Book
import io.realm.Realm

class CreateBookViewModel: ViewModel() {

    private val _title: MutableLiveData<String> = MutableLiveData()
    private val _author: MutableLiveData<String> = MutableLiveData()
    private val _price: MutableLiveData<Int> = MutableLiveData()
    private val _description: MutableLiveData<String> = MutableLiveData()
    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    private val _onCompleteCreating: MutableLiveData<Unit> = MutableLiveData()

    private val realm: Realm = Realm.getDefaultInstance()

    val title: LiveData<String>
        get() = _title
    val author: LiveData<String>
        get() = _author
    val price: LiveData<Int>
        get() = _price
    val description: LiveData<String>
        get() = _description
    val errorMessage: LiveData<String>
        get() = _errorMessage
    val onCompleteCreating: LiveData<Unit>
        get() = _onCompleteCreating

    fun updateTitle(title: String) {
        _title.postValue(title)
    }

    fun updateDescription(description: String) {
        _description.postValue(description)
    }

    fun updatePrice(price: Int) {
        _price.postValue(price)
    }

    fun updateAuthor(author: String) {
        _author.postValue(author)
    }

    fun createBook() {
        if (
            checkInputAndShowError(title.value, "タイトルが入力されていません") &&
            checkInputAndShowError(author.value, "著者名が入力されていません") &&
                    checkInputAndShowError(price.value.toString(), "値段が入力されていません") &&
                    checkInputAndShowError(description.value, "説明が入力されていません")
        ) {
            realm.executeTransaction {
                val book = it.createObject(Book::class.java)
                book.title = title.value!!
                book.author = author.value!!
                book.price = price.value!!
                book.description = description.value!!
            }
            _onCompleteCreating.postValue(Unit)
        }
    }

    fun checkInputAndShowError(input: String?, errorMessage: String): Boolean {
        if (input?.isEmpty() ?: true) {
            _errorMessage.postValue(errorMessage)
            return false
        }
        return true
    }
}