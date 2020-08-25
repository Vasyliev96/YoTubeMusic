package vasyliev.android.yotubemusic

import androidx.lifecycle.ViewModel
import vasyliev.android.yotubemusic.db.MusicListData
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

    private lateinit var yoStorage: YoStorage
    val yoTubeMusicData = mutableListOf<YoTubeSongData>()

    fun loadMusic() {
        val music = MusicListData()
        var int = 0

        music.rawSoundId.forEach { item ->
            val yoTubeSong = YoTubeSongData()
            yoTubeSong.filePath = item
            yoTubeSong.songName = music.songName[int]
            yoTubeSong.artistsName = "Author ${int % 4 + 1}"
            yoTubeSong.genre = "Genre ${int % 3 + 1}"
            YoRepository.addSong(yoTubeSong)
            int += 1
        }
    }


}