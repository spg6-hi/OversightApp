package com.example.oversighttest.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oversighttest.R;
import com.example.oversighttest.entities.Transaction;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerTransactionAdapter extends RecyclerView.Adapter<RecyclerTransactionAdapter.ViewHolder> {
    private String[] localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //private final TextView textView;
        private final TextView tvAmount;
        private final TextView tvCategory;
        private final TextView tvDate;

        public ViewHolder(@NonNull View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            //TextView textView = (TextView) view.findViewById(R.id.textView);
            this.tvAmount = (TextView) view.findViewById(R.id.tvAmount);
            this.tvCategory = (TextView) view.findViewById(R.id.tvCategory);
            this.tvDate = (TextView) view.findViewById(R.id.tvDate);
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
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     *
     * public RecyclerTransactionAdapter(Context context, ArrayList<Transaction> transactions) {
     *
     *         this.context = context;
     *         this.transactions = transactions;
     *     }
     */
        private Context context;
        private ArrayList<Transaction> transactions = new ArrayList<>();
    public RecyclerTransactionAdapter(Context context, ArrayList<Transaction> transactions) {
        //localDataSet = dataSet;
        this.context = context;
        this.transactions = transactions;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_transaction, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Transaction transaction = transactions.get(position);

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTvAmount().setTextColor((Color.WHITE));
        viewHolder.getTvCategory().setTextColor((Color.WHITE));
        viewHolder.getTvDate().setTextColor((Color.WHITE));
        viewHolder.getTvAmount().setText(Integer.toString(transaction.getAmount()));
        viewHolder.getTvCategory().setText(transaction.getCategory().getDisplayName());
        viewHolder.getTvDate().setText((transaction.getDate().toString()));

        /*viewHolder.tvAmount.setTextColor(Color.WHITE);
        viewHolder.tvCategory.setTextColor(Color.WHITE);
        viewHolder.tvDate.setTextColor(Color.WHITE);
        viewHolder.tvAmount.setText(Integer.toString(transaction.getAmount()));
        viewHolder.tvCategory.setText(transaction.getCategory().getDisplayName());
        viewHolder.tvDate.setText((transaction.getDate().toString()));  */  }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.transactions.size();
    }
}
