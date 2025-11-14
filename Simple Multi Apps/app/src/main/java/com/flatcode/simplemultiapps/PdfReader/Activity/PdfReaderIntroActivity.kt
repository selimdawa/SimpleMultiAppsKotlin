package com.flatcode.simplemultiapps.PdfReader.Activity

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.flatcode.simplemultiapps.R
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import com.github.paolorotolo.appintro.model.SliderPage

class PdfReaderIntroActivity : AppIntro() {
    var bg = Color.parseColor("#000000")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //getSupportActionBar().hide();
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val one = SliderPage()
        one.title = getString(R.string.title_permission)
        one.description = getString(R.string.description__permission)
        one.imageDrawable = R.drawable.patterns_permissions
        one.bgColor = bg
        addSlide(AppIntroFragment.newInstance(one))
        askForPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        //}
        showSkipButton(false)
        showStatusBar(false)
    }

    override fun onDonePressed(currentFragment: Fragment) {
        super.onDonePressed(currentFragment)
        finish()
    }
}