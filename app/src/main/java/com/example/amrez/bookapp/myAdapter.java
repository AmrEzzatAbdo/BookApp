package com.example.amrez.bookapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by amrez on 10/6/2017.
 */

public class myAdapter extends ArrayAdapter<B_object> {

    ArrayList<B_object> BookArrayList;
    Context context;
    LayoutInflater vi;

    //constructor
    public myAdapter(Context context, ArrayList<B_object> objects) {
        super(context, 0, objects);
        BookArrayList = objects;
        this.context = context;
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //List checking
        View listItemView = convertView;
        ViewHolder holder;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.book_list_item, parent, false);

            // find view  by the holder
            holder = new ViewHolder();
            holder.Name = (TextView) listItemView.findViewById(R.id.name);
            holder.author = (TextView) listItemView.findViewById(R.id.author);
            holder.difinition = (TextView) listItemView.findViewById(R.id.information);

            listItemView.setTag(holder);
        } else {
            holder = (ViewHolder) listItemView.getTag();
        }
        //set data
        holder.Name.setText(BookArrayList.get(position).getName());
        holder.author.setText(BookArrayList.get(position).getAuthor());
        holder.difinition.setText(BookArrayList.get(position).getTitle_info());

        return listItemView;
    }

    // holder for clas definition
    static class ViewHolder {
        public TextView Name;
        public TextView author;
        public TextView difinition;
    }

}
