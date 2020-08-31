package vasyliev.android.yotubemusic.activitymusiclist

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vasyliev.android.yotubemusic.contentprovider.MusicManager
import vasyliev.android.yotubemusic.musicdatabase.*

private const val PREFERENCE_FIRST_START = "is music list starting for the first time"

class MusicListViewModel : ViewModel() {

    var currentAuthor: String? = null
    var currentGenre: String? = null
    lateinit var authors: Array<String>
    lateinit var genres: Array<String>
    private var currentAuthorAndGenreLiveData = MutableLiveData(listOf(currentAuthor, currentGenre))
    var musicListLiveData: LiveData<List<SongData>>? =
        Transformations.switchMap(currentAuthorAndGenreLiveData) { param ->
            liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
                emit(
                    MusicManager.getMusic(param[0], param[1])
                )
            }
        }

    fun uploadMusicToDatabase(context: Context) {
        if (context.getSharedPreferences(
                PREFERENCE_FIRST_START,
                MODE_PRIVATE
            ).getBoolean(PREFERENCE_FIRST_START, true)
        ) {
            context.getSharedPreferences(PREFERENCE_FIRST_START, MODE_PRIVATE).edit().apply {
                putBoolean(PREFERENCE_FIRST_START, false)
                apply()
            }
            val musicFiles = MusicFilesData()
            var counter = 0

            musicFiles.rawSongId.forEach { item ->
                val yoTubeSong = SongData()
                yoTubeSong.rawResId = item
                yoTubeSong.songName = musicFiles.songName[counter]
                /*
                * hardcoded authors (1-4) and genres (1-3) for each song
                 */
                yoTubeSong.author = "Author ${counter % 4 + 1}"
                yoTubeSong.genre = "Genre ${counter % 3 + 1}"

                viewModelScope.launch {
                    withContext(Dispatchers.Default) {
                        MusicManager.addMusic(
                            yoTubeSong
                        )
                    }
                }

                counter += 1
            }
        }
    }

    fun getMusic() {
        currentAuthorAndGenreLiveData.value = listOf(currentAuthor, currentGenre)
    }

}