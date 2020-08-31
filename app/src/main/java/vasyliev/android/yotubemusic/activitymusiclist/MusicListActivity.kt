package vasyliev.android.yotubemusic.activitymusiclist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_music_list.*
import vasyliev.android.yotubemusic.R
import vasyliev.android.yotubemusic.musicdatabase.SongData

class MusicListActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    MusicListAdapter.CallbackItemClickListener {
    private lateinit var musicListAdapter: MusicListAdapter
    private lateinit var musicListRecyclerView: RecyclerView
    private val musicListViewModel: MusicListViewModel by lazy {
        ViewModelProvider(this).get(MusicListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_list)
        musicListViewModel.apply {
            authors = resources.getStringArray(R.array.authors_array)
            genres = resources.getStringArray(R.array.genres_array)
            uploadMusicToDatabase(this@MusicListActivity)
            getMusic()
        }
        musicListRecyclerView = findViewById(R.id.recyclerViewMusic)
        musicListRecyclerView.layoutManager = LinearLayoutManager(this)
        musicListAdapter = MusicListAdapter(emptyList(), this)
        musicListRecyclerView.adapter = musicListAdapter
    }

    override fun onStart() {
        super.onStart()
        setSpinner()
        val musicObserver:Observer<List<SongData>> =
            Observer<List<SongData>> { updatedList -> updateUI(updatedList) }

        musicListViewModel.musicListLiveData?.observe(
            this,
            musicObserver
        )
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            if (parent == spinnerAuthors) {
                if (position == 0) {
                    musicListViewModel.currentAuthor = null
                } else {
                    musicListViewModel.currentAuthor = parent.getItemAtPosition(position).toString()
                }
            } else {
                if (position == 0) {
                    musicListViewModel.currentGenre = null
                } else {
                    musicListViewModel.currentGenre =
                        parent.getItemAtPosition(position).toString()
                }
            }
            musicListViewModel.getMusic()
        }
    }

    override fun onNothingSelected(arg0: AdapterView<*>?) {
        Toast.makeText(
            this@MusicListActivity,
            getString(R.string.on_nothing_selected_toast_text),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onItemClick(position: Int) {
        sendBroadcast(
            Intent(ACTION_SONG_SELECTED).putExtra(
                SONG_ID,
                musicListAdapter.getItem(position).id.toString()
            )
        )
        onBackPressed()
    }

    private fun updateUI(users: List<SongData>) {
        musicListAdapter = MusicListAdapter(users, this)
        musicListRecyclerView.adapter = musicListAdapter
    }

    private fun setSpinner() {
        spinnerAuthors.onItemSelectedListener = this
        spinnerGenres.onItemSelectedListener = this
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            musicListViewModel.authors
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAuthors.adapter = adapter
        }
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            musicListViewModel.genres
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGenres.adapter = adapter
        }
    }

    companion object {
        const val SONG_ID = "song_id"
        const val PERM_PRIVATE = "vasyliev.android.yotubemusic.PRIVATE"
        const val ACTION_SONG_SELECTED = "vasyliev.android.yotubemusic.song_selected"
    }
}