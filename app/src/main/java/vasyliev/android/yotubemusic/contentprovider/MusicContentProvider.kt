package vasyliev.android.yotubemusic.contentprovider

import android.content.*
import android.database.Cursor
import android.net.Uri
import androidx.room.Room
import vasyliev.android.yotubemusic.musicdatabase.MusicDao
import vasyliev.android.yotubemusic.musicdatabase.MusicDB
import vasyliev.android.yotubemusic.musicdatabase.SongData

class MusicContentProvider : ContentProvider() {

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
                id = musicDao.addSong(
                    SongData.fromContentValues(
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
                cursor = musicDao.getMusic()
                if (context != null) {
                    cursor.setNotificationUri(context!!.contentResolver, uri)
                    return cursor
                }
            }
            ID_MUSIC_DATA_WITH_SELECTION -> {
                if (selection == SongData.AUTHOR_NAME + SongData.GENRE) {
                    if (selectionArgs != null) {
                        cursor =
                            musicDao.getMusicByGenreAndAuthor(selectionArgs[1], selectionArgs[0])
                        return cursor
                    }
                } else if (selection == SongData.AUTHOR_NAME) {
                    if (selectionArgs != null) {
                        cursor = musicDao.getMusicByAuthor(selectionArgs[0])
                        return cursor
                    }
                } else if (selection == SongData.GENRE) {
                    if (selectionArgs != null) {
                        cursor = musicDao.getMusicByGenre(selectionArgs[0])
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
        private const val AUTHORITY =
            "vasyliev.android.yotubemusic.contentprovider.MusicContentProvider"
        private const val DATABASE_NAME = "song-data"
        private const val MUSIC_TABLE_NAME = "songdata"
        private const val WITH_SELECTION = "withSelection"
        private const val ID_MUSIC_DATA = 1
        private const val ID_MUSIC_DATA_WITH_SELECTION = 2

        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$MUSIC_TABLE_NAME")
        val CONTENT_SELECTION_URI: Uri =
            Uri.parse("content://$AUTHORITY/$MUSIC_TABLE_NAME/$WITH_SELECTION")
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, MUSIC_TABLE_NAME, ID_MUSIC_DATA)
            addURI(AUTHORITY, "$MUSIC_TABLE_NAME/$WITH_SELECTION", ID_MUSIC_DATA_WITH_SELECTION)
        }

        private lateinit var musicDB: MusicDB
        private lateinit var musicDao: MusicDao

        fun initialize(context: Context) {
            musicDB = Room.databaseBuilder(
                context.applicationContext,
                MusicDB::
                class.java,
                DATABASE_NAME
            ).build()
            musicDao = musicDB.musicDao()
        }
    }
}