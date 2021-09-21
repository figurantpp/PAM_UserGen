package com.example.usergen.view.fragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usergen.R;
import com.example.usergen.model.User;
import com.example.usergen.util.RandomApiInfo;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    private final List<User> users;
    private final Context context;

    @NonNull
    private final Consumer<User> onClick;

    public UserListAdapter(
            @NonNull List<User> users,
            @NonNull Context context,
            @NonNull Consumer<User> onClick
    ) {
        this.users = users;
        this.context = context;
        this.onClick = onClick;
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

    class UserViewHolder extends RecyclerView.ViewHolder {

        private final TextView userItemName;
        private final TextView userItemNationality;
        private final ImageView userItemImage;
        private final ImageView isFavoriteImageView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userItemName = itemView.findViewById(R.id.txtUserItemName);

            userItemImage = itemView.findViewById(R.id.userItemImage);

            userItemNationality = itemView.findViewById(R.id.txtUserItemNationality);

            isFavoriteImageView = itemView.findViewById(R.id.userList_isFavoriteImageView);
        }

        public void bind(User user){

            userItemName.setText(user.getName());

            userItemNationality.setText(
                    RandomApiInfo.NATIONALITY_NAMES.getOrDefault(
                            user.getNationality(),
                            user.getNationality()
                    )
            );

            isFavoriteImageView.setImageResource(
                    user.isFavorite()
                            ? R.drawable.ic_heart_filled
                            : R.drawable.ic_heart_not_filled
            );

            isFavoriteImageView.setOnClickListener(
                    v -> {
                        isFavoriteImageView.setImageResource(
                                user.isFavorite()
                                ? R.drawable.ic_heart_not_filled
                                : R.drawable.ic_heart_filled
                        );

                        onClick.accept(user);
                    }
            );

            try {
                userItemImage.setImageBitmap(user.getProfileImage().getBitmapSync());
                userItemImage.setContentDescription(
                        String.format(context.getString(R.string.format_imageDescription), user.getName()));
            }
            catch (IOException exception)
            {
                Log.e("adapter", "shh");
            }
        }
    }
}
