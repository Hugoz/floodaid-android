package au.com.floodaid.maps;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import au.com.floodaid.R;
import au.com.floodaid.provider.Place;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * This class manages a set of items to be displayed on the map
 * 
 * @author hsterin
 */
public class MapItemsOverlay extends ItemizedOverlay<OverlayItem> {
	
	// This list will hold the list of items to overlay on the map
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	
	// Context of the map
	private Context mContext;
	
	/**
	 * Constructor 
	 * 
	 * @param defaultMarker
	 */
	public MapItemsOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}
	
	/**
	 * Constructor with context
	 * 
	 * @param defaultMarker
	 * @param context
	 */
	public MapItemsOverlay(Drawable defaultMarker, Context context) {
		  super(boundCenter(defaultMarker));
		  mContext = context;
		}

	/**
	 * Add an item to the overlay
	 * 
	 * @param overlay
	 */
	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}
	
	/**
	 * Required method called by populate();
	 */
	@Override
	protected OverlayItem createItem(int i) {
	  return mOverlays.get(i);
	}
	
	/**
	 * Required method
	 */
	@Override
	public int size() {
	  return mOverlays.size();
	}
	
	
	@Override
	protected boolean onTap(int index) {
		
	  OverlayItem item = mOverlays.get(index);
	  
	  // Add special window if the item is a Place
	  if (item instanceof PlaceOverlayItem) {
		  Place p = ((PlaceOverlayItem) item).getPlace();
		  View view = View.inflate(mContext, R.layout.map_dialog, null);
		  TextView textView = (TextView) view.findViewById(R.id.map_dialog_text);
		  textView.setMovementMethod(LinkMovementMethod.getInstance());
		  textView.setText(p.getAddress() + "\n" + p.getWebsite() + "\n\n" + p.getDetails());
		  
		  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		  dialog.setTitle(p.getName());
		  dialog.setView(view);
		  dialog.show();
	  }
	  // Default OverlayItem
	  else {
		  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		  dialog.setTitle(item.getTitle());
		  dialog.setMessage(item.getSnippet());
		  dialog.show();
	  }
	  
	  return true;
	}
}
