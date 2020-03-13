package org.rbusjahn.compactdao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DatabaseShutdownTest {

    private DatabaseShutdown cut;

    @Before
    public void setup(){
        cut = new DatabaseShutdown();
    }

    @Test
    public void testStart(){

        DatabaseShutdown.register();

        String message = cut.start();
        Assert.assertEquals("Derby system shutdown.", message);
    }

}