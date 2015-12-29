package com.howest.nmct.bob.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.howest.nmct.bob.R;
import com.squareup.picasso.Picasso;

/**
 * illyism
 * 28/12/15
 */
public class UserAdapter extends CursorAdapter {
    private int colIndexText;
    private int colIndexPicture;

    public UserAdapter(Context context, int colIndexText, int colIndexPicture) {
        super(context, null, 0);
        this.colIndexText = colIndexText;
        this.colIndexPicture = colIndexPicture;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvText = (TextView) view.findViewById(R.id.text);
        ImageView imgImage = (ImageView) view.findViewById(R.id.image);

        tvText.setText(cursor.getString(colIndexText));
        Picasso p = Picasso.with(context);
        p.load(mCursor.getString(colIndexPicture))
                .fit()
                .centerCrop()
                .into(imgImage);
    }
}
