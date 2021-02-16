package dev.fummicc1.lit.bookshelf.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dev.fummicc1.lit.bookshelf.R
import dev.fummicc1.lit.bookshelf.viewmodels.CreateBookViewModel
import kotlinx.android.synthetic.main.activity_create_book.*

class CreateBookActivity : AppCompatActivity() {

    val viewModel: CreateBookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_book)

        makeBackButtonEnable()

        viewModel.title.observe(this, Observer {

        })

        viewModel.author.observe(this, Observer {

        })

        viewModel.errorMessage.observe(this, Observer {

        })

        viewModel.price.observe(this, Observer {

        })

        viewModel.description.observe(this, Observer {

        })


    }

    //　戻るボタンを有効にする
    fun makeBackButtonEnable() {
        // supportActionBarにToolbarを設定する
        setSupportActionBar(createBookToolBar)
        // AppCompactActivityからActionBarを取得するには`getActionBar`じゃなくて`getSupportActionBar`を使用する
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Optionが選択された時に実行される
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            (android.R.id.home) -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}