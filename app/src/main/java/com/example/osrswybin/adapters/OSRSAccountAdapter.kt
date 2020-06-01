package com.example.osrswybin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.osrswybin.R
import com.example.osrswybin.models.Hiscores
import com.example.osrswybin.models.OSRSAccount
import kotlinx.android.synthetic.main.tracked_user.view.*

class OSRSAccountAdapter(private val osrsAccounts: List<OSRSAccount>, private val onClickListener: (OSRSAccount) -> Unit) : RecyclerView.Adapter<OSRSAccountAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.tracked_user, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return osrsAccounts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(osrsAccounts[position])

        holder.itemView.setOnClickListener {
            onClickListener.invoke(osrsAccounts[position])
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(osrsAccount: OSRSAccount) {
            itemView.tvLastUpdate.text = osrsAccount.username
            itemView.tvOverall.text = osrsAccount.getSkillByName("Overall").level.toString()
            itemView.tvCombat.text = Hiscores.calculateCombatLevel(osrsAccount).toString()
        }
    }
}