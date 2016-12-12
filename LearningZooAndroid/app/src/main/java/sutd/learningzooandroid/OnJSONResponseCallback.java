package sutd.learningzooandroid;

import org.json.JSONArray;
import org.json.JSONObject;

public interface OnJSONResponseCallback {
    public void onJSONResponse(boolean success, JSONObject response);
}
