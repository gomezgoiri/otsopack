package otsopack.droid.dataaccess.simplestore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OtsobaseOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    
    static private final String DATABASE_NAME = "Otsopack";
	static protected final String TABLE_NAME = "Graphs";
	
    final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
		"	graphuri VARCHAR(1000)," +
		"	spaceuri VARCHAR(1000)," +
		"	format VARCHAR(100)," +
		"	data BLOB" +
		");";

    OtsobaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}