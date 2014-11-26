package cz.sio2.crowler;

import java.io.File;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import cz.sio2.crowler.connectors.FileJenaConnector;
import cz.sio2.crowler.scenario.JsonScenarioParser;
import cz.sio2.crowler.scenario.Scenario;
import cz.sio2.crowler.selenium.WebDriverCrawler;

//import cz.sio2.crowler.connectors.SesameJenaConnector;

public class Runner {

    // @Option(name="--sesameUrl")
    // private String sesameUrl = null;
    //
    // @Option(name="--repositoryId")
    // private String repositoryId = null;

    // @Option(name = "--confClass", usage="class of configuration to be used")
    // private String confClass = null;

    @Option(name = "--rdfDir", usage = "print output as RDF files into directory specified by this argument (currently default, required)")
    private String rdfDir = null;

    // @Option(name = "--jarDir",
    // usage="pass directory of the crowler.jar jarball (currently required)")
    // private String jarDir = null;

    @Option(name = "--scenario", usage = "path to xml scenario (currently required)")
    private String scenarioPath = null;

    // @Option(name = "--selenium", usage =
    // "flag if selenium crowling is to be used (currently default)")
    // private boolean selenium = true;

    public static void main(String[] args) {
        new Runner().doMain(args);
    }

    public void doMain(String[] args) {

        System.out.println("Working Directory = "
                + System.getProperty("user.dir"));

        // System.out.println("Jar directory = " + jarDir);

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
            System.err
                    .println("usage: java -jar crowler.jar [args - see below]");
            // print the list of available options
            parser.printUsage(System.err);
            System.err.println();

            return;
        }

        JenaConnector connector = null;

        if (rdfDir != null) {
            // connector = new FileJenaConnector(new File(jarDir, file), false);
            connector = new FileJenaConnector(new File(rdfDir), false);
        }
        // if (sesame != null) {
        // XTrustProvider.install();
        // final SesameJenaConnector cx = new SesameJenaConnector();
        // cx.setServerUrl(sesameUrl);
        // cx.setRepositoryId(repositoryId);
        //
        // //cx.setServerUrl("http://onto.mondis.cz/openrdf-sesame");//args[1]);
        // //cx.setRepositoryId("mondis-webdata");//args[2]);//id+"-"+Calendar.getInstance());
        // //cx.setServerUrl("https://dev.sio2.cz/openrdf-sesame");//args[1]);
        // //cx.setRepositoryId("monumnet-webdata");//args[2]);//id+"-"+Calendar.getInstance());
        // connector = cx;
        // }

        // Parse scenario
        // Scenario scenario = Scenario.parse(scenarioPath);
        Scenario scenario = JsonScenarioParser.parse(scenarioPath);

        // Run crowler
        WebDriverCrawler wdc = new WebDriverCrawler(connector, scenario);
        wdc.doIt();

        // try {
        // final ConfigurationFactory conf_fact = (ConfigurationFactory) Class
        // .forName(confClass).newInstance();
        //
        // new FullCrawler(connector).run(conf_fact
        // .getConfiguration(new HashMap(System.getProperties())));
        // } catch (ClassNotFoundException cnfe) {
        // cnfe.printStackTrace();
        // } catch (InstantiationException e) {
        // e.printStackTrace();
        // } catch (IllegalAccessException e) {
        // e.printStackTrace();
        // }
    }

}
