package pt.ipleiria.sas.mobile.cantinaipl.controller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;

public class HorizontalListView extends HorizontalScrollView {

	private int adapterSize;
	private View view;
	private LinearLayout viewGroup;
	private Adapter adapterView;
	private LayoutParams layoutParams;
	private AdapterView.OnItemClickListener onItemClickListener;

	public HorizontalListView(Context context) {
		super(context);
		createList(context, null);
	}

	public HorizontalListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		createList(context, attrs);
	}

	public HorizontalListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		createList(context, attrs);
	}

	private void createList(Context context, AttributeSet attrs) {
		this.layoutParams = new LayoutParams(context, attrs);
		this.viewGroup = new LinearLayout(context);
		this.addView(viewGroup);
	}

	private OnClickListener onListItemClick(final int position) {
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				getOnItemClickListener().onItemClick(null, v, position,
						v.getId());
			}
		};

		return listener;
	}

	public void setAdapter(SpinnerAdapter adapter) {
		adapterView = adapter;
		adapterSize = adapter.getCount();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		layoutParams.width = (h * 7) / 6;
		layoutParams.height = h;
		for (int i = 0; i < adapterSize; i++) {
			view = adapterView.getView(i, viewGroup.getRootView(), viewGroup);
			view.setLayoutParams(layoutParams);
			viewGroup.addView(view, i);
			viewGroup.getChildAt(i).setOnClickListener(onListItemClick(i));
		}
	}

	public AdapterView.OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public void setOnItemClickListener(
			AdapterView.OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

}
