package ru.vachok.tutu.data;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.vachok.tutu.conf.AbstractForms;
import ru.vachok.tutu.conf.BackEngine;
import ru.vachok.tutu.conf.TestConfigure;
import ru.vachok.tutu.conf.TestConfigureThreadsLogMaker;

import java.util.Date;
import java.util.Map;


/**
 @see TrainsKeeper
 @since 16.11.2019 (18:11) */
public class TrainsKeeperTest {
    
    
    private static final TestConfigure TEST_CONFIGURE_THREADS_LOG_MAKER = new TestConfigureThreadsLogMaker(TrainsKeeperTest.class.getSimpleName(), System.nanoTime());
    
    private BackEngine keep;
    
    @BeforeClass
    public void setUp() {
        Thread.currentThread().setName(getClass().getSimpleName().substring(0, 5));
        TEST_CONFIGURE_THREADS_LOG_MAKER.before();
    }
    
    @AfterClass
    public void tearDown() {
        TEST_CONFIGURE_THREADS_LOG_MAKER.after();
    }
    
    @BeforeMethod
    public void initKeeper() {
        this.keep = new TrainsKeeper(38905, 37705);
    }
    
    @Test
    public void testGetComingTrains() {
        keep.getComingTrains();
        Map<Date, String> trains = keep.getComingTrains();
        Assert.assertTrue(AbstractForms.fromArray(trains).contains(new Date().toString().split(" ")[0]));
    }
    
    @Test
    public void testTestToString() {
        String s = keep.toString();
        long time = new Date().getTime();
        Assert.assertTrue(s.contains("TrainsKeeper["), s);
    }
}