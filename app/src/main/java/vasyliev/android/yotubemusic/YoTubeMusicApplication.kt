package vasyliev.android.yotubemusic

import android.app.Application
import vasyliev.android.yotubemusic.db.MyContentProvider
import vasyliev.android.yotubemusic.db.YoRepository

class YoTubeMusicApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        YoRepository.initialize(this)
        MyContentProvider.initialize(this)
    }
}