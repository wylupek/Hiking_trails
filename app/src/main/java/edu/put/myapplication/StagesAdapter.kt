package edu.put.myapplication
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StagesAdapter(private val stages: List<String>) : RecyclerView.Adapter<StagesAdapter.StagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StagesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return StagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: StagesViewHolder, position: Int) {
        val stage = stages[position]
        holder.bind(stage)
    }

    override fun getItemCount(): Int {
        return stages.size
    }

    inner class StagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val stageTextView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(stage: String) {
            stageTextView.text = stage
        }
    }
}