package tcss450.uw.edu.mobileproject.offlineDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import tcss450.uw.edu.mobileproject.R;
import tcss450.uw.edu.mobileproject.model.Answer;
import tcss450.uw.edu.mobileproject.model.Question;

/**
 * Created by KyleD on 5/30/16.
 */
public class ProjectDB {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Question.db";

    public static final String QUESTION_TABLE = "Questions";
    private static final String TAG_TABLE = "Tags";
    private SQLiteDatabase mSQLiteDatabase;

    public ProjectDB(Context context) {
        ProjectDBHelper dbHelper = new ProjectDBHelper(context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = dbHelper.getWritableDatabase();
    }

    /**
     * Inserts the course into the local sqlite table. Returns true if successful, false otherwise.
     * @param id
     * @param email
     * @param date
     * @param questDetail
     * @param company
     * @return true or false
     */
    public boolean insertQuestion(String id, String email, String date, String questDetail, String company) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("QuestID", id);
        contentValues.put("email", email);
        contentValues.put("DatePost", date);
        contentValues.put("QuestDetail", questDetail);
        contentValues.put("Company", company);

        long rowId = mSQLiteDatabase.insert(QUESTION_TABLE, null, contentValues);
        return rowId != -1;
    }

    public void deleteQuestionsTable() {
        mSQLiteDatabase.delete(QUESTION_TABLE, null, null);
    }

    public void closeDB() {
        mSQLiteDatabase.close();
    }

    public boolean insertTag(String tagName, String questId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("TagName", tagName);
        contentValues.put("QuestID", questId);

        long rowId = mSQLiteDatabase.insert(TAG_TABLE, null, contentValues);
        return rowId != -1;
    }

    public void deleteTagsTable() {
        mSQLiteDatabase.delete(TAG_TABLE, null, null);
    }

    /**
     * Returns the list of questions from the local Question table.
     * @return
     */
    public List<Question> getQuestionsList() {
        String[] columns = {
                "QuestID","email","DatePost","QuestDetail","Company"
        };

        Cursor c = mSQLiteDatabase.query(
                QUESTION_TABLE,   // The table to query
                columns,        // The columns to return
                null,           // the columns for the WHERE clause
                null,           // the values for the WHERE clause
                null,           // don't group the rows
                null,           // don't filter by row groups
                null            // The sort order
        );
        return convertToQuestionList(c);
    }

    private List<Question> convertToQuestionList(Cursor c) {
        c.moveToFirst();
        List<Question> list = new ArrayList<>();
        for (int i = 0; i < c.getCount(); i++) {
            String id = c.getString(0);
            String email = c.getString(1);
            String date = c.getString(2);
            String questDetail = c.getString(3);
            String company = c.getString(4);
            Question quest = new Question(id, email, date, questDetail, company);
            list.add(quest);
            c.moveToNext();
        }

        return list;
    }

    public List<Question> getQuestionsListBasedOnTag(String tag) {
        String myQuery = "SELECT Questions.QuestID, email, DatePost, QuestDetail, Company " +
                "FROM Questions JOIN Tags ON Questions.QuestID = Tags.QuestID " +
                "WHERE TagName=? ORDER BY DatePost DESC";
        Cursor c = mSQLiteDatabase.rawQuery(myQuery, new String[]{tag});
        return convertToQuestionList(c);
    }

    /**
     * Returns the list of tags from the local Tag table.
     * @return
     */
    public List<String> getTagsList() {
        String[] columns = {
                "TagName"
        };
        Cursor c = mSQLiteDatabase.query(true,
                TAG_TABLE,   // The table to query
                columns,        // The columns to return
                null,           // the columns for the WHERE clause
                null,           // the values for the WHERE clause
                null,           // don't group the rows
                null,           // don't filter by row groups
                "TagName",           // The sort order
                null
        );
        c.moveToFirst();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < c.getCount(); i++) {
            list.add(c.getString(0));
            c.moveToNext();
        }
        return list;
    }

    class ProjectDBHelper extends SQLiteOpenHelper {
        private final String CREATE_QUESTION_SQL;
        private final String CREATE_TAG_SQL;

        private final String DROP_QUESTION_SQL;
        private final String DROP_TAG_SQL;

        public ProjectDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            CREATE_QUESTION_SQL = context.getString(R.string.CREATE_QUESTION_SQL);
            DROP_QUESTION_SQL = context.getString(R.string.DROP_QUESTION_SQL);
            CREATE_TAG_SQL = context.getString(R.string.CREATE_TAG_SQL);
            DROP_TAG_SQL = context.getString(R.string.DROP_TAG_SQL);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_QUESTION_SQL);
            sqLiteDatabase.execSQL(CREATE_TAG_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_QUESTION_SQL);
            sqLiteDatabase.execSQL(DROP_TAG_SQL);
            onCreate(sqLiteDatabase);
        }
    }


}
