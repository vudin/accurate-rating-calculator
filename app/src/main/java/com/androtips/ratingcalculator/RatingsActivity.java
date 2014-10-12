package com.androtips.ratingcalculator;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androtips.ratingcalculator.anim.Animations;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class RatingsActivity extends Activity {
    private static final String RATING_CALCULATOR = "RATING_CALCULATOR";

    private static final String SHARED_PREFS_FILE = "RATING_HISTORY";
    private static final String RATINGS = "RATINGS";

    private Menu menu;

    private LinearLayout calculator;
    private LinearLayout resultAndHistory;
    private EditText five;
    private EditText four;
    private EditText three;
    private EditText two;
    private EditText one;
    private TextView result;
    private ListView historyList;
    private ArrayList<RatingRecord> ratingsHistory = new ArrayList<RatingRecord>();
    private RatingsHistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        calculator = (LinearLayout) findViewById(R.id.calculator);
        resultAndHistory = (LinearLayout) findViewById(R.id.result_and_history);

        five = (EditText) findViewById(R.id.five_et);
        four = (EditText) findViewById(R.id.four_et);
        three = (EditText) findViewById(R.id.three_et);
        two = (EditText) findViewById(R.id.two_et);
        one = (EditText) findViewById(R.id.one_et);
        Button calculate = (Button) findViewById(R.id.calculate);
        calculate.setOnClickListener(new CalculateClickListener());
        result = (TextView) findViewById(R.id.result);
        historyList = (ListView) findViewById(R.id.history);

        readRatingsHistoryFromPrefs();
        populateListView();
        showCalculator(false);
    }

    private void populateListView() {
        historyAdapter = new RatingsHistoryAdapter(this, ratingsHistory);
        historyList.setAdapter(historyAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.activity_ratings, menu);
        if (isLandscapeOrTablet()) {
            menu.findItem(R.id.switch_views).setVisible(false);
            menu.findItem(R.id.delete_history).setVisible(true);
        } else {
            menu.findItem(R.id.switch_views).setVisible(true);
            menu.findItem(R.id.delete_history).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switch_views:
                if (calculator.getVisibility() == View.VISIBLE) {
                    showResultAndHistory();
                } else {
                    showCalculator(true);
                }
                break;
            case R.id.delete_history:
                deleteHistory();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteHistory() {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS_FILE, 0);
        preferences.edit().remove(RATINGS).apply();
        ratingsHistory = new ArrayList<RatingRecord>();
        populateListView();
        showCalculator(true);
    }

    @SuppressWarnings("unchecked")
    private void readRatingsHistoryFromPrefs() {
        // load ratings history from preference
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);

        try {
            ratingsHistory = (ArrayList<RatingRecord>) ObjectSerializer.deserialize(prefs.getString(RATINGS, ObjectSerializer.serialize(new ArrayList<RatingRecord>())));
        } catch (IOException e) {
            Log.e(RATING_CALCULATOR, "", e);
        } catch (ClassNotFoundException e) {
            Log.e(RATING_CALCULATOR, "", e);
        }
    }

    private double calculateAverageRating(String fiveStars, String fourStars, String threeStars, String twoStars, String oneStar) {
        return ((Double.parseDouble(fiveStars) * 5) + (Double.parseDouble(fourStars) * 4) + (Double.parseDouble(threeStars) * 3) + (Double.parseDouble(twoStars) * 2) + (Double.parseDouble(oneStar))) / (Double.parseDouble(fiveStars) + Double.parseDouble(fourStars) + Double.parseDouble(threeStars) + Double.parseDouble(twoStars) + Double.parseDouble(oneStar));
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) RatingsActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(five.getWindowToken(), 0);
    }

    private void displayResult(double average) {
        showResultAndHistory();
        DecimalFormat df = new DecimalFormat("0.00000000");
        result.setText(getString(R.string.latest_average_rating, df.format(average)));
    }

    public void addRatingRecordToHistory(RatingRecord record) {
        if (null == ratingsHistory) {
            ratingsHistory = new ArrayList<RatingRecord>();
        }
        ratingsHistory.add(0, record);

        // save the ratings list to preference
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        try {
            editor.putString(RATINGS, ObjectSerializer.serialize(ratingsHistory));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.apply();
    }

    private void clearValues() {
        five.setText("");
        four.setText("");
        three.setText("");
        two.setText("");
        one.setText("");
    }

    private void showCalculator(boolean animate) {
        clearValues();
        if (!isLandscapeOrTablet()) {
            if (animate && ratingsHistory != null && !ratingsHistory.isEmpty()) {
                Animations.rotate3d(resultAndHistory, calculator);
            } else {
                calculator.setVisibility(View.VISIBLE);
                resultAndHistory.setVisibility(View.GONE);
            }
            getActionBar().setTitle(R.string.app_name);
            if (menu != null) {
                menu.findItem(R.id.switch_views).setIcon(R.drawable.ic_action_calc_to_list);
                menu.findItem(R.id.delete_history).setVisible(false);
            }
        }
    }

    private void showResultAndHistory() {
        hideSoftKeyboard();
        if (!isLandscapeOrTablet()) {
            if (ratingsHistory != null && !ratingsHistory.isEmpty()) {
                Animations.rotate3d(calculator, resultAndHistory);
            } else {
                calculator.setVisibility(View.GONE);
                resultAndHistory.setVisibility(View.VISIBLE);
            }
            getActionBar().setTitle(R.string.ratings_history);
            if (menu != null) {
                menu.findItem(R.id.switch_views).setIcon(R.drawable.ic_action_list_to_calc);
                menu.findItem(R.id.delete_history).setVisible(ratingsHistory != null && !ratingsHistory.isEmpty());
            }
        }
        updateAdapter();
        result.setText("");
    }

    private void updateAdapter() {
        historyList.smoothScrollToPosition(0);
        historyAdapter.setItems(ratingsHistory);
    }

    private boolean isLandscapeOrTablet() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE || getResources().getBoolean(R.bool.is_tablet);
    }

    private final class CalculateClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            String fiveStars = five.getText().toString();
            String fourStars = four.getText().toString();
            String threeStars = three.getText().toString();
            String twoStars = two.getText().toString();
            String oneStar = one.getText().toString();
            if (!fiveStars.isEmpty()
                    && !fourStars.isEmpty()
                    && !threeStars.isEmpty()
                    && !twoStars.isEmpty()
                    && !oneStar.isEmpty()) {

                // Calculate the rating
                double average = calculateAverageRating(fiveStars, fourStars, threeStars, twoStars, oneStar);

                // Save ratings & result for feature reference
                addRatingRecordToHistory(new RatingRecord(fiveStars, fourStars, threeStars, twoStars, oneStar, average, DateFormat.getDateTimeInstance().format(new Date())));

                displayResult(average);
            } else {
                Toast.makeText(RatingsActivity.this, R.string.error_message, Toast.LENGTH_SHORT).show();
                result.setText("");
            }
        }
    }
}
