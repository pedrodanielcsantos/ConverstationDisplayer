package com.pedrosantos.conversationdisplayer;

import com.pedrosantos.conversationdisplayer.models.CDDataSet;
import com.pedrosantos.conversationdisplayer.models.CDError;
import com.pedrosantos.conversationdisplayer.promises.CDPromise;
import com.pedrosantos.conversationdisplayer.promises.DataSetPromises;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView resultLabel = (TextView) findViewById(R.id.dummy_text_view);

        // WIP - just testing code for now.
        CDPromise.when(DataSetPromises.getDataSet())
                .done(new DoneCallback<CDDataSet>() {
                    @Override
                    public void onDone(final CDDataSet result) {
                        resultLabel.setText("Success. " + result.getUserList().size() + " users and " + result.getMessageList().size() + " messages");
                    }
                })
                .fail(new FailCallback<CDError>() {
                    @Override
                    public void onFail(final CDError error) {
                        resultLabel.setText("Fail. Error code: " + error.getCode() + " | Message: " + error.getMessage());
                    }
                });
    }
}
