package edu.fordham.todoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item text";
    public static final String KEY_ITEM_POSITION = "item position";
    public static final int EDIT_TEXT_CODE= 20;

    List<String> items;
    Button button;
    EditText item;
    RecyclerView rvView;
    Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        item = findViewById(R.id.item);
        rvView = findViewById(R.id.recycleView);

        loadItems();
        Adapter.OnLongClickListener onLongClickListener = new Adapter.OnLongClickListener(){

            @Override
            public void onItemLongClicked(int adapterPosition) {
                items.remove(adapterPosition);
                adapter.notifyItemRemoved(adapterPosition);
                Toast.makeText(getApplicationContext(),"Item was removed",Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        Adapter.OnClickListener onClickListener = new Adapter.OnClickListener() {

            @Override
            public void onItemClicked(int adapterPosition) {
                Intent i = new Intent(MainActivity.this,EditActivity.class);
                i.putExtra(KEY_ITEM_TEXT,items.get(adapterPosition));
                i.putExtra(KEY_ITEM_POSITION,adapterPosition);
                startActivityForResult(i,EDIT_TEXT_CODE);
            }
        };
        adapter = new Adapter(items, onLongClickListener,onClickListener);
        rvView.setAdapter(adapter);
        rvView.setLayoutManager(new LinearLayoutManager(this));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = item.getText().toString();
                items.add(todoItem);
                adapter.notifyItemInserted(items.size()-1);
                item.setText("");
                Toast.makeText(getApplicationContext(),"Item was added",Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            items.set(position,itemText);
            adapter.notifyItemChanged(position);
            saveItems();
            Toast.makeText(getApplicationContext(),"Item was updated",Toast.LENGTH_SHORT).show();
        }else{
            Log.w("MainActivity","Unknown call to OnActivityResult");
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(),"data.txt");
    }
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }catch (IOException e){
            Log.e("MainActivity","Error reading items" + e);
            items = new ArrayList<>();
        }
    }

    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(),items);
        } catch (IOException e) {
            Log.e("MainActivity","Error writing items" + e);
        }
    }
}