package com.mousser.myapplication.Security;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void hashPassword_hashesCorrectly() {
        String plainTextPassword = "doggy";
        String hash = PasswordUtils.hashPassword(plainTextPassword);
        assertTrue(BCrypt.checkpw(plainTextPassword, hash));    }

    @Test
    public void checkPassword_checksCorrectly() {
        String plainTextPassword = "doggy";
        String hashedPassword = BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
        assertTrue(PasswordUtils.checkPassword(plainTextPassword, hashedPassword));
    }
}