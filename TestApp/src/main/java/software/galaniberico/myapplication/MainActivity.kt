package software.galaniberico.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import software.galaniberico.navigator.facade.Navigate
import software.galaniberico.navigator.tags.Navigation

class MainActivity : AppCompatActivity() {
    var a = 45
    var b = TestingType("b", 46)
    var nullVar: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    @Navigation("withTag", MainActivity2::class)
    fun withTag(){
        Log.w("TEST", "All Correct")
    }
    @Navigation("withTag2", MainActivity2::class)
    fun withTag2(){
        Log.w("TEST", "All Correct")
    }

    @Navigation("withBadType", MainActivity2::class)
    fun withBadType(){
        Log.w("TEST", "All Correct")
    }

    @Navigation("withNoOldField", MainActivity2::class)
    fun withNoOldField(){
        Log.w("TEST", "All Correct")
    }

    @Navigation("withTooTargets", MainActivity2::class)
    fun withTooTargets(){
        Log.w("TEST", "All Correct")
    }

    @Navigation("withTooTargets", MainActivity2::class)
    fun withTooTargets2(){
        Log.w("TEST", "All Correct")
    }

    @Navigation("withNavigateData", MainActivity2::class)
    fun withNavigateData(){
        Navigate.with("value1", 47)
    }

    @Navigation("getId", MainActivity2::class)
    fun getId(){
        Navigate.with("value1", 49)
        Navigate.with("value2", "34445")
    }


}

class TestingType(val ss: String, val ii: Int)