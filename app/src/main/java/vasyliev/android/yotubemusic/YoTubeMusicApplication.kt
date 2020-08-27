package vasyliev.android.yotubemusic

import android.app.Application
import vasyliev.android.yotubemusic.contentprovider.MusicContentProvider
import vasyliev.android.yotubemusic.musicdatabase.MusicRepository

class YoTubeMusicApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MusicRepository.initialize(this)
        MusicContentProvider.initialize(this)
    }
}