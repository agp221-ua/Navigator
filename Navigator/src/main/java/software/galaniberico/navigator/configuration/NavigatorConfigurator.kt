package software.galaniberico.navigator.configuration

import android.app.Application
import android.util.Log
import software.galaniberico.moduledroid.subcomponents.kernelconfigurator.PluginConfigurator

class NavigatorConfigurator : PluginConfigurator {
    override fun configure(app: Application) {
        Log.i("HW", "HELLO WORLD")
    }
}


