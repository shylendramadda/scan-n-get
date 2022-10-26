package com.geeklabs.myscanner.extensions

import android.content.Context
import android.os.Handler
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

var View.visible
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

var View.canDisplay
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.INVISIBLE
    }

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, null)
}

fun EditText.onTextChanged(listener: (String) -> Unit) {

    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            val s = editable.toString()
            listener(s)
        }

        override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
    })
}

fun replaceInvalidCharacters(value: String, allowedChars: String): String {
    var finalValue = value.replace("\\p{So}+".toRegex(), "") // To remove emojis when copy paste
    if (finalValue.isNotEmpty()) {
        val lastChar = finalValue.last()
        if (!allowedChars.contains(lastChar, false)) {
            finalValue = finalValue.dropLast(1)
        }
    }
    return finalValue
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(rootView, InputMethodManager.SHOW_IMPLICIT)
}

fun withDelay(delayInMillis: Long, block: () -> Unit) {
    Handler().postDelayed(Runnable(block), delayInMillis)
}

fun EditText.filterEmojisAndDigits(allowedChars: String, maxLimit: Int = 250) {
    filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
        source.filter {
            Character.getType(it) != Character.SURROGATE.toInt() &&
                    Character.getType(it) != Character.OTHER_SYMBOL.toInt() &&
                    allowedChars.contains(it, false)
        }
    }, InputFilter.LengthFilter(maxLimit))
}

fun RecyclerView.autoFitColumns(columnWidth: Int) {
    val displayMetrics = this.context.resources.displayMetrics
    val noOfColumns = ((displayMetrics.widthPixels / displayMetrics.density) / columnWidth).toInt()
    this.layoutManager = GridLayoutManager(this.context, noOfColumns)
}