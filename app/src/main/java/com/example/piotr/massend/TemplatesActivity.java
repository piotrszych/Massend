package com.example.piotr.massend;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piotr.massend.utils.Consts;
import com.example.piotr.massend.utils.DatabaseDummy;
import com.example.piotr.massend.utils.Template;

import java.util.ArrayList;

public class TemplatesActivity extends Activity {

    //controls
    private ListView lv_template_holder;
    private Button button_add_new;
    DatabaseDummy db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates);

        db = DatabaseDummy.getInstance(getApplicationContext());

        lv_template_holder = (ListView) findViewById(R.id.templates_listview);

        ArrayList<Template> alist_template = db.getAllTemplates();

        final TemplateArrayAdapter taa = new TemplateArrayAdapter(this, alist_template);
        lv_template_holder.setAdapter(taa);
        lv_template_holder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String templateContent = ((Template) lv_template_holder.getItemAtPosition(position)).getContent();
                //prepare and return data
                Intent data = new Intent();
                data.putExtra(Consts.DATA_TEMPLATE_CONTENT, templateContent);
                setResult(Consts.REQUEST_CODE_GET_TEMPLATE, data);
                finish();
            }
        });

        lv_template_holder.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO wybor: edycja, usuniecie
                String[] options = {"Edytuj", "Usuń"};

                final Template template = (Template) lv_template_holder.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(TemplatesActivity.this);
                builder.setTitle(template.getName());
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:     //edytuj
                                //TODO obsluga edycji
                                Toast.makeText(TemplatesActivity.this, "Edycja", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:     //usun
                                ConfirmDeleteDialog confirmDelete = new ConfirmDeleteDialog(TemplatesActivity.this, template, taa, db);
                                confirmDelete.show();
                                break;
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });

        //button for adding new template
        button_add_new = (Button) findViewById(R.id.templates_button_add_new);
        button_add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNewTemplateIntent = new Intent(TemplatesActivity.this, TemplateEditAddActivity.class);
                addNewTemplateIntent.putExtra(Consts.REQUEST_CODE, Consts.REQUEST_CODE_ADD_TEMPLATE);
                startActivityForResult(addNewTemplateIntent, Consts.REQUEST_CODE_ADD_TEMPLATE);
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

    private class ConfirmDeleteDialog extends AlertDialog {
        public ConfirmDeleteDialog(Context context, final Template t, final TemplateArrayAdapter taa, final DatabaseDummy db) {
            super(context);

            setTitle(R.string.templates_delete_warning_title);
            setMessage(getString(R.string.templates_delete_warning, t.getName()));
            setButton(BUTTON_POSITIVE, getString(R.string.dialog_yes), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.deleteTemplateByName(t.getName());
                    taa.remove(t);
                    //notify adapter of changes
                    taa.notifyDataSetChanged();
                }
            });
            setButton(BUTTON_NEGATIVE, getString(R.string.dialog_no), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });
        }
    }
}

