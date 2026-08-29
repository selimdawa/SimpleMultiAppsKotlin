package com.flatcode.simplemultiapps.pdfreader.activity

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import com.flatcode.simplemultiapps.R
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import com.github.paolorotolo.appintro.model.SliderPage

class PdfReaderIntroActivity : AppIntro() {

    private val backgroundColor = Color.parseColor("#000000")

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val sliderPage = SliderPage().apply {
            title = getString(R.string.title_permission)
            description = getString(R.string.description__permission)
            imageDrawable = R.drawable.patterns_permissions
            bgColor = backgroundColor
        }

        addSlide(AppIntroFragment.newInstance(sliderPage))

        askForPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)

        showSkipButton(false)
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        finish()
    }
}