package ro.pub.cs.systems.eim.practicaltest01

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ServiceBroadcastReciver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val data: String? = when (action) {
            Constants.ACTION_DATE -> "Date received: ${intent.getStringExtra(Constants.DATA)}"
            Constants.ACTION_AVG_ARITHMETIC -> "Arithmetic average received: ${
                intent.getFloatExtra(
                    Constants.DATA,
                    0F
                )
            }"

            Constants.ACTION_AVG_GEOMETRIC -> "Geometric average received: ${
                intent.getFloatExtra(
                    Constants.DATA,
                    0F
                )
            }"

            else -> "Unknown action"
        }
        Log.d(Constants.TAG, data ?: "")
    }
}