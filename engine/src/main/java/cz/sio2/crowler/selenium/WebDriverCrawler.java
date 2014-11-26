package cz.sio2.crowler.selenium;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import cz.sio2.crowler.JenaConnector;
import cz.sio2.crowler.scenario.OntoElemStep;
import cz.sio2.crowler.scenario.Scenario;
import cz.sio2.crowler.scenario.Step;
import cz.sio2.crowler.scenario.ValueOfStep;

public class WebDriverCrawler {
    private Logger logger = LoggerFactory.getLogger(WebDriverCrawler.class.getName());

    private JenaConnector connector;
    private Scenario scenario;
    private FirefoxDriver wd;

    public WebDriverCrawler(JenaConnector connector, Scenario scenario) {
        this.connector = connector;
        this.scenario = scenario;
        this.wd = new FirefoxDriver();
    }

    public void doIt() {

        this.connector.connect();

        // -----------------------------------

        final OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

        // TODO Kde se vezme ontology ID?
        String ontoId = String.format("http://kub1x.org/onto/%s-%s", scenario.getName(), (new SimpleDateFormat("yyyyMMddHHHmmssSSS").format(new Date())));
        final Ontology ont = model.createOntology(ontoId);

        // Potrebujeme seznam importu
        // e.x.: http://onto.mondis.cz/resource/npu/
        // for (final String imp : scenario.getImports()) {
        // ont.addImport(ModelFactory.createOntologyModel().createOntology(imp));
        // }

        // -----------------------------------

        wd.manage().window().maximize();

        // wd.get("url");

        wd.navigate().to(scenario.getUrl());

        handleSteps(scenario.getSteps());

        // Persist
        final Model persistModel = this.connector.getModel(ontoId);
        persistModel.add(model);
        this.connector.closeModel(persistModel);

        // List<WebElement> list = new ArrayList<>();
        //
        // String id = "";
        // list = wd.findElements(By.id(id));
        //
        // String xpath = "";
        // list = wd.findElements(By.xpath(xpath));
        //
        // String selector = "";
        // list = wd.findElements(By.cssSelector(selector));

        wd.close(); // quit()
        this.connector.disconnect();
    }

    private void handleSteps(List<Step> steps) {
        for (Step s : steps) {
            switch (s.getCommand()) {
            case ONTO_ELEM:
                handleStep((OntoElemStep) s);
            case VALUE_OF:
                handleStep((ValueOfStep) s);
            default:
                throw new RuntimeException("unknown command");
            }
        }

    }

    private void handleStep(OntoElemStep s) {
        String selector = s.getSelector();
        String typeof = s.getTypeof();
        List<WebElement> elements = this.wd.findElements(By.cssSelector(selector));
        for (WebElement element : elements) {

        }

    }

    private void handleStep(ValueOfStep s) {

    }

}
