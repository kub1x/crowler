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

    @Option(name = "--help", usage = "print this help")
    private boolean isHelp = false;

    @Option(name = "--scenario", usage = "path to xml scenario (currently required)")
    private String scenarioPath = null;

    @Option(name = "--rdfDir", usage = "store output RDF files into directory specified by this argument")
    private String rdfDir = null;

    @Option(name = "--sesameUrl", usage = "URL of sesame repository (if specified --rdfDir is ignored)")
    private String sesameUrl = null;

    @Option(name = "--repositoryId", usage = "id of target sesame repository, required with --sesameUrl")
    private String repositoryId = null;

    @Option(name = "--phantom", usage = "specify where PhantomJs is installed (Firefox will be used otherwise)")
    private String phantomPath = null; // "C:/Program Files/phantomjs-1.9.8/phantomjs.exe";

    public static void main(String[] args) throws Exception {
        new Runner().doMain(args);
    }

    public void doMain(String[] args) throws Exception {
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

        if (isHelp) {
            dieWithUsage("", parser);
        }

        if (scenarioPath == null) {
            dieWithUsage("Missing argument: --scenario", parser);
        }

        if (phantomPath != null) {
            System.setProperty("phantom.path", phantomPath);
        } else {
            // dieWithUsage("Missing argument: --phantom", parser);
        }

        JenaConnector connector = null;
        if (sesameUrl != null && repositoryId != null) {
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
        } else {
            // Default to current directory
            System.err.println("Warning: missing some target attribute. Storing results into current directory. ");
            connector = new FileJenaConnector(new File("."), false);
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

    void dieWithUsage(String msg, CmdLineParser parser) {
        System.err.println(msg);
        System.err.println();
        System.err.println("usage:   java -jar crowler.jar --scenario <file_path> (--rdfDir <dir_path> | (--sesameUrl <url> --repositoryId <id>)) [--phantom <phantom_path>]");
        System.err.println();
        parser.printUsage(System.err);
        System.err.println();
        System.exit(1);
    }
}
