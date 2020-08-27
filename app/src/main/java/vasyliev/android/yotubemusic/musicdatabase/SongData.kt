package vasyliev.android.yotubemusic.musicdatabase

import android.content.ContentValues
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class SongData(
    @PrimaryKey var id: UUID = UUID.randomUUID(),
    var songName: String = "",
    var author: String = "",
    var genre: String = "",
    var rawResId: Int? = null
) {

    companion object {
        const val ID = "id"
        const val SONG_NAME = "songName"
        const val AUTHOR_NAME = "authorName"
        const val GENRE = "genre"
        const val RAW_RES_ID = "rawResourceId"
        fun fromContentValues(contentValues: ContentValues?): SongData? {
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
    }
}