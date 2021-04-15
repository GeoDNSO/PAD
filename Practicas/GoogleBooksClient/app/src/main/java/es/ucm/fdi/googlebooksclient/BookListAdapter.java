package es.ucm.fdi.googlebooksclient;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder>{

    private MainActivity mainActivity;
    private List<BookInfo> bookInfoList;

    public BookListAdapter(MainActivity mainActivity, List<BookInfo> bookInfoList){
        this.bookInfoList = bookInfoList;
        this.mainActivity = mainActivity;
    }

    public void setBookInfoList(List<BookInfo> bookInfoList) {
        if(this.bookInfoList == null || this.bookInfoList.isEmpty()){
            this.bookInfoList = bookInfoList;
            return;
        }
        this.bookInfoList.addAll(bookInfoList);
    }

    public void clearList() {
        this.bookInfoList.clear();
    }

    @NonNull
    @Override
    public BookListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item, parent, false);

        return new BookListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookListAdapter.ViewHolder holder, int position) {
        BookInfo bookInfo = bookInfoList.get(position);

        holder.tvTitle.setText(bookInfo.getTitle());
        holder.tvAuthor.setText(bookInfo.getAuthors());
        holder.tvLink.setText(bookInfo.getInfoLink().toString());
    }

    @Override
    public int getItemCount() {
        return bookInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView tvTitle;
        private TextView tvAuthor;
        private TextView tvLink;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.bookCardView);
            tvTitle = itemView.findViewById(R.id.book_item_title);
            tvAuthor = itemView.findViewById(R.id.book_item_authors);
            tvLink = itemView.findViewById(R.id.book_item_link);


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String urlString = tvLink.getText().toString();
                    Log.i("BOOK_ADAPTER", "La url a abrir es: " + urlString);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                    mainActivity.startActivity(browserIntent);
                }
            });
        }
    }
}
