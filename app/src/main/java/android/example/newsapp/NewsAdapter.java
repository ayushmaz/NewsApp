package android.example.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News>{

    @NonNull
    @Override
    public View getView(int position,@Nullable View convertView,@NonNull ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        News currentNews = getItem(position);

        TextView sectionName = (TextView) convertView.findViewById(R.id.section_name);
        sectionName.setText(currentNews.getSectionName());

        TextView title = (TextView)convertView.findViewById(R.id.title);
        title.setText(currentNews.getTitle());

        TextView date = (TextView) convertView.findViewById(R.id.date);
        date.setText(currentNews.getDate());

        TextView time = (TextView) convertView.findViewById(R.id.time);
        time.setText(currentNews.getTime());

        TextView circleTextView = (TextView) convertView.findViewById(R.id.circle_view);
        circleTextView.setText(currentNews.getSectionName().substring(0,1));
        return convertView;
    }

    public NewsAdapter(@NonNull Context context, ArrayList<News> news_list) {
        super(context,0,news_list);
    }
}
