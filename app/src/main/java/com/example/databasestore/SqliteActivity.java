package com.example.databasestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.databasestore.SqliteHelper;

import org.w3c.dom.Text;

public class SqliteActivity extends AppCompatActivity {

    private TextView currBalance, time, amount, reason;
    private TableLayout history;
    private TableRow row;
    private ScrollView scroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase db;
        db = openOrCreateDatabase("TransactionHistory", MODE_PRIVATE, null);

        currBalance = findViewById(R.id.textView);
        history = findViewById(R.id.tableLayout);
        row = findViewById(R.id.tableRow);
        time = findViewById(R.id.time);
        scroller = findViewById(R.id.scrollView2);

        //db.execSQL("Drop Table If Exists History");

        //SqliteHelper dbHelper = new SqliteHelper(this);
        //db = dbHelper.getWritableDatabase();

        final String CREATE_TABLE_CONTAIN = "CREATE TABLE IF NOT EXISTS History ("
                + "ID INTEGER primary key AUTOINCREMENT,"
                + "Date TEXT,"
                + "Amount DOUBLE,"
                + "Reason TEXT);";
        db.execSQL(CREATE_TABLE_CONTAIN);

        /*db.execSQL("Insert Into History (Date, Amount, Reason) Values ('3-24-2020', 50.00, 'Allowance')");
        db.execSQL("Insert Into History (Date, Amount, Reason) Values ('3-25-2020', 800.00, 'Grief')");
        db.execSQL("Insert Into History (Date, Amount, Reason) Values ('4-13-2020', 70.00, 'Dinner')");
        db.execSQL("Insert Into History (Date, Amount, Reason) Values ('4-30-2020', 220.00, 'Vacation')");
        db.execSQL("Insert Into History (Date, Amount, Reason) Values ('5-2-2020', 300.00, 'Graduation')");
        db.execSQL("Insert Into History (Date, Amount, Reason) Values ('5-2-2020', -100.00, 'Expenses')");
        db.execSQL("Insert Into History (Date, Amount, Reason) Values ('5-30-2020', 55.00, 'Gift-Cards')");*/

        long count = DatabaseUtils.queryNumEntries(db, "History");

        String s[] = {"id", "date", "amount", "reason"};
        Cursor a = db.query("History", s, null, null, null, null, null);
        for(int i = 0; i < count; i++) {
            a.moveToPosition(i);
            history.addView(createNewTextView2(a.getString(1), a.getString(2), a.getString(3)));
        }
        a.close();

        //Cursor b = db.rawQuery("SELECT SUM(Amount) as Total FROM History", null);
        //int balance = b.getInt(b.getColumnIndex("Total"));

        currBalance.setText("$1380.00");
        //b.close();
    }

    private TextView createNewTextView1(String added) {
        final TextView text = new TextView(this);
        text.setText(added);
        return text;
    }
    private TableRow createNewTextView2(String date, String amount, String reason) {
        final TableRow text = new TableRow(this);
        text.addView(createNewTextView1(date), 305, 170);
        text.addView(createNewTextView1(amount), 365, 170);
        text.addView(createNewTextView1(reason), 300, 170);
        return text;
    }
}
