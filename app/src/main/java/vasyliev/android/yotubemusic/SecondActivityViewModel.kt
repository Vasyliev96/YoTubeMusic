package vasyliev.android.yotubemusic

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import vasyliev.android.yotubemusic.db.*
import java.util.concurrent.Executors

class SecondActivityViewModel : ViewModel() {

    private var executor = Executors.newSingleThreadExecutor()
    var currentArtist: String? = null
    var currentGenre: String? = null
    private var currentLiveData = MutableLiveData(listOf(currentArtist, currentGenre))
    var songListLiveData: LiveData<List<YoTubeSongData>>? =
        Transformations.switchMap(currentLiveData) { param ->
            liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
                emit(
                    MyContentResolver().getMusic(param[0], param[1])
                )
            }
        }

    fun loadMusic() {
        val music = MusicListData()
        var int = 0
        val myContentResolver = MyContentResolver()

        music.rawSoundId.forEach { item ->
            val yoTubeSong = YoTubeSongData()
            yoTubeSong.filePath = item
            yoTubeSong.songName = music.songName[int]
            yoTubeSong.artistsName = "Author ${int % 4 + 1}"
            yoTubeSong.genre = "Genre ${int % 3 + 1}"

            executor.execute { myContentResolver.addMusic(yoTubeSong) }

            int += 1
        }
    }

    fun getMusic() {
        currentLiveData.value = listOf(currentArtist, currentGenre)
        //executor.execute { songListLiveData = MyContentResolver().getMusic(currentArtist, currentGenre) }
    }

}