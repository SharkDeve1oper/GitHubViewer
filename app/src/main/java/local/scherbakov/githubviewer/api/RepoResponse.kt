package local.scherbakov.githubviewer.api

import local.scherbakov.githubviewer.model.Repository

data class RepoResponse(
    val total_count: Int,
    val items: List<Repository>
)