package local.scherbakov.githubviewer

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import local.scherbakov.githubviewer.backend.RepoViewModel
import local.scherbakov.githubviewer.frontend.RepoAdapter
import local.scherbakov.githubviewer.model.Repository
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: RepoViewModel
    private lateinit var adapter: RepoAdapter

    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    private val delayMillis: Long = 500  // Задержка в миллисекундах для debounce

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация ViewModel и адаптера
        viewModel = ViewModelProvider(this)[RepoViewModel::class.java]
        adapter = RepoAdapter { repo -> openDetails(repo) }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val searchEditText: EditText = findViewById(R.id.searchEditText)

        // Обработчик изменения текста с debounce
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // Не нужно ничего делать
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Отменяем предыдущие запросы, если они ещё не отправлены
                searchRunnable?.let { handler.removeCallbacks(it) }

                // Отправляем запрос через задержку
                val query = charSequence.toString().trim()
                if (query.isNotBlank()) {
                    searchRunnable = Runnable {
                        viewModel.search(query)  // Отправка запроса
                    }
                    handler.postDelayed(searchRunnable!!, delayMillis)
                }
            }

            override fun afterTextChanged(editable: Editable?) {
                // Не нужно ничего делать
            }
        })

        // Обработчик клавиши "Поиск"
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = searchEditText.text.toString().trim()
                if (query.isNotBlank()) {
                    viewModel.search(query)  // Повторно отправляем запрос
                }
                true
            } else false
        }

        // Наблюдатель за результатами
        viewModel.repositories.observe(this, Observer { repos ->
            adapter.submitList(repos)  // Обновляем список
        })
    }

    // Переход на экран с подробностями о репозитории
    private fun openDetails(repo: Repository) {
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra("REPO", repo)
        }
        startActivity(intent)
    }
}
