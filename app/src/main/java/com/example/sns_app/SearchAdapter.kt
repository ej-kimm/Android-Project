package com.example.sns_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SearchAdapter(private val context: Fragment) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var items: List<SearchData> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_search, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtId: TextView = itemView.findViewById(R.id.textView)
        private val txtName: TextView = itemView.findViewById(R.id.followerName)
        private val imgProfile: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(item: SearchData) {
            txtId.text = item.id
            txtName.text = item.name
            Glide.with(itemView).load(item.img).into(imgProfile)
        }
    }

    fun setSearchDataList(list: List<SearchData>) {
        items = list
        notifyDataSetChanged()
    }
}