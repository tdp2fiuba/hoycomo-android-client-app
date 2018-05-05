package com.ar.tdp2fiuba.hoycomo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ar.tdp2fiuba.hoycomo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    List<String> mDataset;

    public ImagePagerAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mDataset = imageUrls;
    }

    @Override
    public int getCount() {
        if(mDataset != null){
            return mDataset.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.image_view_pager_layout, container, false);

        ImageView imageView = itemView.findViewById(R.id.view_pager_image);

        Picasso.get()
                .load(mDataset.get(position))
                .into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}