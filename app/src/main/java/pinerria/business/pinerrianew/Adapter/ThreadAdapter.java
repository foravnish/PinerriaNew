package pinerria.business.pinerrianew.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;

import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;
import pinerria.business.pinerrianew.Utils.Message;

/**
 * Created by Belal on 5/29/2016.
 */
//Class extending RecyclerviewAdapter
public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.ViewHolder> {

    //user id
    private int userId;
    private Context context;

    //Tag for tracking self message
    private int SELF;

    //ArrayList of messages object containing all the messages in the thread
    private ArrayList<Message> messages;
    String name;

    //Constructor
    public ThreadAdapter(Context context, ArrayList<Message> messages, int userId) {
        this.userId = userId;
        this.messages = messages;
        this.context = context;
        SELF = Integer.valueOf(MyPrefrences.getUserID(context));
    }

    //IN this method we are tracking the self message
    @Override
    public int getItemViewType(int position) {
        //getting message object of current position
        Message message = messages.get(position);
        //If its owner  id is  equals to the logged in user id
        if (message.getUsersId() == userId) {
            //Returning self
            name = MyPrefrences.getUSENAME(context);
            return SELF;
        }
        name = Util.Sender_Name;
        //else returning position
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Creating view
        View itemView;
        if (viewType == SELF) {
            //Inflating the layout self
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_thread, parent, false);
        } else {
            //else inflating the layout others
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_thread_other, parent, false);
        }
        //returing the view
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Adding messages to the views
        Message message = messages.get(position);
        holder.textViewMessage.setText(message.getMessage());
        holder.textViewTime.setText(name + " , " + message.getSentAt());
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    //Initializing views
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewMessage;
        public TextView textViewTime;
        ImageView wrng_img;


        public ViewHolder(View itemView) {
            super(itemView);
            textViewMessage = (TextView) itemView.findViewById(R.id.textViewMessage);
            textViewTime = (TextView) itemView.findViewById(R.id.textViewTime);
            wrng_img = (ImageView) itemView.findViewById(R.id.wrng_img);
        }
    }
}
