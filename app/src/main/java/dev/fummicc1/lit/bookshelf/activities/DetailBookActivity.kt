package dev.fummicc1.lit.bookshelf.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dev.fummicc1.lit.bookshelf.R
import dev.fummicc1.lit.bookshelf.datas.Book
import dev.fummicc1.lit.bookshelf.viewmodels.DetailBookViewModel
import kotlinx.android.synthetic.main.activity_detail_book.*

class DetailBookActivity : AppCompatActivity() {

    private val viewModel: DetailBookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_book)



        val book = intent.getParcelableExtra("detail_book") as? Book

        if (book == null) {
            return
        }


    }
}