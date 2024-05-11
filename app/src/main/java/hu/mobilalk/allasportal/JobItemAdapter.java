package hu.mobilalk.allasportal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class JobItemAdapter extends RecyclerView.Adapter<JobItemAdapter.ViewHolder> implements Filterable {
    private ArrayList<JobItem> myJobItemsData;
    private ArrayList<JobItem> myJobItemsDataAll;
    private Context myContext;
    private int lastPos = -1;


    public JobItemAdapter(Context context, ArrayList<JobItem> itemsData) {
        this.myJobItemsData = itemsData;
        this.myJobItemsDataAll = itemsData;
        this.myContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(myContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull JobItemAdapter.ViewHolder holder, int position) {
        JobItem currentItem = myJobItemsData.get(position);

        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPos){
            Animation animation = AnimationUtils.loadAnimation(myContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPos = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return myJobItemsData.size();
    }


    @Override
    public Filter getFilter() {
        return jobFilter;
    }

    private Filter jobFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<JobItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(constraint == null || constraint.length()==0){
                results.count = myJobItemsDataAll.size();
                results.values = myJobItemsDataAll;
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(JobItem item : myJobItemsDataAll){
                    if(item.getTitle().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;

            }

            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            myJobItemsData = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView myTitle;
        private TextView myDescription;
        private ImageView myImage;
        private RatingBar myRatingBar;
        private TextView myLocatin;
        private JobItem currentItem;

        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            myImage = itemView.findViewById(R.id.itemImage);
            myTitle = itemView.findViewById(R.id.itemTitle);
            myRatingBar = itemView.findViewById(R.id.ratingBar);
            myDescription = itemView.findViewById(R.id.description);
            myLocatin = itemView.findViewById(R.id.location);

            itemView.findViewById(R.id.detalisBtn).setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        v.animate().scaleX(1f).scaleY(1f).setDuration(100);
                        break;
                }
                return false;
            });

            itemView.findViewById(R.id.updateBtn).setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        v.animate().scaleX(1f).scaleY(1f).setDuration(100);
                        break;
                }
                return false;
            });

            itemView.findViewById(R.id.deleteBtn).setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        v.animate().scaleX(1f).scaleY(1f).setDuration(100);
                        break;
                }
                return false;
            });

            itemView.findViewById(R.id.detalisBtn).setOnClickListener(v -> {
                if (myContext instanceof Activity) {
                    Intent intent = new Intent(myContext, JobDetailsActivity.class);
                    intent.putExtra("TITLE", currentItem.getTitle());
                    intent.putExtra("LONG_DESCRIPTION", currentItem.getLongDescription());
                    intent.putExtra("LOCATION", currentItem.getLocation());
                    intent.putExtra("RATING", currentItem.getCompanyRating());
                    intent.putExtra("IMAGE_RESOURCE", currentItem.getImageResource());
                    ((Activity) myContext).startActivity(intent);
                }
            });
        }

        public void bindTo(JobItem currentItem) {
            this.currentItem = currentItem;

            myTitle.setText(currentItem.getTitle());
            myRatingBar.setRating(currentItem.getCompanyRating());
            myDescription.setText(currentItem.getDescription());
            myLocatin.setText(currentItem.getLocation());

            Glide.with(myContext).load(currentItem.getImageResource()).into(myImage);

            itemView.findViewById(R.id.deleteBtn).setOnClickListener(view -> {
                itemView.animate().translationX(itemView.getWidth()).alpha(0).setDuration(300).withEndAction(() -> {
                    ((JobsActivity) myContext).deleteItem(currentItem);
                    itemView.setTranslationX(0);
                    itemView.setAlpha(1);
                });
            });
            itemView.findViewById(R.id.updateBtn).setOnClickListener(view -> ((JobsActivity) myContext).updateItem(currentItem));
        }
    };

}


