package com.kitobim.util

import android.content.Context
import android.content.res.Configuration
import com.kitobim.data.local.preference.PreferenceHelper
import com.kitobim.data.local.preference.PreferenceHelper.get
import com.kitobim.data.local.preference.PreferenceHelper.set
import java.util.*


object LocaleHelper {

    fun setLocale(c: Context, language: String = getLanguage(c)): Context {
        persistLanguage(c, language)
        return updateResources(c, language)
    }

    fun getLanguage(c: Context): String {
        val prefs = PreferenceHelper.defaultPrefs(c)
        return prefs[Constants.LANGUAGE, "uz"]
    }

    fun persistLanguage(c: Context, language: String) {
        val prefs = PreferenceHelper.defaultPrefs(c)
        prefs[Constants.LANGUAGE] = language
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val res = context.resources
        val config = Configuration(res.configuration)

        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}
