package com.example.utsryanrizaldy;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class MenuAdapter  extends ArrayAdapter<MenuClass> {


    private MenuClass[] menuList;
    private int resource;


    public MenuAdapter(Context context, int resource, MenuClass[] menuList) {
        super(context, resource, menuList);
        this.resource = resource;
        this.menuList= menuList;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View line;
        LayoutInflater  layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        line = layoutInflater.inflate(resource,null);

        TextView name = (TextView)line.findViewById(R.id.textView);
        ImageView image = (ImageView)line.findViewById(R.id.imageView);
        Button addToCartButton = (Button)line.findViewById(R.id.add_to_cart);

        MenuClass menus = menuList[position];
        name.setText(menus.getName());
        image.setImageResource(menus.getImage());

        addToCartButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), menus.getName() + " added to cart", Toast.LENGTH_SHORT).show();
        });

        return line;

    }
}
