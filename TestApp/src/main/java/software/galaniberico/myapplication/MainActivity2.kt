package software.galaniberico.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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

        if (Navigate.id(this) == "multipleConsecutive") {
            Log.w("TEEEEEST", "PASSED")
            val v = Navigate.get<Int>("multipleConsecutive")
                ?: return
            if (v != 0){
                Navigate.from(this).to("multipleConsecutive", MainActivity2::class) {
                    Navigate.with("multipleConsecutive", v-1)
                }
                return
            }
            Navigate.from(this).to(MainActivity3::class) {
                Navigate.with("withLoaded2", "OK")
            }
            return
        }
        if (Navigate.id(this) == "withLoadedEmpty") {
            Navigate.from(this).to(MainActivity3::class) {
                Navigate.withLoaded()
            }
            return
        }
        if (Navigate.id(this) == "withLoadedOneElement") {
            Navigate.from(this).to(MainActivity3::class) {
                Navigate.withLoaded("withLoaded")
            }
            return
        }
        if (Navigate.id(this) == "withLoadedSeveralElements") {
            Navigate.from(this).to(MainActivity3::class) {
                Navigate.withLoaded("withLoaded", "withLoaded2")
            }
            return
        }

        if (Navigate.id(this) == "withResultEmpty"){
            Navigate.get("to do",{})?.let { it() }
        }
        if (Navigate.id(this) == "withResultBlank"){
            Navigate.get("to do",{})?.let { it() }
        }
        if (Navigate.id(this) == "withResultMultipleValues"){
            Navigate.get("to do",{})?.let { it() }
        }
        if (Navigate.id(this) == "withResultNull"){
            Navigate.get("to do",{})?.let { it() }
        }
        if (Navigate.id(this) == "withResultSameId"){
            Navigate.get("to do",{})?.let { it() }
        }
        if (Navigate.id(this) == "withResultDifferentValueTypes"){
            Navigate.get("to do",{})?.let { it() }
        }
        if (Navigate.id(this) == "getResultEmpty"){
            Navigate.get("to do",{})?.let { it() }
        }
        if (Navigate.id(this) == "getResultBlank"){
            Navigate.get("to do",{})?.let { it() }
        }
        if (Navigate.id(this) == "getResultMultipleValues"){
            Navigate.get("to do",{})?.let { it() }
        }
        if (Navigate.id(this) == "getResultNull"){
            Navigate.get("to do",{})?.let { it() }
        }
        if (Navigate.id(this) == "getResultSameId"){
            Navigate.get("to do",{})?.let { it() }
        }
        if (Navigate.id(this) == "getResultDifferentValueTypes"){
            Navigate.get("to do",{})?.let { it() }
        }
        if (Navigate.id(this) == "getResultDefault"){
            Navigate.get("to do",{})?.let { it() }
        }

        if(Navigate.get("return", false) == true)
            Navigate.back(this)

    }

    override fun onResume() {
        super.onResume()
        if (Navigate.id(this) == "onResultNavigationNested"){
            Navigate.get("to do",{})?.let { it() }
        }
    }
}