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

class MusicListAdapter(private var musicData: List<SongData>, context: Context) :
    RecyclerView.Adapter<MusicListAdapter.ViewHolder>() {

    private var context: Context? = null
    private var inflater: LayoutInflater
    private var callbackItemClickListener: CallbackItemClickListener? = null

    init {
        this.context = context
        inflater = LayoutInflater.from(this.context)
        callbackItemClickListener = this.context as CallbackItemClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = inflater.inflate(R.layout.music_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() =
        musicData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(musicData[position])
    }

    fun getItem(position: Int): SongData {
        return musicData[position]
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var songData: SongData
        private val songNameTextView: TextView = itemView.listItemTextViewSongName
        private val authorsNameTextView: TextView = itemView.listItemTextViewAuthor
        private val genreTextView: TextView = itemView.listItemTextViewGenre

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(songData: SongData) {
            this.songData = songData
            songNameTextView.text = this.songData.songName
            authorsNameTextView.text = this.songData.author
            genreTextView.text = this.songData.genre
        }

        override fun onClick(v: View?) {
            if (callbackItemClickListener == null) return
            callbackItemClickListener?.onItemClick(v!!, adapterPosition)
        }
    }

    interface CallbackItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}