package com.example.piotr.massend.utils;

public class Consts {
    public static int MIN_CHARS_LOGIN = 3;
    public static int MIN_CHARS_PASSWORD = 6;

    public static boolean DO_TRUNCATE = false;       //true if reset

    public static final int REQUEST_CODE_GET_TEMPLATE = 0x001;
    public static final int REQUEST_CODE_NOT_COMPLETED = 0x010;
    public static final int REQUEST_CODE_ADD_TEMPLATE = 0x005;
    public static final int REQUEST_CODE_EDIT_TEMPLATE = 0x006;
    public static final int REQUEST_CODE_GET_CONTACTS = 0x011;

    //communication between activities
    public static final String DATA_TEMPLATE_NAME = "dataTemplateName";
    public static final String DATA_TEMPLATE_CONTENT = "dataTemplateContent";
    public static final String DATA_TEMPLATE_POSITION = "templatePosition";
    public static final String DATA_CONTACTS = "sentContacts";

    public static final String REQUEST_CODE = "requestCode";
}
