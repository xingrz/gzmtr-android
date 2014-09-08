package us.xingrz.gzmtr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.LinkedList;

import us.xingrz.gzmtr.data.Line;
import us.xingrz.gzmtr.net.GsonRequest;

public class LinesActivity extends Activity {

    private static final String TAG = "LinesActivity";

    private static final String INDEX = "https://github.com/gzmtr/lines/raw/v1/index.json";

    private RequestQueue requestQueue;
    private LinkedList<String> urls;
    private HashMap<String, Line> lines = new HashMap<String, Line>();

    private LinesAdapter adapter = new LinesAdapter();

    private ListView list;
    private View loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lines);

        list = (ListView) findViewById(R.id.lines);
        list.setVisibility(View.GONE);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showItem(adapter.getItem(position));
            }
        });

        loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        requestQueue = Volley.newRequestQueue(this);

        loadIndex();
    }

    @Override
    protected void onDestroy() {
        requestQueue.cancelAll(this);
        super.onDestroy();
    }

    private void loadIndex() {
        urls = null;
        lines.clear();

        requestQueue.add(new GsonRequest<LinkedList>(
                INDEX, LinkedList.class,
                new Response.Listener<LinkedList>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public void onResponse(LinkedList response) {
                        urls = response;
                        for (String url : urls) {
                            loadItem(url);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "failed to load index", error);
                        handleError();
                    }
                }
        ));
    }

    private void loadItem(final String url) {
        requestQueue.add(new GsonRequest<Line>(
                url, Line.class,
                new Response.Listener<Line>() {
                    @Override
                    public void onResponse(Line response) {
                        lines.put(url, response);
                        maybeFinishedLoading();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "failed to load " + url, error);
                        handleError();
                    }
                }
        ));
    }

    private void maybeFinishedLoading() {
        if (urls == null || lines.size() != urls.size()) {
            return;
        }

        adapter.notifyDataSetChanged();

        loading.setVisibility(View.GONE);
        list.setVisibility(View.VISIBLE);
    }

    private void handleError() {
        requestQueue.cancelAll(this);
    }

    private void showItem(Line item) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("json", GsonRequest.gson.toJson(item));
        startActivity(intent);
    }

    private class LinesAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return lines.size();
        }

        @Override
        public Line getItem(int position) {
            return urls == null ? null : lines.get(urls.get(position));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Line item = getItem(position);

            if (item == null) {
                return null;
            }

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.lines_item, parent, false);
            }

            ((TextView) convertView.findViewById(R.id.name)).setText(item.symbols.full);

            return convertView;
        }

    }

}
