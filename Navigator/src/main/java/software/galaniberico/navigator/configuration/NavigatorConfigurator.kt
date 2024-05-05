package software.galaniberico.navigator.configuration

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import software.galaniberico.moduledroid.facade.Facade
import software.galaniberico.moduledroid.subcomponents.kernelconfigurator.PluginConfigurator
import software.galaniberico.navigator.data.NavigateData
import software.galaniberico.navigator.navigation.LandManager

class NavigatorConfigurator : PluginConfigurator {
    override fun configure(app: Application) {
        Log.i(PLUGIN_LOG_TAG, "Starting plugin configuration")

        Facade.addOnPreCreateSubscription { activity: Activity, _: Bundle? ->
            try {
                Facade.getInternalId(activity)
                LandManager.land(activity)
            } catch (e: Exception){
                NavigatorConfigurations.landingErrorHandler(e)
            }
        }

        Facade.addOnPreDestroySubscription {
            NavigateData.saveParentData(it)
        }

        Facade.addOnPostDestroySubscription {
            if (!it.isChangingConfigurations)
                NavigateData.remove(it)
        }

        Facade.addOnSaveInstanceStateSubscription { it: Activity, _: Bundle ->
            NavigateData.saveParentData(it)
        }

        Facade.addOnPreResumeSubscription {
            NavigateData.executeOnResult(it)
        }

        Log.i(PLUGIN_LOG_TAG, "Plugin configured successfully")
    }

}




