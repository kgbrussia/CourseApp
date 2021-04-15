package com.kgbrussia.courseapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder

class MainActivity : AppCompatActivity(),
    ContactListFragment.OnContactClickedListener,
    ContactService.ServiceInterface {

    private var contactService: ContactService? = null
    private var bound: Boolean = false
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as ContactService.ContactBinder
            contactService = binder.getService()
            bound = true
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
}