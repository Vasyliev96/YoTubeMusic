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

class MainActivity : AppCompatActivity(), ServiceConnection {

    private val mainActivityViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }

    private val onSongSelected = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            mainActivityViewModel.musicPlayerService?.stopSong(0)
            val songId = intent.getStringExtra(MusicListActivity.SONG_ID)
            mainActivityViewModel.loadSong(UUID.fromString(songId))
            mainActivityViewModel.setDefaultSong(songId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityViewModel.getDefaultSong()

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
                    mainActivityViewModel.currentSongFilepath =
                        mainActivityViewModel.songData.rawResId
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
            mainActivityViewModel.action(BUTTON_PLAY_SONG)
        }
        buttonPauseSong.setOnClickListener {
            mainActivityViewModel.action(BUTTON_PAUSE_SONG)
        }
        buttonStopSong.setOnClickListener {
            mainActivityViewModel.action(BUTTON_STOP_SONG)
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
        mainActivityViewModel.musicPlayerService = null
    }

    companion object {
        const val PREF_DEFAULT_SONG = "preference default song"
        const val PREF_SONG_ID = "preference song id"
        const val BUTTON_PLAY_SONG = "play"
        const val BUTTON_PAUSE_SONG = "pause"
        const val BUTTON_STOP_SONG = "stop"
    }
}