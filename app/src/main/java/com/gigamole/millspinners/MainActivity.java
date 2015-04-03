package com.gigamole.millspinners;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gigamole.millspinners.lib.MultiArcSpinner;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MultiArcSpinner semiCircleSetView = (MultiArcSpinner) findViewById(R.id.semi);
        semiCircleSetView.setColors(getResources().getIntArray(R.array.rebirth_colors), true);
        semiCircleSetView.setAutostarted(true);
        semiCircleSetView.setOnClickListener(new View.OnClickListener() {
            int counter;

            @Override
            public void onClick(View v) {
                if (counter++ % 2 == 0) {
                    semiCircleSetView.start();
                } else {
                    semiCircleSetView.finish();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
