package software.galaniberico.navigator

import android.app.Application
import android.util.Log
import software.galaniberico.moduledroid.subcomponents.kernelconfigurator.PluginConfigurator

class MyPluginConfigurator : PluginConfigurator {
    override fun configure(app: Application) {
        Log.i("HW", "HELLO WORLD")
    }
}


