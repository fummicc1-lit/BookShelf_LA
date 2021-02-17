package dev.fummicc1.lit.bookshelf.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import dev.fummicc1.lit.bookshelf.R
import dev.fummicc1.lit.bookshelf.datas.Book
import dev.fummicc1.lit.bookshelf.viewmodels.DetailBookViewModel
import kotlinx.android.synthetic.main.activity_detail_book.*
import kotlinx.android.synthetic.main.item_book_list.*

class DetailBookActivity : AppCompatActivity() {

    private val viewModel: DetailBookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_book)

        configureToolBar()

        val bookId = intent.getIntExtra("detail_book_id", 0)
        viewModel.fetchBook(bookId).observe(this, Observer { book ->
            if (book != null) {
                viewModel.updateTitle(book.title)
                viewModel.updateAuthor(book.author)
                viewModel.updateprice(book.price)
                viewModel.updateDescription(book.description)
                viewModel.id = book.id
            }
        })

        viewModel.title.observe(this, Observer {
            titleContentTextViewInDetail.text = it
        })

        viewModel.author.observe(this, Observer {
            authorContentTextViewInDetail.text = it
        })

        viewModel.price.observe(this, Observer {
            val text = it.toString()
            priceContentTextViewInDetail.text = "¥$text"
        })

        viewModel.description.observe(this, Observer {
            descriptionContentTextViewInDetail.text = it
        })

        viewModel.move.observe(this, Observer {
            when (it.first) {
                DetailBookViewModel.Destination.EDIT -> {
                    val intent = Intent(this, CreateBookActivity::class.java)
                    intent.putExtra("destination_model", it.second)
                    startActivity(intent)
                }
                DetailBookViewModel.Destination.COLLECTION -> finish()
            }
        })

        viewModel.showConfirmAlert.observe(this, Observer {
            AlertDialog.Builder(this)
                .setTitle("本の削除")
                .setMessage(it)
                .setPositiveButton("OK", { dialog, which ->
                    viewModel.delete()
                })
                .setNegativeButton("CANCEL", { _,_ -> })
                .show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflator = menuInflater
        inflator.inflate(R.menu.detail_book_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            (android.R.id.home) -> finish()
            (R.id.delete_book) -> {
                viewModel.confirmDelete()
            }
            (R.id.edit_book) -> {
                viewModel.edit()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun configureToolBar() {
        setSupportActionBar(detailBookToolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}