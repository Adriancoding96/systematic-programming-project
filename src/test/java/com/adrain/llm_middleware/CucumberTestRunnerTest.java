package com.adrain.llm_middleware;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = "cucumber.glue", value = "com.adrain.llm_middleware.steps")
@ConfigurationParameter(key = "cucumber.plugin", value = "pretty")
public class CucumberTestRunnerTest {

  
}
