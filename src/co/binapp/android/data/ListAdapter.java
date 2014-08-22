package co.binapp.android.data;

import java.util.ArrayList;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;

import co.binapp.android.R;
import co.binapp.android.data.AppObjects.BinListViewHolder;
import co.binapp.android.data.AppObjects.BinObject;
import co.binapp.android.data.DataStoreConstants.Bins.TypeValues;
import co.binapp.android.data.Fonts.Roboto;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
	
	Context viewContext;
	AssetManager assets;
	Display display;
	AQuery aq;
	Point size = new Point();
	Fonts mFonts = new Fonts();
	ImageOptions imageOptions = new ImageOptions();
	ArrayList<BinObject> binsArrayList = new ArrayList<BinObject>();
	
	public ListAdapter(Context inContext, AssetManager inAssets, Display inDisplay) {
		viewContext = inContext;
		assets = inAssets;
		display = inDisplay;
		display.getSize(size);
		aq = new AQuery(viewContext);
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
				convertView = quoteCard(parent, holder, inflater, binsArrayList.get(position));
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

			default:
				break;
			}
		}
		
		return convertView;
	}

	private View quoteCard(ViewGroup parent, BinListViewHolder holder, LayoutInflater inflater, BinObject binObject) {
		View convertView;
		convertView = inflater.inflate(R.layout.quote_card, parent, false);
		holder.content = (TextView) convertView.findViewById(R.id.quoteCard_Body);
		convertView.setTag(holder);
		
		/* Load content below */
		holder.content.setText(binObject.content);
		mFonts.typeFaceConstructor(holder.content, Roboto.Slab.REGULAR, assets);
		
		return convertView;
	}

	private View imageCard(ViewGroup parent, BinListViewHolder holder, LayoutInflater inflater, BinObject binObject) {
		View convertView;
		convertView = inflater.inflate(R.layout.image_card, parent, false);
		holder.image = (ImageView) convertView.findViewById(R.id.imageCard_Image);
		holder.title = (TextView) convertView.findViewById(R.id.imageCard_Title);
		holder.content = (TextView) convertView.findViewById(R.id.imageCard_Body);
		convertView.setTag(holder);
		
		/* Load content below */
		holder.title.setText(binObject.title);
		holder.content.setText(binObject.content);
		
		if (StringProcessor.hasContents(binObject.title)) {
			mFonts.typeFaceConstructor(holder.title, Fonts.Roboto.Slab.REGULAR, assets);
			holder.title.setVisibility(View.VISIBLE);
		}
		
		if (StringProcessor.hasContents(binObject.imgurl)) {
			holder.image.setVisibility(View.VISIBLE);
			aq.id(holder.image).image(
					binObject.imgurl, // Image URL 
					true, // memCache
					true, // fileCache
					0, // target width
					0, // fall back id
					null, // preset
					AQuery.FADE_IN, // animation ID
					AQuery.RATIO_PRESERVE // aspect ratio
					);
			
		}
		
		mFonts.typeFaceConstructor(holder.content, Fonts.Roboto.LIGHT, assets);
		
		return convertView;
	}

	private View textCard(ViewGroup parent, BinListViewHolder holder, LayoutInflater inflater, BinObject binObject) {
		View convertView;
		convertView = inflater.inflate(R.layout.text_card, parent, false);
		holder.title = (TextView) convertView.findViewById(R.id.textCard_Title);
		holder.image = (ImageView) convertView.findViewById(R.id.textCard_Image);
		holder.content = (TextView) convertView.findViewById(R.id.textCard_Body);
		convertView.setTag(holder);
		
		/* Load content below */
		holder.content.setText(binObject.content);
		
		if (StringProcessor.hasContents(binObject.title)) {
			holder.title.setText(binObject.title);
			mFonts.typeFaceConstructor(holder.title, Fonts.Roboto.Slab.REGULAR, assets);
			holder.title.setVisibility(View.VISIBLE);
		}
		
		if (StringProcessor.hasContents(binObject.imgurl)) {
			holder.image.setVisibility(View.VISIBLE);
			aq.id(holder.image).image(
					binObject.imgurl, // Image URL 
					true, // memCache
					true, // fileCache
					0, // target width
					0, // fall back id
					null, // preset
					AQuery.FADE_IN, // animation ID
					AQuery.RATIO_PRESERVE // aspect ratio
					);
		}

		mFonts.typeFaceConstructor(holder.content, Fonts.Roboto.LIGHT, assets);
		
		return convertView;
	}

}
