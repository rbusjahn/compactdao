package org.rbusjahn.compactdao.cucumber;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty", "html:target/cucumber_html", "json:target/cucumber.json" })
public class CucumberTest {

}
