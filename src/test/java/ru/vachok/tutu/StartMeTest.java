package ru.vachok.tutu;


import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import ru.vachok.tutu.conf.TestConfigure;
import ru.vachok.tutu.conf.TestConfigureThreadsLogMaker;


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
}