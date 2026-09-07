package com.flatcode.simplemultiapps.webapp

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.utils.DATA
import com.flatcode.simplemultiapps.utils.intent1
import com.flatcode.simplemultiapps.databinding.ActivityWebAppBinding

class WebAppActivity : AppCompatActivity() {

    private var _binding: ActivityWebAppBinding? = null
    private val binding get() = _binding!!

    private val context: Context = this@WebAppActivity
    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = ActivityWebAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.nameSpace.setText(R.string.web_app)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
        }

        binding.webSite.setOnClickListener {
            context.intent1(WebViewActivity::class.java) {
                putExtra(DATA.WEB_NAME, DATA.WEBSITE)
            }
        }
        binding.instagram.setOnClickListener {
            context.intent1(WebViewActivity::class.java) {
                putExtra(DATA.WEB_NAME, DATA.INSTAGRAM)
            }
        }
        binding.twitter.setOnClickListener {
            context.intent1(WebViewActivity::class.java) {
                putExtra(DATA.WEB_NAME, DATA.TWITTER)
            }
        }
        binding.facebook.setOnClickListener {
            context.intent1(WebViewActivity::class.java) {
                putExtra(DATA.WEB_NAME, DATA.FACEBOOK)
            }
        }

        binding.aboutUs.setOnClickListener { showAboutUsDialog() }
        binding.support.setOnClickListener { showSupportDialog() }
        binding.shareApp.setOnClickListener { shareApp() }
        binding.rateApp.setOnClickListener { rateApp() }
    }

    private fun showAboutUsDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.item_web_card, null)
        val aboutAppLayout = dialogView.findViewById<RelativeLayout>(R.id.about_app)
        val contactLayout = dialogView.findViewById<RelativeLayout>(R.id.contact)
        val aboutUsText = dialogView.findViewById<TextView>(R.id.about_us)
        val closeText = dialogView.findViewById<TextView>(R.id.close)

        aboutAppLayout.visibility = View.VISIBLE
        contactLayout.visibility = View.GONE
        aboutUsText.visibility = View.VISIBLE
        aboutUsText.text = getString(R.string.about_us_content)

        alertDialog = AlertDialog.Builder(context).setView(dialogView).create().apply {
            closeText.setOnClickListener { dismiss() }
            window?.setBackgroundDrawable(ContextCompat.getColor(context, R.color.transparent).toDrawable())
            show()
        }
    }

    private fun showSupportDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.item_web_card, null)
        val aboutAppLayout = dialogView.findViewById<RelativeLayout>(R.id.about_app)
        val contactLayout = dialogView.findViewById<RelativeLayout>(R.id.contact)
        val aboutUsText = dialogView.findViewById<TextView>(R.id.about_us)
        val emailImage = dialogView.findViewById<ImageView>(R.id.email)
        val phoneImage = dialogView.findViewById<ImageView>(R.id.phone)
        val closeText = dialogView.findViewById<TextView>(R.id.close)

        aboutAppLayout.visibility = View.GONE
        contactLayout.visibility = View.VISIBLE
        aboutUsText.visibility = View.GONE

        alertDialog = AlertDialog.Builder(context).setView(dialogView).create().apply {
            closeText.setOnClickListener { dismiss() }
            window?.setBackgroundDrawable(ContextCompat.getColor(context, R.color.transparent).toDrawable())

            emailImage.setOnClickListener {
                val emailSelectorIntent = Intent(Intent.ACTION_SENDTO).apply { data = DATA.MAILTO_SCHEME.toUri() }
                val emailIntent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(DATA.myEmail))
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    selector = emailSelectorIntent
                }
                startActivity(emailIntent)
            }

            phoneImage.setOnClickListener {
                val callIntent = Intent(Intent.ACTION_CALL).apply { data = "${DATA.TEL_SCHEME}${DATA.myMobileNumber}".toUri() }
                startActivity(callIntent)
            }
            show()
        }
    }

    private fun shareApp() {
        val share = Intent(Intent.ACTION_SEND).apply {
            type = DATA.TEXT_PLAIN
            addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
            putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_text, packageName))
        }
        startActivity(Intent.createChooser(share, getString(R.string.share_link)))
    }

    private fun rateApp() {
        val uri = "${DATA.MARKET_SCHEME}$packageName".toUri()
        val goToMarket = Intent(Intent.ACTION_VIEW, uri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        }
        try {
            startActivity(goToMarket)
        } catch (_: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, DATA.mySite.toUri()))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            val message = if ((grantResults.isNotEmpty()) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getString(R.string.permission_granted)
            } else {
                getString(R.string.permission_denied)
            }
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}