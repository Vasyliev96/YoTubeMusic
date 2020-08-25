package vasyliev.android.yotubemusic

import android.annotation.SuppressLint
import android.content.*
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import vasyliev.android.yotubemusic.db.YoTubeSongData
import java.util.*

private const val PREF_DEFAULT_SONG = "prefDefSong"
private const val PREF_DEFAULT_SONG_TIME = "prefDefSongTime"

class MainActivity : AppCompatActivity() {
    
    private lateinit var songData: YoTubeSongData
    private var serviceConnection: ServiceConnection? = null
    private var bound = false
    private val mainActivityViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }

    private val onShowNotification = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val songId: UUID = UUID.fromString(intent.getStringExtra(SecondActivity.SONG_ID))
            mainActivityViewModel.loadSong(songId)
            stopService(Intent(this@MainActivity, MainActivityService::class.java))
            nullifyPrefTime()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val defSong = getSharedPreferences(PREF_DEFAULT_SONG, MODE_PRIVATE)
        val defSongFile = defSong.getString("songFile", null)
        if (defSongFile != null)
            mainActivityViewModel.loadSong(UUID.fromString(defSongFile))
        setContentView(R.layout.activity_main)
        songData = YoTubeSongData()
        mainActivityViewModel.songLiveData.observe(
            this,
            { song ->
                song?.let {
                    songData = song
                    mainActivityViewModel.currentSongFilepath = songData.filePath
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
        textViewArtist.text = songData.artistsName
        textViewGenre.text = songData.genre
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(SecondActivity.ACTION_SONG_SELECTED)
        this.registerReceiver(onShowNotification, filter, SecondActivity.PERM_PRIVATE, null)
        buttonChooseArtist.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
        buttonPlayMusic.setOnClickListener {
            when (mainActivityViewModel.currentSongFilepath) {
                null -> {
                    Toast.makeText(this, "No music to play", Toast.LENGTH_SHORT).show()
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
        buttonPauseMusic.setOnClickListener {
            when (bound) {
                true -> {
                    Toast.makeText(
                        this,
                        "Already paused",
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
        buttonStopMusic.setOnClickListener {
            stopService(Intent(this, MainActivityService::class.java))
            nullifyPrefTime()
        }
    }

    fun nullifyPrefTime(){
        getSharedPreferences(PREF_DEFAULT_SONG_TIME, MODE_PRIVATE).edit().apply {
            putInt("songTime", 0)
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
        val defSongPref = getSharedPreferences(PREF_DEFAULT_SONG, MODE_PRIVATE)
        defSongPref.edit().apply {
            putString("songFile", mainActivityViewModel.songLiveData.value?.id.toString())
            apply()
        }
        this.unregisterReceiver(onShowNotification)
    }
}