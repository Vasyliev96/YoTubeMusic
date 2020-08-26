package vasyliev.android.yotubemusic.db

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface YoTubeDAO {
    //getSongs(): Cursor
    @Query("SELECT * FROM yotubesongdata")
    fun getSongs(): Cursor//LiveData<List<YoTubeSongData>>//Cursor

    //getSong(id:UUID):Cursor
    @Query("SELECT * FROM yotubesongdata WHERE id=(:id)")
    fun getSong(id: UUID): LiveData<YoTubeSongData?>

    //Cursor
    @Query("SELECT * FROM yotubesongdata WHERE artistsName=(:artistName)")
    fun getSongsByArtist(artistName: String): Cursor//LiveData<List<YoTubeSongData>>//Cursor

    //Cursor
    @Query("SELECT * FROM yotubesongdata WHERE genre=(:genre)")
    fun getSongsByGenre(genre: String): Cursor//LiveData<List<YoTubeSongData>>//Cursor

    //Cursor
    @Query("SELECT * FROM yotubesongdata WHERE genre=(:genre) AND artistsName=(:artistName)")
    fun getSongsByGenreAndArtist(
        genre: String,
        artistName: String
    ): Cursor//LiveData<List<YoTubeSongData>>//Cursor

    @Insert
    fun addSong(songData: YoTubeSongData?): Long //Int - return row ID for newly inserted data

    @Update
    fun updateSong(songData: YoTubeSongData?): Int //Int - return a number of songs updated

    // @Delete
    //fun deleteSong(songId: UUID): Int //Int - return a number of songs deleted
}