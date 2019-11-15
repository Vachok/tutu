package ru.vachok.tutu;


import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.vachok.tutu.conf.BackEngine;
import ru.vachok.tutu.conf.TestConfigure;
import ru.vachok.tutu.conf.TestConfigureThreadsLogMaker;
import ru.vachok.tutu.parser.SiteParser;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;


public class StartMeTest {
    
    
    private static final TestConfigure TEST_CONFIGURE_THREADS_LOG_MAKER = new TestConfigureThreadsLogMaker(StartMe.class.getSimpleName(), System.nanoTime());
    
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
    public void testMain(){
        StartMe.main(new String[]{""});
    }
}