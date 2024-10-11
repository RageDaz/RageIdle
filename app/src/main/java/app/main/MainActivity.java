package app.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextView countdownLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countdownLabel = findViewById(R.id.countdownLabel);
        countdownLabel.setText("Timer Not Set");

       // Calendar calendar = Calendar.getInstance();
       // calendar.add(Calendar.MINUTE, 1); // Add 1 minute
       // startCountDown(calendar.getTime());

        Intent intent=getIntent();
        ProcessIntent(intent);

    }

    private void  ProcessIntent (Intent intent){
        if (intent.hasExtra("targetDate")) {
            String targetDateString = intent.getStringExtra("targetDate");
            if (targetDateString == null) {
                targetDateString = "NULL";
            }

            try {
                // Parse the date string
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.US);
                Date targetDate = dateFormat.parse(targetDateString);

                // Start the countdown
                startCountDown(targetDate);

            } catch (ParseException e) {
                // Handle invalid date format
                startCountDown(new Date(System.currentTimeMillis() + 10000)); // 10 seconds from now
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ProcessIntent(intent);
    }

    public void startCountDown(Date future) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            long futureInMillis = future.getTime();
            long currentInMillis = System.currentTimeMillis();

            long timeDifference = futureInMillis - currentInMillis;

            // Create a countdown timer
            new CountDownTimer(timeDifference, 1000) {
                public void onTick(long millisUntilFinished) {
                    // Convert milliseconds into days, hours, minutes, and seconds
                    long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                    long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24;
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;

                    // Display the countdown in a log or UI
                    if (days > 0) {
                        countdownLabel.setText(String.format("%dd %02d:%02d:%02d", days, hours, minutes, seconds));
                    } else {
                        countdownLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                    }
                }

                public void onFinish() {
                    countdownLabel.setText("Time's Up");
                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}