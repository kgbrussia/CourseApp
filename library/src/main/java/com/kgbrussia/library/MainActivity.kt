package com.kgbrussia.library

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kgbrussia.courseapp.library.R
import com.kgbrussia.library.fragments.*
import java.lang.ref.WeakReference

private const val FRAGMENT_TAG = "FRAGMENT_TAG"
const val DIALOG_TAG = "DIALOG_TAG"

class MainActivity : AppCompatActivity(),
    ItemClickListener,
    ContactPermissionDialog.PermissionDialogDisplayer {

    private var contactPermissionDialog: ContactPermissionDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val startedFromNotification: Boolean = intent.extras?.containsKey(ID_ARG) ?: false
        val isStartCheckPermission: Boolean = intent.extras?.getBoolean(CONTACT_PERMISSION) ?: true
        if(savedInstanceState == null){
            replaceStartFragment(isStartCheckPermission)
            if(startedFromNotification)
                startContactDetailsFromNotification(intent)
        }

        if(!isStartCheckPermission && savedInstanceState == null) {
            replaceStartFragment(isStartCheckPermission)
            val id = requireNotNull(intent?.extras?.getString(ID_ARG))
            onContactClickedWithPop(id)
        } else if(savedInstanceState == null) {
            replaceStartFragment(false)
        }
    }

    override fun displayPermissionDialog(message: Int) {
        if(contactPermissionDialog == null) {
            contactPermissionDialog = ContactPermissionDialog.newInstance(message)
        }
        if(contactPermissionDialog?.isAdded == false) {
            contactPermissionDialog?.show(supportFragmentManager, DIALOG_TAG)
        }
    }

    override fun onItemClicked(id: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, ContactDetailsFragment.newInstance(id))
            .addToBackStack(null)
            .commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if(contactPermissionDialog?.isAdded == true) {
            contactPermissionDialog?.dismissAllowingStateLoss()
        }
        super.onSaveInstanceState(outState)
    }

    fun checkPermission() {
        val weakReferenceFragment = WeakReference(supportFragmentManager.findFragmentByTag(FRAGMENT_TAG))
        when (val fragment = weakReferenceFragment.get()) {
            is ContactListFragment -> fragment.requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            is ContactDetailsFragment -> fragment.requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    private fun replaceStartFragment(isStartCheckPermission: Boolean) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ContactListFragment.newInstance(isStartCheckPermission), FRAGMENT_TAG)
            .commit()
    }

    private fun startContactDetailsFromNotification(intent: Intent?) {
        val fragmentManager = supportFragmentManager
        if(fragmentManager.backStackEntryCount==1) {
            fragmentManager.popBackStack()
        }
        val id: String = requireNotNull(intent?.extras?.getString(ID_ARG))
        onItemClicked(id)
    }

    private fun onContactClickedWithPop(id: String) {
        val fragmentManager = supportFragmentManager
        if(fragmentManager.backStackEntryCount==1) {
            fragmentManager.popBackStack()
        }
        onItemClicked(id)
    }
}