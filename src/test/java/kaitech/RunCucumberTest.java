package kaitech;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features="src/main/resources/features",
		 glue="stepdefs",
         plugin={"pretty"},
         snippets = CucumberOptions.SnippetType.CAMELCASE)
		 
public class RunCucumberTest {
}