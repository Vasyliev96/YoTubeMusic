package vasyliev.android.yotubemusic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import vasyliev.android.yotubemusic.db.YoRepository
import vasyliev.android.yotubemusic.db.YoTubeSongData
import java.util.*

class MainActivityViewModel : ViewModel() {
    private val songIdLiveData = MutableLiveData<UUID>()
    var currentSongFilepath: Int? = null
    var songLiveData: LiveData<YoTubeSongData?> =
        Transformations.switchMap(songIdLiveData) { songId ->
            YoRepository.getSong(songId)
        }

    fun loadSong(songId: UUID) {
        songIdLiveData.value = songId
    }
}