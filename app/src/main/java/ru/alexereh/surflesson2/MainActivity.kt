package ru.alexereh.surflesson2

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.ContextCompat.RECEIVER_EXPORTED as COMPAT_RECEIVER_EXPORTED


class MainActivity : AppCompatActivity() {
    private val receiver = SurfReceiver {
        lastMessage = it
    }
    private var lastKey: String? = null
    private var lastMessage: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        registerSurfReceiver()
        registerSurfResolver()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun registerSurfResolver() {
        val resolver = contentResolver
        val uri = Uri.parse(SURF_PROVIDER_URI_STRING)
        val cursor = resolver.query(uri, null, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                @SuppressLint("Range")
                val text = it.getString(it.getColumnIndex("text"))
                lastKey = text
                Toast.makeText(
                    this,
                    "Ключ из провайдера $text",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun registerSurfReceiver() {
        val filter = IntentFilter(SURF_ACTION)
        ContextCompat.registerReceiver(this, receiver, filter, COMPAT_RECEIVER_EXPORTED)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("key", lastKey)
        outState.putString("message", lastMessage)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val key = savedInstanceState.getString("key")
        val message = savedInstanceState.getString("message")
        lastKey = key
        lastMessage = message
        Log.d("LOL", "Key: '$lastKey', message: '$lastMessage'")
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    companion object {
        const val SURF_ACTION = "ru.shalkoff.vsu_lesson2_2024.SURF_ACTION"
        const val SURF_PROVIDER_URI_STRING = "content://dev.surf.android.provider/text"
    }
}