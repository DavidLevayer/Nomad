package android.nomadproject.com.nomad.mapfragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    private static int indice = 0;
    private final static int[] defaultplaces = {
            R.drawable.place0,
            R.drawable.place1,
            R.drawable.place2,
            R.drawable.place3,
            R.drawable.place4,
            R.drawable.place5,
            R.drawable.place6,
            R.drawable.place7,
            R.drawable.place8,
            R.drawable.place9
    };

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
        else {
            Bitmap b = BitmapFactory.decodeResource(getContext().getResources(),defaultplaces[indice]);
            item.setImage(b);
            mImage.setImageBitmap(item.getImage());
            indice = (indice+1) % defaultplaces.length;
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
