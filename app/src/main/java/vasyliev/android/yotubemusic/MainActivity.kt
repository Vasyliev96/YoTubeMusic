package vasyliev.android.yotubemusic

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import vasyliev.android.yotubemusic.db.YoTubeSongData
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var songData: YoTubeSongData
    private val mainActivityViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }

    private val onShowNotification = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val songId: UUID = UUID.fromString(intent.getStringExtra(SecondActivity.SONG_ID))
            mainActivityViewModel.loadSong(songId)
            Toast.makeText(
                this@MainActivity,
                "Got a broadcast: $songId",
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        songData = YoTubeSongData()
        mainActivityViewModel.songLiveData.observe(
            this,
            { song ->
                song?.let {
                    songData = song
                    updateUI()
                }
            }
        )
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
    }

    override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(onShowNotification)
    }
}