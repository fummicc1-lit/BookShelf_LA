package dev.fummicc1.lit.bookshelf.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dev.fummicc1.lit.bookshelf.R
import dev.fummicc1.lit.bookshelf.datas.Book
import dev.fummicc1.lit.bookshelf.viewmodels.BookListViewModel
import dev.fummicc1.lit.bookshelf.views.BookListAdapter
import kotlinx.android.synthetic.main.activity_book_list.*

class BookListActivity : AppCompatActivity() {

    val viewModel by viewModels<BookListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        val adapterListener = object : BookListAdapter.OnItemClickListener {
            override fun onItemClick(item: Book) {
                val intent = Intent(this@BookListActivity, DetailBookActivity::class.java)
                intent.putExtra("detail_book", item)
                startActivity(intent)
            }
        }

        // RecyclerViewの設定
        val adapter = BookListAdapter(this, viewModel.bookList, adapterListener)
        // GridLayoutManager...横と縦の両方にアイテムを並べる
        // LinearLayoutManager...横いっぱいにして縦にアイテムを並べる
        recyclerView.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        moveToCreateBookButton.setOnClickListener {
            val intent = Intent(this, CreateBookActivity::class.java)
            startActivity(intent)
        }

        // エンプティステートの切り替え(RealmAdapterだと初期化以降に判断が難しいのでRoomに移行したら追加実装する)
//        val isEmpty = viewModel.bookList.isEmpty()
//        emptyStateTextView.visibility = if (isEmpty) View.VISIBLE else View.INVISIBLE
    }
}