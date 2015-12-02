package com.example.piotr.massend.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DatabaseDummy {

    static DatabaseDummy instance;

    //table names etc.
    private final String TABLE_USERS = "Users";
    private final String COL_USERS_USERNAME = "username";
    private final String COL_USERS_PASSWORD = "password";

    private final String TABLE_TEMPLATES = "Template";
    private final String COL_TEMPLATES_ID = "id_template";
    private final String COL_TEMPLATES_TITLE = "title";
    private final String COL_TEMPLATES_CONTENT = "content";

    private final String TABLE_CONTACTS = "Contacts";
    private final String COL_CONTACTS_ID = "id_contact";
    private final String COL_CONTACTS_NAME = "cname";
    private final String COL_CONTACTS_PHONE = "cphone_number";
    private final String COL_CONTACTS_GROUP = "cgroup";

    private final String TABLE_WKLEJKA = "Wklejka";
    private final String COL_WKLEJKA_ID = "id_wklejka";
    private final String COL_WKLEJKA_CONTENT = "wk_content";

    private Context context;    //should be initialized with application context
    private SQLiteDatabase database;

    public static DatabaseDummy getInstance(Context context) {
        if(instance == null) {
            instance = new DatabaseDummy(context);
        }
        return instance;
    }

    private DatabaseDummy(Context context) {
        if(instance == null) {
            this.context = context;
            database = context.openOrCreateDatabase("database.mp3", Context.MODE_PRIVATE, null);
            for(String create_table_query : getCreateDatabaseQueries()) {
                database.execSQL(create_table_query);
            }
            //check if populated
            String count = "SELECT COUNT(*) FROM " + TABLE_CONTACTS;
            Cursor c = database.rawQuery(count, null);
            c.moveToFirst();
            if(c.getInt(0) == 0 || Consts.DO_TRUNCATE) {
                populateDatabase(Consts.DO_TRUNCATE);
            }
            c.close();
        }
    }

    private String[] getCreateDatabaseQueries() {
        return new String[]{"CREATE TABLE IF NOT EXISTS " + TABLE_TEMPLATES + " (" +
                COL_TEMPLATES_ID + " INTEGER PRIMARY KEY," +
                COL_TEMPLATES_TITLE + " TEXT," +
                COL_TEMPLATES_CONTENT + " TEXT" +
                ");"
                , "CREATE TABLE IF NOT EXISTS " +TABLE_CONTACTS+ " (" +
                COL_CONTACTS_ID + " INTEGER PRIMARY KEY," +
                COL_CONTACTS_NAME + " TEXT," +
                COL_CONTACTS_PHONE + " TEXT," +
                COL_CONTACTS_GROUP + " TEXT" +
                ");"
                , "CREATE TABLE IF NOT EXISTS " + TABLE_WKLEJKA + " (" +
                COL_WKLEJKA_ID + " INTEGER PRIMARY KEY," +
                COL_WKLEJKA_CONTENT + " TEXT" +
                ");"
                , "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                COL_USERS_USERNAME + " TEXT PRIMARY KEY," +
                COL_USERS_PASSWORD + " TEXT" +
                ");"};
    }

    private long insertIntoContact(String name, String number, String group) {

        ContentValues cv = new ContentValues();
        cv.put(COL_CONTACTS_NAME, name);
        cv.put(COL_CONTACTS_PHONE, number);
        cv.put(COL_CONTACTS_GROUP, group);
        return database.insert(TABLE_CONTACTS, null, cv);
    }

    private long insertIntoUsers(String username, String password) {
        ContentValues cv = new ContentValues();
        cv.put(COL_USERS_USERNAME, username);
        cv.put(COL_USERS_PASSWORD, password);
        return database.insert(TABLE_USERS, null, cv);
    }

    //TODO edit template

    public long insertIntoTemplates(String name, String content) {
        ContentValues cv = new ContentValues();
        cv.put(COL_TEMPLATES_TITLE, name);
        cv.put(COL_TEMPLATES_CONTENT, content);
        return database.insert(TABLE_TEMPLATES, null, cv);
    }

    public void deleteTemplateByName(String name) {
        database.delete(TABLE_TEMPLATES, COL_TEMPLATES_TITLE + " = " + name, null);
    }

    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> alist_toReturn = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_CONTACTS;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            alist_toReturn.add(new Contact(cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            cursor.moveToNext();
        }
        cursor.close();
        return alist_toReturn;
    }

    public ArrayList<Template> getAllTemplates() {
        ArrayList<Template> alist_toReturn = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_TEMPLATES;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            alist_toReturn.add(new Template(cursor.getString(1), cursor.getString(2)));
            cursor.moveToNext();
        }
        cursor.close();
        return alist_toReturn;
    }

    private void populateDatabase(boolean ifTruncate) {
        if(ifTruncate) {
            String[] turncate_queries = {"DELETE FROM " + TABLE_USERS + ";"
                    , "DELETE FROM " + TABLE_CONTACTS + ";"
                    , "DELETE FROM " + TABLE_TEMPLATES + ";"
                    , "DELETE FROM " + TABLE_WKLEJKA + ";"
                    , "VACUUM;"
            };
            for (String query :
                    turncate_queries) {
                database.execSQL(query);
            }
        }

        //contacts
        insertIntoContact("Franciszek Kimono", "555666222", "DiscoBuddies");
        insertIntoContact("Saitama", "123679222", "DiscoBuddies");
        insertIntoContact("Mama", "123789456", "Rodzina");
        insertIntoContact("Brat", "745832609", "Rodzina");
        insertIntoContact("Tata", "777741252", "Rodzina");
        //templates
        insertIntoTemplates("Zdanie egzaminu", "Cześć, chciałem tylko dać znać, że udało mi się zaliczyć egzamin z ");
        insertIntoTemplates("Impra dziś", "Sup, wbijamy gdzieś w miasto o  ?");
        //users
        insertIntoUsers("user", "password");
        insertIntoUsers("fkimono", "klubdisco");
    }

    public int checkLoginPassword(String username, String password) {
        //0 if user/pass match, 1 if user match, 2 if user does not match

        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USERS_USERNAME + " = '" + username + "'";
        Cursor cursor = database.rawQuery(query, null);
        if(!cursor.moveToFirst()) {
            cursor.close();
            return 2;
        }
        else {
            if(cursor.getString(1).equals(password)) {
                cursor.close();
                return 0;
            }
            else {
                cursor.close();
                return 1;
            }
        }
    } //public int checkLoginPassword(String username, String password) {
}
