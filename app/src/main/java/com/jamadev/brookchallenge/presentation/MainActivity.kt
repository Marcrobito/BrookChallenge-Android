package com.jamadev.brookchallenge.presentation

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.jamadev.brookchallenge.R
import com.jamadev.brookchallenge.data.Repository
import com.jamadev.brookchallenge.base.Constants
import com.jamadev.brookchallenge.base.Constants.fitnessOptions


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var messageTitle:String
    private lateinit var messageBody:String
    private lateinit var positiveButtonText:String
    private lateinit var negativeButtonText:String
    private lateinit var account:GoogleSignInAccount
    private lateinit var repository: Repository

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        account = GoogleSignIn.getAccountForExtension(this, fitnessOptions)

        viewModel.hasGoogleFitPermission.observe(this){
            if(!it) showAlertDialog() else viewModel.permissionHasGranted()
        }

        messageTitle = resources.getString(R.string.permission_required)
        messageBody = resources.getString(R.string.permission_message)
        positiveButtonText = resources.getString(R.string.accept)
        negativeButtonText = resources.getString(R.string.do_it_later)

    }

    override fun onPause() {
        super.onPause()
        viewModel.setRepository(null)
        repository.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        repository  = Repository(this)
        viewModel.setRepository(repository)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
                viewModel.permissionHasGranted()
            }
        }
    }

    private fun showAlertDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(messageTitle)
        builder.setMessage(messageBody)
        builder.setPositiveButton(positiveButtonText) { _: DialogInterface, _: Int ->
            viewModel.userAskForPermission()
        }
        builder.setNegativeButton(negativeButtonText, null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }



}