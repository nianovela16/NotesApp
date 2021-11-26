package com.android.notesapp.repo

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.android.notesapp.model.NoteData
import com.android.notesapp.room.MyDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object NoteRepo {
    fun saveNote(context: Context, noteData: NoteData): MutableLiveData<Boolean> {
        val mld = MutableLiveData<Boolean>()

        CoroutineScope(Dispatchers.IO).launch {
            val db = Room.databaseBuilder(context, MyDatabase::class.java,
                MyDatabase.DATABASE_NAME).build()

            try {
                db.notesDao().saveNote(noteData)
                mld.postValue(true)
            } catch (e: Exception) {
                Log.e(MyDatabase.TABLE_NOTE, e.stackTraceToString())
                mld.postValue(false)
            }
        }

        return mld
    }

    fun deleteNote(context: Context, noteId: Long): MutableLiveData<Int> {
        val mld = MutableLiveData<Int>()


        CoroutineScope(Dispatchers.IO).launch {
            val db = Room.databaseBuilder(context, MyDatabase::class.java,
                MyDatabase.DATABASE_NAME).build()
            mld.postValue(db.notesDao().deleteNote(noteId))
        }

        return mld
    }

    fun getNotes(context: Context): MutableLiveData<List<NoteData>?> {
        val mld = MutableLiveData<List<NoteData>?>()

        CoroutineScope(Dispatchers.IO).launch {
            val db = Room.databaseBuilder(context, MyDatabase::class.java,
                MyDatabase.DATABASE_NAME).build()
            try {
                val notes = db.notesDao().getNotes()
                mld.postValue(notes)
            } catch (e: Exception) {
                Log.e(MyDatabase.TABLE_NOTE, e.stackTraceToString())
                mld.postValue(null)
            }
        }

        return mld
    }
}