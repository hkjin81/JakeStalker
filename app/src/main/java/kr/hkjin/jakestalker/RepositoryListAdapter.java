package kr.hkjin.jakestalker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hkjin81 on 2017. 4. 25..
 */

public class RepositoryListAdapter extends RecyclerView.Adapter {
    public static final int ITEMTYPE_NONE = 0;
    public static final int ITEMTYPE_ITEM = 1;
    public static final int ITEMTYPE_LAST = 2;

    private LayoutInflater mInflator;
    private List<RepositoryItem> items;
    private static final RoundedRectTransform transform = new RoundedRectTransform(16f, 16f);

    public RepositoryListAdapter(Context context) {
        mInflator = LayoutInflater.from(context);
    }

    public RepositoryListAdapter(Context context, List<RepositoryItem> items) {
        mInflator = LayoutInflater.from(context);
        this.items = items;
    }

    public void setItems(List<RepositoryItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (items != null ) {
            if (items.size() == 0) {
                return ITEMTYPE_NONE;
            }
            else {
                if (items.size() == position) {
                    return ITEMTYPE_LAST;
                }
                else {
                    return ITEMTYPE_ITEM;
                }
            }
        }
        else {
            return ITEMTYPE_NONE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case ITEMTYPE_ITEM: {
                View view = mInflator.inflate(R.layout.repo_item_view, parent, false);
                viewHolder = new RepositoryItemViewHolder(view);
                break;
            }
            case ITEMTYPE_LAST: {
                View view = mInflator.inflate(R.layout.last_item_view, parent, false);
                viewHolder = new LastItemViewHolder(view);
                break;
            }
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEMTYPE_ITEM: {
                RepositoryItemViewHolder vh = (RepositoryItemViewHolder)holder;
                RepositoryItem item = items.get(position);
                if (item.imageUrl.isEmpty() == false) {
                    Picasso.with(mInflator.getContext())
                            .load(item.imageUrl)
                            .placeholder(R.drawable.shape_item_placeholder)
                            .error(R.drawable.ic_menu_gallery)
                            .resizeDimen(R.dimen.profile_image_size, R.dimen.profile_image_size)
                            .centerCrop()
                            .transform(transform)
                            .into(vh.image);
                }
                vh.title.setText(item.title);
                vh.description.setText(item.description);
                vh.starCount.setText(item.getStarCountText());
                break;
            }
            case ITEMTYPE_LAST: {
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (items != null ) {
            if (items.size() == 0) {
                return 0;
            }
            else {
                return items.size() + 1;
            }
        }
        else {
            return 0;
        }
    }
}
