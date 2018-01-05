package pl.przepisnik.cucumber.stepdefs;

import pl.przepisnik.PrzepisnikApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = PrzepisnikApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
