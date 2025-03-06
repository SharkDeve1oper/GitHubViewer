package local.scherbakov.githubviewer.api

import local.scherbakov.githubviewer.model.GitHubResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search/repositories")
    fun searchRepositories(
        @Query("q") query: String
    ): Call<GitHubResponse>
}
