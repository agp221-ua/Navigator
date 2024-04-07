package software.galaniberico.myapplication

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import software.galaniberico.navigator.configuration.PLUGIN_LOG_TAG
import software.galaniberico.navigator.facade.Navigate
import software.galaniberico.navigator.tags.Land

class MainActivity2 : AppCompatActivity() {
    @Land("a", "ir a 2")
    private var b: Activity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TextView>(R.id.tvPrueba).text = "Set to 45"
        findViewById<TextView>(R.id.tvPrueba).setOnClickListener {
            val aaaa = parent
            Navigate.withResult("buttonName", "45")
            Navigate.back()
        }
    }
}