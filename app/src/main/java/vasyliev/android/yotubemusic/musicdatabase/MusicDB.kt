package vasyliev.android.yotubemusic.musicdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SongData::class], version = 1, exportSchema = false)
@TypeConverters(MusicTypeConverter::class)
abstract class MusicDB : RoomDatabase() {
    abstract fun musicDao(): MusicDao
}