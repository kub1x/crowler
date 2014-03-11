package cz.sio2.crowler.configurations.npu;

import cz.sio2.crowler.Utils;
import cz.sio2.crowler.Factory;
import cz.sio2.crowler.Vocabulary;
import cz.sio2.crowler.model.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class ImmovableHeritageConfiguration extends MonumnetConfiguration implements ConfigurationFactory {

    public static final int MAX_TO = 1738; // all including monuments that are not protected any more, 29.9.2013

    public Configuration getConfiguration(final Map<String,String> properties) {
        final Configuration conf = new Configuration();

        final int from = loadInteger("from",properties,0);
        final int to = loadInteger("to",properties,MAX_TO);

        conf.setSchemas(new String[]{NPU.idPrefix});
        conf.setEncoding("iso-8859-2");
        conf.setLang("cs");
        conf.setPublisher(NPU.MONDIS_IRI + "organisation/npu");

        SimpleDateFormat f = new SimpleDateFormat();
        f.applyPattern("dd.MM.yyyy");
        String date = f.format(Calendar.getInstance().getTime());
        conf.setNextPageResolver(new TableRecordsNextPageResolver(MONUMNET_URL+"pamfond/list.php?hledani=1&KrOk=&HiZe=&VybUzemi=1&sNazSidOb=&Adresa=&Cdom=&Pamatka=&CiRejst=&Uz=&PrirUbytOd=3.5.1958&PrirUbytDo=" + date + "&Start=", from, to, 25));

        conf.setId(Utils.getFullId(MONUMNET_PREFIX + "immovable"));
        conf.setBaseOntoPrefix(NPU.idPrefix);

        String detailPageFormat = "div table tbody tr td table tbody tr th:contains(%s) ~ td";
        String detailLandRegistry = "div#parcely table tr.list ~ tr.list";                  // filter out first row

        ClassSpec chObject = Factory.createClassSpec(NPU.monumnetRecord.getURI());

        conf.addInitialDefinition(Factory.createInitialDefinition(chObject, Factory.createJSoupSelector("table tbody tr.list")));
        chObject.addSpec(Factory.createDPSpec(Factory.createJSoupSelector("td:eq(0)"), NPU.hasCHObjectNumber.getURI()));

        ClassSpec sDistrict = Factory.createClassSpec(NPU.district.getURI());
        chObject.addSpec(Factory.createOPSpec(Factory.createJSoupSelector("td:eq(2)"), NPU.hasDistrict.getURI(), sDistrict));
        sDistrict.addSpec(true, Factory.createDPSpec(Vocabulary.RDFS_LABEL));

        chObject.addSpec(Factory.createDPSpec(Factory.createJSoupSelector("td:eq(3)"), NPU.hasLocalityName.getURI()));
        chObject.addSpec(Factory.createDPSpec(Factory.createJSoupSelector("td:eq(4)"), NPU.hasIdentifiableLandRegistry.getURI()));
        final NewDocumentSelector s = Factory.createNewDocumentSelector(conf.getEncoding(),
                Factory.createAttributePatternMatchingURLCreator("onclick", ".*([0-9]+).*", MONUMNET_URL+"pamfond/list.php?IdReg=" + "{0}"));
                                                                                 // TODO maybe '*('[0-9]+').*'
        chObject.addSpec(Factory.createDPSpec(Factory.createChainedFirstElementSelector(s, Factory.createJSoupSelector(String.format(detailPageFormat, "Číslo popisné"))), NPU.hasLandRegistryNumber.getURI(), Vocabulary.XSD_INTEGER, false));

        chObject.addSpec(Factory.createAPSpec(Factory.createJSoupSelector("td:eq(6)"), Vocabulary.RDFS_LABEL));
        chObject.addSpec(Factory.createDPSpec(Factory.createJSoupSelector("td:eq(7)"), NPU.hasLocation.getURI()));
        chObject.addSpec(Factory.createDPSpec(Factory.createJSoupSelector("td:eq(8)"), NPU.hasHouseNumber.getURI(), null, false));
        chObject.addSpec(true,Factory.createDPSpec(Factory.createJSoupSelector("td:eq(12)"), NPU.hasIdRegNumber.getURI(),Vocabulary.XSD_INTEGER,false));

        ClassSpec sHistoricCountry = Factory.createClassSpec(NPU.historicCountry.getURI());
        chObject.addSpec(Factory.createOPSpec(Factory.createChainedFirstElementSelector(s, Factory.createJSoupSelector(String.format(detailPageFormat, "Historická země"))), NPU.hasHistoricCountry.getURI(), sHistoricCountry));
        sHistoricCountry.addSpec(true, Factory.createDPSpec(Vocabulary.RDFS_LABEL));

        ClassSpec sLandRegistry = Factory.createClassSpec(NPU.landregistry.getURI());
        chObject.addSpec(Factory.createOPSpec(Factory.createChainedFirstElementSelector(s, Factory.createJSoupSelector(String.format(detailPageFormat, "Katastrální území"))), NPU.hasLandRegistry.getURI(), sLandRegistry));
        sLandRegistry.addSpec(true, Factory.createDPSpec(Vocabulary.RDFS_LABEL));

        chObject.addSpec(Factory.createDPSpec(Factory.createChainedFirstElementSelector(s, Factory.createJSoupSelector(String.format(detailPageFormat, "Památkou od"))), NPU.isRegisteredMonumentSince.getURI()));
        chObject.addSpec(Factory.createDPSpec(Factory.createChainedFirstElementSelector(s, Factory.createJSoupSelector(String.format(detailPageFormat, "Ochrana stav/typ uzavření"))), NPU.hasProtectionState.getURI()));

        ClassSpec sBuildingOffice = Factory.createClassSpec(NPU.buildingOffice.getURI());
        chObject.addSpec(Factory.createOPSpec(Factory.createChainedFirstElementSelector(s, Factory.createJSoupSelector(String.format(detailPageFormat, "Stavební úřad"))), NPU.hasBuildingOffice.getURI(), sBuildingOffice));
        sBuildingOffice.addSpec(true, Factory.createDPSpec(Vocabulary.RDFS_LABEL));

        ClassSpec sRevenueOffice = Factory.createClassSpec(NPU.revenueOffice.getURI());
        chObject.addSpec(Factory.createOPSpec(Factory.createChainedFirstElementSelector(s, Factory.createJSoupSelector(String.format(detailPageFormat, "Finanční úřad"))), NPU.hasRevenueOffice.getURI(), sRevenueOffice));
        sRevenueOffice.addSpec(true, Factory.createDPSpec(Vocabulary.RDFS_LABEL));

        ClassSpec sLandRegistryRecord = Factory.createClassSpec(NPU.landregistryrecord.getURI());
        chObject.addSpec(Factory.createOPSpec(Factory.createChainedFirstElementSelector(s, Factory.createJSoupSelector(detailLandRegistry)), NPU.hasLandRegistryRecord.getURI(), sLandRegistryRecord));
        sLandRegistryRecord.addSpec(true, Factory.createDPSpec(Factory.createJSoupSelector("td:eq(1)"), NPU.hasCadastralNumber.getURI()));
        // TODO dil
        sLandRegistryRecord.addSpec(Factory.createDPSpec(Factory.createJSoupSelector("td:eq(4)"), NPU.hasProtectionRestriction.getURI()));
        sLandRegistryRecord.addSpec(Factory.createDPSpec(Factory.createJSoupSelector("td:eq(5)"), Vocabulary.RDFS_COMMENT));     // specifikace, poznámka
        sLandRegistryRecord.addSpec(true, Factory.createOPSpec(Factory.createJSoupSelector(10, "th:contains(Katastrální území) ~ td"), NPU.hasLandRegistry.getURI(), sLandRegistry));

        return conf;
    }

}
