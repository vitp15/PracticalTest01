package ro.pub.cs.systems.eim.practicaltest01

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ro.pub.cs.systems.eim.practicaltest01.Constants.PRAG

class PracticalTest01MainActivity : AppCompatActivity() {
    private lateinit var text1: TextView
    private lateinit var text2: TextView
    private lateinit var button1: Button
    private lateinit var button2: Button
    private var btn1Clicks: Int = 0
    private var btn2Clicks = 0
    private lateinit var serviceBroadcastReciver: ServiceBroadcastReciver
    private lateinit var serviceIntentFilter: IntentFilter
    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val resultMsg = data?.getStringExtra("result")
            Toast.makeText(this, resultMsg, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                this,
                "Error when sending data back to main activity",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        text1 = findViewById(R.id.text1)
        text2 = findViewById(R.id.text2)
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button1.setOnClickListener { v ->
            text1.text = "${++btn1Clicks}"
            checkIfStartService()
        }
        button2.setOnClickListener { v ->
            text2.text = "${++btn2Clicks}"
            checkIfStartService()
        }

        val secondActivity: Button = findViewById(R.id.secondActivity)
        secondActivity.setOnClickListener {
            val secondActivityIntent = Intent(this, PracticalTest01SecondaryActivity::class.java)
            secondActivityIntent.putExtra("btn1Clicks", btn1Clicks)
            secondActivityIntent.putExtra("btn2Clicks", btn2Clicks)
            activityResultLauncher.launch(secondActivityIntent)
        }

        serviceBroadcastReciver = ServiceBroadcastReciver()

        serviceIntentFilter = IntentFilter()
        serviceIntentFilter.addAction(Constants.ACTION_DATE)
        serviceIntentFilter.addAction(Constants.ACTION_AVG_GEOMETRIC)
        serviceIntentFilter.addAction(Constants.ACTION_AVG_ARITHMETIC)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("btn1Clicks", btn1Clicks)
        outState.putInt("btn2Clicks", btn2Clicks)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        btn1Clicks = savedInstanceState.getInt("btn1Clicks", 0)
        btn2Clicks = savedInstanceState.getInt("btn2Clicks", 0)
        text1.text = "${btn1Clicks}"
        text2.text = "${btn2Clicks}"
    }

    fun checkIfStartService() {
        if (btn1Clicks + btn2Clicks >= PRAG) {
            val intent = Intent().apply {
                component = ComponentName(
                    "ro.pub.cs.systems.eim.practicaltest01",
                    "ro.pub.cs.systems.eim.practicaltest01.PracticalTest01Service"
                )
            }
            intent.putExtra("btn1Clicks", btn1Clicks)
            intent.putExtra("btn2Clicks", btn2Clicks)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(serviceBroadcastReciver, serviceIntentFilter, RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(serviceBroadcastReciver, serviceIntentFilter)
        }
    }

    override fun onPause() {
        unregisterReceiver(serviceBroadcastReciver)
        super.onPause()
    }

    override fun onDestroy() {
        val serviceIntent = Intent()
        serviceIntent.component = ComponentName(
            "ro.pub.cs.systems.eim.practicaltest01",
            "ro.pub.cs.systems.eim.practicaltest01.PracticalTest01Service"
        )
        stopService(serviceIntent)
        super.onDestroy()
    }
}