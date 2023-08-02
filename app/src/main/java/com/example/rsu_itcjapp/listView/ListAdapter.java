package com.example.rsu_itcjapp.listView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rsu_itcjapp.R;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<DataList> implements View.OnClickListener{

    private ArrayList<DataList> rowItems;
    Context context;

    private static class ViewHolder {
        TextView txtRowName;
        ImageView imgIcon;
    }

    public ListAdapter(Context context, ArrayList<DataList> datos){
        super(context, R.layout.row_item, datos);
        this.rowItems = datos;
        this.context = context;
    }


    @Override
    public void onClick(View view) {
        int posicion = (Integer) view.getTag();
        Object obj = getItem(posicion);
        DataList itemDesc = (DataList) obj;

        /*switch(view.getId()){
            case R.id.item_info:

                break;
        }

         */
    }

    @Override
    public View getView(int posicion, View view, ViewGroup parent){
        DataList item = getItem(posicion);
        ViewHolder viewHolder;

        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtRowName = (TextView) view.findViewById(R.id.txt_row_name);
            viewHolder.imgIcon = (ImageView) view.findViewById(R.id.img_row_icon);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.txtRowName.setText(item.getName());
        viewHolder.imgIcon.setImageResource(item.getIconImage());
        viewHolder.imgIcon.setContentDescription(item.getName());

        return view;
    }
}
