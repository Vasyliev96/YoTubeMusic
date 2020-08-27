package vasyliev.android.yotubemusic.db

import android.content.ContentValues
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class YoTubeSongData(
    @PrimaryKey var id: UUID = UUID.randomUUID(),
    var songName: String = "",
    var artistsName: String = "",
    var genre: String = "",
    var filePath: Int? = null
) {

    companion object {
        const val ID = "0"
        const val SONG_NAME = "songName"
        const val ARTIST_NAME = "artistsName"
        const val GENRE = "genre"
        const val FILE_PATH = "filePath"
        fun fromContentValues(contentValues: ContentValues?): YoTubeSongData? {
            val music = YoTubeSongData()
            if (contentValues != null) {
                if (contentValues.containsKey(ID)){
                    music.id= UUID.fromString(contentValues.getAsString(ID))
                }
                if (contentValues.containsKey(SONG_NAME)) {
                    music.songName = contentValues.getAsString(SONG_NAME)
                }
                if (contentValues.containsKey(ARTIST_NAME)) {
                    music.artistsName = contentValues.getAsString(ARTIST_NAME)
                }
                if (contentValues.containsKey(GENRE)) {
                    music.genre = contentValues.getAsString(GENRE)
                }
                if (contentValues.containsKey(FILE_PATH)) {
                    music.filePath = contentValues.getAsInteger(FILE_PATH)
                }
            }
            return music
        }
    }
}