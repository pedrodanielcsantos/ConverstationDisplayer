package com.pedrosantos.conversationdisplayer.datasources;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Unit tests to SplashDataSource
 */

public class SplashDataSourceUnitTest extends BaseDataSourceUnitTest<SplashDataSource> {

    @Test
    public void isUsernameValidTest() {
        assertFalse("Username should be invalid - it's empty", mDataSource.isUsernameValid(""));
        assertFalse("Username should be invalid - it's null", mDataSource.isUsernameValid(null));
        assertFalse("Username should be invalid - contains spaces", mDataSource.isUsernameValid("username with spaces"));
        assertTrue("Username should be valid", mDataSource.isUsernameValid("usernameWithoutSpaces"));
    }

}
