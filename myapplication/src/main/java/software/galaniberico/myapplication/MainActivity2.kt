package software.galaniberico.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import software.galaniberico.moduledroid.facade.Facade
import software.galaniberico.navigator.facade.Navigate
import software.galaniberico.navigator.tags.Land

class MainActivity2 : AppCompatActivity() {
    @Land("a", "withTag")
    private var aLand: Int = 0

    @Land("b", "withTag")
    private var bLand: TestingType = TestingType("default", 0)

    @Land("nullVar", "withTag")
    private var nullVarLand: String? = ""

    @Land("a", "withBadType")
    private var badType: TestingType? = null

    @Land("nofield", "withNoOldField")
    private var noOldField: String = "no value"

    @Land("a")
    private var always: Int = 0


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TextView>(R.id.tvA).text = "$aLand"
        findViewById<TextView>(R.id.tvB).text = "${bLand.ii} ${bLand.ss}"
        findViewById<TextView>(R.id.tvNull).text = "$nullVarLand"
        findViewById<TextView>(R.id.tvBadType).text = "$badType"
        findViewById<TextView>(R.id.tvNoOldField).text = noOldField
        findViewById<TextView>(R.id.tvAlways).text = "$always"
        findViewById<TextView>(R.id.tvId).text = "${Facade.getId(this)}"
        findViewById<TextView>(R.id.tvWith).text = "${Navigate.get<Int>("value1") ?: "default"}"
        findViewById<TextView>(R.id.tvWith2).text = "${Navigate.get<String>("value2", "default")}"

        if (Navigate.id(this) == "withLoadedEmpty") {
            Navigate.from(this).to(MainActivity3::class) {
                Navigate.withLoaded()
            }
        }
        if (Navigate.id(this) == "withLoadedOneElement") {
            Navigate.from(this).to(MainActivity3::class) {
                Navigate.withLoaded("withLoaded")
            }
        }
        if (Navigate.id(this) == "withLoadedSeveralElements") {
            Navigate.from(this).to(MainActivity3::class) {
                Navigate.withLoaded("withLoaded", "withLoaded2")
            }
        }
    }
}