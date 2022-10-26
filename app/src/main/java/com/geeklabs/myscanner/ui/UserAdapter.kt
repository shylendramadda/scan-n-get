package com.geeklabs.myscanner.ui

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.geeklabs.myscanner.R
import com.geeklabs.myscanner.models.User
import com.geeklabs.myscanner.utils.AppUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_user.view.*


class UserAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mList = mutableListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) holder.bindItems(mList[position], position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindItems(user: User, position: Int) = with(itemView) {
            tvSn.text = "${position + 1}."
            var content = ""
            val splits = user.rawData.split(";")
            splits.forEach {
                if (it.trim().isNotEmpty()) {
                    content += it.split(":")[1] + ", "
                }
            }
            content = content.removeSuffix(", ")
            tvData.text = content
            tvTime.text = AppUtils.dateFormat1(user.createdTime)
            ivCopy.setOnClickListener {
                val userInfo = Gson().toJson(user, User::class.java)
                val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Info", userInfo)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        }
    }
}