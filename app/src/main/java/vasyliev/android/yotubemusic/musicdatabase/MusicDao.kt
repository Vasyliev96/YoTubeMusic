package vasyliev.android.yotubemusic.musicdatabase

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface MusicDao {
    @Query("SELECT * FROM songdata")
    fun getMusic(): Cursor

    @Query("SELECT * FROM songdata WHERE id=(:id)")
    fun getSong(id: UUID): LiveData<SongData?>

    @Query("SELECT * FROM songdata WHERE author=(:author)")
    fun getMusicByAuthor(author: String): Cursor

    @Query("SELECT * FROM songdata WHERE genre=(:genre)")
    fun getMusicByGenre(genre: String): Cursor

    @Query("SELECT * FROM songdata WHERE genre=(:genre) AND author=(:authorName)")
    fun getMusicByGenreAndAuthor(
        genre: String,
        authorName: String
    ): Cursor

    @Insert
    fun addSong(songData: SongData?): Long
}