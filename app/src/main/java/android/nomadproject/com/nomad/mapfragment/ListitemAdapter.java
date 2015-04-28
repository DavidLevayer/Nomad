package android.nomadproject.com.nomad.mapfragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.nomadproject.com.nomad.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by David Levayer on 27/04/15.
 */
public class ListitemAdapter extends ArrayAdapter<Listitem> {

    public ListitemAdapter(Context context, int resource, Listitem[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        // Get the data item for this position
        Listitem item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_map_listitem, parent, false);
        }

        TextView mTitle = (TextView) convertView.findViewById(R.id.title);
        ImageView mImage = (ImageView) convertView.findViewById(R.id.image);
        mTitle.setText(item.getTitle());
        Bitmap image = item.getImage();
        if(image != null)
            mImage.setImageBitmap(item.getImage());

        // Return the completed view to render on screen
        return convertView;
    }
}
