package com.android.notesapp.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.android.notesapp.R
import com.android.notesapp.model.NoteData
import com.android.notesapp.util.Constants
import com.android.notesapp.viewmodel.FormViewModel
import com.squareup.moshi.Moshi

class FormActivity : AppCompatActivity() {
    private lateinit var titleEt: EditText
    private lateinit var contentEt: EditText
    private lateinit var fvm: FormViewModel
    private var dataPos = -1
    private var data: NoteData? = null
    private val moshiNote = Moshi.Builder().build().adapter(NoteData::class.java)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        val toolbar = findViewById<Toolbar>(R.id.form_toolbar)
        titleEt = findViewById(R.id.form_title)
        contentEt = findViewById(R.id.form_content)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.findViewById<TextView>(R.id.toolbar_title).text = "Note"

        dataPos = intent.getIntExtra(Constants.FORM_POS, -1)
        intent.getStringExtra(Constants.FORM_DATA)?.let {
            data = moshiNote.fromJson(it)
        }
        data?.let { noteData ->
            titleEt.setText(noteData.title)
            contentEt.setText(noteData.content)
        }

        fvm = ViewModelProvider(this)[FormViewModel::class.java]
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.form_menu_delete -> {
                fvm.deleteNote(this, data!!.id).observe(this, { deleteCount ->
                    Log.v("deletecount", "$deleteCount")
                    if(deleteCount == 1) {
                        val resultIntent = Intent()
                        resultIntent.putExtra(Constants.FORM_POS, dataPos)
                        setResult(Constants.FORM_RES_DELETE, resultIntent)
                        finish()
                    }
                })
            }
            R.id.form_menu_save -> {
                if(titleEt.text.toString().isNotEmpty() && contentEt.text.toString().isNotEmpty()) {
                    val note =
                        if(data == null) {
                            NoteData(
                                title = titleEt.text.toString(),
                                content = contentEt.text.toString()
                            )
                        } else {
                            data!!.title = titleEt.text.toString()
                            data!!.content = contentEt.text.toString()
                            data!!
                        }
                    fvm.saveNote(this, note).observe(this, { saveSuccess ->
                        if(saveSuccess) {
                            val resultIntent = Intent()
                            resultIntent.putExtra(Constants.FORM_POS, dataPos)
                            if(dataPos == -1)
                                setResult(Constants.FORM_RES_INSERT, resultIntent)
                            else
                                setResult(Constants.FORM_RES_UPDATE, resultIntent)
                            finish()
                        }
                    })
                } else {
                    Toast.makeText(this, "title and content must not be empty", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.form_menu, menu)
        if(intent.getIntExtra(Constants.FORM_POS, -1) == -1)
            menu?.findItem(R.id.form_menu_delete)?.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }
}