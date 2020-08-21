package vasyliev.android.yotubemusic.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "yo-tube-music-data"

object YoRepository {

    private lateinit var yoTubeDB: YoTubeDB
    private lateinit var yoTubeDAO: YoTubeDAO
    private val executor = Executors.newSingleThreadExecutor()

    fun initialize(context: Context) {
        yoTubeDB = Room.databaseBuilder(
            context.applicationContext,
            YoTubeDB::class.java,
            DATABASE_NAME
        ).build()
        yoTubeDAO = yoTubeDB.yoTubeDAO()
    }

    fun getSongs(): LiveData<List<YoTubeSongData>> = yoTubeDAO.getSongs()

    fun getSong(id: UUID): LiveData<YoTubeSongData?> = yoTubeDAO.getSong(id)
    fun getSongsByArtist(artistName: String): LiveData<List<YoTubeSongData>> =
        yoTubeDAO.getSongsByArtist(artistName)

    fun getSongsByGenre(genre: String): LiveData<List<YoTubeSongData>> =
        yoTubeDAO.getSongsByGenre(genre)

    fun getSongsByGenreAndArtist(
        genre: String,
        artistName: String
    ): LiveData<List<YoTubeSongData>> = yoTubeDAO.getSongsByGenreAndArtist(genre, artistName)

    fun addSong(user: YoTubeSongData) {
        executor.execute {
            yoTubeDAO.addSong(user)
        }
    }

}