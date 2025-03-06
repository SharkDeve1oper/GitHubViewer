package local.scherbakov.githubviewer.backend

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import local.scherbakov.githubviewer.api.RetrofitClient
import local.scherbakov.githubviewer.model.GitHubResponse
import local.scherbakov.githubviewer.model.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepoRepository {
    private val apiService = RetrofitClient.apiService

    fun searchRepositories(query: String): LiveData<List<Repository>> {
        val data = MutableLiveData<List<Repository>>()

        apiService.searchRepositories(query).enqueue(object : Callback<GitHubResponse> {
            override fun onResponse(call: Call<GitHubResponse>, response: Response<GitHubResponse>) {
                if (response.isSuccessful) {
                    val repositories = response.body()?.items ?: emptyList()
                    Log.d("API_RESPONSE", "Получено ${repositories.size} репозиториев")
                    data.postValue(repositories)  // Обновляем данные в LiveData
                } else {
                    Log.e("API_ERROR", "Ошибка ответа: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GitHubResponse>, t: Throwable) {
                Log.e("API_FAILURE", "Ошибка сети: ${t.message}", t)
            }
        })

        return data
    }
}
