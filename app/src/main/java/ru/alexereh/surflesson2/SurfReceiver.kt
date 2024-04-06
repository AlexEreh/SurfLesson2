package ru.alexereh.surflesson2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class SurfReceiver(private val onMessageReceived: (String) -> Unit): BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == MainActivity.SURF_ACTION) {
            val message = intent.getStringExtra("message")
            onMessageReceived(message!!)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}