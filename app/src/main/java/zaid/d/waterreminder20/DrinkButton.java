package zaid.d.waterreminder20;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.method.BaseKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class DrinkButton extends AppCompatActivity {

    // ATTRIBUTES //
    private DataManager dataManager;
    private Button button;
    private TextView glassesText;
    private Background background;
    private Context context;


    // CONSTRUCTOR //
    DrinkButton(Context c, Button layoutButton, TextView glassesT, Background bg) {
        context = c;
        dataManager = new DataManager(c);
        button = layoutButton;
        glassesText = glassesT;
        background = bg;
    }

    // METHODS //

    // When the button is clicked
    public void buttonClicked() {
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                // If the button can be clicked
                if (isButtonClickable()) {

                    // Increases the amount of glasses the user has drank
                    dataManager.savePreviousGlassesConsumed(dataManager.getPreviousGlassesConsumed()+1);

                    // Decreases the amounts of times the use can click the button
                    dataManager.saveButtonClicks(dataManager.getButtonClicks()-1);

                    //Schedule
                    TimeManager timeManager = new TimeManager(context);
                    timeManager.scheduleNextDrink();

                    // Sets the text on the screen to the amount of glasses the user has drank
                    String glassesDrank = String.valueOf(dataManager.getPreviousGlassesConsumed());
                    glassesText.setText(glassesDrank);

                    // Sets last time button pressed to now
                    dataManager.saveLastButtonPressTime(System.currentTimeMillis());

                    // Increases the progress bar
                    background.setProgressBar();

                    // Disables the button if the user has no button clicks
                    isButtonClickable();

                    if (dataManager.getPreviousGlassesConsumed() == 1)
                        timeManager.startDailyAlarm(dataManager.getNextDayTime());

                }
            }
        });
    }

    // Returns if the button is able to be clicked and enables it if it is
    public boolean isButtonClickable() {

        // Gets the amounts of glasses consumed by the user today
        int glassesConsumed = dataManager.getPreviousGlassesConsumed();
        // Gets the amount times the user can click the button
        int clickEnabled = dataManager.getButtonClicks();
        // Gets the next time the user can drink water

        // Enables/Disables button depending on result and returns
        boolean result = (glassesConsumed < 8 && clickEnabled > 0);
        button.setEnabled(result);
        return result;
    }

}
