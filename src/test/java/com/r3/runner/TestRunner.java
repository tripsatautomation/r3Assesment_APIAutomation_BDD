package com.r3.runner;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources", glue = { "com/r3/runner", "com/r3/stepdef" }

		, plugin = { "pretty", "json:target/cucumber.json", "html:target/cucumber-reports",
				"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:" }, monochrome = true)
public class TestRunner extends AbstractTestNGCucumberTests {

	public static void main(String[] args) {
		//io.cucumber.core.cli.Main.run(args, Thread.currentThread().getContextClassLoader());
	}

}
