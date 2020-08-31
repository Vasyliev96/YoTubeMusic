package vasyliev.android.yotubemusic

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import vasyliev.android.yotubemusic.activitymusiclist.MusicListActivity
import vasyliev.android.yotubemusic.musicdatabase.SongData
import vasyliev.android.yotubemusic.service.MusicPlayerService
import vasyliev.android.yotubemusic.service.MusicPlayerService.MusicPlayerServiceBinder
import java.util.*


const val PREF_DEFAULT_SONG = "preference default song"
const val PREF_SONG_ID = "preference song id"

class MainActivity : AppCompatActivity(), ServiceConnection {

    private val mainActivityViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }

    private val onSongSelected = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            mainActivityViewModel.musicPlayerService?.stopSong(0)
            val songId: UUID = UUID.fromString(intent.getStringExtra(MusicListActivity.SONG_ID))
            mainActivityViewModel.loadSong(songId)
            getSharedPreferences(PREF_DEFAULT_SONG, MODE_PRIVATE).edit().apply {
                putString(PREF_SONG_ID, songId.toString())
                apply()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val defaultSongID =
            getSharedPreferences(PREF_DEFAULT_SONG, MODE_PRIVATE).getString(PREF_SONG_ID, null)

        if (defaultSongID != null) {
            mainActivityViewModel.loadSong(UUID.fromString(defaultSongID))
        }

        registerReceiver(
            onSongSelected,
            IntentFilter(MusicListActivity.ACTION_SONG_SELECTED),
            MusicListActivity.PERM_PRIVATE,
            null
        )

        bindService(Intent(this, MusicPlayerService::class.java), this, BIND_AUTO_CREATE)

        setContentView(R.layout.activity_main)
        mainActivityViewModel.songData = SongData()
        mainActivityViewModel.songLiveData.observe(
            this,
            { song ->
                song?.let {
                    mainActivityViewModel.songData = song
                    mainActivityViewModel.currentSongFilepath = mainActivityViewModel.songData.rawResId
                    updateUI()
                }
            }
        )
    }

    private fun updateUI() {
        textViewSongName.text = mainActivityViewModel.songData.songName
        textViewAuthor.text = mainActivityViewModel.songData.author
        textViewGenre.text = mainActivityViewModel.songData.genre
    }

    override fun onStart() {
        super.onStart()
        buttonSelectSong.setOnClickListener {
            startActivity(Intent(this, MusicListActivity::class.java))
        }
        buttonPlaySong.setOnClickListener {
            mainActivityViewModel.action(this, it)
        }
        buttonPauseSong.setOnClickListener {
            mainActivityViewModel.action(this, it)
        }
        buttonStopSong.setOnClickListener {
            mainActivityViewModel.action(this, it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(onSongSelected)
        unbindService(this)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        mainActivityViewModel.musicPlayerService =
            (service as MusicPlayerServiceBinder).getService()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
    }
}