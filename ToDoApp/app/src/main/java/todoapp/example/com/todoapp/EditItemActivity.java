package todoapp.example.com.todoapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class EditItemActivity extends ActionBarActivity {
    private EditText etEditText;
    private int textPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // get the text of the item being edited and populate the et
        String oldText = getIntent().getStringExtra("text");
        etEditText = (EditText) findViewById(R.id.etEditText);
        etEditText.setText(oldText);

        textPosition = getIntent().getIntExtra("position", -1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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

    public void onSave(View v) {
        // return the changed text and the original position.
        Intent result = new Intent();
        result.putExtra("text", etEditText.getText().toString());
        result.putExtra("position", textPosition);
        setResult(RESULT_OK, result);
        this.finish();
    }
}
