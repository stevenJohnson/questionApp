package edu.drake.questionapp;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class QListAdapter extends ArrayAdapter<HashMap<String,String>> {
  private final Context context;
  //private final String[] names;
 //private final String[] questions;
  private final ArrayList<HashMap<String, String>> list;


  public QListAdapter(Context context, ArrayList<HashMap<String, String>> list) {
    super(context, R.layout.rowlayout, list);
    this.context = context;
    this.list = list;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
    TextView textViewQ = (TextView) rowView.findViewById(R.id.question);
    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
    TextView textViewN = (TextView) rowView.findViewById(R.id.name);
    TextView textViewA = (TextView) rowView.findViewById(R.id.answers);
    TextView textViewL = (TextView) rowView.findViewById(R.id.likes);
    HashMap<String, String> quesGet = list.get(position);
    textViewQ.setText(quesGet.get("question"));
    HashMap<String, String> nameGet = list.get(position);
    textViewN.setText(nameGet.get("name"));
    textViewA.setText("5 A ");
    textViewL.setText("10 likes");
  
    return rowView;
  }
} 