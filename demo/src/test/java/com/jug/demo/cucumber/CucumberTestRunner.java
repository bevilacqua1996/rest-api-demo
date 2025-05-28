package com.jug.demo.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.jug.demo.cucumber",
    plugin = {"pretty", "html:target/cucumber-reports"}
)
public class CucumberTestRunner {
    // This class is empty. It's just a runner.
}