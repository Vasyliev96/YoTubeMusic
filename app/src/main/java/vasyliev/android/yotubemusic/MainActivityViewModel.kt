package vasyliev.android.yotubemusic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import vasyliev.android.yotubemusic.musicdatabase.MusicRepository
import vasyliev.android.yotubemusic.musicdatabase.SongData
import java.util.*

class MainActivityViewModel : ViewModel() {

    private val songIdLiveData = MutableLiveData<UUID>()
    var currentSongFilepath: Int? = null
    var songLiveData: LiveData<SongData?> =
        Transformations.switchMap(songIdLiveData) { songId ->
            MusicRepository.getSong(songId)
        }

    fun loadSong(songId: UUID) {
        songIdLiveData.value = songId
    }
}