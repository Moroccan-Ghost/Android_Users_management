package com.example.projetmobileapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserRVAdapter extends RecyclerView.Adapter<UserRVAdapter.ViewHolder> {
    // creating variables
    private ArrayList<UserRVModal> UserRVModalArrayList;
    private Context context;
    private UserClickInterface UserClickInterface;
    int lastPos = -1;

    // creating a constructor.
    public UserRVAdapter(ArrayList<UserRVModal> UserRVModalArrayList, Context context, UserClickInterface UserClickInterface) {
        this.UserRVModalArrayList = UserRVModalArrayList;
        this.context = context;
        this.UserClickInterface = UserClickInterface;
    }

    @NonNull
    @Override
    public UserRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating layout file.
        View view = LayoutInflater.from(context).inflate(R.layout.user_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRVAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // setting data to our recycler view item.
        UserRVModal UserRVModal = UserRVModalArrayList.get(position);
        holder.UserTV.setText(UserRVModal.getUserName());
        holder.UserPriceTV.setText(UserRVModal.getUserAge() + " years Old");

        // adding animation to recycler view item.
        setAnimation(holder.itemView, position);
        holder.UserIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserClickInterface.onUserClick(position);
            }
        });
    }

    private void setAnimation(View itemView, int position) {
        if (position > lastPos) {
            //setting animation.
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPos = position;
        }
    }

    @Override
    public int getItemCount() {
        return UserRVModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variable for our image view and text view on below line.
        private ImageView UserIV;
        private TextView UserTV, UserPriceTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing variables.
            UserIV = itemView.findViewById(R.id.idIVUser);
            UserTV = itemView.findViewById(R.id.idTVUserName);
            UserPriceTV = itemView.findViewById(R.id.idTVUserAge);
        }
    }

    // creating a interface for on click
    public interface UserClickInterface {
        void onUserClick(int position);
    }
}
