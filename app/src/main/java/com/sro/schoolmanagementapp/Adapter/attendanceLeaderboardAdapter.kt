package com.sro.schoolmanagementapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sro.schoolmanagementapp.Activity.MainActivity
import com.sro.schoolmanagementapp.Model.Attendance
import com.sro.schoolmanagementapp.R
import java.util.zip.Inflater

class attendanceLeaderboardAdapter(
    private val context: Context,
    private val source: String,
    private val list: List<Attendance>
) : RecyclerView.Adapter<attendanceLeaderboardAdapter.alviewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): alviewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_attendance_leaderboard, parent, false)
        return alviewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: alviewHolder, position: Int) {
        holder.studentName.text = list[position].studentName

        val daysCount = list[position].studentDates.count { it == ' ' }

        if (source == "attendanceLeaderboard") {
            holder.noofdays.text = daysCount.toString()
        }

        if (source == "classAttendance") {
            holder.noofdays.text = list[position].studentRoll
        }
    }

    class alviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var studentName = itemView.findViewById<TextView>(R.id.studentName)
        var noofdays = itemView.findViewById<TextView>(R.id.noofdays)

    }

}