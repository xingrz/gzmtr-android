package us.xingrz.gzmtr;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;

import us.xingrz.gzmtr.data.Line;
import us.xingrz.gzmtr.net.GsonRequest;

public class DetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String json = getIntent().getStringExtra("json");
        if (TextUtils.isEmpty(json)) {
            finish();
            return;
        }

        Line line = GsonRequest.gson.fromJson(json, Line.class);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);

            if (line.symbols.color != null) {
                actionBar.setBackgroundDrawable(new ColorDrawable(line.symbols.getColor()));
            }

            if (line.symbols.color != null && isBrighter(line.symbols.getColor())) {
                actionBar.setTitle(coloredText(true, line.symbols.full));
                if (!TextUtils.isEmpty(line.symbols.alias)) {
                    actionBar.setSubtitle(coloredText(true, line.symbols.alias));
                }
            } else {
                actionBar.setTitle(coloredText(false, line.symbols.full));
                if (!TextUtils.isEmpty(line.symbols.alias)) {
                    actionBar.setSubtitle(coloredText(false, line.symbols.alias));
                }
            }
        }
    }

    private boolean isBrighter(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return hsv[1] < 0.20 && hsv[2] > 0.50;
    }

    private CharSequence coloredText(boolean brighter, String text) {
        return Html.fromHtml(String.format("<font color=\"%s\">%s</font>",
                brighter ? "#000000" : "#ffffff", text));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
