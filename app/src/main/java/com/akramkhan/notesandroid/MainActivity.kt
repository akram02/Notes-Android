package com.akramkhan.notesandroid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.akramkhan.notesandroid.adapter.MyAdapter
import com.akramkhan.notesandroid.model.Notes
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate


class MainActivity : AppCompatActivity() {
    private val url = "http://192.168.0.104:8080/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val restTemplate = RestTemplate()
        restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())


        val context = this
        doAsync {
            val allNotes = restTemplate.getForObject(url, Array<Notes>::class.java)
            uiThread {
                recyclerView.adapter = MyAdapter(allNotes, context)
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        fab.onClick {
            startActivity<EditorActivity>()
            finish()
        }
    }
}
