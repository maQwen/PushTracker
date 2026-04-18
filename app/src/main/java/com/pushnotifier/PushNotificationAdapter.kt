package com.pushnotifier

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pushnotifier.data.PushNotification
import java.text.SimpleDateFormat
import java.util.*

class PushNotificationAdapter(
    private val onDeleteClick: (PushNotification) -> Unit
) : ListAdapter<PushNotification, PushNotificationAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onDeleteClick)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textPackageName: TextView = itemView.findViewById(R.id.textPackageName)
        private val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        private val textMessage: TextView = itemView.findViewById(R.id.textMessage)
        private val textTimestamp: TextView = itemView.findViewById(R.id.textTimestamp)
        private val buttonDelete: TextView = itemView.findViewById(R.id.buttonDelete)

        fun bind(notification: PushNotification, onDeleteClick: (PushNotification) -> Unit) {
            textPackageName.text = notification.packageName
            textTitle.text = notification.title ?: "No Title"
            textMessage.text = notification.message ?: "No Message"
            textTimestamp.text = formatTimestamp(notification.timestamp)

            buttonDelete.setOnClickListener {
                onDeleteClick(notification)
            }
        }

        private fun formatTimestamp(timestamp: Long): String {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<PushNotification>() {
        override fun areItemsTheSame(oldItem: PushNotification, newItem: PushNotification): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PushNotification, newItem: PushNotification): Boolean {
            return oldItem == newItem
        }
    }
}
