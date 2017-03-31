package com.lianjia.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 重写TypeAdapter
 * Created by FuJie on 2017/2/15.
 */
public class GsonUtil {

    private  static final  String  DATEFORMAT_default = "yyyy-MM-dd HH:mm:ss";

    /*
    默认的反序列化
     */
    public  static <T> T fromJsonDefault(String json, TypeToken<T> typeToken){
        Gson gson = new Gson();
        return  gson.fromJson(json, typeToken.getType());
    }

    /*
    字符串转list或者map
     */
    public  static <T> T fromJson(String json, TypeToken<T> typeToken){
        //Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<T>(){}.getType(),
         //       new MapTypeAdapter()).create();
        Gson gson = new GsonBuilder().setDateFormat(DATEFORMAT_default).registerTypeAdapter(new TypeToken<T>(){}.getType(),
               new MapTypeAdapter()).create();
        return  gson.fromJson(json, typeToken.getType());
    }

    public  static <T> List<T> fromJsonList(String json, TypeToken<T> typeToken){
        Gson gson = new GsonBuilder().setDateFormat(DATEFORMAT_default).registerTypeAdapter(new TypeToken<List<T>>(){}.getType(),
                new MapTypeAdapter()).create();
        return  gson.fromJson(json, typeToken.getType());
    }

    public  static <T> T fromJson(String json, Class<T> cls){
        Gson gson = new GsonBuilder().setDateFormat(DATEFORMAT_default).create();
        return gson.fromJson(json, cls);
    }

    public static  class  MapTypeAdapter extends TypeAdapter<Object>{

        @Override
        public Object read(com.google.gson.stream.JsonReader in) throws IOException{
            com.google.gson.stream.JsonToken token = in.peek();
            switch (token){
                case BEGIN_ARRAY:
                    List<Object> list = new ArrayList<Object>();
                    in.beginArray();
                    while (in.hasNext()){
                        list.add(read(in));
                    }
                    in.endArray();
                    return list;

                case BEGIN_OBJECT:
                    Map<String, Object> map = new LinkedTreeMap<String, Object>();
                    in.beginObject();
                    while (in.hasNext()){
                        map.put(in.nextName(), read(in));
                    }
                    in.endObject();
                    return  map;

                case STRING:
                    return  in.nextString();

                case NUMBER:
                    double dbNum = in.nextDouble();
                    if(dbNum > Long.MAX_VALUE){
                        return dbNum;
                    }
                    long lngNum = (long) dbNum;
                    if(dbNum == lngNum){
                        return lngNum;
                    }else{
                        return dbNum;
                    }
                case BOOLEAN:
                    return in.nextBoolean();

                case NULL:
                    in.nextNull();
                    return  null;

                default:
                    return  new IllegalStateException();

            }
        }

        @Override
        public  void  write(com.google.gson.stream.JsonWriter out, Object value) throws  IOException{

        }
    }


}
