import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pockie.R
import com.example.pockie.databinding.FragmentPostItemBinding
import com.example.pockie.domain.model.Contact
import com.example.pockie.domain.model.PostItem
import com.example.pockie.presentation.utils.OnPostItemClickListener
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.EmojiPopup
import com.vanniktech.emoji.google.GoogleEmojiProvider

class ImagePagerAdapter(private val listener: OnPostItemClickListener) : ListAdapter<PostItem, RecyclerView.ViewHolder>(PostItemDiffUtilCallback()) {

    inner class ImageViewHolder(private val binding: FragmentPostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.image
        fun bind(postItem: PostItem){
            Glide.with(image.context)
                .load(postItem.imageUrl)
                .into(image)

            binding.reaction.setOnClickListener {
                EmojiManager.install(GoogleEmojiProvider())
                val emojiPopUp = EmojiPopup.Builder.fromRootView(binding.root).build(binding.message)
                emojiPopUp.toggle()

            }

            binding.takePhoto.setOnClickListener {
                listener.onPostItemClicked()
            }

            binding.menuBtn.setOnClickListener { view ->
                val popUpMenu = PopupMenu(view.context, view)
                popUpMenu.menuInflater.inflate(R.menu.pop_up_menu, popUpMenu.menu)

                popUpMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.download -> {

                            true
                        }

                        else -> {

                            true
                        }
                    }
                }
                popUpMenu.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = FragmentPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentPostItem = getItem(position)
        (holder as ImageViewHolder).bind(currentPostItem)
    }

    class PostItemDiffUtilCallback: DiffUtil.ItemCallback<PostItem>(){
        override fun areItemsTheSame(oldItem: PostItem, newItem: PostItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PostItem, newItem: PostItem): Boolean {
            return oldItem == newItem
        }

    }
}
