package edu.put.myapplication
import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TimesAdapter(private val context: Context, private val cursor: Cursor?) : RecyclerView.Adapter<TimesAdapter.TimesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return TimesViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimesViewHolder, position: Int) {
        if (cursor != null && cursor.moveToPosition(position)) {
            val timeIndex = cursor.getColumnIndex(DBHelper.TIME_COL)
            val dateIndex = cursor.getColumnIndex(DBHelper.DATE_COL)

            // Check if column indexes are valid
            if (timeIndex != -1 && dateIndex != -1) {
                val time = cursor.getString(timeIndex)
                val date = cursor.getString(dateIndex)
                holder.bind(date, time)
            }
        }
    }

    override fun getItemCount(): Int {
        return cursor?.count ?: 0
    }

    inner class TimesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val timeTextView: TextView = itemView.findViewById(android.R.id.text1)
        private var layoutParams = timeTextView.layoutParams as RecyclerView.LayoutParams

        fun bind(date:String, time: String) {
            val text = "$date - $time"
            timeTextView.text = text
            layoutParams.topMargin = context.resources.getDimensionPixelSize(R.dimen.recycler_list_margin)
            layoutParams.bottomMargin = context.resources.getDimensionPixelSize(R.dimen.recycler_list_margin)
        }
    }
}

