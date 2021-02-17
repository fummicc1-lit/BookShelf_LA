package dev.fummicc1.lit.bookshelf.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    fun createBook() {
        if (
            checkInputAndShowError(title.value, InputField.TITLE) &&
            checkInputAndShowError(author.value, InputField.AUTHOR) &&
                    checkInputAndShowError(price.value.toString(), InputField.PRICE) &&
                    checkInputAndShowError(description.value,  InputField.DESCRIPTION)
        ) {
            realm.executeTransactionAsync {
                val book = it.createObject(Book::class.java, UUID.randomUUID().toString())
                book.title = title.value!!
                book.author = author.value!!
                book.price = price.value!!
                book.description = description.value!!
            }
            _onCompleteCreating.postValue(Unit)
        }
    }

    fun checkInputAndShowError(input: String?, inputField: InputField): Boolean {
        if (input?.isEmpty() ?: true) {
            _errorInput.postValue(inputField)
            return false
        }
        return true
    }
}