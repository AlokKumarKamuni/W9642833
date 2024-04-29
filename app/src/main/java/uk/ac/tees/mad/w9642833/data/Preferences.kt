package uk.ac.tees.mad.w9642833.data

import android.content.Context
import android.content.SharedPreferences

object Preferences {


    fun getPref(context:Context): SharedPreferences? {
        return context.getSharedPreferences("my_pref",Context.MODE_PRIVATE)
    }

}