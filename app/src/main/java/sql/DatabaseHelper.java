package sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import model.User;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "UserManager.db";

    // User table name
    private static final String TABLE_USER = "user";
    private static final String TABLE_USERDETAILS = "userdetails"; //notsure pa if i will separate them
    private static final String TABLE_RECORD="record"; //record table
    private static final String TABLE_REFMTYPE="refmtype"; //REFMTYPE table
    private static final String TABLE_ALARM="alarm"; //alarm table
    private static final String TABLE_TIPS="tips"; //tips table

    // User Table Columns names
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_FIRSTNAME = "user_first_name";
    private static final String COLUMN_USER_LASTNAME = "user_last_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    private static final String COLUMN_USER_BDAY = "user_bday";
    private static final String COLUMN_USER_HEIGHT = "user_height";
    private static final String COLUMN_USER_WEIGHT = "user_weight";
    private static final String COLUMN_USER_INSULINTYPE = "user_insulin";
    private static final String COLUMN_USER_MAX_BLOODTYPE = "user_max_bloodtype";
    private static final String COLUMN_USER_MIN_BLOODTYPE = "user_min_bloodtype";
    private static final String COLUMN_USER_TIPSID = "user_tipsid";

    //Record Table
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_MTYPE = "meal_type";
    private static final String COLUMN_SUGARVALUE= "sugar_value";
    private static final String COLUMN_TIME = "time";

    //Ref MealType Record
    private static final String COLUMN_MTYPE_ID ="mealtype_id";
    private static final String COLUMN_MTYPE_NAME ="mealtype_name";

    //Alarm Table
    private static final String COLUMN_ALARM_HOUR ="alarm_hr";
    private static final String COLUMN_ALARM_MIN ="alarm_min";

    //Tips Table
    private static final String COLUMN_TIP_ID ="tip_id";
    private static final String COLUMN_TIP_TITLE ="tip_title";
    private static final String COLUMN_TIP_DESC ="tip_desc";
    private static final String COLUMN_TIP_IMAGE ="tip_image";



    // create table sql query
    //USERS TABLE
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USERNAME + " TEXT PRIMARY KEY,"
            + COLUMN_USER_FIRSTNAME + " TEXT,"
            + COLUMN_USER_LASTNAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT,"
            + COLUMN_USER_BDAY +"TEXT,"
            + COLUMN_USER_HEIGHT + "TEXT,"
            + COLUMN_USER_WEIGHT + "TEXT,"
            + COLUMN_USER_INSULINTYPE + "TEXT,"
            + COLUMN_USER_MAX_BLOODTYPE +"INTEGER,"
            + COLUMN_USER_MIN_BLOODTYPE + "INTEGER,"
            + COLUMN_USER_TIPSID + "TEXT" + ")";

    //RECORD TABLE
    private String CREATE_TABLE_RECORD = "CREATE TABLE " + TABLE_RECORD + "("
            + COLUMN_USERNAME + "TEXT,"
            + COLUMN_MTYPE + " INTEGER,"
            + COLUMN_SUGARVALUE + " INTEGER,"
            + COLUMN_TIME + "TIME,"
            + "FOREIGN KEY ("+COLUMN_USERNAME+") REFERENCES "+TABLE_USER+"("+COLUMN_USERNAME+")"
            + "FOREIGN KEY ("+COLUMN_MTYPE+") REFERENCES "+TABLE_REFMTYPE+"("+COLUMN_MTYPE+")" + ")";

    //REF MTYPE TABLE
    private String CREATE_TABLE_REFMTYPE = "CREATE TABLE" + TABLE_REFMTYPE + "("
            + COLUMN_MTYPE_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_MTYPE_NAME + "TEXT" + ")";

    //ALARM TABLE
    private String CREATE_TABLE_ALARM = "CREATE TABLE" + TABLE_ALARM + "("
            + COLUMN_ALARM_HOUR + "NUMBER,"
            + COLUMN_ALARM_MIN + "NUMBER,"
            + COLUMN_USERNAME + "TEXT"
            + "FOREIGN KEY ("+COLUMN_USERNAME+") REFERENCES "+TABLE_USER+"("+COLUMN_USERNAME+")" + ")";

    //TIPS TABLE
    private String CREATE_TABLE_TIPS = "CREATE TABLE" + TABLE_TIPS + "("
            + COLUMN_TIP_ID + "INTEGER,"
            + COLUMN_TIP_TITLE + "TEXT,"
            + COLUMN_TIP_DESC + "TEXT,"
            + COLUMN_TIP_IMAGE + "BLOB"
            + COLUMN_USER_TIPSID + "TEXT"
            + "FOREIGN KEY ("+COLUMN_USER_TIPSID+") REFERENCES "+TABLE_USER+"("+COLUMN_USER_TIPSID+")" + ")";



    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private String DROP_TABLE_RECORD = "DROP TABLE IF EXISTS " + TABLE_RECORD;
    private String DROP_TABLE_REFMTYPE = "DROP TABLE IF EXISTS " + TABLE_REFMTYPE;
    private String DROP_TABLE_ALARM = "DROP TABLE IF EXISTS " + TABLE_ALARM;
    private String DROP_TABLE_TIPS = "DROP TABLE IF EXISTS " + TABLE_TIPS;

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_TABLE_RECORD);
        db.execSQL(CREATE_TABLE_REFMTYPE);
        db.execSQL(CREATE_TABLE_ALARM);
        db.execSQL(CREATE_TABLE_TIPS);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_TABLE_RECORD);
        db.execSQL(DROP_TABLE_REFMTYPE);
        db.execSQL(DROP_TABLE_ALARM);
        db.execSQL(DROP_TABLE_TIPS);

        // Create tables again
        onCreate(db);

    }

    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_FIRSTNAME, user.getFname());
        values.put(COLUMN_USER_LASTNAME, user.getLname());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        values.put(COLUMN_USER_BDAY, user.getBday());
        values.put(COLUMN_USER_HEIGHT, user.getHeight());
        values.put(COLUMN_USER_WEIGHT, user.getWeight());
        values.put(COLUMN_USER_INSULINTYPE, user.getInsulintype());
        values.put(COLUMN_USER_MAX_BLOODTYPE,user.getMaxblood());
        values.put(COLUMN_USER_MIN_BLOODTYPE,user.getMinblood());
        values.put(COLUMN_USER_TIPSID,user.getTips());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }
    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(COLUMN_USER_NAME, user.getId());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // updating row
        //db.update(TABLE_USER, values, COLUMN_USER_NAME + " = ?",
            //    new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method to check user exist or not
     *
     * @param id
     * @return true/false
     */
    public boolean checkUser(String id) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_NAME
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?";

        // selection argument
        String[] selectionArgs = {id};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'ectect@yahoo.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param id
     * @param password
     * @return true/false
     */
    public boolean checkUser(String id, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_NAME
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {id, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }
}