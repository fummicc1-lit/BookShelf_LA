package dev.fummicc1.lit.bookshelf.views

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import dev.fummicc1.lit.bookshelf.R
import dev.fummicc1.lit.bookshelf.datas.Book
import java.text.DateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class BookListAdapter(val context: Context): RecyclerView.Adapter<BookListAdapter.ViewHolder>() {

    private val bookList: MutableList<Book> = mutableListOf()

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
        val authorTextView = view.findViewById<TextView>(R.id.authorTextView)
        val timeTextView = view.findViewById<TextView>(R.id.timeTextView)
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
        val bookLocalDateTime = LocalDateTime.ofInstant(book.createdAt.toInstant(), ZoneId.systemDefault())
        val minute: Long = ChronoUnit.MINUTES.between(now, bookLocalDateTime)
        val hour: Long = ChronoUnit.HOURS.between(now, bookLocalDateTime)
        val timeText: String
        if (hour > 0 && hour < 24) {
            timeText = "${hour}時間前"
        } else if (minute >= 0) {
            timeText = "${minute}分前"
        } else {
            timeText = DateFormat.getDateInstance().format(book.createdAt)
        }
        holder.timeTextView.text = timeText
    }

    fun updateBookList(bookList: List<Book>, merge: Boolean = false) {
        if (merge.not()) {
            this.bookList.clear()
        }
        this.bookList.addAll(bookList)
        notifyDataSetChanged()
    }
}