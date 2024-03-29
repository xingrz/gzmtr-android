package us.xingrz.gzmtr.net;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;

import us.xingrz.gzmtr.data.EnumTypeAdapter;
import us.xingrz.gzmtr.data.Line;

public class GsonRequest<T> extends Request<T> {

    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(
                    Line.Status.class,
                    new EnumTypeAdapter<Line.Status>(Line.Status.PLANNING))
            .registerTypeAdapter(
                    Line.Fleet.Type.class,
                    new EnumTypeAdapter<Line.Fleet.Type>(Line.Fleet.Type.A))
            .registerTypeAdapter(
                    Line.Block.Type.class,
                    new EnumTypeAdapter<Line.Block.Type>(Line.Block.Type.FTGS))
            .registerTypeAdapter(
                    Line.Electrify.Form.class,
                    new EnumTypeAdapter<Line.Electrify.Form>(Line.Electrify.Form.OVERHEAD_CABLE))
            .registerTypeAdapter(
                    Line.Electrify.Type.class,
                    new EnumTypeAdapter<Line.Electrify.Type>(Line.Electrify.Type.AC))
            .registerTypeAdapter(
                    Line.Form.class,
                    new EnumTypeAdapter<Line.Form>(Line.Form.TUNNEL))
            .create();

    private final Class<T> type;
    private final Response.Listener<T> listener;

    public GsonRequest(String url, Class<T> type,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.type = type;
        this.listener = listener;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, type),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

}
