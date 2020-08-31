package vasyliev.android.yotubemusic.activitymusiclist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.music_list_item.view.*
import vasyliev.android.yotubemusic.R
import vasyliev.android.yotubemusic.musicdatabase.SongData

class MusicListAdapter(private var musicData: List<SongData>, val context: Context) :
    RecyclerView.Adapter<MusicListAdapter.SongItemViewHolder>() {

    private var inflater: LayoutInflater = LayoutInflater.from(this.context)
    private var callbackItemClickListener: CallbackItemClickListener =
        context as CallbackItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongItemViewHolder {
        val view = inflater.inflate(R.layout.music_list_item, parent, false)
        return SongItemViewHolder(view)
    }

    override fun getItemCount() = musicData.size

    override fun onBindViewHolder(holderSongItem: SongItemViewHolder, position: Int) {
        holderSongItem.bind(musicData[position])
    }

    fun getItem(position: Int): SongData {
        return musicData[position]
    }

    inner class SongItemViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(songData: SongData) {
            itemView.listItemTextViewSongName.text = songData.songName
            itemView.listItemTextViewAuthor.text = songData.author
            itemView.listItemTextViewGenre.text = songData.genre
        }

        override fun onClick(view: View?) {
            callbackItemClickListener.onItemClick(adapterPosition)
        }
    }

    interface CallbackItemClickListener {
        fun onItemClick(position: Int)
    }
}