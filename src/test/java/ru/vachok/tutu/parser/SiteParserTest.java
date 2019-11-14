package ru.vachok.tutu.parser;


import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.vachok.tutu.conf.AbstractForms;
import ru.vachok.tutu.conf.TestConfigure;
import ru.vachok.tutu.conf.TestConfigureThreadsLogMaker;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class SiteParserTest {
    
    
    private static final TestConfigure TEST_CONFIGURE_THREADS_LOG_MAKER = new TestConfigureThreadsLogMaker(SiteParser.class.getSimpleName(), System.nanoTime());
    
    @BeforeClass
    public void setUp() {
        Thread.currentThread().setName(getClass().getSimpleName().substring(0, 5));
        TEST_CONFIGURE_THREADS_LOG_MAKER.before();
    }
    
    @AfterClass
    public void tearDown() {
        TEST_CONFIGURE_THREADS_LOG_MAKER.after();
    }
    
    @Test
    public void testGetDate(){
        Date fromIstra = siteConnect("https://www.tutu.ru/rasp.php?st1=37805&st2=37505");
        Date toIstra = siteConnect("https://www.tutu.ru/rasp.php?st1=37505&st2=37805");
        System.out.println("toIstra = " + toIstra);
        System.out.println("fromIstra = " + fromIstra);
    }
    
    private Date siteConnect(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Date retDate;
        Deque<Date> dates = new LinkedList<>();
        try(Response response = okHttpClient.newCall(request).execute()){
            String string = response.body().string();
            String jsonArrStr = string.split("\\Qwindow.modelParams = \\E")[1].split(";")[0];
            jsonArrStr=jsonArrStr.replace("]]","]");
            JsonArray jsonArray = Json.parse(jsonArrStr).asArray();
            List<JsonValue> values = jsonArray.values();
            
            for (JsonValue value:values){
                JsonValue jsonValue = value.asObject().get("trip");
                JsonObject jsonValues = jsonValue.asObject();
                JsonValue departureRouteStation = jsonValues.get("departureRouteStation");
                String departureDatetime = departureRouteStation.asObject().get("departureDatetime").asString();
                String[] dateTime = departureDatetime.split("T");
                
                departureDatetime =dateTime[0]+"|"+ dateTime[1].split("\\Q:00.\\E")[0];
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd|kk:mm");
                Date parse = simpleDateFormat.parse(departureDatetime);
                
                if (System.currentTimeMillis() < parse.getTime()) {
                    dates.add(parse);
                }
            }
        }
        catch (IOException | ParseException e) {
            Assert.assertNull(e, e.getMessage() + "\n" + AbstractForms.fromArray(e));
        }
        retDate = dates.getFirst();
        return retDate;
    }
}