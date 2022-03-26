package com.example.oversighttest.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.oversighttest.R;
import com.example.oversighttest.entities.Transaction;
import com.example.oversighttest.pages.TransactionsPage;

import java.util.ArrayList;
import java.util.List;
//https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
//Not used. Using ReclyerTransactionAdapter instead.
public class TransactionAdapter extends ArrayAdapter<Transaction> {
    private ArrayList<Transaction> transactions;
    public TransactionAdapter(Context context, ArrayList<Transaction> transactions) {
        //super(context, 0, transactions);
        super(context, -1);
        this.transactions = transactions;
    }
    //Override onCreateViewHolder method
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Transaction transaction = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_transaction, parent, false);
        }
        // Lookup view for data population
        TextView tvAmount = (TextView) convertView.findViewById(R.id.tvAmount);
        TextView tvCategory = (TextView) convertView.findViewById(R.id.tvCategory);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        // Populate the data into the template view using the data object
        tvAmount.setTextColor(Color.WHITE);
        tvCategory.setTextColor(Color.WHITE);
        tvDate.setTextColor(Color.WHITE);
        tvAmount.setText(Integer.toString(transaction.getAmount()));
        tvCategory.setText(transaction.getCategory().getDisplayName());
        tvDate.setText((transaction.getDate().toString()));

        // Return the completed view to render on screen
        return convertView;
    }
}
