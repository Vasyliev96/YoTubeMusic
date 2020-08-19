package vasyliev.android.yotubemusic

import android.content.res.AssetManager
import android.util.Log

private const val TAG = "YoStorage"
private const val SOUNDS_FOLDER = "yomusic"

class YoStorage(private val assets: AssetManager) {
    fun loadSounds(): List<String> {
        return try {
            val soundNames = assets.list(SOUNDS_FOLDER)!!
            Log.d(TAG, "Found ${soundNames.size} sounds")
            soundNames.asList()
        } catch (e: Exception) {
            Log.e(TAG, "Could not list assets", e)
            emptyList()
        }
    }
}