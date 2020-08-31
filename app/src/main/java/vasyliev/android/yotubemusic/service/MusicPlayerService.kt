package vasyliev.android.yotubemusic.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity

private const val PREF_DEFAULT_SONG_TIME = "preference default song time"

class MusicPlayerService : Service() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder? {
        return MusicPlayerServiceBinder()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return true
    }

    override fun onDestroy() {
        if (mediaPlayer != null)
            stopSong(mediaPlayer!!.currentPosition)
    }

    fun playSong(songFile: Int) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, songFile)
            mediaPlayer!!.seekTo(
                getSharedPreferences(
                    PREF_DEFAULT_SONG_TIME,
                    AppCompatActivity.MODE_PRIVATE
                ).getInt(PREF_DEFAULT_SONG_TIME, 0)
            )
            mediaPlayer!!.isLooping = false
            mediaPlayer!!.start()
            mediaPlayer!!.setOnCompletionListener {
                stopSelf()
            }
        } else if (!mediaPlayer!!.isPlaying) {
            mediaPlayer!!.start()
        }
    }

    fun pauseSong() {
        if (mediaPlayer != null) {
            mediaPlayer!!.pause()
        }
    }

    fun stopSong(mediaPlayerPosition: Int) {
        if (mediaPlayer != null) {
            getSharedPreferences(PREF_DEFAULT_SONG_TIME, MODE_PRIVATE).edit().apply {
                putInt(PREF_DEFAULT_SONG_TIME, mediaPlayerPosition)
                apply()
            }
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    inner class MusicPlayerServiceBinder : Binder() {
        fun getService(): MusicPlayerService {
            return this@MusicPlayerService
        }
    }
}