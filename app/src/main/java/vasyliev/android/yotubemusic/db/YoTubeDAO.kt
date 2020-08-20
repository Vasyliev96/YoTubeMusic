package vasyliev.android.yotubemusic.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.*

@Dao
interface YoTubeDAO {
    @Query("SELECT * FROM yotubesongdata")
    fun getSongs(): LiveData<List<YoTubeSongData>>

    @Query("SELECT * FROM yotubesongdata WHERE id=(:id)")
    fun getSong(id: UUID): LiveData<YoTubeSongData?>

    @Query("SELECT * FROM yotubesongdata WHERE artistsName=(:artistName)")
    fun getSongsByArtist(artistName: String): LiveData<List<YoTubeSongData>>

    @Query("SELECT * FROM yotubesongdata WHERE genre=(:genre)")
    fun getSongsByGenre(genre: String): LiveData<List<YoTubeSongData>>

    @Query("SELECT * FROM yotubesongdata WHERE genre=(:genre) AND artistsName=(:artistName)")
    fun getSongsByGenreAndArtist(genre: String, artistName: String): LiveData<List<YoTubeSongData>>

    @Insert
    fun addSong(songData: YoTubeSongData)
}