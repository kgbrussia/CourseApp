package com.kgbrussia.library.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kgbrussia.courseapp.library.R
import com.kgbrussia.library.ContactPermissionDialog
import com.kgbrussia.library.contactdetails.CONTACT_PERMISSION
import com.kgbrussia.library.contactlist.ContactListViewModel
import com.kgbrussia.library.di.HasComponent
import com.kgbrussia.library.recyclers.ContactListAdapter
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class ContactListFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: ContactListViewModel
    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var adapter: ContactListAdapter? = null
    private var itemClickListener: ItemClickListener? = null
    private var isContactPermissionGranted = true
    private var displayer: ContactPermissionDialog.PermissionDialogDisplayer? = null
    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                loadContacts()
            } else {
                when {
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                        displayer?.displayPermissionDialog(R.string.contactPermissionDialogList)
                    }
                    else -> {
                        Toast.makeText(
                            context,
                            getString(R.string.noAccessGranted),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ItemClickListener) {
            itemClickListener = context
        }
        if (context is ContactPermissionDialog.PermissionDialogDisplayer) {
            displayer = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProgressBar()
        initRecyclerView()
        initViewModel()
        initSearchView()
    }

    override fun onStart() {
        super.onStart()
        isContactPermissionGranted = arguments?.getBoolean(CONTACT_PERMISSION) ?: true
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                loadContacts()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                displayer?.displayPermissionDialog(R.string.contactPermissionDialogDetails)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }

    override fun onDestroyView() {
        adapter = null
        recyclerView = null
        progressBar = null
        super.onDestroyView()
    }

    override fun onDetach() {
        super.onDetach()
        itemClickListener = null
        displayer = null
        requestPermissionLauncher.unregister()
    }

    private fun injectFragment() {
        (requireActivity().application as HasComponent)
            .getAppComponent()
            .contactListContainer()
            .inject(this)
        viewModel = ViewModelProvider(this, factory).get(ContactListViewModel::class.java)
    }

    private fun initProgressBar() {
        progressBar = requireView().findViewById(R.id.progress_bar_contact_list)
        viewModel.isLoadingIndicatorVisible.observe(
            viewLifecycleOwner,
            { isLoadingIndicatorVisible ->
                progressBar?.isVisible = isLoadingIndicatorVisible
            }
        )
    }

    private fun initViewModel() {
        viewModel.contacts.observe(viewLifecycleOwner, { adapter?.submitList(it) })
    }

    private fun initRecyclerView() {
        recyclerView = requireView().findViewById(R.id.contact_list_recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration =
            DividerItemDecoration(requireContext().applicationContext, RecyclerView.VERTICAL)
        val dividerDrawable: Drawable? =
            ContextCompat.getDrawable(requireContext(), R.drawable.contact_divider_drawable)
        if (dividerDrawable != null) dividerItemDecoration.setDrawable(dividerDrawable)
        recyclerView?.addItemDecoration(dividerItemDecoration)
        adapter = ContactListAdapter { id: String ->
            itemClickListener?.onItemClicked(id)
        }
        recyclerView?.adapter = adapter
    }

    private fun initSearchView() {
        val searchView = requireView().findViewById<SearchView>(R.id.contact_list_search_view)
        val publishSubject: PublishSubject<String> = PublishSubject.create()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    publishSubject.onNext(newText)
                }
                return false
            }
        })
        viewModel.searchQueryEntered(requireContext(), publishSubject)
    }

    private fun loadContacts() = viewModel.contactListLoaded(requireContext())

    companion object {
        fun newInstance(isContactPermissionGranted: Boolean): ContactListFragment {
            val args = Bundle()
            args.putBoolean(CONTACT_PERMISSION, isContactPermissionGranted)
            val fragment = ContactListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

interface ItemClickListener {
    fun onItemClicked(id: String)
}