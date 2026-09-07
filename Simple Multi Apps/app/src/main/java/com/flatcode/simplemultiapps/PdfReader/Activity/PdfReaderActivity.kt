package com.flatcode.simplemultiapps.pdfreader.activity

import android.Manifest
import android.app.Activity
import android.app.ActivityManager.TaskDescription
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.print.PrintManager
import android.provider.OpenableColumns
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.os.BundleCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.databinding.ActivityPdfReaderBinding
import com.flatcode.simplemultiapps.databinding.DialogPdfReaderPasswordBinding
import com.flatcode.simplemultiapps.pdfreader.DownloadPDFFile
import com.flatcode.simplemultiapps.pdfreader.adapter.PdfDocumentAdapter
import com.flatcode.simplemultiapps.pdfreader.viewmodel.PdfViewModel
import com.flatcode.simplemultiapps.utils.DATA
import com.flatcode.simplemultiapps.utils.canWriteToDownloadFolder
import com.flatcode.simplemultiapps.utils.createFileShareIntent
import com.flatcode.simplemultiapps.utils.createPlainTextShareIntent
import com.flatcode.simplemultiapps.utils.intent1
import com.flatcode.simplemultiapps.utils.writeBytesToFile
import com.github.barteksc.pdfviewer.PDFView.Configurator
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.github.barteksc.pdfviewer.util.Constants
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.shockwave.pdfium.PdfPasswordException
import java.io.FileNotFoundException
import java.io.IOException
import java.util.Locale
import kotlin.system.exitProcess

class PdfReaderActivity : AppCompatActivity() {

    private val activity: Activity = this@PdfReaderActivity
    private var mgr: PrintManager? = null
    private var prefManager: SharedPreferences? = null
    private var uri: Uri? = null
    private var pageNumber = 0
    private var pdfPassword: String? = null
    private var pdfFileName: String? = ""
    private var downloadedPdfFileContent: ByteArray? = null
    private var isBottomNavigationHidden = false
    private var isFullscreenToggled = false
    private var viewBinding: ActivityPdfReaderBinding? = null

    private val documentPickerLauncher = registerForActivityResult(
        OpenDocument(),
    ) { selectedDocumentUri: Uri? -> openSelectedDocument(selectedDocumentUri) }
    private val saveToDownloadPermissionLauncher = registerForActivityResult(
        RequestPermission(),
    ) { isPermissionGranted: Boolean ->
        saveDownloadedFileAfterPermissionRequest(isPermissionGranted)
    }
    private val readFileErrorPermissionLauncher = registerForActivityResult(
        RequestPermission(),
    ) { isPermissionGranted: Boolean ->
        restartAppIfGranted(isPermissionGranted)
    }

    private val viewModel: PdfViewModel by lazy {
        ViewModelProvider(this)[PdfViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPdfReaderBinding.inflate(layoutInflater)
        setContentView(viewBinding!!.root)

        ViewCompat.setOnApplyWindowInsetsListener(viewBinding!!.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        Constants.THUMBNAIL_RATIO = 1f
        setBottomBarListeners()
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        prefManager = PreferenceManager.getDefaultSharedPreferences(this)
        mgr = getSystemService(PRINT_SERVICE) as PrintManager

        onFirstInstall()
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState)
        } else {
            uri = intent.data
            if (uri == null) pickFile()
        }
        displayFromUri(uri)
    }

    public override fun onResume() {
        super.onResume()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (prefManager!!.getBoolean(DATA.SCREEN_ON_PREF, false)) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    private fun onFirstInstall() {
        val isFirstRun = prefManager!!.getBoolean(DATA.FIRST_INSTALL, true)
        if (isFirstRun) {
            activity.intent1(PdfReaderIntroActivity::class.java)
            prefManager!!.edit {
                putBoolean(DATA.FIRST_INSTALL, false)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(DATA.URI, uri)
        outState.putInt(DATA.PAGE_NUMBER, pageNumber)
        outState.putString(DATA.PDF_PASSWORD, pdfPassword)
        super.onSaveInstanceState(outState)
    }

    private fun restoreInstanceState(savedState: Bundle) {
        uri = BundleCompat.getParcelable(savedState, DATA.URI, Uri::class.java)
        pageNumber = savedState.getInt(DATA.PAGE_NUMBER)
        pdfPassword = savedState.getString(DATA.PDF_PASSWORD)
    }


    fun shareFile() {
        val sharingIntent: Intent = if ((uri!!.scheme != null) && uri!!.scheme!!.startsWith(DATA.HTTP)) {
            createPlainTextShareIntent(getString(R.string.share_file), uri.toString())
        } else {
            createFileShareIntent(getString(R.string.share_file), pdfFileName, uri)
        }
        startActivity(sharingIntent)
    }

    private fun openSelectedDocument(selectedDocumentUri: Uri?) {
        if (selectedDocumentUri == null) {
            return
        }
        if (uri == null || selectedDocumentUri == uri) {
            uri = selectedDocumentUri
            displayFromUri(uri)
        } else {
            val intent = Intent(activity, javaClass)
            intent.data = selectedDocumentUri
            startActivity(intent)
        }
    }

    private fun pickFile() {
        try {
            documentPickerLauncher.launch(arrayOf(DATA.APPLICATION_PDF))
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(activity, R.string.toast_pick_file_error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setBottomBarListeners() {
        viewBinding!!.bottomNavigation.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.pickFile -> pickFile()
                R.id.metaFile -> if (uri != null) showPdfMetaDialog()
                R.id.shareFile -> if (uri != null) shareFile()
                R.id.printFile -> if (uri != null) printDocument()
                R.id.fullscreen -> {
                    toggleFullscreen()
                    return@setOnItemSelectedListener true
                }

                else -> {}
            }
            false
        }
    }


    fun configurePdfViewAndLoad(viewConfigurator: Configurator) {
        if (!prefManager!!.getBoolean(DATA.PDF_THEME_PREF, false)) {
            viewBinding!!.pdfView.setBackgroundColor(
                ContextCompat.getColor(this, R.color.gray_light)
            )
        } else {
            viewBinding!!.pdfView.setBackgroundColor(
                ContextCompat.getColor(this, R.color.gray_pdf)
            )
        }
        viewBinding!!.pdfView.useBestQuality(prefManager!!.getBoolean(DATA.QUALITY_PREF, false))
        viewBinding!!.pdfView.minZoom = 0.5f
        viewBinding!!.pdfView.midZoom = 2.0f
        viewBinding!!.pdfView.maxZoom = 5.0f
        viewConfigurator
            .defaultPage(pageNumber)
            .onPageChange { page: Int, pageCount: Int -> setCurrentPage(page, pageCount) }
            .enableAnnotationRendering(true)
            .enableAntialiasing(prefManager!!.getBoolean(DATA.ALIAS_PREF, true))
            .onTap { _: MotionEvent -> toggleBottomNavigationVisibility() }
            .onPageScroll { _: Int, positionOffset: Float ->
                toggleBottomNavigationAccordingToPosition(positionOffset)
            }
            .scrollHandle(DefaultScrollHandle(activity))
            .spacing(10) // in dp
            .onError { exception: Throwable -> handleFileOpeningError(exception) }
            .onPageError { page: Int, err: Throwable? -> Log.e(TAG, "Cannot load page $page", err) }
            .pageFitPolicy(FitPolicy.WIDTH)
            .password(pdfPassword)
            .swipeHorizontal(prefManager!!.getBoolean(DATA.SCROLL_PREF, false))
            .autoSpacing(prefManager!!.getBoolean(DATA.SCROLL_PREF, false))
            .pageSnap(prefManager!!.getBoolean(DATA.SNAP_PREF, false))
            .pageFling(prefManager!!.getBoolean(DATA.FLING_PREF, false))
            .nightMode(prefManager!!.getBoolean(DATA.PDF_THEME_PREF, false))
            .load()
    }

    private fun handleFileOpeningError(exception: Throwable) {
        if (exception is PdfPasswordException) {
            if (pdfPassword != null) {
                Toast.makeText(activity, R.string.wrong_password, Toast.LENGTH_SHORT).show()
                pdfPassword = null
            }
            askForPdfPassword()
        } else if (couldNotOpenFileDueToMissingPermission(exception)) {
            readFileErrorPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            Toast.makeText(activity, R.string.file_opening_error, Toast.LENGTH_LONG).show()
            Log.e(TAG, "Error when opening file", exception)
        }
    }

    private fun couldNotOpenFileDueToMissingPermission(e: Throwable): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val exceptionMessage = e.message
            return e is FileNotFoundException && exceptionMessage != null && exceptionMessage.contains(
                DATA.PERMISSION_DENIED
            )
        }

        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }

        val exceptionMessage = e.message
        return e is FileNotFoundException && exceptionMessage != null && exceptionMessage.contains(DATA.PERMISSION_DENIED)
    }

    private fun restartAppIfGranted(isPermissionGranted: Boolean) {
        if (isPermissionGranted) {
            exitProcess(0)
        } else {
            Toast.makeText(activity, R.string.file_opening_error, Toast.LENGTH_LONG).show()
        }
    }

    private fun toggleBottomNavigationAccordingToPosition(positionOffset: Float) {
        if (positionOffset == 0f) {
            showBottomNavigationView()
        } else if (!isBottomNavigationHidden) {
            hideBottomNavigationView()
        }
    }

    private fun toggleBottomNavigationVisibility(): Boolean {
        if (isBottomNavigationHidden) {
            showBottomNavigationView()
        } else {
            hideBottomNavigationView()
        }
        return true
    }

    private fun hideBottomNavigationView() {
        isBottomNavigationHidden = true
        viewBinding!!.bottomNavigation.animate()
            .translationY(viewBinding!!.bottomNavigation.height.toFloat()).duration = 100
    }

    private fun showBottomNavigationView() {
        isBottomNavigationHidden = false
        viewBinding!!.bottomNavigation.animate()
            .translationY(0f).duration = 100
    }

    private fun toggleFullscreen() {
        if (!isFullscreenToggled) {
            isFullscreenToggled = true

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.let { controller ->
                    controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                    controller.systemBarsBehavior =
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }
        } else {
            isFullscreenToggled = false

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            }
        }
    }

    fun displayFromUri(uri: Uri?) {
        if (uri == null) {
            title = ""
            return
        }
        pdfFileName = getFileName(uri)
        title = pdfFileName

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val taskDescription = TaskDescription.Builder()
                .setLabel(pdfFileName)
                .build()
            setTaskDescription(taskDescription)
        }

        val scheme = uri.scheme
        if (scheme != null && scheme.contains(DATA.HTTP)) {
            downloadOrShowDownloadedFile(uri)
        } else {
            configurePdfViewAndLoad(viewBinding!!.pdfView.fromUri(uri))
        }
    }

    private fun downloadOrShowDownloadedFile(uri: Uri) {
        val cachedBytes = viewModel.downloadedPdfFileContent

        if (cachedBytes != null) {
            configurePdfViewAndLoad(viewBinding!!.pdfView.fromBytes(cachedBytes))
        } else {
            viewBinding!!.progressBar.visibility = View.VISIBLE
            val downloadPDFFile = DownloadPDFFile(this)
            downloadPDFFile.execute(uri.toString())
        }
    }

    fun hideProgressBar() {
        viewBinding!!.progressBar.visibility = View.GONE
    }

    fun saveToFileAndDisplay(pdfFileContent: ByteArray?) {
        viewModel.downloadedPdfFileContent = pdfFileContent
        saveToDownloadFolderIfAllowed(pdfFileContent)
        configurePdfViewAndLoad(viewBinding!!.pdfView.fromBytes(pdfFileContent))
    }

    private fun saveToDownloadFolderIfAllowed(fileContent: ByteArray?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q || activity.canWriteToDownloadFolder()) {
            trySaveToDownloadFolder(fileContent, showSuccessMessage = false)
        } else {
            saveToDownloadPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun trySaveToDownloadFolder(fileContent: ByteArray?, showSuccessMessage: Boolean) {
        try {
            val downloadDirectory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            downloadDirectory.writeBytesToFile(pdfFileName, fileContent)
            if (showSuccessMessage) {
                Toast.makeText(activity, R.string.saved_to_download, Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error while saving file to download folder", e)
            Toast.makeText(activity, R.string.save_to_download_failed, Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveDownloadedFileAfterPermissionRequest(isPermissionGranted: Boolean) {
        if (isPermissionGranted) {
            trySaveToDownloadFolder(downloadedPdfFileContent, true)
        } else {
            Toast.makeText(activity, R.string.save_to_download_failed, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setCurrentPage(page: Int, pageCount: Int) {
        pageNumber = page
        title = String.format(Locale.US, "%s %s / %s", "$pdfFileName ", page + 1, pageCount)
    }

    fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme != null && uri.scheme == DATA.SCHEME_CONTENT) {
            try {
                contentResolver.query(uri, null, null, null, null).use { cursor ->
                    if (cursor != null && cursor.moveToFirst()) {
                        val indexDisplayName = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        if (indexDisplayName != -1) {
                            result = cursor.getString(indexDisplayName)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "Couldn't retrieve file name", e)
            }
        }
        if (result == null) {
            result = uri.lastPathSegment
        }
        return result
    }

    private fun printDocument() {
        mgr!!.print(pdfFileName!!, PdfDocumentAdapter(activity, uri!!), null)
    }

    fun askForPdfPassword() {
        val dialogBinding = DialogPdfReaderPasswordBinding.inflate(layoutInflater)
        val alert = AlertDialog.Builder(activity)
            .setTitle(R.string.protected_pdf)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.ok) { _: DialogInterface?, _: Int ->
                pdfPassword = dialogBinding.passwordInput.text.toString()
                displayFromUri(uri)
            }
            .setIcon(R.drawable.lock_icon)
            .create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }

    fun showPdfMetaDialog() {
        val meta = viewBinding!!.pdfView.documentMeta
        if (meta != null) {
            val dialogArgs = Bundle()
            dialogArgs.putString(PdfMetaDialog.TITLE_ARGUMENT, meta.title)
            dialogArgs.putString(PdfMetaDialog.AUTHOR_ARGUMENT, meta.author)
            dialogArgs.putString(PdfMetaDialog.CREATION_DATE_ARGUMENT, meta.creationDate)
            val dialog: DialogFragment = PdfMetaDialog()
            dialog.arguments = dialogArgs
            dialog.show(supportFragmentManager, DATA.META_DIALOG)
        }
    }

    class PdfMetaDialog : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(requireContext())
            return builder.setTitle(R.string.meta)
                .setMessage(
                    """
    ${getString(R.string.pdf_title, requireArguments().getString(TITLE_ARGUMENT))}
    ${getString(R.string.pdf_author, requireArguments().getString(AUTHOR_ARGUMENT))}
    ${getString(R.string.pdf_creation_date, requireArguments().getString(CREATION_DATE_ARGUMENT))}
    """.trimIndent()
                )
                .setPositiveButton(R.string.ok) { _: DialogInterface?, _: Int -> }
                .setIcon(R.drawable.info_icon)
                .create()
        }

        companion object {
            const val TITLE_ARGUMENT = DATA.TITLE
            const val AUTHOR_ARGUMENT = DATA.AUTHOR
            const val CREATION_DATE_ARGUMENT = DATA.PUBLISHED
        }
    }

    companion object {
        private val TAG = PdfReaderActivity::class.java.simpleName
    }
}
