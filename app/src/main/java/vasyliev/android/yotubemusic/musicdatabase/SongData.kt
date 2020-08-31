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
)