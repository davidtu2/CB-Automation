package com.set.movi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	MoviNesterBlackTest.class, 
	MoviLightTaskBlackTest.class, 
	MoviNesterFogTest.class, 
	MoviLightTaskFogTest.class, 
	MoviNesterWhiteTest.class, 
	MoviLightTaskWhiteTest.class,
	MoviStoolBlackTest.class,
	MoviStoolFogTest.class,
	MoviStoolWhiteTest.class
	})
public class AllMoviTests {

}
