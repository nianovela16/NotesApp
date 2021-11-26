package com.android.notesapp.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.android.notesapp.R
import com.android.notesapp.model.NoteData
import com.android.notesapp.util.Constants
import com.android.notesapp.view.FormActivity
import com.squareup.moshi.Moshi

class NoteAdapter(private val context: Context, private val notes: ArrayList<NoteData>,
                  private val resultForm: ActivityResultLauncher<Intent>):
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>()  {

    private val moshiNote = Moshi.Builder().build().adapter(NoteData::class.java)

    class NoteViewHolder: RecyclerView.ViewHolder {
        var title: TextView
        var content: TextView
        constructor(itemView: View) : super(itemView) {
            title = itemView.findViewById(R.id.note_item_title)
            content = itemView.findViewById(R.id.note_item_content)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.title.text = notes[position].title
        holder.content.text = notes[position].content
        holder.itemView.setOnClickListener {
            val intent = Intent(context, FormActivity::class.java)
            intent.putExtra(Constants.FORM_POS, position)
            intent.putExtra(Constants.FORM_DATA, moshiNote.toJson(notes[position]))
            resultForm.launch(intent)
        }
    }

    override fun getItemCount(): Int = notes.size
}