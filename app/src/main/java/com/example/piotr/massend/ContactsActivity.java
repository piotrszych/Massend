package com.example.piotr.massend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.piotr.massend.utils.Consts;
import com.example.piotr.massend.utils.Contact;
import com.example.piotr.massend.utils.DatabaseDummy;

import java.util.ArrayList;
import java.util.Collections;

public class ContactsActivity extends Activity {

    //database
    DatabaseDummy db;

    //controls
    ListView contactsListView;

    ContactsArrayAdapter caa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        //get database
        db = DatabaseDummy.getInstance(this);

        //initialize controls
        contactsListView = (ListView) findViewById(R.id.contacts_list_view);
        ArrayList<Contact[]> groupedListOfContacts = db.getAllContactsGrouped();
        caa = new ContactsArrayAdapter(ContactsActivity.this, groupedListOfContacts);
        contactsListView.setAdapter(caa);
    }

    @Override
    public void onBackPressed() {

        ArrayList<Contact> contactsToSend = caa.getCheckedContacts();
        Intent data = new Intent();
        data.putExtra(Consts.DATA_CONTACTS, contactsToSend);
        setResult(RESULT_OK, data);
        finish();
    }

    private class ContactsArrayAdapter extends ArrayAdapter<Contact> {

        ArrayList<Contact[]> contactsGroupsList;
        ArrayList<Contact> contactsList;

        public ContactsArrayAdapter(Context context, ArrayList<Contact[]> contacts) {
            super(context, 0);
            //construct super-array. NOTE: they are already sorted!
            contactsList = new ArrayList<>();
            for(Contact[] contactsArray : contacts) {
                Collections.addAll(contactsList, contactsArray);
            }
            //add all contacts
            addAll(contactsList);
            this.contactsGroupsList = contacts;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Contact contact = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_contacts, parent, false);
            }

            //handle separator
            RelativeLayout separator = (RelativeLayout) convertView.findViewById(R.id.listitem_separator_wrapper);
            if(position == 0 || !getItem(position - 1).getGroup().equals(contact.getGroup())) {
                TextView separatorTitle = (TextView) convertView.findViewById(R.id.listitem_contacts_separator_name);
                separatorTitle.setText(contact.getGroup());
                //getView is called multiple times, so we have to show separator for each iteration
                separator.setVisibility(View.VISIBLE);

                //TODO add checkbox listener
                CheckBox groupCB = (CheckBox) convertView.findViewById(R.id.listitem_contacts_group_checkbox);
                groupCB.setVisibility(View.GONE);
            }
            else {
                separator.setVisibility(View.GONE);
            }

            //handle item
            TextView itemName = (TextView) convertView.findViewById(R.id.listitem_contacts_name);
            itemName.setText(contact.getName());
            CheckBox itemCheckBox = (CheckBox) convertView.findViewById(R.id.listitem_contacts_item_checkbox);
            itemCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //isChecked is NEW state of button
                    contact.checkOrUncheck();
                    if(isWholeGroupChecked(contact.getGroup())) {
                        //TODO check/uncheck group
                    }
                }
            });

            return convertView;
        }

        protected ArrayList<Contact> getCheckedContacts() {
            ArrayList<Contact> contactsToReturn = new ArrayList<>();

            for (Contact contact : contactsList) {
                if(contact.isChecked()) {
                    contactsToReturn.add(contact);
                }
            }

            return contactsToReturn;
        }

        protected boolean isWholeGroupChecked(String groupName) {
            for(Contact contact : contactsList) {
                if(contact.getGroup().equals(groupName) && !contact.isChecked())
                    return false;
            }

            return true;
        }

        protected int getFirstGroupItemPosition(String groupName) {
            for(int i = 0; i < contactsList.size(); i++) {
                if(contactsList.get(i).getGroup().equals(groupName)) {
                    return i;
                }
            }
            return -1;
        }
    }
}
