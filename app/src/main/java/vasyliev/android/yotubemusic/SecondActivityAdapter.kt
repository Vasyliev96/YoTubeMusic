package vasyliev.android.yotubemusic

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_search_list_item.view.*
import vasyliev.android.yotubemusic.db.YoTubeSongData

class SecondActivityAdapter(private var yoTubeMusicData: List<YoTubeSongData>, context: Context) :
    RecyclerView.Adapter<SecondActivityAdapter.ViewHolder>() {
    private var context: Context? = null
    private var inflater: LayoutInflater
    private var callbackClickListener: CallbackItemClickListener? = null

    init {
        this.context = context
        inflater = LayoutInflater.from(this.context)
        callbackClickListener = this.context as CallbackItemClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SecondActivityAdapter.ViewHolder {
        val view = inflater.inflate(R.layout.activity_search_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() =
        yoTubeMusicData.size

    override fun onBindViewHolder(holder: SecondActivityAdapter.ViewHolder, position: Int) {
        holder.bind(yoTubeMusicData[position])
    }

    fun getItem(position: Int): YoTubeSongData {
        return yoTubeMusicData[position]
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var yoTubeSong: YoTubeSongData
        private val songNameTextView: TextView = itemView.textViewSongName
        private val artistsNameTextView: TextView = itemView.textViewArtist
        private val genreTextView: TextView = itemView.textViewGere

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(yoTubeSong: YoTubeSongData) {
            this.yoTubeSong = yoTubeSong
            songNameTextView.text = this.yoTubeSong.songName
            artistsNameTextView.text = this.yoTubeSong.artistsName
            genreTextView.text = this.yoTubeSong.genre
        }

        override fun onClick(v: View?) {
            if (callbackClickListener == null) return
            callbackClickListener?.onItemClick(v!!, adapterPosition)
        }
    }


    interface CallbackItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}