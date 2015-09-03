package com.todor.appslist;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.todor.appslist.model.SingleItem;

import java.util.List;

public class ListAdapter extends ArrayAdapter<SingleItem> {
    public ListAdapter (Context context, List<SingleItem> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SingleItem singleItem = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_item, parent, false);
        }

        TextView id = (TextView) convertView.findViewById(R.id.itemId);
        TextView name = (TextView) convertView.findViewById(R.id.title);
        TextView desc = (TextView) convertView.findViewById(R.id.description);
        ImageView image = (ImageView) convertView.findViewById(R.id.imageView);
        final Button download = (Button) convertView.findViewById(R.id.download);

        Picasso.with(getContext()).load(singleItem.getImage()).into(image);
        id.setText(singleItem.getId());
        name.setText(singleItem.getTitle());
        desc.setText(Html.fromHtml(singleItem.getDescription()));

        if(isPackageInstalled(singleItem.getAppPackage())) {
            download.setText("This app is already installed");
            download.setEnabled(false);
        }

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (singleItem.getProxy().isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + singleItem.getURI()));
                    getContext().startActivity(intent);
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + singleItem.getURI() + singleItem.getProxy()));
                    getContext().startActivity(intent);
                }
            }
        });
        return convertView;
    }

    private boolean isPackageInstalled(String packageName) {
        PackageManager pm = getContext().getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
