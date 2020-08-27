package vasyliev.android.yotubemusic.activitymusiclist

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vasyliev.android.yotubemusic.contentprovider.MusicContentResolver
import vasyliev.android.yotubemusic.musicdatabase.*

class MusicListViewModel : ViewModel() {

    var currentAuthor: String? = null
    var currentGenre: String? = null
    private var currentAuthorAndGenreLiveData = MutableLiveData(listOf(currentAuthor, currentGenre))
    var musicListLiveData: LiveData<List<SongData>>? =
        Transformations.switchMap(currentAuthorAndGenreLiveData) { param ->
            liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
                emit(
                    MusicContentResolver().getMusic(param[0], param[1])
                )
            }
        }

    fun uploadMusicToDatabase() {
        val musicFiles = MusicFilesData()
        var int = 0
        val myContentResolver = MusicContentResolver()

        musicFiles.rawSongId.forEach { item ->
            val yoTubeSong = SongData()
            yoTubeSong.rawResId = item
            yoTubeSong.songName = musicFiles.songName[int]
            yoTubeSong.author = "Author ${int % 4 + 1}"
            yoTubeSong.genre = "Genre ${int % 3 + 1}"

            viewModelScope.launch {
                withContext(Dispatchers.Default) {
                    myContentResolver.addMusic(
                        yoTubeSong
                    )
                }
            }

            int += 1
        }
    }

    fun getMusic() {
        currentAuthorAndGenreLiveData.value = listOf(currentAuthor, currentGenre)
    }

}