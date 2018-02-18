package com.akramkhan.notesandroid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.akramkhan.notesandroid.model.Notes
import kotlinx.android.synthetic.main.activity_editor.*
import kotlinx.android.synthetic.main.content_editor.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate


class EditorActivity : AppCompatActivity() {
    var id: String? = ""
    private val url = "http://192.168.0.104:8080/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        setSupportActionBar(toolbar)
        id = intent.getStringExtra("id")
        if (id==null) id="0"
        getNote()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun getNote() {
        val restTemplate = RestTemplate()
        restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())
        if (id != "0") {
            doAsync {
                val note = restTemplate.getForObject(url + id, Notes::class.java)
                uiThread {
                    editTitle.setText(note.title)
                    description.setText(note.description)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (id == "0") menuInflater.inflate(R.menu.editor_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> sendNote()
            R.id.delete -> delete()
        }
        return true
    }

    override fun onBackPressed() {
        startActivity<MainActivity>()
        super.onBackPressed()
    }

    private fun delete() {
        doAsync {
            RestTemplate().delete(url + id)
            uiThread {
                toast("Deleted successfully")
                finishAndGo()
            }
        }
    }
    private fun sendNote() {
        val rt = RestTemplate()
        rt.messageConverters.add(StringHttpMessageConverter())
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val title = editTitle.text.toString()
        val body = description.text.toString()
        if(title=="" || body=="") {
            toast("Title or Body will not empty")
            return
        }
        val jsonBody = "{\"id\": \"$id\",\"title\": \"$title\", \"description\": \"$body\"}"
        val entity = HttpEntity<String>(jsonBody, headers)
        doAsync {
            rt.exchange(url, HttpMethod.POST, entity, String::class.java)
            uiThread {
                toast("${if (id=="0") "Saved" else "Updated"} successfully")
                finishAndGo()
            }
        }
    }

    private fun finishAndGo() {
        startActivity<MainActivity>()
        finish()
    }
}
