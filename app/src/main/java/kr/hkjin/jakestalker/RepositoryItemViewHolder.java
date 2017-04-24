package kr.hkjin.jakestalker;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by hkjin81 on 2017. 4. 25..
 */

class RepositoryItemViewHolder extends RecyclerView.ViewHolder {
    public ImageView image;
    public TextView title;
    public TextView description;
    public TextView starCount;

    public RepositoryItemViewHolder(View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.itemImage);
        title = (TextView) itemView.findViewById(R.id.title);
        description = (TextView) itemView.findViewById(R.id.description);
        starCount = (TextView) itemView.findViewById(R.id.starCount);
    }
}
