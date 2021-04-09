package es.ucm.fdi.googlebooksclient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder>{

    private List<BookInfo> bookInfoList;

    public BookListAdapter(List<BookInfo> bookInfoList){
        this.bookInfoList = bookInfoList;
    }

    public void setBookInfoList(List<BookInfo> bookInfoList) {
        this.bookInfoList = bookInfoList;
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

        private TextView tvTitle;
        private TextView tvAuthor;
        private TextView tvLink;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.book_item_title);
            tvAuthor = itemView.findViewById(R.id.book_item_authors);
            tvLink = itemView.findViewById(R.id.book_item_link);
        }
    }
}
