package vasyliev.android.yotubemusic

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_search.*
import vasyliev.android.yotubemusic.db.YoTubeSongData


class SecondActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    SecondActivityAdapter.CallbackItemClickListener {
    private val artists: Array<String?> = arrayOf(
        "Artists",
        "Author 1",
        "Author 2",
        "Author 3",
        "Author 4",
        "Author 5",
        "Author 6",
    )
    private val genres: Array<String?> = arrayOf(
        "Genres",
        "Genre 1",
        "Genre 2",
        "Genre 3",
        "Genre 4",
        "Genre 5",
        "Genre 6",
        "Genre 7",
        "Genre 8",
    )

    private lateinit var secondActivityAdapter: SecondActivityAdapter
    private lateinit var yoTubeMusicRecyclerView: RecyclerView
    private val secondActivityViewModel: SecondActivityViewModel by lazy {
        ViewModelProvider(this).get(SecondActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSpinner()
        //secondActivityViewModel.loadMusic(assets)

        yoTubeMusicRecyclerView = findViewById(R.id.recyclerViewYoTubeMusic)
        yoTubeMusicRecyclerView.layoutManager = LinearLayoutManager(this)
        secondActivityAdapter =
            SecondActivityAdapter(emptyList(), this)
        yoTubeMusicRecyclerView.adapter = secondActivityAdapter
    }

    override fun onStart() {
        super.onStart()
        secondActivityViewModel.songListLiveData.observe(
            this,
            { songs ->
                songs?.let {
                    updateUI(songs)
                }
            }
        )
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            if (parent == spinnerArtists) {
                if (position == 0) {
                    secondActivityViewModel.currentArtist = null
                } else {
                    secondActivityViewModel.currentArtist =
                        parent.getItemAtPosition(position).toString()
                }
                resetObserver()
            } else {
                if (position == 0) {
                    secondActivityViewModel.currentGenre = null
                } else {
                    secondActivityViewModel.currentGenre =
                        parent.getItemAtPosition(position).toString()
                }
                resetObserver()
            }
        }
    }

    override fun onNothingSelected(arg0: AdapterView<*>?) {
        Toast.makeText(
            this@SecondActivity,
            "nothing selected",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onItemClick(view: View, position: Int) {
        sendBroadcast(
            Intent(ACTION_SONG_SELECTED).putExtra(
                SONG_ID,
                secondActivityAdapter.getItem(position).id.toString()
            )
        )
        onBackPressed()
    }

    private fun resetObserver() {
        secondActivityViewModel.requestMusic()
        secondActivityViewModel.songListLiveData.removeObservers(this)
        secondActivityViewModel.songListLiveData.observe(
            this,
            { songs ->
                songs?.let {
                    updateUI(songs)
                }
            }
        )
    }

    private fun updateUI(users: List<YoTubeSongData>) {
        secondActivityAdapter = SecondActivityAdapter(users, this)
        yoTubeMusicRecyclerView.adapter = secondActivityAdapter
    }

    private fun setSpinner() {
        spinnerArtists.onItemSelectedListener = this
        spinnerGenres.onItemSelectedListener = this
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            artists
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerArtists.adapter = adapter
        }
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            genres
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