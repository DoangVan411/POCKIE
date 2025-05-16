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
import com.example.pockie.domain.model.Post
import com.example.pockie.domain.model.PostItem
import com.example.pockie.presentation.utils.OnPostItemClickListener
import com.example.pockie.presentation.utils.Utils
import com.google.firebase.auth.FirebaseAuth

class ImagePagerAdapter(
    private val listener: OnPostItemClickListener,
    private val onLikeClicked: (Post) -> Unit,
    private val onDownload: (Post) -> Unit,
    private val onReplyPost: (String, Post) -> Unit,
) : ListAdapter<PostItem, RecyclerView.ViewHolder>(PostItemDiffUtilCallback()) {

    inner class ImageViewHolder(private val binding: FragmentPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val image = binding.image
        fun bind(post: PostItem) {
            Glide.with(image.context)
                .load(post.post.imageUrl)
                .into(image)

            binding.takePhoto.setOnClickListener {
                listener.onPostItemClicked()
            }

            if (!post.post.content.isNullOrEmpty()) {
                binding.caption.text = post.post.content
                binding.caption.visibility = View.VISIBLE
            }

            binding.infor.text = "${post.fullname} - ${Utils.formatDate(post.post.createAt)}"

            val currentId = FirebaseAuth.getInstance().currentUser?.uid.toString()
            val userLiked = post.post.likedBy
            if (userLiked.contains(currentId)) {
                binding.reaction.setBackgroundResource(R.drawable.ic_favourite)
            } else {
                binding.reaction.setBackgroundResource(R.drawable.ic_unfavourite)
            }
            binding.reaction.setOnClickListener {
                if (!userLiked.contains(currentId)) {
                    binding.reaction.setBackgroundResource(R.drawable.ic_favourite)
                    post.post.likedBy.add(currentId)
                    binding.likeCount.text = post.post.likedBy.size.toString()
                    onLikeClicked(post.post)
                } else {
                    binding.reaction.setBackgroundResource(R.drawable.ic_unfavourite)
                    post.post.likedBy.remove(currentId)
                    binding.likeCount.text = post.post.likedBy.size.toString()
                    onLikeClicked(post.post)
                }
            }

            binding.likeCount.text = post.post.likedBy.size.toString()

            binding.menuBtn.setOnClickListener { view ->
                val popUpMenu = PopupMenu(view.context, view)
                popUpMenu.menuInflater.inflate(R.menu.pop_up_menu, popUpMenu.menu)

                popUpMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.download -> {
                            onDownload(post.post)
                            true
                        }
                        else -> {
                            true
                        }
                    }
                }
                popUpMenu.show()
            }

            binding.btnSend.setOnClickListener{
                onReplyPost(binding.etMessage.text.toString(), post.post)
                binding.etMessage.text.clear()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding =
            FragmentPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentPostItem = getItem(position)
        (holder as ImageViewHolder).bind(currentPostItem)
    }

    class PostItemDiffUtilCallback : DiffUtil.ItemCallback<PostItem>() {
        override fun areItemsTheSame(oldItem: PostItem, newItem: PostItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PostItem, newItem: PostItem): Boolean {
            return oldItem == newItem
        }

    }
}
