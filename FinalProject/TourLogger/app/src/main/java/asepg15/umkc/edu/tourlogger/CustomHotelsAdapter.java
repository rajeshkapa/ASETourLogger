package asepg15.umkc.edu.tourlogger;

/**
 * Created by kh1441 on 4/13/15.
 */


        import android.app.Activity;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.AsyncTask;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import java.io.InputStream;

public class CustomHotelsAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final String[] imgid;
    private final String[] vicinity;

    public CustomHotelsAdapter(Activity context, String[] itemname, String[] imgid,String[] vicinity) {
        super(context, R.layout.activity_hotels, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
        this.vicinity=vicinity;
    }
    private static final String LOG_TAG = "ExampleApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/photo";

    private static final String API_KEY = "AIzaSyA23jRoKzRI16Al7c88DpHxHDuW_OUtLjU";
    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.activity_hotels, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);
        TextView data=(TextView) rowView.findViewById(R.id.textView2);
        txtTitle.setText(itemname[position]);
        extratxt.setText(vicinity[position]);
        StringBuilder jsonResults = new StringBuilder();
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE);
            sb.append("?key=" + API_KEY);
            sb.append("&photoreference=" + imgid[position]);
            sb.append("&maxwidth=40");
            System.out.println("URL for Hotels"+sb.toString());
            new DownloadImageTask(imageView).execute(sb.toString());

        return rowView;

    };

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}