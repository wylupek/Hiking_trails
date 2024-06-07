package edu.put.myapplication
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StagesAdapter(private val stages: List<String>,
                    private val distances: List<Int>) : RecyclerView.Adapter<StagesAdapter.StagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StagesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return StagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: StagesViewHolder, position: Int) {
        holder.bind(stages[position], distances[position], holder.itemView.context)
    }

    override fun getItemCount(): Int {
        return stages.size
    }

    inner class StagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val stageTextView: TextView = itemView.findViewById(android.R.id.text1)
        private var layoutParams = stageTextView.layoutParams as RecyclerView.LayoutParams

        fun bind(stage: String, distance: Int, context: Context) {
            var time = 0
            when (PACE) {
                "Slow" -> {
                    time = distance / 50
                }
                "Average" -> {
                    time = distance / 75
                }
                "Fast" -> {
                    time = distance / 100
                }
            }

            val text = "$time min - $stage"
            stageTextView.text = text
            layoutParams.topMargin = context.resources.getDimensionPixelSize(R.dimen.recycler_list_margin)
            layoutParams.bottomMargin = context.resources.getDimensionPixelSize(R.dimen.recycler_list_margin)

            stageTextView.layoutParams = layoutParams
        }
    }
}