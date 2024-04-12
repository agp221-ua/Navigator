package software.galaniberico.myapplication

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import software.galaniberico.navigator.facade.Navigate

class MainActivity4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main4)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        if (Navigate.id(this) == "neverLoad"){
            try {
                Navigate.get("to do",{})?.let { it() }
            } catch (e: Exception) {
                findViewById<TextView>(R.id.tvError).text = e::class.java.name.split(".").last()
            }
            return
        }
        if (Navigate.id(this) == "manualLoadNotCalled"){
            try {
                Navigate.get("to do",{})?.let { it() }
            } catch (e: Exception) {
                findViewById<TextView>(R.id.tvError).text = e::class.java.name.split(".").last()
            }
            return
        }
        if (Navigate.id(this) == "manualLoad"){
            try {
                Navigate.load(this)
                Navigate.get("to do",{})?.let { it() }
                Navigate.nullify()
                findViewById<TextView>(R.id.tvError).text = "all ok"
            } catch (e: Exception) {
                findViewById<TextView>(R.id.tvError).text = e::class.java.name.split(".").last()
            }
            return
        }
        if (Navigate.id(this) == "autoLoadNotNulled"){
            try {
                Navigate.get("to do",{})?.let { it() }
                Navigate.from(this).to("autoLoadNotNulled2", MainActivity4::class)
            } catch (e: Exception) {
                findViewById<TextView>(R.id.tvError).text = e::class.java.name.split(".").last()
            }
            return
        }
        if (Navigate.id(this) == "autoLoad"){
            try {
                Navigate.get("to do",{})?.let { it() }
                Navigate.nullify()
                Navigate.from(this).to("autoLoad2", MainActivity4::class)
                findViewById<TextView>(R.id.tvError).text = "all ok"
            } catch (e: Exception) {
                findViewById<TextView>(R.id.tvError).text = e::class.java.name.split(".").last()
            }
            return
        }
        if (Navigate.id(this) == "autoLoad2"){
            try {
                Navigate.get("to do",{})?.let { it() }
                Navigate.nullify()
                findViewById<TextView>(R.id.tvError).text = "all ok"
            } catch (e: Exception) {
                findViewById<TextView>(R.id.tvError).text = e::class.java.name.split(".").last()
            }
            return
        }
        if (Navigate.id(this) == "autoLoadNotToNullify"){
            try {
                Navigate.get("to do",{})?.let { it() }
                findViewById<TextView>(R.id.tvError).text = "all ok"
            } catch (e: Exception) {
                findViewById<TextView>(R.id.tvError).text = e::class.java.name.split(".").last()
            }
            return
        }
    }
}