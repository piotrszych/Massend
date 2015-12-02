package com.example.piotr.massend;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.piotr.massend.utils.DatabaseDummy;
import com.example.piotr.massend.utils.Template;

import java.util.ArrayList;

public class TemplatesActivity extends AppCompatActivity {

    private ListView lv_template_holder;
    DatabaseDummy db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates);

        db = DatabaseDummy.getInstance(getApplicationContext());

        lv_template_holder = (ListView) findViewById(R.id.templates_listview);

        ArrayList<Template> alist_template = db.getAllTemplates();

        TemplateArrayAdapter taa = new TemplateArrayAdapter(this, alist_template);
        lv_template_holder.setAdapter(taa);
        lv_template_holder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO przechodzenie do aktywnosci template
            }
        });

        lv_template_holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //TODO usuwanie
                return false;
            }
        });
    }

    private class TemplateArrayAdapter extends ArrayAdapter<Template> {

        public TemplateArrayAdapter(Context context, ArrayList<Template> templates_title) {
            super(context, 0, templates_title);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Template tpl = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_templates, parent, false);
            }
            TextView tvTitle = (TextView) convertView.findViewById(R.id.listitem_templates_tv);
            tvTitle.setText(tpl.getName());
            return convertView;
        }
    }
}

