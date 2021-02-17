package dev.fummicc1.lit.bookshelf.datas

import android.content.Context
import androidx.room.*

@Database(entities = arrayOf(Book::class), version = 1)
@TypeConverters(Converters::class)
abstract class BookDatabase: RoomDatabase() {
    abstract fun bookDao(): BookDAO

    companion object {
        @Volatile
        private var _insatnce: BookDatabase? = null

        fun getDataBase(context: Context): BookDatabase {
            return _insatnce ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    "book_database"
                ).build()

                _insatnce = instance
                instance
            }
        }
    }
}