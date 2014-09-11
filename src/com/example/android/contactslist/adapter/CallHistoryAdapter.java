package com.example.android.contactslist.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.example.android.contactslist.R;
import com.example.android.contactslist.util.ImageLoader;

public class CallHistoryAdapter extends CursorAdapter {

	private Context context;
	private ImageLoader mImageLoader;
	private LayoutInflater mInflater;

	public CallHistoryAdapter(Context context, ImageLoader mImageLoader){
		super(context, null, 0);

		this.context = context;
		this.mImageLoader = mImageLoader;

		// Stores inflater for use later
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// Gets handles to individual view resources
		final ViewHolder holder = (ViewHolder) view.getTag();
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		final View itemLayout =
				mInflater.inflate(R.layout.call_list_item, viewGroup, false);

		// Creates a new ViewHolder in which to store handles to each view resource. This
		// allows bindView() to retrieve stored references instead of calling findViewById for
		// each instance of the layout.
		final ViewHolder holder = new ViewHolder();
		holder.callerName = (TextView) itemLayout.findViewById(R.id.caller_name);
		holder.callType = (TextView) itemLayout.findViewById(R.id.call_type);
		holder.callerImage = (QuickContactBadge) itemLayout.findViewById(R.id.caller_image);

		// Stores the resourceHolder instance in itemLayout. This makes resourceHolder
		// available to bindView and other methods that receive a handle to the item view.
		itemLayout.setTag(holder);

		// Returns the item layout view
		return itemLayout;
	}

	private class ViewHolder {
		TextView callerName;
		TextView callType;
		QuickContactBadge callerImage;
	}

}
