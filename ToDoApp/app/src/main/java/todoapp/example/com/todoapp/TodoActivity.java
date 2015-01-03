package todoapp.example.com.todoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public class TodoActivity extends ActionBarActivity {
    private ArrayList<TodoItem> todoItems;
    private ArrayAdapter<TodoItem> todoAdapter;
    private ListView lvItems;
    private EditText etNewItem;
    private CheckBox cbHighPriority;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        etNewItem = (EditText) findViewById(R.id.etNewItem);
        lvItems = (ListView) findViewById(R.id.lvItems);
        cbHighPriority = (CheckBox) findViewById(R.id.cbHighPriority);

        // read in saved items and add to the array adapter
        readTodoItems();
        todoAdapter = new TodoItemAdapter(this, todoItems);
        lvItems.setAdapter(todoAdapter);
        setupListViewListener();
    }

    private void setupListViewListener() {
        // set up long click to delete an item
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);
                todoAdapter.notifyDataSetChanged();
                saveTodoItems(); // save changes
                return true;
            }
        });


        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set up click to launch the Edit Item Activity, passing it the text and position
                Intent i = new Intent(TodoActivity.this, EditItemActivity.class);
                i.putExtra("body", todoItems.get(position).getBody());
                i.putExtra("priority", todoItems.get(position).getPriority());
                i.putExtra("position", position);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // when the edit activity is done, save the new changes
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String newText = data.getExtras().getString("body");
            int newPriority = data.getExtras().getInt("priority");
            int pos = data.getIntExtra("position", -1);
            todoItems.set(pos, new TodoItem(newText, newPriority));
            todoAdapter.notifyDataSetChanged();
            saveTodoItems();
        }
    }

    private void readTodoItems() {
        TodoItemDatabase db = new TodoItemDatabase(this);
        todoItems = (ArrayList) db.getAllTodoItems();

    }

    private void saveTodoItems() {
        TodoItemDatabase db = new TodoItemDatabase(this);
        db.writeTodoItems(todoItems);
    }

    public void onAddedItem(View v) {
        // add a new item to the list
        TodoItem item = new TodoItem(etNewItem.getText().toString(), cbHighPriority.isChecked() ? TodoItem.HIGH_PRIORITY : TodoItem.NORMAL_PRIORITY);
        todoAdapter.add(item);
        saveTodoItems(); // save the changes
        etNewItem.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
