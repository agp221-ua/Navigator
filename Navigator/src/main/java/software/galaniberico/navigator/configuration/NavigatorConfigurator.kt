package software.galaniberico.navigator.configuration

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import software.galaniberico.moduledroid.facade.Facade
import software.galaniberico.moduledroid.subcomponents.kernelconfigurator.PluginConfigurator
import software.galaniberico.navigator.lifecicle.ComingActivityPile
import software.galaniberico.navigator.lifecicle.LandManager
import software.galaniberico.navigator.navigation.NavigateDataManager
import software.galaniberico.navigator.navigation.ResultDataManager

class NavigatorConfigurator : PluginConfigurator {
    override fun configure(app: Application) {
        Log.i(PLUGIN_LOG_TAG, "Starting plugin configuration")

        Facade.addOnCreateSubscription { activity: Activity, _: Bundle? ->
            Facade.getIdOrProvideOne(activity)
            LandManager.land(activity)
        }

        Facade.addOnDestroySubscription {
            ComingActivityPile.saveParentData(it)
        }

        Facade.addOnResumeSubscription {
            if (ResultDataManager.currentOutputResult == null
                || ResultDataManager.currentOutputResult!!.parentId != Facade.getId(it))
                return@addOnResumeSubscription
            ResultDataManager.executeOnResult()
        }

        Log.i(PLUGIN_LOG_TAG, "Plugin configured successfully")
    }

}




