package dev.fummicc1.lit.bookshelf.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dev.fummicc1.lit.bookshelf.R
import dev.fummicc1.lit.bookshelf.viewmodels.BookListViewModel
import dev.fummicc1.lit.bookshelf.views.BookListAdapter
import kotlinx.android.synthetic.main.activity_book_list.*

class BookListActivity : AppCompatActivity() {

    val viewModel by viewModels<BookListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        // RecyclerViewの設定
        val adapter = BookListAdapter(this, viewModel.bookList)
        // GridLayoutManager...横と縦の両方にアイテムを並べる
        // LinearLayoutManager...横いっぱいにして縦にアイテムを並べる
        recyclerView.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        moveToCreateBookButton.setOnClickListener {
            val intent = Intent(this, CreateBookActivity::class.java)
            startActivity(intent)
        }
    }
}