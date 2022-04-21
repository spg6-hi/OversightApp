package com.example.oversighttest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oversighttest.R;
import com.example.oversighttest.entities.Transaction;
import com.example.oversighttest.activities.TransactionsFragment;

import java.time.LocalDate;
import java.util.ArrayList;
//adapter that handles loading recycler view (similar to list) of transactions
public class RecyclerTransactionAdapter extends RecyclerView.Adapter<RecyclerTransactionAdapter.ViewHolder> {
    private static final String TAG = null;
    private final Context context;
    private TransactionsFragment tp;
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private Fragment mFragment;

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //private final TextView textView;
        private final TextView tvAmount;
        private final TextView tvCategory;
        private final TextView tvDate;
        private final TextView mtvId;
        private View parentView;


        public ViewHolder(@NonNull View view) {
            super(view);
            this.parentView = view;

            //from item_transaction.xml
            this.tvAmount = (TextView) view.findViewById(R.id.tvAmount);
            this.tvCategory = (TextView) view.findViewById(R.id.tvCategory);
            this.tvDate = (TextView) view.findViewById(R.id.tvDate);
            this.mtvId = (TextView) view.findViewById(R.id.mtvId);

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

        public TextView getTvId(){return mtvId;}
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param //dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     *
     */
    public RecyclerTransactionAdapter(Context contx, ArrayList<Transaction> transactions, TransactionsFragment tp) {
        this.context = contx;
        this.transactions = transactions;
        this.tp = tp;
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
            if(transaction.getDate() != null) {
                viewHolder.getTvDate().setText((transaction.getDate().toString()));
            }
            else {
                viewHolder.getTvDate().setText(LocalDate.now().toString());
            }
            viewHolder.getTvId().setText(Long.toString(transaction.getID()));

        }else{
            notifyItemChanged( position );
        }
        viewHolder.parentView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                ViewHolder v = new ViewHolder(view);
                String s = v.getTvId().getText().toString();
                int id = Integer.parseInt(s);
                long ID = Integer.toUnsignedLong(id);

                tp.deleteTransaction(ID);
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
