package ro.pub.cs.systems.eim.practicaltest01

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PracticalTest01SecondaryActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_secondary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btn1Clicks = intent.getIntExtra("btn1Clicks", 0)
        val btn2Clicks = intent.getIntExtra("btn2Clicks", 0)
        val text: TextView = findViewById(R.id.text)
        val ok: Button = findViewById(R.id.ok)
        val cancel: Button = findViewById(R.id.cancel)

        text.text = "Total button clicks: ${btn1Clicks + btn2Clicks}"
        ok.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("result", "ok")
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        cancel.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("result", "cancel")
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}