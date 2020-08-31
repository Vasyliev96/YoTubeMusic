package vasyliev.android.yotubemusic.contentprovider

import android.content.*
import android.database.Cursor
import android.net.Uri
import androidx.room.Room
import vasyliev.android.yotubemusic.musicdatabase.MusicDao
import vasyliev.android.yotubemusic.musicdatabase.MusicDB
import vasyliev.android.yotubemusic.musicdatabase.MusicRepository
import vasyliev.android.yotubemusic.musicdatabase.SongData
import java.util.*

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
                id = MusicRepository.musicDao.addSong(
                    fromContentValues(
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
                cursor = MusicRepository.musicDao.getMusic()
                if (context != null) {
                    cursor.setNotificationUri(context!!.contentResolver, uri)
                    return cursor
                }
            }
            ID_MUSIC_DATA_WITH_SELECTION -> {
                if (selection == AUTHOR_NAME + GENRE) {
                    if (selectionArgs != null) {
                        cursor =
                            MusicRepository.musicDao.getMusicByGenreAndAuthor(
                                selectionArgs[1],
                                selectionArgs[0]
                            )
                        return cursor
                    }
                } else if (selection == AUTHOR_NAME) {
                    if (selectionArgs != null) {
                        cursor = MusicRepository.musicDao.getMusicByAuthor(selectionArgs[0])
                        return cursor
                    }
                } else if (selection == GENRE) {
                    if (selectionArgs != null) {
                        cursor = MusicRepository.musicDao.getMusicByGenre(selectionArgs[0])
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

    private fun fromContentValues(contentValues: ContentValues?): SongData? {
        val music = SongData()
        if (contentValues != null) {
            if (contentValues.containsKey(ID)) {
                music.id = UUID.fromString(contentValues.getAsString(ID))
            }
            if (contentValues.containsKey(SONG_NAME)) {
                music.songName = contentValues.getAsString(SONG_NAME)
            }
            if (contentValues.containsKey(AUTHOR_NAME)) {
                music.author = contentValues.getAsString(AUTHOR_NAME)
            }
            if (contentValues.containsKey(GENRE)) {
                music.genre = contentValues.getAsString(GENRE)
            }
            if (contentValues.containsKey(RAW_RES_ID)) {
                music.rawResId = contentValues.getAsInteger(RAW_RES_ID)
            }
        }
        return music
    }

    companion object {
        private const val AUTHORITY =
            "vasyliev.android.yotubemusic.contentprovider.MusicContentProvider"
        private const val MUSIC_TABLE_NAME = "songdata"
        private const val WITH_SELECTION = "withSelection"
        private const val ID_MUSIC_DATA = 1
        private const val ID_MUSIC_DATA_WITH_SELECTION = 2
        const val ID = "id"
        const val SONG_NAME = "songName"
        const val AUTHOR_NAME = "authorName"
        const val GENRE = "genre"
        const val RAW_RES_ID = "rawResourceId"

        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$MUSIC_TABLE_NAME")
        val CONTENT_SELECTION_URI: Uri =
            Uri.parse("content://$AUTHORITY/$MUSIC_TABLE_NAME/$WITH_SELECTION")
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, MUSIC_TABLE_NAME, ID_MUSIC_DATA)
            addURI(AUTHORITY, "$MUSIC_TABLE_NAME/$WITH_SELECTION", ID_MUSIC_DATA_WITH_SELECTION)
        }
    }
}