package com.android.notesapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.notesapp.model.NoteData
import com.android.notesapp.repo.NoteRepo

class MainViewModel: ViewModel() {
    fun getNotes(context: Context): LiveData<List<NoteData>?> =
        NoteRepo.getNotes(context)
}