package vasyliev.android.yotubemusic

import android.app.Application
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import vasyliev.android.yotubemusic.musicdatabase.MusicRepository
import vasyliev.android.yotubemusic.musicdatabase.SongData
import vasyliev.android.yotubemusic.service.MusicPlayerService
import java.util.*

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

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

    fun getDefaultSong() {
        val defaultSongID = getApplication<Application>().getSharedPreferences(
            MainActivity.PREF_DEFAULT_SONG,
            AppCompatActivity.MODE_PRIVATE
        ).getString(MainActivity.PREF_SONG_ID, null)
        if (defaultSongID != null) {
            songIdLiveData.value = UUID.fromString(defaultSongID)
        }
    }

    fun setDefaultSong(defaultSongId: String?) {
        if (defaultSongId != null) {
            getApplication<Application>().getSharedPreferences(
                MainActivity.PREF_DEFAULT_SONG,
                AppCompatActivity.MODE_PRIVATE
            ).edit().apply {
                putString(MainActivity.PREF_SONG_ID, defaultSongId)
                apply()
            }
        }
    }

    fun action(button: String) {
        when (currentSongFilepath) {
            null -> {
                Toast.makeText(
                    getApplication(),
                    "no song",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                when (button) {
                    MainActivity.BUTTON_PLAY_SONG -> {
                        musicPlayerService?.playSong(currentSongFilepath!!)
                    }
                    MainActivity.BUTTON_PAUSE_SONG -> {
                        musicPlayerService?.pauseSong()
                    }
                    MainActivity.BUTTON_STOP_SONG -> {
                        musicPlayerService?.stopSong(0)
                    }
                }
            }
        }
    }
}