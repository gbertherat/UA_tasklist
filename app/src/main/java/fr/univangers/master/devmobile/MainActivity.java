package fr.univangers.master.devmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> data;
    private ActivityResultLauncher<Intent> launchAddTaskActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = new ArrayList<>();
        Collections.addAll(data, FakeData.get_tasks());

        TaskAdapter adapter = new TaskAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Pattern weightPattern = Pattern.compile("<[1-3]>");
        for(String string: data){
            String weight = "";
            Matcher m = weightPattern.matcher(string);
            if(m.find()){
                weight = m.group();
                String label = string.replace(weight + " ", "");

                weight = weight .replace("<","")
                                .replace(">","");

                adapter.add(label, weight);
            }
        }

        launchAddTaskActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intentData = result.getData();
                        String label = intentData.getStringExtra("taskLabel");
                        String weight = intentData.getStringExtra("taskWeight");

                        if(label != null && weight != null) {
                            adapter.add(label, weight);
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addTaskMenu) {
            Intent intent = new Intent(this, AddTaskActivity.class);
            launchAddTaskActivity.launch(intent);
        }
        return true;
    }
}