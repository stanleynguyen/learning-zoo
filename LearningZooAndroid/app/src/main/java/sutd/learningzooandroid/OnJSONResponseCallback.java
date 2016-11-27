package sutd.learningzooandroid;

import org.json.JSONArray;
import org.json.JSONObject;

public interface OnJSONResponseCallback {
    void onJSONResponse(boolean success, JSONObject response);
}

interface OnJSONArrayResponseCallback{
    void onJSONArrayResponse(boolean success, JSONArray response);
}