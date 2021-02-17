package dev.fummicc1.lit.bookshelf.views

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import dev.fummicc1.lit.bookshelf.R
import dev.fummicc1.lit.bookshelf.datas.Book
import kotlinx.android.synthetic.main.item_book_list.view.*
import java.text.DateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class BookListAdapter(
    val context: Context,
    val listener: OnItemClickListener
): RecyclerView.Adapter<BookListAdapter.ViewHolder>() {

    var bookList: MutableList<Book> = mutableListOf()

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val titleTextView = view.titleTextView
        val authorTextView = view.authorTextView
        val timeTextView = view.timeTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_book_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = bookList.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = bookList[position]
        holder.titleTextView.text = book.title
        holder.authorTextView.text = book.author
        val now = LocalDateTime.now()
        val bookLocalDateTime = LocalDateTime.ofInstant(book.updatedAt.toInstant(), ZoneId.systemDefault())
        val minute: Long = ChronoUnit.MINUTES.between(bookLocalDateTime, now)
        val hour: Long = ChronoUnit.HOURS.between(bookLocalDateTime, now)
        val timeText: String
        if (hour > 0 && hour < 24) {
            timeText = "${hour}時間前"
        } else if (minute >= 0 && minute < 60) {
            timeText = "${minute}分前"
        } else {
            timeText = DateFormat.getDateInstance().format(book.updatedAt)
        }
        holder.timeTextView.text = timeText

        holder.view.setOnClickListener {
            listener.onItemClick(book)
        }
    }

    fun updateBookList(bookList: List<Book>) {
        this.bookList.clear()
        this.bookList.addAll(bookList)
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(item: Book)
    }
}