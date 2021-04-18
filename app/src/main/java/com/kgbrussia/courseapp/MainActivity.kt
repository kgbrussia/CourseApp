package com.kgbrussia.courseapp

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import java.lang.ref.WeakReference

private const val FRAGMENT_TAG = "FRAGMENT_TAG"
const val DIALOG_TAG = "DIALOG_TAG"

class MainActivity : AppCompatActivity(),
    ContactListFragment.OnContactClickedListener,
    ContactPermissionDialog.PermissionDialogDisplayer,
    ContactService.ServiceInterface {

    private var contactService: ContactService? = null
    private var contactPermissionDialog: ContactPermissionDialog? = null
    private var bound: Boolean = false
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as ContactService.ContactBinder
            contactService = binder.getService()
            bound = true
            val isStartCheckPermission: Boolean = intent.extras?.getBoolean(CONTACT_PERMISSION) ?: true
            checkPermission(isStartCheckPermission)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            bound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val startedFromNotification: Boolean = intent.extras?.containsKey(ID_ARG) ?: false
        if(savedInstanceState == null){
            initStartFragment()
            if(startedFromNotification)
                startContactDetailsFromNotification(intent)
        }
        initContactService()

        val isStartCheckPermission: Boolean = intent.extras?.getBoolean(CONTACT_PERMISSION) ?: true
        if(!isStartCheckPermission && savedInstanceState == null) {
            replaceStartFragment(isStartCheckPermission)
            val id = requireNotNull(intent?.extras?.getString(ID_ARG))
            onContactClickedWithPop(id)
        } else if(savedInstanceState == null) {
            replaceStartFragment(false)
        }
    }

    fun initContactService(){
        Intent(this, ContactService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    fun initStartFragment(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, ContactListFragment.newInstance())
            .commit()
    }

    fun replaceStartFragment(isStartCheckPermission: Boolean) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ContactListFragment.newInstance(isStartCheckPermission), FRAGMENT_TAG)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (bound) {
            unbindService(connection)
            bound = false
        }
    }

    private fun startContactDetailsFromNotification(intent: Intent?) {
        val fragmentManager = supportFragmentManager
        if(fragmentManager.backStackEntryCount==1) {
            fragmentManager.popBackStack()
        }
        val id: String = requireNotNull(intent?.extras?.getString(ID_ARG))
        onContactClicked(id)
    }

    override fun getService(): ContactService? = contactService

    override fun onContactClicked(id: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, ContactDetailsFragment.newInstance(id))
            .addToBackStack(null)
            .commit()
    }

    private fun onContactClickedWithPop(id: String) {
        val fragmentManager = supportFragmentManager
        if(fragmentManager.backStackEntryCount==1) {
            fragmentManager.popBackStack()
        }
        onContactClicked(id)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if(contactPermissionDialog?.isAdded == true) {
            contactPermissionDialog?.dismissAllowingStateLoss()
        }
        super.onSaveInstanceState(outState)
    }

    override fun displayPermissionDialog(message: Int) {
        if(contactPermissionDialog == null) {
            contactPermissionDialog = ContactPermissionDialog.newInstance(message)
        }
        if(contactPermissionDialog?.isAdded == false) {
            contactPermissionDialog?.show(supportFragmentManager, DIALOG_TAG)
        }
    }

    fun checkPermission() {
        val weakReferenceFragment = WeakReference(supportFragmentManager.findFragmentByTag(FRAGMENT_TAG))
        when (val fragment = weakReferenceFragment.get()) {
            is ContactListFragment -> fragment.requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            is ContactDetailsFragment -> fragment.requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    fun checkPermission(isContactPermissionGranted: Boolean) {
        val weakReferenceFragment = WeakReference(supportFragmentManager.findFragmentByTag(FRAGMENT_TAG))
        when (val fragment = weakReferenceFragment.get()) {
            is ContactListFragment -> {
                supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ContactListFragment.newInstance(isContactPermissionGranted), FRAGMENT_TAG)
                .commit()
            }
            is ContactDetailsFragment -> onContactClickedWithPop(fragment.id.toString())
        }
    }
}