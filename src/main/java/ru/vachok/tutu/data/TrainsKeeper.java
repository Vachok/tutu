package ru.vachok.tutu.data;


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
import java.util.concurrent.ConcurrentSkipListMap;


/**
 @since 16.11.2019 (18:06) */
public class TrainsKeeper implements BackEngine {
    
    
    private static final Map<Date, String> todayMap = new ConcurrentSkipListMap<>();
    
    private MessageToUser messageToUser = MessageToUser.getInstance();
    
    private int stationCodeFrom;
    
    private int stationCodeTo;
    
    public TrainsKeeper(int stationCodeFrom, int stationCodeTo) {
        this.stationCodeFrom = stationCodeFrom;
        this.stationCodeTo = stationCodeTo;
    }
    
    @Override
    public Map<Date, String> getComingTrains() {
        if (todayMap.size() == 0) {
            fillToday();
        }
        else {
            Set<Map.Entry<Date, String>> entries = todayMap.entrySet();
            entries.stream().forEach(this::actualizeMap);
        }
        return todayMap;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(",\n", TrainsKeeper.class.getSimpleName() + "[\n", "\n]")
                .toString();
    }
    
    private Map<Date, String> getComingTrains0() {
        Deque<Date> fromIstra = getComingTrain(stationCodeFrom, stationCodeTo);
        Deque<Date> toIstra = getComingTrain(stationCodeTo, stationCodeFrom);
        
        Map<Date, String> retMap = new TreeMap<>();
        while (!fromIstra.isEmpty()) {
            Date from = fromIstra.remove();
            retMap.put(from, "from");
        }
        while (!toIstra.isEmpty()) {
            Date to = toIstra.remove();
            retMap.put(to, "to");
        }
        return retMap;
    }
    
    private void actualizeMap(@NotNull Map.Entry<Date, String> entry) {
        todayMap.forEach((date, destination)->{
            if (date.getTime() < System.currentTimeMillis()) {
                todayMap.remove(date);
            }
        });
    }
    
    private void fillToday() {
        Map<Date, String> trains = getComingTrains0();
        todayMap.putAll(trains);
    }
    
    private Deque<Date> getComingTrain(int stationCodeFrom, int stationCodeTo) {
        String url = "https://www.tutu.ru/rasp.php?st1=" + stationCodeFrom + "&st2=" + stationCodeTo;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Deque<Date> dateDeque = null;
        try (Response response = okHttpClient.newCall(request).execute()) {
            try (ResponseBody responseBody = response.body()) {
                dateDeque = new ArrayDeque<>(parseResponseBody(responseBody));
            }
        }
        catch (IOException | ParseException | RuntimeException e) {
            messageToUser.error("SiteParser.siteConnect", e.getMessage(), AbstractForms.networkerTrace(e.getStackTrace()));
        }
        return dateDeque;
    }
    
    private @NotNull Deque<Date> parseResponseBody(@NotNull ResponseBody responseBody) throws IOException, ParseException {
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