package software.galaniberico.navigator.configuration

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import software.galaniberico.moduledroid.facade.Facade
import software.galaniberico.moduledroid.subcomponents.kernelconfigurator.PluginConfigurator
import software.galaniberico.navigator.lifecicle.LandManager

class NavigatorConfigurator : PluginConfigurator {
    override fun configure(app: Application) {
        Log.i(PLUGIN_LOG_TAG, "Starting plugin configuration")

        Facade.addOnCreateSubscription { activity: Activity, _: Bundle? ->
            LandManager.land(activity)
        }

        Log.i(PLUGIN_LOG_TAG, "Plugin configured successfully")
    }

}




