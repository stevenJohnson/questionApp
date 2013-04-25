package utilities;

import java.util.ArrayList;
import java.util.HashMap;

import edu.drake.questionapp.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AListAdapter extends ArrayAdapter<HashMap<String,String>> {
  private final Context context;
  //private final String[] names;
 //private final String[] questions;
  private final ArrayList<HashMap<String, String>> list;


  public AListAdapter(Context context, ArrayList<HashMap<String, String>> list) {
    super(context, R.layout.anslayout, list);
    this.context = context;
    this.list = list;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.anslayout, parent, false);
    TextView textViewA = (TextView) rowView.findViewById(R.id.answer);
    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
    TextView textViewN = (TextView) rowView.findViewById(R.id.name);
    TextView textViewL = (TextView) rowView.findViewById(R.id.likes);
    HashMap<String, String> ansGet = list.get(position);
    textViewA.setText(ansGet.get("answer"));
    HashMap<String, String> nameGet = list.get(position);
    textViewN.setText(nameGet.get("name"));
    textViewL.setText("10 likes");
  
    return rowView;
  }
} 