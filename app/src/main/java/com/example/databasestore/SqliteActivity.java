package com.example.databasestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText dateFromEdit, dateToEdit, amountFromEdit, amountToEdit;
    private Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SQLiteDatabase db;
        db = openOrCreateDatabase("TransactionHistory", MODE_PRIVATE, null);

        currBalance = findViewById(R.id.textView);
        history = findViewById(R.id.tableLayout);
        row = findViewById(R.id.tableRow);
        time = findViewById(R.id.time);
        scroller = findViewById(R.id.scrollView2);
        dateFromEdit = findViewById(R.id.dateEdit);
        dateToEdit = findViewById(R.id.dateEdit2);
        amountFromEdit = findViewById(R.id.amountFrom);
        amountToEdit = findViewById(R.id.amountTo);
        search = findViewById(R.id.button2);

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

        //Cursor a = db.query("History", s, null, null, null, null, null);
        /*for(int i = 0; i < count; i++) {
            a.moveToPosition(i);
            history.addView(createNewTextView2(a.getString(1), a.getString(2), a.getString(3)));
        }*/
        //a.close();

        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String dateFrom = dateFromEdit.getText().toString();
                String dateTo = dateToEdit.getText().toString();
                String amountFrom = amountFromEdit.getText().toString();
                String amountTo = amountToEdit.getText().toString();
                searchQuery(dateFrom, dateTo, amountFrom, amountTo, db);
            }
        });

        currBalance.setText("$1380.00");
    }

    public void ClearTable(){
        int count = history.getChildCount();
        for (int i = 1; i < count; i++) {
            history.removeViewAt(1);
        }
    }

    public void searchQuery(String dateFrom, String dateTo, String priceFrom, String priceTo, SQLiteDatabase db) {
        ClearTable();
        String s[] = {"id", "date", "amount", "reason"};
        Cursor result = db.query("History", s, null, null, null, null, null);
        Double priceFromDo = null;
        Double priceToDo = null;
        if (!priceFrom.isEmpty())
        {
            priceFromDo = Double.parseDouble(priceFrom);
        }
        if (!priceTo.isEmpty())
        {
            priceToDo = Double.parseDouble(priceTo);
        }

        if (priceFromDo != null && priceToDo == null && dateFrom.isEmpty() && dateTo.isEmpty())
        {
            result = db.rawQuery("SELECT * FROM History WHERE Amount >= " + priceFromDo, null);
        }
        else if (priceFromDo == null && priceToDo != null && dateFrom.isEmpty() && dateTo.isEmpty())
        {
            result = db.rawQuery("SELECT * FROM History WHERE Amount <= " + priceToDo, null);
        }
        else if (priceFromDo == null && priceToDo == null && !dateFrom.isEmpty() && dateTo.isEmpty())
        {
            result = db.rawQuery("SELECT * FROM History WHERE Date >= '" + dateFrom + "'", null);
        }
        else if (priceFromDo == null && priceToDo == null && dateFrom.isEmpty() && !dateTo.isEmpty())
        {
            result = db.rawQuery("SELECT * FROM History WHERE Date <= '" + dateTo + "'", null);
        }
        else if (priceFromDo != null && priceToDo != null && dateFrom.isEmpty() && dateTo.isEmpty())
        {
            result = db.rawQuery("SELECT * FROM History WHERE Amount >= " + priceFromDo + " AND Amount <= " + priceToDo, null);
        }
        else if (priceFromDo == null && priceToDo == null && !dateFrom.isEmpty() && !dateTo.isEmpty())
        {
            result = db.rawQuery("SELECT * FROM History WHERE Date >= '" + dateFrom + "' AND Date <= '" + dateTo + "'", null);
        }
        else if (priceFromDo != null && priceToDo == null && !dateFrom.isEmpty() && dateTo.isEmpty())
        {
            result = db.rawQuery("SELECT * FROM History WHERE Amount >= " + priceFromDo + " AND Date >= '" + dateFrom + "'", null);
        }
        else if (priceFromDo != null && priceToDo == null && dateFrom.isEmpty() && !dateTo.isEmpty())
        {
            result = db.rawQuery("SELECT * FROM History WHERE Amount >= " + priceFromDo + " AND Date <= '" + dateTo + "'", null);
        }
        else if (priceFromDo == null && priceToDo != null && !dateFrom.isEmpty() && dateTo.isEmpty())
        {
            result = db.rawQuery("SELECT * FROM History WHERE Amount <= " + priceToDo + " AND Date >= '" + dateFrom + "'", null);
        }
        else if (priceFromDo == null && priceToDo != null && dateFrom.isEmpty() &&!dateTo.isEmpty())
        {
            result = db.rawQuery("SELECT * FROM History WHERE Amount <= " + priceToDo + " AND Date <= '" + dateTo + "'", null);
        }
        else if (priceFromDo != null && priceToDo != null && !dateFrom.isEmpty() && dateTo.isEmpty())
        {
            result = db.rawQuery("SELECT * FROM History WHERE Amount >= " + priceFromDo + " AND Amount <= " +
                    priceToDo + " AND Date >= '" + dateFrom + "'", null);
        }
        else if (priceFromDo != null && priceToDo != null && dateFrom.isEmpty() && !dateTo.isEmpty())
        {
            result = db.rawQuery("SELECT * FROM History WHERE Amount >= " + priceFromDo + " AND Amount <= " +
                    priceToDo + " AND Date <= '" + dateTo + "'", null);
        }
        else if (priceFromDo == null && priceToDo != null && !dateFrom.isEmpty() && !dateTo.isEmpty())
        {
            result = db.rawQuery("SELECT * FROM History WHERE Amount <= " + priceToDo + " AND Date >= '" +
                    dateFrom + "' AND Date <= '" + dateTo + "'", null);
        }
        else if (priceFromDo != null && priceToDo == null && !dateFrom.isEmpty() && !dateTo.isEmpty())
        {
            result = db.rawQuery("SELECT * FROM History WHERE Amount >= " + priceFromDo + " AND Date >= '" +
                    dateFrom + "' AND Date <= '" + dateTo +"'", null);
        }
        else if (priceFromDo != null && priceToDo != null && dateFrom != null && dateTo != null)
        {
            result = db.rawQuery("SELECT * FROM History WHERE Amount >= " + priceFromDo + " AND Amount <= " + priceToDo + " AND Date >= '" +
                    dateFrom + "' AND Date <= '" + dateTo + "'", null);
        }

        while(result.moveToNext()) {
            history.addView(createNewTextView2(result.getString(1), result.getString(2), result.getString(3)));
        }
        result.close();
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
