package com.kgbrussia.courseapp

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

const val TITLE_DIALOG = "TITLE_DIALOG"

class ContactPermissionDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = requireNotNull(arguments?.getInt(TITLE_DIALOG))
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
        fun newInstance(message: Int): ContactPermissionDialog {
            val fragment = ContactPermissionDialog()
            val args = Bundle()
            args.putInt(TITLE_DIALOG, message)
            fragment.arguments = args
            return fragment
        }
    }
}