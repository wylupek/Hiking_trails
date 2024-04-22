package edu.put.myapplication;

import androidx.recyclerview.widget.RecyclerView
import edu.put.myapplication.databinding.CardCellBinding;

class CardViewHolder(
    private val cardCellBinding: CardCellBinding,
    private val clickListener: TrailClickListener
) : RecyclerView.ViewHolder(cardCellBinding.root) {
    fun bindTrail(trail: Trail) {
        cardCellBinding.trailImg.setImageResource(trail.imgId)
        cardCellBinding.trailName.text = trail.name

        cardCellBinding.cardView.setOnClickListener {
            clickListener.onClick(trail)
        }
    }
}
