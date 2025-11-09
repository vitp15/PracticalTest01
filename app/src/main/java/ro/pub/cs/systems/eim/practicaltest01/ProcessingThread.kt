package ro.pub.cs.systems.eim.practicaltest01

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.util.Log
import java.util.Date
import kotlin.math.sqrt

class ProcessingThread(
    private val context: Context,
    btn1Clicks: Int,
    btn2Clicks: Int
) : Thread() {
    private val avg_arithmetic: Float
    private val avg_geometric: Float

    @Volatile
    var isRunning = true

    init {
        avg_arithmetic = (btn1Clicks.toFloat() + btn2Clicks.toFloat()) / 2
        avg_geometric = sqrt(btn1Clicks.toFloat() * btn2Clicks.toFloat())
    }

    override fun run() {
        Log.d(
            Constants.TAG,
            "Thread.run() was invoked, PID: ${android.os.Process.myPid()} TID: ${android.os.Process.myTid()}"
        )
        while (isRunning) {
            sendMessage()
            sleepThread()
        }
    }

    private fun sleepThread() {
        try {
            sleep(Constants.SLEEP_TIME)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun sendMessage() {
        val intent = Intent()

        val messageType = (1..3).random()
        when (messageType) {
            1 -> {
                intent.action = Constants.ACTION_DATE
                intent.putExtra(Constants.DATA, SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date()))
            }

            2 -> {
                intent.action = Constants.ACTION_AVG_ARITHMETIC
                intent.putExtra(Constants.DATA, avg_arithmetic)
            }

            3 -> {
                intent.action = Constants.ACTION_AVG_GEOMETRIC
                intent.putExtra(Constants.DATA, avg_geometric)
            }
        }
        intent.setPackage(context.packageName)
        context.sendBroadcast(intent)
    }

    fun stopThread() {
        isRunning = false
    }
}