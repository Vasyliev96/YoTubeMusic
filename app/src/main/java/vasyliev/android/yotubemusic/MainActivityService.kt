package vasyliev.android.yotubemusic

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

private const val EXTRA_SONG_TO_PLAY = "vasyliev.android.yotubemusic.song_to_play"

class MainActivityService : Service() {
    private var songFile: Int? = null
    var myPlayer: MediaPlayer? = null
    override fun onBind(intent: Intent?): IBinder? {
        myPlayer!!.pause()
        return Binder()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        myPlayer!!.start()
        return true
    }

    override fun onRebind(intent: Intent?) {
        myPlayer!!.pause()
        super.onRebind(intent)
    }

    override fun onCreate() {
    }

    override fun onStart(intent: Intent?, startid: Int) {
        if (intent != null) {
            songFile = intent.getIntExtra(EXTRA_SONG_TO_PLAY, 0)
        }
        myPlayer = MediaPlayer.create(this, songFile!!)
        myPlayer!!.isLooping = false
        myPlayer!!.start()
    }

    override fun onDestroy() {
        myPlayer!!.release()
        myPlayer = null
    }

    companion object {
        fun newMainActivityServiceIntent(context: Context, songFileId: Int): Intent {
            return Intent(context, MainActivityService::class.java).putExtra(
                EXTRA_SONG_TO_PLAY, songFileId
            )
        }
    }
}