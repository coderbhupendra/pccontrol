package database;

import java.util.ArrayList;
import java.util.List;



import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelperSong extends SQLiteOpenHelper {

  public static final String TABLE_COMMENTS = "comments";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_COMMENT = "comment";

  private static final String DATABASE_NAME = "commment.db";
  private static final int DATABASE_VERSION = 17;

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table "
      + TABLE_COMMENTS + "(" + COLUMN_ID
      + " integer primary key autoincrement, " + COLUMN_COMMENT
      + " text not null);";

  
  public MySQLiteHelperSong(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
	  
    database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
    onCreate(db);
  }
  
  public List<String> getAllToDos() {
		List<String> todos = new ArrayList<String>();
		String selectQuery = "SELECT  * FROM " + TABLE_COMMENTS;

		Log.e("LOG", selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				
				// adding to todo list
				todos.add(c.getString(c.getColumnIndex(COLUMN_COMMENT)));
			} while (c.moveToNext());
		}

		return todos;
	}

  public void deleteToDo(String comm) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_COMMENTS, COLUMN_COMMENT + " = ?",
				new String[] { String.valueOf(comm) });
		
	}
  
} 