package kaitech;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features="src/test/resources/features",
		 glue="kaitech.stepdefs",
         plugin={"pretty"},
         snippets = CucumberOptions.SnippetType.CAMELCASE)
		 
public class RunCucumberTest {
}