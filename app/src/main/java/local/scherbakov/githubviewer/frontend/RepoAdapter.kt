package local.scherbakov.githubviewer.frontend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import local.scherbakov.githubviewer.R
import local.scherbakov.githubviewer.model.Repository

class RepoAdapter(private val onClick: (Repository) -> Unit) :
    RecyclerView.Adapter<RepoAdapter.RepoViewHolder>() {

    private var repoList: List<Repository> = emptyList()

    fun submitList(list: List<Repository>) {
        repoList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repo, parent, false)
        return RepoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(repoList[position])
    }

    override fun getItemCount(): Int = repoList.size

    inner class RepoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.repoName)
        private val starsTextView: TextView = itemView.findViewById(R.id.repoStars)
        private val languageTextView: TextView = itemView.findViewById(R.id.repoLanguage)

        fun bind(repo: Repository) {
            nameTextView.text = repo.name
            starsTextView.text = "⭐ ${repo.stars}"
            languageTextView.text = repo.language ?: "Unknown"

            itemView.setOnClickListener { onClick(repo) }
        }
    }
}
