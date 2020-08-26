package vasyliev.android.yotubemusic.db

import android.content.*
import android.database.Cursor
import android.net.Uri
import androidx.room.Room

class MyContentProvider : ContentProvider() {

    override fun delete(
        uri: Uri, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun getType(uri: Uri): String? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val uriType = uriMatcher.match(uri)
        val id: Long
        when (uriType) {
            ID_MUSIC_DATA -> {
                id = yoTubeDAO.addSong(
                    YoTubeSongData.fromContentValues(
                        values
                    )
                )
            }
            else -> throw java.lang.IllegalArgumentException("Unknown URI:$uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return Uri.parse("$MUSIC_TABLE_NAME/$id")
    }

    override fun onCreate(): Boolean {
        return false
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val uriType = uriMatcher.match(uri)
        val cursor: Cursor
        when (uriType) {
            ID_MUSIC_DATA -> {
                cursor = yoTubeDAO.getSongs()
                if (context != null) {
                    cursor.setNotificationUri(context!!.contentResolver, uri)
                    return cursor
                }
            }
            ID_MUSIC_DATA_ITEM -> {
                if (selection == YoTubeSongData.ARTIST_NAME + YoTubeSongData.GENRE) {
                    if (selectionArgs != null) {
                        cursor =
                            yoTubeDAO.getSongsByGenreAndArtist(selectionArgs[1], selectionArgs[0])
                        return cursor
                    }
                } else if (selection == YoTubeSongData.ARTIST_NAME) {
                    if (selectionArgs != null) {
                        cursor = yoTubeDAO.getSongsByArtist(selectionArgs[0])
                        return cursor
                    }
                } else if (selection == YoTubeSongData.GENRE) {
                    if (selectionArgs != null) {
                        cursor = yoTubeDAO.getSongsByGenre(selectionArgs[0])
                        return cursor
                    }
                }
            }
        }
        throw IllegalArgumentException("Unknown URI:$uri")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        throw UnsupportedOperationException("Not yet implemented")
    }

    companion object {
        private const val AUTHORITY = "vasyliev.android.yotubemusic.db.MyContentProvider"
        private const val DATABASE_NAME = "yo-tube-music-data"
        private const val MUSIC_TABLE_NAME = "yotubesongdata"
        private const val WITH_SELECTION = "withSelection"
        private const val ID_MUSIC_DATA = 1
        private const val ID_MUSIC_DATA_ITEM = 2

        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$MUSIC_TABLE_NAME")
        val CONTENT_SELECTION_URI: Uri =
            Uri.parse("content://$AUTHORITY/$MUSIC_TABLE_NAME/$WITH_SELECTION")

        private lateinit var yoTubeDB: YoTubeDB
        private lateinit var yoTubeDAO: YoTubeDAO
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, MUSIC_TABLE_NAME, ID_MUSIC_DATA)
            addURI(AUTHORITY, "$MUSIC_TABLE_NAME/$WITH_SELECTION", ID_MUSIC_DATA_ITEM)
        }

        fun initialize(context: Context) {
            yoTubeDB = Room.databaseBuilder(
                context.applicationContext,
                YoTubeDB::
                class.java,
                DATABASE_NAME
            ).build()
            yoTubeDAO = yoTubeDB.yoTubeDAO()
        }
    }
}