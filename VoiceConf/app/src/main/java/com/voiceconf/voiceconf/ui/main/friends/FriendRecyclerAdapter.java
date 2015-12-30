package com.voiceconf.voiceconf.ui.main.friends;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;
import com.voiceconf.voiceconf.R;
import com.voiceconf.voiceconf.storage.models.Friend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Attila Blenesi on 30 Dec 2015
 */
public class FriendRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //region VARIABLES
    private View.OnClickListener mOnClickListener;
    private View.OnClickListener mAcceptClickListener;
    private View.OnClickListener mDeclineClickListener;

    private List<Friend> mFriends;
    //endregion

    public FriendRecyclerAdapter(View.OnClickListener onItemClickListener, View.OnClickListener acceptClickListenter, View.OnClickListener declineClickListenter) {
        mFriends = new ArrayList<>();
        mOnClickListener = onItemClickListener;
        mAcceptClickListener = acceptClickListenter;
        mDeclineClickListener = declineClickListenter;
    }

    public void update(@NonNull List<Friend> friends) {
        mFriends.clear();
        mFriends.addAll(friends);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        view.setOnClickListener(mOnClickListener);
        view.findViewById(R.id.accept).setOnClickListener(mAcceptClickListener);
        view.findViewById(R.id.decline).setOnClickListener(mDeclineClickListener);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((FriendViewHolder) holder).setup(mFriends.get(position));
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        private ImageButton mAccept;
        private ImageButton mDecline;
        private TextView mPendingText;
        private TextView mFriendName;
        private TextView mEmail;

        public FriendViewHolder(final View itemView) {
            super(itemView);
            mFriendName = (TextView) itemView.findViewById(R.id.user_name);
            mEmail = (TextView) itemView.findViewById(R.id.user_email);
            mPendingText = (TextView) itemView.findViewById(R.id.pending);
            mAccept = (ImageButton) itemView.findViewById(R.id.accept);
            mDecline = (ImageButton) itemView.findViewById(R.id.decline);
        }

        public void setup(Friend friend) {
            if (friend != null) {
                itemView.setTag(friend);
                mAccept.setTag(friend);
                mDecline.setTag(friend);

                if(ParseUser.getCurrentUser().getObjectId().equals(friend.getUser().getObjectId())){
                    setup(friend.getFriend());
                    if(friend.isPending()){
                        mPendingText.setVisibility(View.VISIBLE);
                        mAccept.setVisibility(View.INVISIBLE);
                        mDecline.setVisibility(View.INVISIBLE);
                        return;
                    }
                }else{
                    setup(friend.getUser());
                    if (friend.isPending()){
                        mPendingText.setVisibility(View.INVISIBLE);
                        mAccept.setVisibility(View.VISIBLE);
                        mDecline.setVisibility(View.VISIBLE);
                        return;
                    }
                }

                mPendingText.setVisibility(View.INVISIBLE);
                mAccept.setVisibility(View.INVISIBLE);
                mDecline.setVisibility(View.INVISIBLE);
            }
        }

        private void setup(ParseUser user) {
            // Load friend name
            if (user != null ) {
                if (!TextUtils.isEmpty(user.getUsername())) {
                    mFriendName.setText(user.getUsername());
                }
                if (!TextUtils.isEmpty(user.getEmail())) {
                    mEmail.setText(user.getEmail());
                }
            }

        }
    }
}
