package com.akramkhan.notesandroid.adapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akramkhan.notesandroid.model.Notes
import com.akramkhan.notesandroid.EditorActivity
import com.akramkhan.notesandroid.R
import kotlinx.android.synthetic.main.note_list_item.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity

class MyAdapter(private val items: Array<Notes>, private val mainContext: Context): RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_list_item,parent,false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemText = items[position].title
        holder.view.tvNote.text = itemText
        holder.view.tvNote.onClick {
            mainContext.startActivity<EditorActivity>("id" to "${items[position].id}")
            (mainContext as Activity).finish()
        }
    }
    class ViewHolder(val view: View): RecyclerView.ViewHolder(view)
}