package vasyliev.android.yotubemusic.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [YoTubeSongData::class], version = 1, exportSchema = false)
@TypeConverters(YoTubeTC::class)
abstract class YoTubeDB : RoomDatabase() {
    abstract fun yoTubeDAO(): YoTubeDAO
}