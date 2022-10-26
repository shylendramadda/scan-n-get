package com.geeklabs.myscanner.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.geeklabs.myscanner.App
import com.geeklabs.myscanner.R
import com.geeklabs.myscanner.extensions.applySchedulers
import com.geeklabs.myscanner.extensions.toast
import com.geeklabs.myscanner.extensions.visible
import com.geeklabs.myscanner.models.User
import com.geeklabs.myscanner.usecase.UserUseCase
import com.geeklabs.myscanner.utils.AlertButtonClickListener
import com.geeklabs.myscanner.utils.AppUtils
import com.google.zxing.integration.android.IntentIntegrator
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import javax.inject.Inject


class MainActivity : AppCompatActivity(), AlertButtonClickListener {

    @Inject
    lateinit var userUseCase: UserUseCase
    lateinit var userAdapter: UserAdapter
    private val disposables = CompositeDisposable()
    private var alertDialog: AlertDialog? = null
    private var contents = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as App).applicationComponent.inject(this)
        userAdapter = UserAdapter()
        rcvUsers.adapter = userAdapter
        fbScanner.setOnClickListener {
            launchBarCodeScanner()
        }
        loadUsers()
    }

    @Suppress("DEPRECATION")
    private fun loadUsers() {
        disposables.add(
            userUseCase.getAll()
                .applySchedulers()
                .doOnSubscribe { progressBar.visible = true }
                .doFinally { progressBar.visible = false }
                .subscribe({
                    userAdapter.mList = it
                    userAdapter.notifyItemRangeChanged(0, it.size)
                    tvNoData.visible = it.isEmpty()
                }, {
                    toast(getString(R.string.something_went_wrong))
                })
        )
    }

    private fun launchBarCodeScanner() {
        try {
            val intentIntegrator = IntentIntegrator(this)
            intentIntegrator.setPrompt("Scan a barcode or QR Code")
            intentIntegrator.setOrientationLocked(false)
            intentIntegrator.setBeepEnabled(false)
            intentIntegrator.initiateScan()
        } catch (e: Exception) {
            toast(getString(R.string.something_went_wrong))
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult != null) {
            val contents = intentResult.contents
            if (contents == null) {
                toast("Cancelled")
            } else {
                this.contents = contents
                println("User:$contents")
                showAlert("Registration Info:", contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun showAlert(title: String, msg: String) {
        alertDialog = AppUtils.showAlertWithActions(
            this, title,
            msg, this, "Add", "Cancel"
        )
    }

    override fun onPositiveClicked() {
        saveInfo()
        alertDialog?.dismiss()
        alertDialog = null
    }

    override fun onNegativeClicked() {
        contents = ""
        alertDialog?.dismiss()
        alertDialog = null
    }

    private fun saveInfo() {
        if (contents.isEmpty()) {
            toast("Content should not be empty")
            return
        }
        val user = User()
        user.rawData = contents
        user.createdTime = System.currentTimeMillis()
        disposables.add(
            userUseCase.execute(user)
                .applySchedulers()
                .subscribe({
                    loadUsers()
                }, {
                    toast(getString(R.string.something_went_wrong))
                })
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_export -> copyDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun copyDb() {
        val dbSource = getDatabasePath("ScanNGet.db")
        val rootPath = externalCacheDir
        val root = File(rootPath?.absolutePath + "/temp/")
        val dbDestination = File(root, "ScanNGet.db")
        try {
            copy(dbSource, dbDestination)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    fun copy(sourceFile: File, destFile: File) {
        if (sourceFile.absolutePath.equals(
                destFile.absolutePath,
                ignoreCase = true
            )
        ) return
        if (!destFile.exists()) {
            destFile.parentFile?.mkdirs()
            destFile.createNewFile()
        }
        val source: FileChannel? = null
        val destination: FileChannel? = null
        var fis: FileInputStream? = null
        var fos: FileOutputStream? = null
        try { //	        source = (fis = new FileInputStream(sourceFile)).getChannel();
//	        destination = (fos = new FileOutputStream(destFile)).getChannel();
//
//	        // previous code: destination.transferFrom(source, 0, source.size());
//	        // to avoid infinite loops, should be:
//	        long count = 0;
//	        long size = source.size();
//	        while((count += destination.transferFrom(source, count, size-count))<size);
            fis = FileInputStream(sourceFile)
            fos = FileOutputStream(destFile)
            val buffer = ByteArray(1024 * 1024) // 1MB
            var n: Int
            while (-1 != fis.read(buffer).also { n = it }) {
                fos.write(buffer, 0, n)
            }
        } finally {
            if (source != null) {
                try {
                    source.close()
                } catch (e: IOException) {
                }
            }
            if (destination != null) {
                try {
                    destination.close()
                } catch (e: IOException) {
                }
            }
            if (fis != null) {
                try {
                    fis.close()
                } catch (e: IOException) {
                }
            }
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                }
            }
        }
        startFileShareIntent(destFile.absolutePath)
    }

    private fun startFileShareIntent(filePath: String) { // pass the file path where the actual file is located.
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type =
                "*/*"  // "*/*" will accepts all types of files, if you want specific then change it on your need.
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            putExtra(
                Intent.EXTRA_SUBJECT,
                "Sharing file from the ScanNGet"
            )
            putExtra(
                Intent.EXTRA_TEXT,
                "Sharing file from the ScanNGet"
            )
            val fileURI = FileProvider.getUriForFile(
                this@MainActivity, this@MainActivity.packageName + ".provider",
                File(filePath)
            )
            putExtra(Intent.EXTRA_STREAM, fileURI)
        }
        startActivity(shareIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}