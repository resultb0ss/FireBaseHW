package com.example.firebasehw

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private var mailList: MutableList<Mail>):
    RecyclerView.Adapter<CustomAdapter.MailViewHolder>() {

    class MailViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nameTv: TextView = itemView.findViewById(R.id.mailNameFromTV)
        val mailTv: TextView = itemView.findViewById(R.id.mailTextTV)
    }

    override fun getItemCount(): Int {
        return mailList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MailViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_recycler_view, parent, false)

        return MailViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MailViewHolder, position: Int) {
        val mail = mailList[position]
        holder.nameTv.text = mail.nameFrom
        holder.mailTv.text = mail.mail
    }
}