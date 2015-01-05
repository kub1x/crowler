package cz.sio2.crowler;

import java.io.File;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import cz.sio2.crowler.connectors.FileJenaConnector;
import cz.sio2.crowler.connectors.SesameJenaConnector;
import cz.sio2.crowler.scenario.JsonScenarioParser;
import cz.sio2.crowler.scenario.Scenario;
import cz.sio2.crowler.selenium.WebDriverCrawler;

public class Runner {

    @Option(name = "--sesameUrl")
    private String sesameUrl = null;

    @Option(name = "--repositoryId")
    private String repositoryId = null;

    @Option(name = "--phantom", usage = "specify where Phantom is installed")
    private String phantomPath = "C:/Program Files/phantomjs-1.9.8/phantomjs.exe";

    @Option(name = "--rdfDir", usage = "print output as RDF files into directory specified by this argument (currently default, required)")
    private String rdfDir = null;

    @Option(name = "--scenario", usage = "path to xml scenario (currently required)")
    private String scenarioPath = null;

    public static void main(String[] args) throws Exception {
        new Runner().doMain(args);
    }

    public void doMain(String[] args) throws Exception {

        System.setProperty("phantom.path", phantomPath);

        // Parsing console arguments
        CmdLineParser parser = new CmdLineParser(this);
        parser.setUsageWidth(160); // Wider console output
        try {
            // parse the arguments.
            parser.parseArgument(args);

        } catch (CmdLineException e) {
            // if there's a problem in the command line,
            // you'll get this exception. this will report
            // an error message.
            System.err.println(e.getMessage());
            System.err.println("usage: java -jar crowler.jar [args - see below]");
            // print the list of available options
            parser.printUsage(System.err);
            System.err.println();

            return;
        }

        System.out.println("DEBUG rdfDir: " + rdfDir);
        System.out.println("DEBUG scenarioPath: " + scenarioPath);

        JenaConnector connector = null;
        if (sesameUrl != null) {
            XTrustProvider.install();
            final SesameJenaConnector cx = new SesameJenaConnector();
            cx.setServerUrl(sesameUrl);
            cx.setRepositoryId(repositoryId);
            // cx.setServerUrl("http://onto.mondis.cz/openrdf-sesame");//args[1]);
            // cx.setServerUrl("http://onto.mondis.cz/openrdf-sesame");//args[1]);
            // cx.setRepositoryId("mondis-webdata");//args[2]);//id+"-"+Calendar.getInstance());
            // cx.setServerUrl("https://dev.sio2.cz/openrdf-sesame");//args[1]);
            // cx.setRepositoryId("monumnet-webdata");//args[2]);//id+"-"+Calendar.getInstance());
            connector = cx;
        } else if (rdfDir != null) {
            connector = new FileJenaConnector(new File(rdfDir), false);
        }

        System.out.println("-- starting --");

        // Parse scenario
        Scenario scenario = JsonScenarioParser.parse(scenarioPath);

        System.out.println("-- running WebDriverCrawler --");

        // Run crowler
        try (WebDriverCrawler wdc = new WebDriverCrawler(connector, scenario)) {
            wdc.doIt();
        }

        System.out.println("-- finished --");
    }

}
