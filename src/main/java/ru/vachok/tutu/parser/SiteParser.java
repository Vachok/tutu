package ru.vachok.tutu.parser;


import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import ru.vachok.tutu.conf.AbstractForms;
import ru.vachok.tutu.conf.BackEngine;
import ru.vachok.tutu.conf.MessageToUser;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class SiteParser implements BackEngine {
    
    
    private MessageToUser messageToUser = MessageToUser.getInstance();
    
    @Override public List<Date> getComingTrains() {
        int stationNewIerusalemCode = 37805;
        int stationManihinoCode = 37505;
        Date fromIstra = getComingTrain(stationNewIerusalemCode, stationManihinoCode);
        Date toIstra = getComingTrain(stationManihinoCode, stationNewIerusalemCode);
        
        List<Date> retList = new ArrayList<>();
        retList.add(fromIstra);
        retList.add(toIstra);
        return retList;
    }
    
    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("SiteParser{");
        sb.append("messageToUser=").append(messageToUser);
        sb.append('}');
        return sb.toString();
    }
    
    private Date getComingTrain(int stationCodeFrom, int stationCodeTo) {
        String url = "https://www.tutu.ru/rasp.php?st1="+stationCodeFrom+"&st2="+stationCodeTo;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Date retDate = new Date();
        Deque<Date> dates = new LinkedList<>();
        try (Response response = okHttpClient.newCall(request).execute()) {
            try (ResponseBody responseBody = response.body()) {
                Deque<Date> dateDeque = parseResonseBody(responseBody);
                retDate = dateDeque.getLast();
            }
        }
        catch (IOException | ParseException | RuntimeException e) {
            messageToUser.error("SiteParser.siteConnect", e.getMessage(), AbstractForms.networkerTrace(e.getStackTrace()));
        }
        return retDate;
    }
    
    @NotNull private Deque<Date> parseResonseBody(@NotNull ResponseBody responseBody) throws IOException, ParseException {
        Date retDate = new Date();
        Deque<Date> retDeq = new LinkedList<>();
        String string = responseBody.string();
        responseBody.close();
        String jsonArrStr = string.split("\\Qwindow.modelParams = \\E")[1].split(";")[0];
        jsonArrStr = jsonArrStr.replace("]]", "]");
        JsonArray jsonArray = Json.parse(jsonArrStr).asArray();
        List<JsonValue> values = jsonArray.values();
        for (JsonValue value : values) {
            long trainStamp = parseValue(value);
            if (System.currentTimeMillis() < trainStamp) {
                retDeq.addFirst(new Date(trainStamp));
            }
        }
        return retDeq;
    }
    
    private long parseValue(@NotNull JsonValue value) throws ParseException {
        JsonValue jsonValue = value.asObject().get("trip");
        JsonObject jsonValues = jsonValue.asObject();
        JsonValue departureRouteStation = jsonValues.get("departureRouteStation");
        String departureDatetime = departureRouteStation.asObject().get("departureDatetime").asString();
        String[] dateTime = departureDatetime.split("T");
        
        departureDatetime = dateTime[0] + "|" + dateTime[1].split("\\Q:00.\\E")[0];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd|kk:mm");
        Date parse = simpleDateFormat.parse(departureDatetime);
        return parse.getTime();
    }
}
