package com.practice.branchinternapp.presentation.adapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.practice.branchinternapp.R
import com.practice.branchinternapp.domain.data.MessageListResponse
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ParticularMessageAdapter(
    private val listOfMessage: ArrayList<MessageListResponse>,
    private val navController: NavController,
    private val authToken: String
) : RecyclerView.Adapter<ParticularMessageAdapter.ParticularMessageViewHolder>() {
    inner class ParticularMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var msgId: TextView = itemView.findViewById<TextView>(R.id.message_id)
        var msgBody: TextView = itemView.findViewById<TextView>(R.id.message_body)
        var msgTime: TextView = itemView.findViewById<TextView>(R.id.messgae_time)
        var layoutView: RelativeLayout = itemView.findViewById<RelativeLayout>(R.id.item_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticularMessageViewHolder {
        return ParticularMessageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_message, parent, false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ParticularMessageViewHolder, position: Int) {
        val itemofmsg = listOfMessage[position]
        holder.msgBody.text = itemofmsg.body;
        val dateTime: ZonedDateTime = OffsetDateTime.parse(itemofmsg.timestamp).toZonedDateTime()

        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
        Log.e("Date 1", dateTime.format(formatter))
        holder.msgTime.text = dateTime.format(formatter)

        if (itemofmsg.agent_id == null) {
            holder.msgId.text = "ID: " + itemofmsg.user_id.toString() + " (User)";
        } else {
            holder.msgId.text = "ID: " + itemofmsg.agent_id.toString() + " (Agent)";
            holder.layoutView.setBackgroundResource(R.drawable.rounded_search_box_sender)
        }
        holder.itemView.setOnClickListener {
            //go to detailed screen
            val bundle = bundleOf(
                "thread_id" to itemofmsg.thread_id,
                "auth_token" to authToken
            )
            navController.navigate(R.id.action_listOfMessageFragment_to_messageFragment, bundle)

        }
    }

    override fun getItemCount(): Int {
        Log.i("sizeoflist", listOfMessage.size.toString())
        return listOfMessage.size
    }
}