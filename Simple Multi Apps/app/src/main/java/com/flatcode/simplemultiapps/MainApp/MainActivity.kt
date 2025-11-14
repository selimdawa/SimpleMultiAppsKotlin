package com.flatcode.simplemultiapps.MainApp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.CLASS
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnSharedPreferenceChangeListener {

    private var binding: ActivityMainBinding? = null
    var list: MutableList<Main>? = null
    var adapter: MainAdapter? = null
    var context: Context = this@MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        PreferenceManager.getDefaultSharedPreferences(baseContext)
            .registerOnSharedPreferenceChangeListener(this)
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        // Color Mode ----------------------------- Start
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        // Color Mode -------------------------------- End
        val sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(baseContext)
        if (sharedPreferences.getString(
                "color_option",
                "ONE"
            ) == "ONE" || sharedPreferences.getString(
                "color_option",
                "TWO"
            ) == "TWO" || sharedPreferences.getString(
                "color_option",
                "THREE"
            ) == "THREE" || sharedPreferences.getString(
                "color_option",
                "FOUR"
            ) == "FOUR" || sharedPreferences.getString(
                "color_option",
                "FIVE"
            ) == "FIVE" || sharedPreferences.getString(
                "color_option",
                "SIX"
            ) == "SIX" || sharedPreferences.getString(
                "color_option",
                "SEVEN"
            ) == "SEVEN" || sharedPreferences.getString(
                "color_option",
                "EIGHT"
            ) == "EIGHT" || sharedPreferences.getString(
                "color_option",
                "NINE"
            ) == "NINE" || sharedPreferences.getString("color_option", "TEEN") == "TEEN"
        ) {
            binding!!.toolbar.mode.setBackgroundResource(R.drawable.sun)
        } else if (sharedPreferences.getString(
                "color_option",
                "NIGHT_ONE"
            ) == "NIGHT_ONE" || sharedPreferences.getString(
                "color_option",
                "NIGHT_TWO"
            ) == "NIGHT_TWO" || sharedPreferences.getString(
                "color_option",
                "NIGHT_THREE"
            ) == "NIGHT_THREE" || sharedPreferences.getString(
                "color_option",
                "NIGHT_FOUR"
            ) == "NIGHT_FOUR" || sharedPreferences.getString(
                "color_option",
                "NIGHT_FIVE"
            ) == "NIGHT_FIVE" || sharedPreferences.getString(
                "color_option",
                "NIGHT_SIX"
            ) == "NIGHT_SIX" || sharedPreferences.getString(
                "color_option",
                "NIGHT_SEVEN"
            ) == "NIGHT_SEVEN"
        ) {
            binding!!.toolbar.mode.setBackgroundResource(R.drawable.moon)
        }
        binding!!.recyclerView.setHasFixedSize(true)
        list = ArrayList()
        adapter = MainAdapter(context, list!!)
        binding!!.recyclerView.adapter = adapter

        IdeaPosts(1, 1, 1, 2, 4, 1, 4, 2, 2, 3, 2, 3)
    }

    private fun IdeaPosts(
        I1: Int, I2: Int, I3: Int, I4: Int, I5: Int, I6: Int,
        I7: Int, I8: Int, I9: Int, I10: Int, I11: Int, I12: Int,
    ) {
        list!!.clear()
        val item1 = Main(R.drawable.ic_stop_watch, "Stop Watch", I1, CLASS.STOP_WATCH)
        val item2 = Main(R.drawable.ic_candy_cruch, "Candy Crush Game", I2, CLASS.CANDY_CRUSH_GAME)
        val item3 = Main(R.drawable.ic_multi_delete, "Multiple Delete", I3, CLASS.MULTIPLE_DELETE)
        val item4 =
            Main(R.drawable.ic_random, "Random Img Generating", I4, CLASS.RANDOM_IMG_GENERATING)
        val item5 = Main(R.drawable.ic_blogger, "Blogger App", I5, CLASS.BLOGGER_APP)
        val item6 = Main(R.drawable.ic_joke, "Joke App", I6, CLASS.JOKE_APP)
        val item7 = Main(R.drawable.ic_live_tv, "Live TV", I7, CLASS.LIVE_TV)
        val item8 = Main(R.drawable.ic_news, "News App", I8, CLASS.NEWS_APP)
        val item9 = Main(R.drawable.ic_pdf_reader, "Pdf Reader", I9, CLASS.PDF_READER)
        val item10 = Main(R.drawable.ic_video_player, "Video Player", I10, CLASS.VIDEO_PLAYER)
        val item11 = Main(R.drawable.ic_web, "Web App", I11, CLASS.WEB_APP)
        val item12 = Main(R.drawable.ic_wordpress, "Wordpress Blog", I12, CLASS.WORDPRESS)
        list!!.add(item1)
        list!!.add(item2)
        list!!.add(item3)
        list!!.add(item4)
        list!!.add(item5)
        list!!.add(item6)
        list!!.add(item7)
        list!!.add(item8)
        list!!.add(item9)
        list!!.add(item10)
        list!!.add(item11)
        list!!.add(item12)
        adapter!!.notifyDataSetChanged()
        binding!!.bar.visibility = View.GONE
        binding!!.recyclerView.visibility = View.VISIBLE
    }

    // Color Mode ----------------------------- Start
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "color_option") {
            recreate()
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SETTINGS_CODE) {
            recreate()
        }
    } // Color Mode -------------------------------- End

    companion object {
        private const val SETTINGS_CODE = 234
    }
}