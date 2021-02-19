package dev.fummicc1.lit.bookshelf.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputLayout
import com.google.zxing.integration.android.IntentIntegrator
import dev.fummicc1.lit.bookshelf.R
import dev.fummicc1.lit.bookshelf.viewmodels.CreateBookViewModel
import dev.fummicc1.lit.bookshelf.viewmodels.DetailBookViewModel
import kotlinx.android.synthetic.main.activity_create_book.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateBookActivity : AppCompatActivity() {

    val viewModel: CreateBookViewModel by viewModels()

    val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_book)
        
        // ToolBarのタイトルを空文字に設定
        title = ""

        makeBackButtonEnable()
        
        configureInitialStateIfBookExists()

        viewModel.title.observe(this, Observer {
            if (it.isNotEmpty()) {
                titleInputLayout.error = null
            }
        })

        viewModel.author.observe(this, Observer {
            if (it.isNotEmpty()) {
                authorInputLayout.error = null
            }
        })

        viewModel.price.observe(this, Observer {
            priceInputLayout.error = null
        })

        viewModel.description.observe(this, Observer {
            if (it.isNotEmpty()) {
                descriptionInputLayout.error = null
            }
        })

        viewModel.errorInput.observe(this, Observer {
            val errorInput: TextInputLayout = when (it) {
                CreateBookViewModel.InputField.TITLE -> titleInputLayout
                    CreateBookViewModel.InputField.AUTHOR -> authorInputLayout
                    CreateBookViewModel.InputField.PRICE -> priceInputLayout
                    CreateBookViewModel.InputField.DESCRIPTION -> descriptionInputLayout
            }
            errorInput.error = it.errorMessage()
        })

        viewModel.onCompleteCreating.observe(this, Observer {
            finish()
        })

        titleEdit.addTextChangedListener {
            it?.toString()?.let {
                viewModel.updateTitle(it)
            }
        }

        authorEdit.addTextChangedListener {
            it?.toString()?.let {
                viewModel.updateAuthor(it)
            }
        }

        priceEdit.addTextChangedListener {
            kotlin.runCatching {
                it?.toString()?.let {
                    Integer.parseInt(it)
                }
            }.onSuccess {
                it?.let {
                    viewModel.updatePrice(it)
                }
            }.onFailure {
                priceInputLayout.error = it.localizedMessage
            }
        }

        descriptionEdit.addTextChangedListener {
            it?.toString()?.let {
                viewModel.updateDescription(it)
            }
        }

        createBookButton.setOnClickListener {
            viewModel.persistBook()
        }
    }

    // もし前の画面からBookIdを渡されていた場合、初期状態を変更する
    fun configureInitialStateIfBookExists() {
        val bookId = intent.getParcelableExtra<DetailBookViewModel.EditDestinationModel>("destination_model")?.bookId
        if (bookId == null) return
        val book = viewModel.fetchBook(bookId)
        if (book == null) return
        viewModel.updateTitle(book.title)
        viewModel.updateAuthor(book.author)
        viewModel.updatePrice(book.price)
        viewModel.updateDescription(book.description)
        viewModel.editBookId = bookId

        titleEdit.setText(book.title)
        authorEdit.setText(book.author)
        priceEdit.setText(book.price.toString())
        descriptionEdit.setText(book.description)

        createBookButton.text = "変更"
    }

    //　戻るボタンを有効にする
    fun makeBackButtonEnable() {
        // supportActionBarにToolbarを設定する
        setSupportActionBar(createBookToolBar)
        // AppCompactActivityからActionBarを取得するには`getActionBar`じゃなくて`getSupportActionBar`を使用する
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflator = menuInflater
        inflator.inflate(R.menu.create_book_menu, menu)
        return true
    }

    // Optionが選択された時に実行される
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            (android.R.id.home) -> finish()
            (R.id.scan_barcode) -> {
                IntentIntegrator(this).initiateScan()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        IntentIntegrator.parseActivityResult(requestCode, resultCode, data)?.let { result ->
            scope.launch {
                val bookViewModel = viewModel.fetchBook(result.contents)
                withContext(Dispatchers.Main) {
                    titleEdit.setText(bookViewModel?.title ?: "")
                    authorEdit.setText(bookViewModel?.author ?: "")
                    val price = bookViewModel?.price
                    if (price != null) {
                        priceEdit.setText("$price")
                    }
                    val description = bookViewModel?.description
                    if (description != null) {
                        descriptionEdit.setText(description)
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}