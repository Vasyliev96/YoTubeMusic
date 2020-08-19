package vasyliev.android.yotubemusic

import android.content.res.AssetManager
import androidx.lifecycle.ViewModel
import vasyliev.android.yotubemusic.db.YoTubeSongData

class SecondActivityViewModel : ViewModel() {
    private lateinit var yoStorage: YoStorage
    val yoTubeMusicData = mutableListOf<YoTubeSongData>()

    fun loadMusic(assetManager: AssetManager){
        yoStorage = YoStorage(assetManager)
        yoStorage.loadSounds()
        for (i in 0 until 20) {
            val yoTubeSong = YoTubeSongData()
            yoTubeSong.songName = "Song$i"
            yoTubeSong.artistsName = "Artist$i"
            yoTubeSong.genre = "Genre$i"
            yoTubeMusicData+=yoTubeSong
        }
    }
}