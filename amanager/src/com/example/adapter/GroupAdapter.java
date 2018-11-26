package com.example.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import antistatic.spinnerwheel.adapters.AbstractWheelTextAdapter;

import com.example.amanager.R;

public class GroupAdapter extends AbstractWheelTextAdapter {
	// Countries names
	private String[] group;

	// Countries flags

	/**
	 * Constructor
	 */
	public GroupAdapter(Context context, String group[]) {
		super(context, R.layout.contacts_item, NO_RESOURCE);
		setItemTextResource(R.id.contacts_name);
		this.group = group;
	}

	@Override
	public View getItem(int index, View cachedView, ViewGroup parent) {
		View view = super.getItem(index, cachedView, parent);
		ImageView img = (ImageView) view.findViewById(R.id.imgHead);
		if (index == 0) {
			img.setVisibility(View.VISIBLE);
		} else {
			img.setVisibility(View.GONE);
		}
		TextView textView = (TextView) view.findViewById(R.id.contacts_name);
		textView.setText(group[index]);
		return view;
	}

	@Override
	public int getItemsCount() {
		return group.length;
	}

	@Override
	protected CharSequence getItemText(int index) {
		return group[index];
	}
}