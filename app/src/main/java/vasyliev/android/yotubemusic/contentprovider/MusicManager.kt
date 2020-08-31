package vasyliev.android.yotubemusic.contentprovider

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import vasyliev.android.yotubemusic.musicdatabase.SongData
import java.util.*

object MusicManager {
    private lateinit var context: Context
    private var myContentResolver: ContentResolver? = null

    fun initialize(context: Context) {
        this.context = context
        myContentResolver = context.contentResolver
    }

    fun addMusic(music: SongData) {
        val values = ContentValues()
        values.apply {
            put(MusicContentProvider.ID, music.id.toString())
            put(MusicContentProvider.SONG_NAME, music.songName)
            put(MusicContentProvider.AUTHOR_NAME, music.author)
            put(MusicContentProvider.GENRE, music.genre)
            put(MusicContentProvider.RAW_RES_ID, music.rawResId)
        }
        myContentResolver?.insert(MusicContentProvider.CONTENT_URI, values)
    }

    fun getMusic(artistName: String?, genre: String?): List<SongData> {
        val music = mutableListOf<SongData>()
        val projection = arrayOf(
            MusicContentProvider.ID,
            MusicContentProvider.SONG_NAME,
            MusicContentProvider.AUTHOR_NAME,
            MusicContentProvider.GENRE,
            MusicContentProvider.RAW_RES_ID
        )
        var uri: Uri = MusicContentProvider.CONTENT_URI
        var selection: String? = null
        var selectionArgs: Array<String>? = emptyArray()
        if (artistName == null || genre == null) {
            if (artistName != null) {
                selection = MusicContentProvider.AUTHOR_NAME
                selectionArgs = arrayOf(artistName)
                uri = MusicContentProvider.CONTENT_SELECTION_URI
            } else if (genre != null) {
                selection = MusicContentProvider.GENRE
                selectionArgs = arrayOf(genre)
                uri = MusicContentProvider.CONTENT_SELECTION_URI
            }
        } else {
            selection = MusicContentProvider.AUTHOR_NAME + MusicContentProvider.GENRE
            selectionArgs = arrayOf(artistName, genre)
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
}