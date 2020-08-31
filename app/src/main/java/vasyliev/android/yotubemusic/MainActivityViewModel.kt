package vasyliev.android.yotubemusic

import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.activity_main.*
import vasyliev.android.yotubemusic.musicdatabase.MusicRepository
import vasyliev.android.yotubemusic.musicdatabase.SongData
import vasyliev.android.yotubemusic.service.MusicPlayerService
import java.util.*

class MainActivityViewModel : ViewModel() {

    lateinit var songData: SongData
    var musicPlayerService: MusicPlayerService? = null
    private val songIdLiveData = MutableLiveData<UUID>()
    var currentSongFilepath: Int? = null
    var songLiveData: LiveData<SongData?> =
        Transformations.switchMap(songIdLiveData) { songId ->
            MusicRepository.getSong(songId)
        }

    fun loadSong(songId: UUID) {
        songIdLiveData.value = songId
    }

    fun action(activity: MainActivity, view: View) {
        when (currentSongFilepath) {
            null -> {
                Toast.makeText(
                    activity,
                    activity.getString(R.string.no_song_toast_text),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                when (view) {
                    activity.buttonPlaySong -> {
                        musicPlayerService?.playSong(currentSongFilepath!!)
                    }
                    activity.buttonPauseSong -> {
                        musicPlayerService?.pauseSong()
                    }
                    activity.buttonStopSong -> {
                        musicPlayerService?.stopSong(0)
                    }
                }
            }
        }
    }
}