package local.scherbakov.githubviewer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso
import local.scherbakov.githubviewer.model.Repository

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val repo = intent.getParcelableExtra<Repository>("REPO")

        findViewById<TextView>(R.id.repoName).text = repo?.name
        findViewById<TextView>(R.id.repoOwner).text = "Owner: ${repo?.owner?.login}"
        findViewById<TextView>(R.id.repoStars).text = "Stars: ${repo?.stars}"
        findViewById<TextView>(R.id.repoForks).text = "Forks: ${repo?.forks}"
        findViewById<TextView>(R.id.repoLanguage).text = "Language: ${repo?.language ?: "Unknown"}"
        findViewById<TextView>(R.id.repoDescription).text = repo?.description ?: "No description"

        val imageView = findViewById<ImageView>(R.id.ownerAvatar)
        Picasso.get().load(repo?.owner?.avatarUrl).into(imageView)

        findViewById<Button>(R.id.openGithubButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repo?.url))
            startActivity(intent)
        }
    }
}
