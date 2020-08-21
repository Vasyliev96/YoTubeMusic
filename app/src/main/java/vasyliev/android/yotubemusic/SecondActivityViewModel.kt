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

    private lateinit var yoStorage: YoStorage
    val yoTubeMusicData = mutableListOf<YoTubeSongData>()

    fun loadMusic() {
        val songId = arrayOf(
            R.raw.blackpink_ddu_du_ddu_du,
            R.raw.cjipie,
            R.raw.eh,
            R.raw.houb,
            R.raw.houu,
            R.raw.hruuhb,
            R.raw.indios,
            R.raw.indios_two,
            R.raw.indios_three,
            R.raw.jah,
            R.raw.jeeh,
            R.raw.jhuee,
            R.raw.joooaah,
            R.raw.jueb,
            R.raw.juob,
            R.raw.long_scream,
            R.raw.ludovico_einaudi_bella_notte,
            R.raw.ludovico_einaudi_experience,
            R.raw.oa_h,
            R.raw.oaaaahmmm,
            R.raw.ohm_loko,
            R.raw.ramin_djavadi_point_it_black,
            R.raw.the_godfather_theme_song,
            R.raw.uehea,
            R.raw.uhraa,
            R.raw.uoh,
            R.raw.uueh
        )
        val songName = arrayOf(
            "blackpink_ddu_du_ddu_du",
            "cjipie",
            "eh",
            "houb",
            "houu",
            "hruuhb",
            "indios",
            "indios_two",
            "indios_three",
            "jah",
            "jeeh",
            "jhuee",
            "joooaah",
            "jueb",
            "juob",
            "long_scream",
            "ludovico_einaudi_bella_notte",
            "ludovico_einaudi_experience",
            "oa_h",
            "oaaaahmmm",
            "ohm_loko",
            "ramin_djavadi_point_it_black",
            "the_godfather_theme_song",
            "uehea",
            "uhraa",
            "uoh",
            "uueh"
        )
        var int = 0
        //yoStorage = YoStorage(assetManager)

        songId.forEach { item ->
            val yoTubeSong = YoTubeSongData()
            yoTubeSong.filePath = item
            yoTubeSong.songName = songName[int]
            yoTubeSong.artistsName = "Author ${int % 4 + 1}"
            yoTubeSong.genre = "Genre ${int % 3 + 1}"
            //yoTubeMusicData += yoTubeSong
            YoRepository.addSong(yoTubeSong)
            int += 1
        }
    }


}