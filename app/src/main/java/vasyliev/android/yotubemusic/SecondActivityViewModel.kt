package vasyliev.android.yotubemusic

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vasyliev.android.yotubemusic.db.*
import java.util.concurrent.Executors

class SecondActivityViewModel : ViewModel() {

    private var executor = Executors.newSingleThreadExecutor()
    var currentArtist: String? = null
    var currentGenre: String? = null
    var songListLiveData: LiveData<List<YoTubeSongData>>? = null

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
        executor.execute {
            songListLiveData = MyContentResolver().getMusic(currentArtist, currentGenre)
        }
    }

}