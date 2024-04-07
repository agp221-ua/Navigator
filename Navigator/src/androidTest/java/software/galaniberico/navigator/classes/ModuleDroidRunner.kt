package software.galaniberico.navigator.classes

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import software.galaniberico.moduledroid.subcomponents.kernelconfigurator.ModuleDroidApplication

class ModuleDroidRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, ModuleDroidApplicationToTest::class.java.name, context)
    }
}