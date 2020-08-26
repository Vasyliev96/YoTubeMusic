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
import vasyliev.android.yotubemusic.db.MyContentResolver
import vasyliev.android.yotubemusic.db.YoTubeSongData

private const val PREF_FIRST_TIME = "isAppFirstTimeLaunched"

class SecondActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    SecondActivityAdapter.CallbackItemClickListener {
    private val artists: Array<String?> = arrayOf(
        "Artists",
        "Author 1",
        "Author 2",
        "Author 3",
        "Author 4"
    )
    private val genres: Array<String?> = arrayOf(
        "Genres",
        "Genre 1",
        "Genre 2",
        "Genre 3"
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
        MyContentResolver.context = this

        val defSong = getSharedPreferences(PREF_FIRST_TIME, MODE_PRIVATE)

        if (defSong.getBoolean("booleanFirstTime", true)) {
            secondActivityViewModel.loadMusic()
            getSharedPreferences(PREF_FIRST_TIME, MODE_PRIVATE).edit().apply {
                putBoolean("booleanFirstTime", false)
                apply()
            }
        }

        secondActivityViewModel.getMusic()
        yoTubeMusicRecyclerView = findViewById(R.id.recyclerViewYoTubeMusic)
        yoTubeMusicRecyclerView.layoutManager = LinearLayoutManager(this)
        secondActivityAdapter =
            SecondActivityAdapter(emptyList(), this)
        yoTubeMusicRecyclerView.adapter = secondActivityAdapter
    }

    override fun onStart() {
        super.onStart()
        secondActivityViewModel.songListLiveData?.observe(
            this,
            { songs ->
                songs?.let {
                    updateUI(songs)
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        MyContentResolver.context = null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            if (parent == spinnerArtists) {
                if (position == 0) {
                    secondActivityViewModel.currentArtist = null
                    //secondActivityViewModel.getMusic(this)
                } else {
                    secondActivityViewModel.currentArtist =
                        parent.getItemAtPosition(position).toString()
                    //secondActivityViewModel.getMusic(this)
                }
                updateObserver()
            } else {
                if (position == 0) {
                    secondActivityViewModel.currentGenre = null
                    //secondActivityViewModel.getMusic(this)
                } else {
                    secondActivityViewModel.currentGenre =
                        parent.getItemAtPosition(position).toString()
                    //secondActivityViewModel.getMusic(this)
                }
                updateObserver()
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

    private fun updateObserver() {
        //secondActivityViewModel.requestMusic()
        secondActivityViewModel.getMusic()
        secondActivityViewModel.songListLiveData?.removeObservers(this)
        secondActivityViewModel.songListLiveData?.observe(
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