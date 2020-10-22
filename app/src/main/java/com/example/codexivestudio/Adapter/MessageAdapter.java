package com.example.codexivestudio.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codexivestudio.Model.Chat;
import com.example.codexivestudio.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;


    FirebaseUser mFirebaseUser;
    private Context mContext;
    private List<Chat> mChat;
    private String mImageUrl;


    public MessageAdapter(Context mContext, List<Chat> mChat,String mImageUrl) {
        this.mContext = mContext;
        this.mChat = mChat;
        this.mImageUrl = mImageUrl;
    }


    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Chat chat = mChat.get(position);

        holder.ShowMessage.setText(chat.getMessage());

        if(mImageUrl.equals("default")){
            holder.ProfileImage.setImageResource(R.mipmap.ic_launcher);
        }else{
           // Glide.with(mContext).load(mImageUrl).into(holder.ProfileImage);
        }


    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public TextView ShowMessage;
        public ImageView ProfileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ShowMessage = itemView.findViewById(R.id.show_message);
            ProfileImage = itemView.findViewById(R.id.profile_image);

        }


    }

    @Override
    public int getItemViewType(int position) {
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mChat.get(position).getSender().equals(mFirebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
