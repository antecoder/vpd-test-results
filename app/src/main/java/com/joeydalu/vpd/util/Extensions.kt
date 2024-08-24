package com.joeydalu.vpd.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar

/**
 * @return *true* if this string is a valid email address.
 */
fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

/**
 * Casts a live-data to mutable form.
 * NOTE: This is nullable
 */
fun <T> LiveData<T>.mutable(): MutableLiveData<T>? {
    return this as? MutableLiveData<T>
}

/**
 * Shows a snackbar message
 *
 * @param messageRes The resource-id of the message to show.
 */
fun Fragment.showSnackbar(messageRes: Int) {
    this.view?.let {
        Snackbar.make(it, messageRes, Snackbar.LENGTH_LONG).show()
    }
}

/**
 * Shows a snackbar message
 *
 * @param messageRes The resource-id of the message to show.
 */
fun AppCompatActivity.showSnackbar(messageRes: Int, view: View) {
    Snackbar.make(this, view, this.getText(messageRes), Snackbar.LENGTH_LONG).show()
}

/**
 * Shows the soft-keyboard
 */
fun View.showKeyboard() = ViewCompat.getWindowInsetsController(this)?.show(WindowInsetsCompat.Type.ime())

/**
 * Hides the soft-keyboard
 */
fun View.hideKeyboard() = ViewCompat.getWindowInsetsController(this)?.hide(WindowInsetsCompat.Type.ime())

/**
 * Shows the soft-keyboard
 */
fun Dialog.showKeyboard() = window?.decorView?.showKeyboard()

/**
 * Hides the soft-keyboard
 */
fun Dialog.hideKeyboard() = window?.decorView?.hideKeyboard()

/**
 * Shows the soft-keyboard
 */
fun Context.showKeyboard() = getActivity()?.showKeyboard()

/**
 * Hides the soft-keyboard
 */
fun Context.hideKeyboard() = getActivity()?.hideKeyboard()

/**
 * Shows the soft-keyboard
 */
fun Fragment.showKeyboard() = activity?.showKeyboard()

/**
 * Hides the soft-keyboard
 */
fun Fragment.hideKeyboard() = activity?.hideKeyboard()

/**
 * Shows the soft-keyboard
 */
fun Activity.showKeyboard() = WindowCompat.getInsetsController(window, window.decorView).show(WindowInsetsCompat.Type.ime())

/**
 * Hides the soft-keyboard
 */
fun Activity.hideKeyboard() = WindowCompat.getInsetsController(window, window.decorView).hide(WindowInsetsCompat.Type.ime())

fun Context.getActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> this.baseContext.getActivity()
        else -> null
    }
}