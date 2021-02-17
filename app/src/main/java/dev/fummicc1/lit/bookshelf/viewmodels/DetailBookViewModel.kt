package dev.fummicc1.lit.bookshelf.viewmodels

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.fummicc1.lit.bookshelf.datas.Book
import io.realm.Realm
import kotlinx.android.parcel.Parcelize

class DetailBookViewModel: ViewModel() {

    enum class Destination {
        COLLECTION,
        EDIT
    }

    @Parcelize
    data class EditDestinationModel(val bookId: String): Parcelable

    private val realm: Realm = Realm.getDefaultInstance()

    private val _title: MutableLiveData<String> = MutableLiveData()
    private val _author: MutableLiveData<String> = MutableLiveData()
    private val _description: MutableLiveData<String> = MutableLiveData()
    private val _price: MutableLiveData<Int> = MutableLiveData()

    private val _showConfirmAlert: MutableLiveData<String> = MutableLiveData()

    private val _move: MutableLiveData<Pair<Destination, Parcelable?>> = MutableLiveData()

    var id: String? = null

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
        if (id == null) {
            return
        }
        realm.executeTransactionAsync {
            it.where(Book::class.java)
                .equalTo("id", id)
                .findAll()
                .apply {
                    this.deleteAllFromRealm()
                    _move.postValue(Pair(Destination.COLLECTION, null))
                }
        }
    }

    fun edit() {
        id?.let {
            val destinationModel = EditDestinationModel(it)
            _move.postValue(Pair(Destination.EDIT, destinationModel))
        }
    }
}