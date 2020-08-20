package vasyliev.android.yotubemusic.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class YoTubeSongData(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var songName: String = "",
    var artistsName: String = "",
    var genre: String = "",
    var filePath: String = ""
)