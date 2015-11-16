package com.nguyenquanganh.cookingassistance.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.nguyenquanganh.cookingassistance.R;

/**
 * Created by nguyenquanganh on 8/29/15.
 */
public class RecipeCursorAdapter extends CursorAdapter{
    public RecipeCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newView = inflater.inflate(R.layout.row_layout, parent, false);

        return newView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView) view.findViewById(R.id.label);
        int columnIndex = cursor.getColumnIndex("season_name");
        if (textView != null)
            textView.setText(cursor.getString(columnIndex));
    }
}
