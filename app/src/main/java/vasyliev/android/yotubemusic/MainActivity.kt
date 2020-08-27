package vasyliev.android.yotubemusic

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import vasyliev.android.yotubemusic.musicdatabase.SongData
import vasyliev.android.yotubemusic.activitymusiclist.MusicListActivity
import vasyliev.android.yotubemusic.service.MainActivityService
import java.util.*

private const val PREF_DEFAULT_SONG = "preference default song"
private const val PREF_DEFAULT_SONG_TIME = "preference default song time"
private const val PREF_SONG_ID = "preference song id"

class MainActivity : AppCompatActivity() {

    private lateinit var songData: SongData
    private var serviceConnection: ServiceConnection? = null
    private var bound = false
    private val mainActivityViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }

    private val onShowNotification = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val songId: UUID = UUID.fromString(intent.getStringExtra(MusicListActivity.SONG_ID))
            mainActivityViewModel.loadSong(songId)
            stopService(Intent(this@MainActivity, MainActivityService::class.java))
            nullifyPrefTime()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val defaultSongID = getSharedPreferences(PREF_DEFAULT_SONG, MODE_PRIVATE).getString(PREF_SONG_ID, null)
        if (defaultSongID != null)
            mainActivityViewModel.loadSong(UUID.fromString(defaultSongID))
        setContentView(R.layout.activity_main)
        songData = SongData()
        mainActivityViewModel.songLiveData.observe(
            this,
            { song ->
                song?.let {
                    songData = song
                    mainActivityViewModel.currentSongFilepath = songData.rawResId
                    updateUI()
                }
            }
        )
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, binder: IBinder) {
                bound = true
            }

            override fun onServiceDisconnected(name: ComponentName) {
                bound = false
            }
        }
    }

    private fun updateUI() {
        textViewSongName.text = songData.songName
        textViewAuthor.text = songData.author
        textViewGenre.text = songData.genre
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(MusicListActivity.ACTION_SONG_SELECTED)
        this.registerReceiver(onShowNotification, filter, MusicListActivity.PERM_PRIVATE, null)
        buttonSelectSong.setOnClickListener {
            startActivity(Intent(this, MusicListActivity::class.java))
        }
        buttonPlaySong.setOnClickListener {
            when (mainActivityViewModel.currentSongFilepath) {
                null -> {
                    Toast.makeText(
                        this,
                        getString(R.string.button_play_song_toast_text),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    when (bound) {
                        false -> {
                            startService(
                                MainActivityService.newMainActivityServiceIntent(
                                    this,
                                    mainActivityViewModel.currentSongFilepath!!
                                )
                            )
                        }
                        true -> {
                            bound = false
                            unbindService(serviceConnection!!)
                        }
                    }
                }
            }
        }
        buttonPauseSong.setOnClickListener {
            when (bound) {
                true -> {
                    Toast.makeText(
                        this,
                        getString(R.string.button_pause_song_toast_text),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                false -> {
                    bindService(
                        Intent(this, MainActivityService::class.java),
                        serviceConnection!!,
                        0
                    )
                }
            }
        }
        buttonStopSong.setOnClickListener {
            stopService(Intent(this, MainActivityService::class.java))
            nullifyPrefTime()
        }
    }

    fun nullifyPrefTime() {
        getSharedPreferences(PREF_DEFAULT_SONG_TIME, MODE_PRIVATE).edit().apply {
            putInt(PREF_DEFAULT_SONG_TIME, 0)
            apply()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bindService(
            Intent(this, MainActivityService::class.java),
            serviceConnection!!,
            0
        )
        stopService(Intent(this, MainActivityService::class.java))
        getSharedPreferences(PREF_DEFAULT_SONG, MODE_PRIVATE).edit().apply {
            putString(PREF_SONG_ID, mainActivityViewModel.songLiveData.value?.id.toString())
            apply()
        }
        this.unregisterReceiver(onShowNotification)
    }
}