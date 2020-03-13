package org.rbusjahn.compactdao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.rbusjahn.compactdao.example.app.AccountModel;
import org.rbusjahn.compactdao.example.app.AccountModelDao;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class AccountTest {

    private AccountModelDao cut;

    @Before
    public void setup(){
        cut = new AccountModelDao();
    }

    @Test
    public void testCrud(){

        AccountModel account = new AccountModel();

        //WHEN
        cut.save(account);

        //THEN
        Assert.assertNotNull(account.getId());

        try {

            DriverManager.getConnection("jdbc:derby:;shutdown=true");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
