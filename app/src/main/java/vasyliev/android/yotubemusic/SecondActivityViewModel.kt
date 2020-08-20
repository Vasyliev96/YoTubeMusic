package vasyliev.android.yotubemusic

import android.content.res.AssetManager
import androidx.lifecycle.ViewModel
import vasyliev.android.yotubemusic.db.YoRepository
import vasyliev.android.yotubemusic.db.YoTubeSongData

class SecondActivityViewModel : ViewModel() {
    var currentArtist: String? = null
    var currentGenre: String? = null
    var songListLiveData = YoRepository.getSongs()
    fun requestMusic() {
        when {
            currentArtist != null && currentGenre != null -> {
                songListLiveData = YoRepository.getSongsByGenreAndArtist(
                    currentGenre!!,
                    currentArtist!!
                )
            }
            currentArtist != null -> {
                songListLiveData = YoRepository.getSongsByArtist(currentArtist!!)
            }
            currentGenre != null -> {
                songListLiveData = YoRepository.getSongsByGenre(currentGenre!!)
            }
            else -> {
                songListLiveData = YoRepository.getSongs()
            }
        }
    }
    //val artists = YoRepository.getArtists()
    //val genres = YoRepository.getGenres()
    /*
    private lateinit var yoStorage: YoStorage
val yoTubeMusicData = mutableListOf<YoTubeSongData>()

    fun loadMusic(assetManager: AssetManager) {
        var int = 0
        yoStorage = YoStorage(assetManager)
        yoStorage.loadSounds().forEach { item ->
            int += 1
            val yoTubeSong = YoTubeSongData()
            val songName = item.split("/").last().removeSuffix(".wav")
            yoTubeSong.songName = "$songName"
            yoTubeSong.artistsName = "Author ${int % 6 + 1}"
            yoTubeSong.genre = "Genre ${int % 8 + 1}"
            yoTubeSong.filePath = item
            //yoTubeMusicData += yoTubeSong
            YoRepository.addSong(yoTubeSong)
        }
    }

*/
}