package com.kgbrussia.library

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.kgbrussia.courseapp.library.R

const val TITLE_DIALOG = "TITLE_DIALOG"

class ContactPermissionDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = requireNotNull(requireArguments().getInt(TITLE_DIALOG))
        return AlertDialog.Builder(activity)
            .setTitle(R.string.contactPermissionDialogTitle)
            .setMessage(message)
            .setPositiveButton(R.string.contactPermissionDialogButton) { _, _ -> (activity as? MainActivity)?.checkPermission() }
            .create()
    }

    override fun onDestroyView() {
        dismissAllowingStateLoss()
        super.onDestroyView()
    }

    interface PermissionDialogDisplayer {
        fun displayPermissionDialog(message: Int)
    }

    companion object {
        fun newInstance(message: Int) = ContactPermissionDialog().apply {
            arguments = bundleOf(TITLE_DIALOG to message)
        }
    }
}