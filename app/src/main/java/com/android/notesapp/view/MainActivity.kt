package com.android.notesapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.notesapp.R
import com.android.notesapp.model.NoteData
import com.android.notesapp.util.Constants
import com.android.notesapp.view.adapter.NoteAdapter
import com.android.notesapp.view.custom.LinearLayoutManagerHandled
import com.android.notesapp.viewmodel.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var userSp: SharedPreferences
    private lateinit var noteAdapter: NoteAdapter
    private val notes = ArrayList<NoteData>()
    private var dbPar = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userSp = getSharedPreferences(Constants.SP_USER, Context.MODE_PRIVATE)

        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        val introTv = findViewById<TextView>(R.id.main_intro)
        val noteRecycler = findViewById<RecyclerView>(R.id.main_notes)
        val noteAddFab = findViewById<FloatingActionButton>(R.id.main_fab)
        val emptyTv = findViewById<TextView>(R.id.main_empty)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.findViewById<TextView>(R.id.toolbar_title).text = "Dashboard"

        introTv.text = "Hello, ${userSp.getString(Constants.CRED_NAME, Constants.CRED_NAME)}"

        val mvm = ViewModelProvider(this)[MainViewModel::class.java]


        val resultForm = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when(result.resultCode) {
                Constants.FORM_RES_INSERT -> {
                    mvm.getNotes(this).observe(this, { lndn ->
                        if(lndn != null) {
                            if(lndn.isNotEmpty()) {
                                emptyTv.visibility = View.GONE
                                noteRecycler.visibility = View.VISIBLE
                                notes.clear()
                                notes.addAll(lndn)
                                noteAdapter.notifyItemInserted(0)
                                noteAdapter.notifyItemRangeChanged(0, noteAdapter.itemCount)
                            } else {
                                emptyTv.visibility = View.VISIBLE
                                noteRecycler.visibility = View.GONE
                            }
                        }
                    })
                }
                Constants.FORM_RES_UPDATE -> {
                    val position = result.data?.getIntExtra(Constants.FORM_POS, 0)
                    mvm.getNotes(this).observe(this, { lndn ->
                        if(lndn != null) {
                            if(lndn.isNotEmpty()) {
                                emptyTv.visibility = View.GONE
                                noteRecycler.visibility = View.VISIBLE
                                notes.clear()
                                notes.addAll(lndn)
                                noteAdapter.notifyItemChanged(position!!)
                            } else {
                                emptyTv.visibility = View.VISIBLE
                                noteRecycler.visibility = View.GONE
                            }
                        }
                    })
                }
                Constants.FORM_RES_DELETE -> {
                    val position = result.data?.getIntExtra(Constants.FORM_POS, 0)
                    notes.removeAt(position!!)
                    noteAdapter.notifyItemRemoved(position)
                    noteAdapter.notifyItemRangeChanged(position, noteAdapter.itemCount)
                    if(notes.isEmpty()) {
                        emptyTv.visibility = View.VISIBLE
                        noteRecycler.visibility = View.GONE
                    }
                }
            }
        }

        noteAdapter = NoteAdapter(this, notes, resultForm)

        noteRecycler.layoutManager = LinearLayoutManagerHandled(this, LinearLayoutManager.VERTICAL, false)
        noteRecycler.adapter = noteAdapter

        mvm.getNotes(this).observe(this, { lndn ->
            if(lndn != null) {
                if(lndn.isNotEmpty()) {
                    emptyTv.visibility = View.GONE
                    noteRecycler.visibility = View.VISIBLE
                    notes.clear()
                    notes.addAll(lndn)
                    noteAdapter.notifyItemInserted(0)
                } else {
                    emptyTv.visibility = View.VISIBLE
                    noteRecycler.visibility = View.GONE
                }
            }
        })

        noteAddFab.setOnClickListener {
            resultForm.launch(Intent(this, FormActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.main_menu_logout -> {
                userSp.edit().clear().apply()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if (dbPar) {
            finishAffinity()
            return
        }
        this.dbPar = true
        Toast.makeText(this, "press again to exit", Toast.LENGTH_SHORT).show()
        Handler(Looper.myLooper()!!).postDelayed({ this.dbPar = false }, 2000)
    }
}