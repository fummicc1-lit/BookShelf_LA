package dev.fummicc1.lit.bookshelf.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
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

        val adapter = BookListAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel.bookList.observe(this, Observer {
            adapter.updateBookList(it)
        })


    }


}