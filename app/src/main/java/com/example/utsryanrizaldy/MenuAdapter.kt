package com.example.utsryanrizaldy


import android.view.LayoutInflater import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MenuAdapter(private val itemList: MutableList<MenuItem>): RecyclerView.Adapter<MenuAdapter.ViewHolder>() {
    private var listener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuAdapter.ViewHolder, position: Int) {
        val item = itemList[position]
        holder.judul.text = item.dishName
        Glide.with(holder.imageView.context).load(item.imageUrl).into(holder.imageView)

        holder.itemView.setOnClickListener{
            listener?.onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    class ViewHolder(@NonNull itemView: View):RecyclerView.ViewHolder(itemView) {
        val imageView : ImageView = itemView.findViewById(R.id.imageView)
        val judul : TextView = itemView.findViewById(R.id.textView)

    }

    interface OnItemClickListener {
        fun onItemClick(item: MenuItem)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}