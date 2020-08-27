package vasyliev.android.yotubemusic.db

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

class MyContentResolver {
    private val myContentResolver: ContentResolver? = context?.contentResolver
    fun addMusic(music: YoTubeSongData) {

        val values = ContentValues()
        values.apply {
            put("id", music.id.toString())
            put("songName", music.songName)
            put("artistsName", music.artistsName)
            put("genre", music.genre)
            put("filePath", music.filePath)
        }
        myContentResolver?.insert(MyContentProvider.CONTENT_URI, values)
    }

    fun getMusic(artistName: String?, genre: String?): List<YoTubeSongData> {
        val projection = arrayOf(
            YoTubeSongData.ID,
            YoTubeSongData.SONG_NAME,
            YoTubeSongData.ARTIST_NAME,
            YoTubeSongData.GENRE,
            YoTubeSongData.FILE_PATH
        )
        var uri: Uri = MyContentProvider.CONTENT_URI
        var selection: String? = null
        var selectionArgs: Array<String>? = emptyArray()
        if (artistName != null && genre != null) {
            selection = YoTubeSongData.ARTIST_NAME + YoTubeSongData.GENRE
            selectionArgs = arrayOf(artistName, genre)
            uri = MyContentProvider.CONTENT_SELECTION_URI
        } else if (artistName != null) {
            selection = YoTubeSongData.ARTIST_NAME
            selectionArgs = arrayOf(artistName)
            uri = MyContentProvider.CONTENT_SELECTION_URI
        } else if (genre != null) {
            selection = YoTubeSongData.GENRE
            selectionArgs = arrayOf(genre)
            uri = MyContentProvider.CONTENT_SELECTION_URI
        }
        val cursor: Cursor? = myContentResolver?.query(
            uri,
            projection,
            selection,
            selectionArgs,
            null
        )
        val music = mutableListOf<YoTubeSongData>()

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                cursor.moveToFirst()
                do {
                    music += YoTubeSongData(
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