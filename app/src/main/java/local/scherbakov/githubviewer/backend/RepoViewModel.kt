package local.scherbakov.githubviewer.backend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import local.scherbakov.githubviewer.model.Repository

class RepoViewModel : ViewModel() {
    private val repository = RepoRepository()

    private val _repositories = MutableLiveData<List<Repository>>()
    val repositories: LiveData<List<Repository>> get() = _repositories

    fun search(query: String) {
        repository.searchRepositories(query).observeForever {
            _repositories.value = it
        }
    }
}
