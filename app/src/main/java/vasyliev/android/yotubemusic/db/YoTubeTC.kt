package vasyliev.android.yotubemusic.db

import androidx.room.TypeConverter
import java.util.*

class YoTubeTC {
    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
}