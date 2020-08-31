package vasyliev.android.yotubemusic.musicdatabase

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.util.*

private const val DATABASE_NAME = "song-data"

object MusicRepository {

    lateinit var musicDB: MusicDB
    lateinit var musicDao: MusicDao

    fun initialize(context: Context) {
        musicDB = Room.databaseBuilder(
            context.applicationContext,
            MusicDB::class.java,
            DATABASE_NAME
        ).build()
        musicDao = musicDB.musicDao()
    }

    fun getSong(id: UUID): LiveData<SongData?> = musicDao.getSong(id)
}