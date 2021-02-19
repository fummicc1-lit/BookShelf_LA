package dev.fummicc1.lit.bookshelf.viewmodels

import android.app.Application
import android.telecom.Call
import androidx.lifecycle.*
import com.google.gson.GsonBuilder
import dev.fummicc1.lit.androidqrcodereader.BookService
import dev.fummicc1.lit.bookshelf.Constants
import dev.fummicc1.lit.bookshelf.datas.Book
import dev.fummicc1.lit.bookshelf.datas.BookDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class CreateBookViewModel(application: Application): AndroidViewModel(application) {

    data class BookViewModel(
        val title: String,
        val author: String?,
        val price: Int?,
        val description: String?
    )

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

    private val database: BookDatabase = BookDatabase.getDataBase(application.applicationContext)

    private val _title: MutableLiveData<String> = MutableLiveData()
    private val _author: MutableLiveData<String> = MutableLiveData()
    private val _price: MutableLiveData<Int> = MutableLiveData()
    private val _description: MutableLiveData<String> = MutableLiveData()
    private val _errorInput: MutableLiveData<InputField> = MutableLiveData()
    private val _onCompleteCreating: MutableLiveData<Unit> = MutableLiveData()

    var editBookId: Int? = null

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

    private fun updateBook(bookId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val book = database.bookDao().get(bookId)

            if (book == null) {
                return@launch
            }

            book.title = _title.value ?: ""
            book.author = _author.value ?: ""
            book.price = _price.value ?: 0
            book.description = _description.value ?: ""
            book.updatedAt = Date()

            database.bookDao().update(book)

            this.launch(Dispatchers.Main) {
                _onCompleteCreating.postValue(Unit)
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
                return
            }
            createBook()
        }
    }

    fun createBook() {
        val title = _title.value!!
        val author = _author.value!!
        val price = _price.value!!
        val description = _description.value!!
        val book = Book(
            0,
            title,
            author,
            price,
            description,
            Date()
        )
        viewModelScope.launch(Dispatchers.IO) {
            database.bookDao().create(book)
            this.launch(Dispatchers.Main) {
                _onCompleteCreating.postValue(Unit)
            }
        }
    }

    fun fetchBook(id: Int): Book? {
        return runBlocking(Dispatchers.IO) {
            database.bookDao().get(id)
        }
    }


    fun checkInputAndShowError(input: String?, inputField: InputField): Boolean {
        if (input?.isEmpty() ?: true) {
            _errorInput.postValue(inputField)
            return false
        }
        return true
    }

    suspend fun fetchBook(isbnNumber: String): BookViewModel?  {
        val gson = GsonBuilder().create()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.bookApiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val bookService = retrofit.create(BookService::class.java)

        val bookResponse= bookService.getBook("isbn:${isbnNumber}")
        val book = bookResponse.items?.firstOrNull()
        if (book == null) {
            return null
        }

        // BookViewModelを作成
        val bookViewModel = BookViewModel(
            book.volumeInfo.title,
            book.volumeInfo.authors.firstOrNull(),
            book.saleInfo?.ratailPrice?.getPrice(),
            book.volumeInfo.description
        )

        // ViewModelのデータを更新
        viewModelScope.launch(Dispatchers.Main) {
            updateTitle(book.volumeInfo.title)
            updateAuthor(book.volumeInfo.authors.first())
            book.saleInfo?.ratailPrice?.getPrice()?.let { price ->
                updatePrice(price)
            }
            book.volumeInfo.description?.let { description ->
                updateDescription(description)
            }
        }

        return bookViewModel
    }
}