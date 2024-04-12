package software.galaniberico.myapplication

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import software.galaniberico.navigator.facade.Navigate
import software.galaniberico.navigator.tags.Land

class MainActivity5 : AppCompatActivity() {

    @Land("noData")
    var noData = 0

    @Land("noAttribute")
    var noAttribute = 1

    @Land("noNavigateData")
    var noNavigateData = 2

    @Land("both")
    var both = 3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main5)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (Navigate.id(this) == "neverLoad") {
            findViewById<TextView>(R.id.tvNoData).text = "$noData"
            findViewById<TextView>(R.id.tvNoAttribute).text = "$noAttribute"
            findViewById<TextView>(R.id.tvNoNavigateData).text = "$noNavigateData"
            findViewById<TextView>(R.id.tvBoth).text = "$both"
        }
        if (Navigate.id(this) == "oldFields") {
            findViewById<TextView>(R.id.tvNoData).text = "$noData"
            findViewById<TextView>(R.id.tvNoAttribute).text = "$noAttribute"
            findViewById<TextView>(R.id.tvNoNavigateData).text = "$noNavigateData"
            findViewById<TextView>(R.id.tvBoth).text = "$both"
        }
        if (Navigate.id(this) == "navigateData") {
            findViewById<TextView>(R.id.tvNoData).text = "$noData"
            findViewById<TextView>(R.id.tvNoAttribute).text = "$noAttribute"
            findViewById<TextView>(R.id.tvNoNavigateData).text = "$noNavigateData"
            findViewById<TextView>(R.id.tvBoth).text = "$both"
        }
        if (Navigate.id(this) == "bothOldFieldNavigateData") {
            findViewById<TextView>(R.id.tvNoData).text = "$noData"
            findViewById<TextView>(R.id.tvNoAttribute).text = "$noAttribute"
            findViewById<TextView>(R.id.tvNoNavigateData).text = "$noNavigateData"
            findViewById<TextView>(R.id.tvBoth).text = "$both"
        }
        if (Navigate.id(this) == "bothNavigateDataOldField") {
            findViewById<TextView>(R.id.tvNoData).text = "$noData"
            findViewById<TextView>(R.id.tvNoAttribute).text = "$noAttribute"
            findViewById<TextView>(R.id.tvNoNavigateData).text = "$noNavigateData"
            findViewById<TextView>(R.id.tvBoth).text = "$both"
        }

        if (Navigate.id(this) == "neverLoadGet") {
            findViewById<TextView>(R.id.tvNoData).text = "${Navigate.get<Int>("noData", 0)}"
            findViewById<TextView>(R.id.tvNoAttribute).text = "${Navigate.get<Int>("noAttribute", 1)}"
            findViewById<TextView>(R.id.tvNoNavigateData).text = "${Navigate.get<Int>("noNavigateData", 2)}"
            findViewById<TextView>(R.id.tvBoth).text = "${Navigate.get<Int>("both", 3)}"
        }
        if (Navigate.id(this) == "oldFieldsGet") {
            findViewById<TextView>(R.id.tvNoData).text = "${Navigate.get<Int>("noData", 0)}"
            findViewById<TextView>(R.id.tvNoAttribute).text = "${Navigate.get<Int>("noAttribute", 1)}"
            findViewById<TextView>(R.id.tvNoNavigateData).text = "${Navigate.get<Int>("noNavigateData", 2)}"
            findViewById<TextView>(R.id.tvBoth).text = "${Navigate.get<Int>("both", 3)}"
        }
        if (Navigate.id(this) == "navigateDataGet") {
            findViewById<TextView>(R.id.tvNoData).text = "${Navigate.get<Int>("noData", 0)}"
            findViewById<TextView>(R.id.tvNoAttribute).text = "${Navigate.get<Int>("noAttribute", 1)}"
            findViewById<TextView>(R.id.tvNoNavigateData).text = "${Navigate.get<Int>("noNavigateData", 2)}"
            findViewById<TextView>(R.id.tvBoth).text = "${Navigate.get<Int>("both", 3)}"
        }
        if (Navigate.id(this) == "bothOldFieldNavigateDataGet") {
            findViewById<TextView>(R.id.tvNoData).text = "${Navigate.get<Int>("noData", 0)}"
            findViewById<TextView>(R.id.tvNoAttribute).text = "${Navigate.get<Int>("noAttribute", 1)}"
            findViewById<TextView>(R.id.tvNoNavigateData).text = "${Navigate.get<Int>("noNavigateData", 2)}"
            findViewById<TextView>(R.id.tvBoth).text = "${Navigate.get<Int>("both", 3)}"
        }
        if (Navigate.id(this) == "bothNavigateDataOldFieldGet") {
            findViewById<TextView>(R.id.tvNoData).text = "${Navigate.get<Int>("noData", 0)}"
            findViewById<TextView>(R.id.tvNoAttribute).text = "${Navigate.get<Int>("noAttribute", 1)}"
            findViewById<TextView>(R.id.tvNoNavigateData).text = "${Navigate.get<Int>("noNavigateData", 2)}"
            findViewById<TextView>(R.id.tvBoth).text = "${Navigate.get<Int>("both", 3)}"
        }
        if (Navigate.id(this) == "activityOrDefault") {
            findViewById<TextView>(R.id.tvNoNavigateData).text = "$noNavigateData"
        }
        if (Navigate.id(this) == "allMapProtocol") {
            findViewById<TextView>(R.id.tvNoNavigateData).text = "${Navigate.get<Int>("noNavigateData", 2)}"
            val context = Navigate.get<Context>("context")
            val view = Navigate.get<View>("view")
            findViewById<TextView>(R.id.tvContext).text = if (context != null) context::class.java.name.split(".").last() else "no value"
            findViewById<TextView>(R.id.tvView).text = if (view != null) view::class.java.name.split(".").last() else "no value"
        }
        if (Navigate.id(this) == "tooReturnTargets") {
            Navigate.back(this)
        }
    }
}