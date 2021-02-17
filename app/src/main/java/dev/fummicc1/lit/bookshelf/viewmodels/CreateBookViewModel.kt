package dev.fummicc1.lit.bookshelf.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import dev.fummicc1.lit.bookshelf.datas.Book
import io.realm.Realm
import java.util.*

class CreateBookViewModel: ViewModel() {

    enum class InputField {
        TITLE {
            override fun errorMessage(): String = "タイトルが入力されていません"
        },
        AUTHOR {
            override fun errorMessage(): String = "著者名が入力されていません"
        },
        PRICE {
            override fun errorMessage(): String = "金額が入力されていません"
        },
        DESCRIPTION {
            override fun errorMessage(): String = "説明が入力されていません"
        };

        abstract fun errorMessage() : String
    }

    private val _title: MutableLiveData<String> = MutableLiveData()
    private val _author: MutableLiveData<String> = MutableLiveData()
    private val _price: MutableLiveData<Int> = MutableLiveData()
    private val _description: MutableLiveData<String> = MutableLiveData()
    private val _errorInput: MutableLiveData<InputField> = MutableLiveData()
    private val _onCompleteCreating: MutableLiveData<Unit> = MutableLiveData()

    private val realm: Realm = Realm.getDefaultInstance()

    var editBookId: String? = null

    val title: LiveData<String>
        get() = _title
    val author: LiveData<String>
        get() = _author
    val price: LiveData<Int>
        get() = _price
    val description: LiveData<String>
        get() = _description
    val errorInput: LiveData<InputField>
        get() = _errorInput
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

    private fun updateBook(bookId: String) {
        realm.executeTransactionAsync {
            it.where(Book::class.java)
                .equalTo("id", bookId)
                .findFirst()
                ?.let {
                    it.title = _title.value ?: ""
                    it.author = _author.value ?: ""
                    it.price = _price.value ?: 0
                    it.description = _description.value ?: ""
                    it.updatedAt = Date()
                }
        }
    }

    fun persistBook() {

        if (
            checkInputAndShowError(_title.value, InputField.TITLE) &&
            checkInputAndShowError(_author.value, InputField.AUTHOR) &&
                    checkInputAndShowError(_price.value.toString(), InputField.PRICE) &&
                    checkInputAndShowError(_description.value,  InputField.DESCRIPTION)
        ) {
            editBookId?.let {
                // 編集状態であればBookをアップデートする
                updateBook(it)
                _onCompleteCreating.postValue(Unit)
                return
            }
            createBook()
            _onCompleteCreating.postValue(Unit)
        }
    }

    fun createBook() {
        realm.executeTransactionAsync {
            val book = it.createObject(Book::class.java, UUID.randomUUID().toString())
            book.title = _title.value!!
            book.author = _author.value!!
            book.price = _price.value!!
            book.description = _description.value!!
        }
    }

    fun fetchBook(id: String): Book? {
        return realm.where(Book::class.java)
            .equalTo("id", id)
            .findFirst()
    }


    fun checkInputAndShowError(input: String?, inputField: InputField): Boolean {
        if (input?.isEmpty() ?: true) {
            _errorInput.postValue(inputField)
            return false
        }
        return true
    }
}