package vasyliev.android.yotubemusic.db

import java.util.*

data class YoTubeSongData(
    val id: UUID = UUID.randomUUID(),
    var songName: String = "",
    var artistsName: String = "",
    var genre: String = "",
    //var filePath: String = ""
)