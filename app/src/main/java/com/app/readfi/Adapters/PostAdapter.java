package com.app.readfi.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.app.readfi.Activities.NewsDetailsActivity;
import com.app.readfi.Models.Post;
import com.app.readfi.R;
import com.app.readfi.databinding.ItemPostBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<Post> posts;
    private StorageReference storageReference;
    private FirebaseAuth auth;

    public PostAdapter(Context context, ArrayList<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Post post = posts.get(position);
        holder.binding.title.setText(post.getPostText());
        holder.binding.description.setText(post.getPostDescription());


            Glide.with(context).load(post.getImg())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewsDetailsActivity.class);
                intent.putExtra("postId", post.getPostID());
                intent.putExtra("postText", post.getPostText());
                intent.putExtra("postDescription", post.getPostDescription());
                intent.putExtra("img", post.getImg());
                context.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ItemPostBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemPostBinding.bind(itemView);
        }




    }
}
