package vasyliev.android.yotubemusic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

//private const val REQUEST_CODE_CHOOSE_SONG = 0

class MainActivity : AppCompatActivity() {
    // private lateinit var yoStorage: YoStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //yoStorage = YoStorage(assets)
        //yoStorage.loadSounds()
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        buttonChooseArtist.setOnClickListener {
            /*
            startActivityForResult(
                Intent(this, SecondActivity::class.java),
                REQUEST_CODE_CHOOSE_SONG
            )

             */
            startActivity(Intent(this, SecondActivity::class.java))
        }
    }
/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

 */
}