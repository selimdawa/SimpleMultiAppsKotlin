package com.flatcode.simplemultiapps.WebApp

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.CLASS
import com.flatcode.simplemultiapps.Unit.DATA
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.Unit.VOID
import com.flatcode.simplemultiapps.databinding.ActivityWebAppBinding

class WebAppActivity : AppCompatActivity() {
    private var binding: ActivityWebAppBinding? = null
    private val context: Context = this@WebAppActivity
    var alertDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityWebAppBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        binding!!.toolbar.nameSpace.setText(R.string.web_app)
        if (ActivityCompat.checkSelfPermission(
                this@WebAppActivity, Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@WebAppActivity, arrayOf(Manifest.permission.CALL_PHONE), 1
            )
        }
        binding!!.webSite.setOnClickListener {
            VOID.IntentExtra(
                context, CLASS.WEB_VIEW, DATA.WEB_NAME, DATA.WEBSITE
            )
        }
        binding!!.instagram.setOnClickListener {
            VOID.IntentExtra(
                context, CLASS.WEB_VIEW, DATA.WEB_NAME, DATA.INSTAGRAM
            )
        }
        binding!!.twitter.setOnClickListener {
            VOID.IntentExtra(
                context, CLASS.WEB_VIEW, DATA.WEB_NAME, DATA.TWITTER
            )
        }
        binding!!.facebook.setOnClickListener {
            VOID.IntentExtra(
                context, CLASS.WEB_VIEW, DATA.WEB_NAME, DATA.FACEBOOK
            )
        }
        binding!!.aboutUs.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(context)
            val dialogView = LayoutInflater.from(context).inflate(R.layout.item_web_card, null)
            dialogBuilder.setView(dialogView)
            val about_app = dialogView.findViewById<RelativeLayout>(R.id.about_app)
            about_app.visibility = View.VISIBLE
            val contact = dialogView.findViewById<RelativeLayout>(R.id.contact)
            contact.visibility = View.GONE
            val about_us = dialogView.findViewById<TextView>(R.id.about_us)
            val close = dialogView.findViewById<TextView>(R.id.close)

            alertDialog = dialogBuilder.create()
            close.setOnClickListener { alertDialog!!.dismiss() }
            about_us.visibility = View.VISIBLE
            about_us.text = DATA.aboutUs
            alertDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog!!.show()
        }
        binding!!.support.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(context)
            val dialogView = LayoutInflater.from(context).inflate(R.layout.item_web_card, null)
            dialogBuilder.setView(dialogView)
            val about_app = dialogView.findViewById<RelativeLayout>(R.id.about_app)
            about_app.visibility = View.GONE
            val contact = dialogView.findViewById<RelativeLayout>(R.id.contact)
            contact.visibility = View.VISIBLE
            val about_us = dialogView.findViewById<TextView>(R.id.about_us)
            about_us.visibility = View.GONE
            val email = dialogView.findViewById<ImageView>(R.id.email)
            val phone = dialogView.findViewById<ImageView>(R.id.phone)
            alertDialog = dialogBuilder.create()
            val close = dialogView.findViewById<TextView>(R.id.close)

            close.setOnClickListener { alertDialog!!.dismiss() }
            alertDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            email.setOnClickListener {
                val emailSelectorIntent = Intent(Intent.ACTION_SENDTO)
                emailSelectorIntent.data = Uri.parse("mailto:")
                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(DATA.myEmail))
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                emailIntent.selector = emailSelectorIntent
                startActivity(emailIntent)
            }
            phone.setOnClickListener {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:" + DATA.myMobileNumber)
                startActivity(callIntent)
            }
            alertDialog!!.show()
        }
        binding!!.shareApp.setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
            share.putExtra(
                Intent.EXTRA_TEXT, """
     Share App with
     https://play.google.com/store/apps/details?id=${context.packageName}
     """.trimIndent()
            )
            startActivity(Intent.createChooser(share, "Share link!"))
        }
        binding!!.rateApp.setOnClickListener {
            val uri = Uri.parse("market://details?id=" + context.packageName)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + context.packageName)
                    )
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT)
                        .show()
                }
                return
            }
        }
    }

    companion object {
        var instance: WebAppActivity? = null
    }

    init {
        instance = this@WebAppActivity
    }
}