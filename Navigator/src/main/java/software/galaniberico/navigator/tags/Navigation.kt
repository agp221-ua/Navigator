package software.galaniberico.navigator.tags

import android.app.Activity
import kotlin.reflect.KClass

/**
 * Navigation annotation determines the method to be called before to start an activity with type `target`
 * when the Navigation with id `id` is called, e.g. call Navigate.to("id-example") will execute the
 * method with `@Navigation("id-example", ExampleActivity::class)` and after that will start an
 * activity with class `ExampleActivity`.
 *
 * All method with this annotation must not have any parameter.
 *
 * @param id The id of the Navigation. This should be unique, because if more than one are found,
 *  only the first match will be executed (depending of the plugin configuration, this behaviour may change).
 * @param target The class of the custom Activity that is wanted to start.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Navigation(val id: String, val target: KClass<out Activity>)