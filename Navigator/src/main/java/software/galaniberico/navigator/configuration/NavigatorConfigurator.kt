package software.galaniberico.navigator.configuration

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import software.galaniberico.moduledroid.facade.Facade
import software.galaniberico.moduledroid.subcomponents.kernelconfigurator.PluginConfigurator
import software.galaniberico.navigator.data.ComingActivityPile
import software.galaniberico.navigator.data.NavigateDataManager
import software.galaniberico.navigator.data.ResultDataManager
import software.galaniberico.navigator.navigation.LandManager

class NavigatorConfigurator : PluginConfigurator {
    override fun configure(app: Application) {
        Log.i(PLUGIN_LOG_TAG, "Starting plugin configuration")

        Facade.addOnCreateSubscription { activity: Activity, _: Bundle? ->
            try {
                val s = Facade.getIdOrProvideOne(activity)
                LandManager.land(activity)
            } catch (e: Exception){
                NavigatorConfigurations.landingErrorHandler(e)
            }
        }

        Facade.addOnDestroySubscription {
            ComingActivityPile.saveParentData(it)
        }

        Facade.addOnSaveInstanceStateSubscription { it: Activity, _: Bundle ->
            ComingActivityPile.saveParentData(it)
        }

        Facade.addOnPostResumeSubscription {
            if (ResultDataManager.currentOutputResult == null
                || ResultDataManager.currentOutputResult!!.parentId != Facade.getId(it))
                return@addOnPostResumeSubscription
            ResultDataManager.executeOnResult()
        }

        Facade.addOnResumeSubscription {
            if (NavigateDataManager.isParent(it)){
                NavigateDataManager.nullifyCurrentOutcomeNavigateData()
            }
        }

        Log.i(PLUGIN_LOG_TAG, "Plugin configured successfully")
    }

}




