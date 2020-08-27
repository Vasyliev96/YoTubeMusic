package vasyliev.android.yotubemusic.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class MainActivityService : Service() {
    private var songFile: Int? = null
    private var mediaPlayer: MediaPlayer? = null
    override fun onBind(intent: Intent?): IBinder? {
        getSharedPreferences(PREF_DEFAULT_SONG_TIME, MODE_PRIVATE).edit().apply {
            putInt("preference default song time", mediaPlayer!!.currentPosition)
            apply()
        }
        mediaPlayer!!.pause()
        return Binder()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        mediaPlayer!!.start()
        return true
    }

    override fun onRebind(intent: Intent?) {
        mediaPlayer!!.pause()
        super.onRebind(intent)
    }

    override fun onCreate() {
    }

    override fun onStart(intent: Intent?, startid: Int) {
        val defaultSongTime = getSharedPreferences(PREF_DEFAULT_SONG_TIME, MODE_PRIVATE).getInt(PREF_DEFAULT_SONG_TIME, 0)
        if (intent != null) {
            songFile = intent.getIntExtra(EXTRA_SONG_TO_PLAY, 0)
        }
        mediaPlayer = MediaPlayer.create(this, songFile!!)
        mediaPlayer!!.seekTo(defaultSongTime)
        mediaPlayer!!.isLooping = false
        mediaPlayer!!.start()
        mediaPlayer!!.setOnCompletionListener {
            getSharedPreferences(PREF_DEFAULT_SONG_TIME, MODE_PRIVATE).edit().apply {
                putInt(PREF_DEFAULT_SONG_TIME, 0)
                apply()
            }
            stopSelf()
        }
    }

    fun getMediaPlayerCurrentPosition(): Int {
        return mediaPlayer!!.currentPosition
    }

    override fun onDestroy() {
        mediaPlayer!!.release()
        mediaPlayer = null
    }

    companion object {

        private const val EXTRA_SONG_TO_PLAY = "vasyliev.android.yotubemusic.song_to_play"
        private const val PREF_DEFAULT_SONG_TIME = "preference default song time"

        fun newMainActivityServiceIntent(context: Context, songFileId: Int): Intent {
            return Intent(context, MainActivityService::class.java).putExtra(
                EXTRA_SONG_TO_PLAY, songFileId
            )
        }
    }
}