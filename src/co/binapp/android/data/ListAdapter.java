package co.binapp.android.data;

import java.util.ArrayList;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;

import co.binapp.android.R;
import co.binapp.android.data.AppObjects.BinListViewHolder;
import co.binapp.android.data.AppObjects.BinObject;
import co.binapp.android.data.DataStoreConstants.Bins.TypeValues;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/* TODO Test this entire class */
public class ListAdapter extends BaseAdapter {
	
	Context viewContext;
	ArrayList<BinObject> binsArrayList = new ArrayList<BinObject>();
	
	public ListAdapter(Context inContext) {
		viewContext = inContext;
	}
	
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
		
		/* Important class instantiations */
		BinListViewHolder holder = new BinListViewHolder();
		AQuery aq = new AQuery(viewContext);
		ImageOptions imageOptions = new ImageOptions();
		LayoutInflater inflater = (LayoutInflater) viewContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		convertView = null;
		imageOptions.fileCache = true;
		imageOptions.memCache = true;
		
		if (convertView == null) {
			switch (binsArrayList.get(position).type) {
			case TypeValues.TEXT:
				convertView = textCard(parent, holder, inflater, binsArrayList.get(position));
				break;
				
			case TypeValues.QUOTE:
				/* TODO Not implemented */
				break;
				
			case TypeValues.LINK:
				convertView = textCard(parent, holder, inflater, binsArrayList.get(position));
				break;
				
			case TypeValues.PHOTO:
				convertView = imageCard(parent, holder, inflater, binsArrayList.get(position));
				break;
				
			case TypeValues.VIDEO:
				convertView = imageCard(parent, holder, inflater, binsArrayList.get(position)); 
				break;
				
			case TypeValues.PRIVATE:
				/* TODO Not implemented */
				break;

			default:
				break;
			}
		}
		
		return convertView;
	}

	private View imageCard(ViewGroup parent, BinListViewHolder holder, LayoutInflater inflater, BinObject binObject) {
		View convertView;
		convertView = inflater.inflate(R.layout.image_card, parent, false);
		holder.image = (ImageView) convertView.findViewById(R.id.imageCard_Image);
		holder.title = (TextView) convertView.findViewById(R.id.imageCard_Title);
		holder.content = (TextView) convertView.findViewById(R.id.imageCard_Body);
		convertView.setTag(holder);
		
		holder.title.setText(binObject.title);
		holder.content.setText(binObject.content);
		
		return convertView;
	}

	private View textCard(ViewGroup parent, BinListViewHolder holder, LayoutInflater inflater, BinObject binObject) {
		View convertView;
		convertView = inflater.inflate(R.layout.text_card, parent, false);
		holder.title = (TextView) convertView.findViewById(R.id.textCard_Title);
		holder.content = (TextView) convertView.findViewById(R.id.textCard_Body);
		convertView.setTag(holder);
		
		holder.title.setText(binObject.content);
		holder.content.setText(binObject.content);
		
		return convertView;
	}

}
