package software.galaniberico.navigator.tags

/**
 * Land annotation is used to associate a value from an attribute of a previous activity
 * to a value in a new activity. Before the new Activity is created,
 * this value is associated with the specified attribute of the previous activity.
 *
 * @param oldField The name of the field where to take the value in the origin Activity. No checks will
 * be done, so make sure that both have the same type.
 *
 * @param id The specific Navigation id the origin must have to set the value, this is, if the origin
 * has another id, this field will be skipped. If `id` is empty or not set, the origin id will not
 * be taken into account
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class Land(val oldField: String, val id: String = "")
