package software.galaniberico.navigator.classes

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import software.galaniberico.moduledroid.subcomponents.kernelconfigurator.ModuleDroidApplication

class ModuleDroidApplicationToTest : ModuleDroidApplication() {
    override fun onCreate() {

        // Set metadata programmatically
        val metaData = hashMapOf<String, String>()
        metaData["moduledroid_plugins"] =
            "software.galaniberico.navigator.configuration.NavigatorConfigurator"

        setMetaData(metaData)
        super.onCreate()
    }

    private fun setMetaData(metaData: Map<String, String>) {
        val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
//        appInfo.metaData?.let { existingMetaData ->
//            val metaDataBuilder = existingMetaData.keySet().associateWithTo(hashMapOf()) { existingMetaData[it]!! }
//            metaData.forEach { (key, value) -> metaDataBuilder[key] = value }
//            val newMetaData = android.os.Bundle()
//            metaDataBuilder.forEach { (key, value) -> newMetaData.putString(key, value as String) }
//            existingMetaData.keySet().forEach { existingMetaData.remove(it) }
//            existingMetaData.putAll(newMetaData)
//        }
        appInfo.metaData = Bundle()

        appInfo.metaData.putString(
            "moduledroid_plugins",
            "software.galaniberico.navigator.configuration.NavigatorConfigurator"
        )

    }
}