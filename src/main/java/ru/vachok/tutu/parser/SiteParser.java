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
import ru.vachok.tutu.conf.InformationFactory;
import ru.vachok.tutu.conf.MessageToUser;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class SiteParser implements BackEngine, InformationFactory {
    
    
    private MessageToUser messageToUser = MessageToUser.getInstance();
    
    private int numOfTrains = 2;
    
    @Override
    public String getInfo() {
        Map<Date, String> trains = getComingTrains();
        String[] strings = new String[numOfTrains];
        Queue<String> stringQueue = new LinkedList<>();
        for (Map.Entry<Date, String> entry : trains.entrySet()) {
            stringQueue.add(entry.getKey() + " - " + entry.getValue());
        }
        for (int i = 0; i < numOfTrains; i++) {
            String add = stringQueue.remove();
            strings[i] = add;
        }
        
        return Arrays.toString(strings);
    }
    
    @Override
    public String getInfoAbout(String aboutWhat) {
        try {
            this.numOfTrains = Integer.parseInt(aboutWhat);
        }
        catch (NumberFormatException e) {
            messageToUser.error(SiteParser.class.getSimpleName(), e.getMessage(), " see line: 57 ***");
            this.numOfTrains = 2;
        }
        return getInfo();
    }
    
    @Override
    public void setClassOption(Object classOption) {
        this.numOfTrains = (int) classOption;
    }
    
    @Override
    public Map<Date, String> getComingTrains() {
        int stationNewIerusalemCode = 37805;
        int stationManihinoCode = 37505;
        Deque<Date> fromIstra = getComingTrain(stationNewIerusalemCode, stationManihinoCode);
        Deque<Date> toIstra = getComingTrain(stationManihinoCode, stationNewIerusalemCode);
        
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
    
    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("SiteParser{");
        sb.append("messageToUser=").append(messageToUser);
        sb.append('}');
        return sb.toString();
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
