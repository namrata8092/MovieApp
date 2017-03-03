package com.nds.nanodegree.movieapp.common;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/*Converting response JSON to gson to parse response as Parcelable object.
* */

/**
 * Created by Namrata Shah on 2/26/2017.
 */
public final class JSONSerializeHelper {

    public JSONSerializeHelper(){}

    public static <T> T deserializeObject(Class<T> objectClass, String json){
        try{
            Gson gson = new Gson();
            return gson.fromJson(json, objectClass);
        }catch(JsonParseException jsonParseException){
            return null;
        }
    }
}
