package com.example.oversighttest.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.JsonToken;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oversighttest.R;
import com.example.oversighttest.entities.Transaction;
import com.example.oversighttest.pages.TransactionsPage;

import java.util.ArrayList;
//adapter that handles loading recycler view (similar to list) of transactions
public class RecyclerTransactionAdapter extends RecyclerView.Adapter<RecyclerTransactionAdapter.ViewHolder> {
    private static final String TAG = null;
    private final Context context;
    private ArrayList<Transaction> transactions = new ArrayList<>();
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //private final TextView textView;
        private final TextView tvAmount;
        private final TextView tvCategory;
        private final TextView tvDate;
        private View parentView;


        public ViewHolder(@NonNull View view) {
            super(view);
            this.parentView = view;
            //from item_transaction.xml
            this.tvAmount = (TextView) view.findViewById(R.id.tvAmount);
            this.tvCategory = (TextView) view.findViewById(R.id.tvCategory);
            this.tvDate = (TextView) view.findViewById(R.id.tvDate);
            // todo: Define click listener for the ViewHolder's View
        }

        public TextView getTvAmount() {
            return tvAmount;
        }

        public TextView getTvCategory() {
            return tvCategory;
        }

        public TextView getTvDate() {
            return tvDate;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param //dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     *
     */
    public RecyclerTransactionAdapter(Context contx, ArrayList<Transaction> transactions) {
        this.context = contx;
        this.transactions = transactions;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_transaction, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.setIsRecyclable(false);
        final Transaction transaction = transactions.get(position);

        if(viewHolder != null) {
            viewHolder.getTvAmount().setText(Integer.toString(transaction.getAmount()));
            viewHolder.getTvCategory().setText(transaction.getCategory().getDisplayName());
            viewHolder.getTvDate().setText((transaction.getDate().toString()));
        }else{
            notifyItemChanged( position );
        }
        viewHolder.parentView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {

                System.out.println("Hello world!");
                return true;
            };


        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.transactions.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
