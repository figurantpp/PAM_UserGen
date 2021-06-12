package com.example.usergen.model.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usergen.R;
import com.example.usergen.model.user.User;
import com.example.usergen.util.ApiInfo;

import java.io.IOException;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    private final List<User> users;

    public UserListAdapter(@NonNull List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.user_list_view, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        public TextView userItemName, userItemNationality;
        public ImageView userItemImage;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userItemName = itemView.findViewById(R.id.txtUserItemName);

            userItemImage = itemView.findViewById(R.id.userItemImage);

            userItemNationality = itemView.findViewById(R.id.txtUserItemNationality);
        }

        public void bind(User user){

            userItemName.setText(user.getName());

            userItemNationality.setText(
                    ApiInfo.NATIONALITY_NAMES.getOrDefault(
                            user.getNationality(),
                            user.getNationality()
                    )
            );

            try {
                userItemImage.setImageBitmap(user.getProfileImage().getBitmap());
            }
            catch (IOException exception)
            {
                Log.e("adapter", "shh");
            }
        }
    }
}
