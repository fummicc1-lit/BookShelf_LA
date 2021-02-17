package dev.fummicc1.lit.bookshelf.datas

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookDAO {
    @Query("SELECT * FROM book")
    fun observeAll(): LiveData<List<Book>>

    @Query("SELECT * FROM book WHERE id == :id")
    fun get(id: Int): Book?

    @Query("SELECT * FROM book WHERE id == :id")
    fun observe(id: Int): LiveData<Book?>

    @Insert
    fun create(book: Book): Long

    @Update
    fun update(book: Book)

    @Delete
    fun delete(book: Book)
}