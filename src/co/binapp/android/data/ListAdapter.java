package co.binapp.android.data;

import java.util.ArrayList;

import co.binapp.android.data.AppObjects.BinObject;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListAdapter extends BaseAdapter {
	
	ArrayList<BinObject> binsArrayList = new ArrayList<BinObject>();
	
	public boolean updateListInAdapter(ArrayList<BinObject> inputBinArrayList) {
		binsArrayList = inputBinArrayList;
		if (getCount() > 0) {
			notifyDataSetChanged();
			return true;
		} else {
			return false;
		}
	}
	
	/* required implemented methods below */
	@Override
	public int getCount() {
		return binsArrayList.size();
	}
	
	/* getItem() usually returns an object
	 * I will be returning a BinObject */
	@Override
	public BinObject getItem(int position) {
		return binsArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 
		return null;
	}

}
