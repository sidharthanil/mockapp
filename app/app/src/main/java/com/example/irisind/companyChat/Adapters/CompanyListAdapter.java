package com.example.irisind.companyChat.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.irisind.companyChat.Models.Company;
import com.example.irisind.companyChat.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by irisind on 1/5/17.
 */

public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.MyViewholder> {

    private static final String TAG = "CmpnyListAdapter";
    private Activity activity;
    private List<Company> companyList;

    public CompanyListAdapter(Activity activity, List<Company> companyList) {
        this.activity = activity;
        this.companyList = companyList;
    }

    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.company_list_item, parent, false);
        return new MyViewholder(itemView);
    }

    public void changeDataset(List<Company> itemList){
        this.companyList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {

        final Company company = companyList.get(position);

        holder.title.setText(company.getCompanyName());
        holder.subtitle.setText(company.getSubAccountName());

        Glide.with(activity).load(company.getProfilePic())
                .asBitmap().centerCrop()
                .placeholder(null)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.profPic);

        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                if (button.getText().toString().equals(activity.getString(R.string.follow))){
                    button.setText(R.string.unfollow);
                    button.setBackgroundResource(R.color.colorPrimary);
                    button.setTextColor(Color.WHITE);
                } else {
                    button.setText(R.string.follow);
                    button.setBackgroundResource(R.drawable.green_rectangle);
                    button.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        private CircleImageView profPic;
        private TextView title,subtitle;
        private Button follow;
        public MyViewholder(View itemView) {
            super(itemView);
            profPic = (CircleImageView) itemView.findViewById(R.id.profPic);
            title = (TextView) itemView.findViewById(R.id.title);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            follow = (Button) itemView.findViewById(R.id.follow);
        }
    }
}
