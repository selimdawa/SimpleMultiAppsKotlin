package com.flatcode.simplemultiapps.Unit

import android.content.Context
import androidx.preference.PreferenceManager
import com.flatcode.simplemultiapps.R

object THEME {
    fun setThemeOfApp(context: Context) {
        val sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(context.applicationContext)
        if (sharedPreferences.getString("color_option", "ONE") == "ONE") {
            context.setTheme(R.style.Base_Theme_OneTheme)
        } else if (sharedPreferences.getString("color_option", "TWO") == "TWO") {
            context.setTheme(R.style.Base_Theme_TwoTheme)
        } else if (sharedPreferences.getString("color_option", "THREE") == "THREE") {
            context.setTheme(R.style.Base_Theme_ThreeTheme)
        } else if (sharedPreferences.getString("color_option", "FOUR") == "FOUR") {
            context.setTheme(R.style.Base_Theme_FourTheme)
        } else if (sharedPreferences.getString("color_option", "FIVE") == "FIVE") {
            context.setTheme(R.style.Base_Theme_FiveTheme)
        } else if (sharedPreferences.getString("color_option", "SIX") == "SIX") {
            context.setTheme(R.style.Base_Theme_SixTheme)
        } else if (sharedPreferences.getString("color_option", "SEVEN") == "SEVEN") {
            context.setTheme(R.style.Base_Theme_SevenTheme)
        } else if (sharedPreferences.getString("color_option", "EIGHT") == "EIGHT") {
            context.setTheme(R.style.Base_Theme_EightTheme)
        } else if (sharedPreferences.getString("color_option", "NINE") == "NINE") {
            context.setTheme(R.style.Base_Theme_NineTheme)
        } else if (sharedPreferences.getString("color_option", "TEEN") == "TEEN") {
            context.setTheme(R.style.Base_Theme_TeenTheme)
        }/* else if (sharedPreferences.getString("color_option", "NIGHT_ONE") == "NIGHT_ONE") {
            context.setTheme(R.style.OneNightTheme)
        } else if (sharedPreferences.getString("color_option", "NIGHT_TWO") == "NIGHT_TWO") {
            context.setTheme(R.style.TwoNightTheme)
        } else if (sharedPreferences.getString("color_option", "NIGHT_THREE") == "NIGHT_THREE") {
            context.setTheme(R.style.ThreeNightTheme)
        } else if (sharedPreferences.getString("color_option", "NIGHT_FOUR") == "NIGHT_FOUR") {
            context.setTheme(R.style.FourNightTheme)
        } else if (sharedPreferences.getString("color_option", "NIGHT_FIVE") == "NIGHT_FIVE") {
            context.setTheme(R.style.FiveNightTheme)
        } else if (sharedPreferences.getString("color_option", "NIGHT_SIX") == "NIGHT_SIX") {
            context.setTheme(R.style.SixNightTheme)
        } else if (sharedPreferences.getString("color_option", "NIGHT_SEVEN") == "NIGHT_SEVEN") {
            context.setTheme(R.style.SevenNightTheme)
        }*/
    }
}