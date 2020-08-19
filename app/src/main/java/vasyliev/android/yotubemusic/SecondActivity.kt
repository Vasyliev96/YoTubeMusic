package vasyliev.android.yotubemusic

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vasyliev.android.yotubemusic.db.YoTubeSongData

class SecondActivity : AppCompatActivity(), SecondActivityAdapter.CallbackItemClickListener {

    private lateinit var simpleUsersAdapter: SecondActivityAdapter
    private lateinit var userRecyclerView: RecyclerView
    private val secondActivityViewModel: SecondActivityViewModel by lazy {
        ViewModelProvider(this).get(SecondActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        secondActivityViewModel.loadMusic(assets)

        userRecyclerView = findViewById(R.id.simpleUserRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        simpleUsersAdapter = SecondActivityAdapter(secondActivityViewModel.yoTubeMusicData, this)
        userRecyclerView.adapter = simpleUsersAdapter
    }

    override fun onStart() {
        super.onStart()

        setSpinner()
    }

    override fun onResume() {
        super.onResume()
        updateUI(secondActivityViewModel.yoTubeMusicData)
    }

    private fun updateUI(users: List<YoTubeSongData>) {
        simpleUsersAdapter = SecondActivityAdapter(users, this)
        userRecyclerView.adapter = simpleUsersAdapter
    }

    private fun setSpinner() {
        val spinnerArtists: Spinner = findViewById(R.id.spinnerArtists)
        val spinnerGenres: Spinner = findViewById(R.id.spinnerGenres)
        ArrayAdapter.createFromResource(
            this,
            R.array.artists_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerArtists.adapter = adapter
        }
        ArrayAdapter.createFromResource(
            this,
            R.array.genres_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGenres.adapter = adapter
        }
    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(
            this,
            "You clicked " + simpleUsersAdapter.getItem(position).artistsName + " on row number " + position,
            Toast.LENGTH_SHORT
        ).show()
        //RETURN SONG ID TO MAIN ACTIVITY
    }
}