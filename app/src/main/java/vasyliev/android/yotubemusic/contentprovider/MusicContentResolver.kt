package vasyliev.android.yotubemusic.contentprovider

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import vasyliev.android.yotubemusic.musicdatabase.SongData
import java.util.*

class MusicContentResolver {

    private val myContentResolver: ContentResolver? = context?.contentResolver

    fun addMusic(music: SongData) {
        val values = ContentValues()
        values.apply {
            put(SongData.ID, music.id.toString())
            put(SongData.SONG_NAME, music.songName)
            put(SongData.AUTHOR_NAME, music.author)
            put(SongData.GENRE, music.genre)
            put(SongData.RAW_RES_ID, music.rawResId)
        }
        myContentResolver?.insert(MusicContentProvider.CONTENT_URI, values)
    }

    fun getMusic(artistName: String?, genre: String?): List<SongData> {
        val music = mutableListOf<SongData>()
        val projection = arrayOf(
            SongData.ID,
            SongData.SONG_NAME,
            SongData.AUTHOR_NAME,
            SongData.GENRE,
            SongData.RAW_RES_ID
        )
        var uri: Uri = MusicContentProvider.CONTENT_URI
        var selection: String? = null
        var selectionArgs: Array<String>? = emptyArray()
        if (artistName != null && genre != null) {
            selection = SongData.AUTHOR_NAME + SongData.GENRE
            selectionArgs = arrayOf(artistName, genre)
            uri = MusicContentProvider.CONTENT_SELECTION_URI
        } else if (artistName != null) {
            selection = SongData.AUTHOR_NAME
            selectionArgs = arrayOf(artistName)
            uri = MusicContentProvider.CONTENT_SELECTION_URI
        } else if (genre != null) {
            selection = SongData.GENRE
            selectionArgs = arrayOf(genre)
            uri = MusicContentProvider.CONTENT_SELECTION_URI
        }
        val cursor: Cursor? = myContentResolver?.query(
            uri,
            projection,
            selection,
            selectionArgs,
            null
        )
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                cursor.moveToFirst()
                do {
                    music += SongData(
                        UUID.fromString(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        Integer.parseInt(cursor.getString(4))
                    )
                } while (cursor.moveToNext())
                cursor.close()
            }
        }
        return music
    }

    companion object {
        var context: Context? = null
    }
}