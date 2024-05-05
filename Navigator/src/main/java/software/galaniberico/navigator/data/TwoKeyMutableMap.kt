package software.galaniberico.navigator.data

import android.util.Log

class TwoKeyMutableMap<K,P,V>{
    private val kmap = mutableMapOf<K,V>()
    private val pmap = mutableMapOf<P,V>()

    fun put(k: K, p: P, v: V) {
        kmap[k] = v
        pmap[p] = v
    }

    fun getK(k: K): V?{
        return kmap[k]
    }

    fun getP(p: P) : V?{
        return pmap[p]
    }

    fun removeK(k: K): V?{
//        Log.w("TEEEEEEEEEST", "DELETING INTERNALLY $k")
        if (!kmap.contains(k))
            return null
        val value = kmap.remove(k)
        var key: P? = null
        pmap.forEach{
            if(it.value == value){
                key = it.key
                return@forEach
            }
        }
        if (key != null)
            pmap.remove(key)

        return value
    }

    fun removeP(p: P): V?{
        Log.w("TEEEEEEEEEST", "DELETING INTERNALLY $p")
        if (!pmap.contains(p))
            return null
        val value = pmap.remove(p)
        var key: K? = null
        kmap.forEach{
            if(it.value == value){
                key = it.key
                return@forEach
            }
        }
        if (key != null)
            kmap.remove(key)

        return value
    }

    fun getKeyset(): Set<K>{
        return kmap.keys
    }

    fun getPeyset(): Set<P>{
        return pmap.keys
    }

}