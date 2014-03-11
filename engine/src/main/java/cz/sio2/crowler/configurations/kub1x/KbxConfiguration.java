package cz.sio2.crowler.configurations.kub1x;

import java.util.Map;

import cz.sio2.crowler.Factory;
import cz.sio2.crowler.Utils;
import cz.sio2.crowler.model.ClassSpec;
import cz.sio2.crowler.model.Configuration;
import cz.sio2.crowler.model.ConfigurationFactory;
import cz.sio2.crowler.model.EnumeratedNextPageResolver;

public class KbxConfiguration implements ConfigurationFactory {

	final String SOURCE_URL = "http://www.inventati.org/kub1x/t/";
	
	@Override
	public Configuration getConfiguration(final Map<String, String> properties) {
        final Configuration conf = new Configuration();
        
        // Set technical details //
        conf.setSchemas(new String[]{KbxModel.idPrefix});
        conf.setEncoding("iso-8859-2");
        conf.setLang("cs");
        conf.setPublisher(SOURCE_URL);
        
        // Build and set source URL list
        //SimpleDateFormat f = new SimpleDateFormat();
        //f.applyPattern("dd.MM.yyyy");
        //String date = f.format(Calendar.getInstance().getTime());
        conf.setNextPageResolver(new EnumeratedNextPageResolver(SOURCE_URL));

        // Set ID and IRI
        conf.setId(Utils.getFullId("person"));
        conf.setBaseOntoPrefix(KbxModel.idPrefix);

        // Get base object (for each tr in table)
        ClassSpec chObject = Factory.createClassSpec(KbxModel.personRecord.getURI());
        conf.addInitialDefinition(Factory.createInitialDefinition(chObject, Factory.createJSoupSelector("table tr")));

        // Get it's first name spec
        chObject.addSpec(true, Factory.createDPSpec(Factory.createJSoupSelector("td:eq(0)"), KbxModel.hasFirstName.getURI()));
        chObject.addSpec(true, Factory.createDPSpec(Factory.createJSoupSelector("td:eq(1)"), KbxModel.hasFamilyName.getURI()));
        chObject.addSpec(Factory.createDPSpec(Factory.createJSoupSelector("td:eq(2)"), KbxModel.hasPhoneNumber.getURI()));

        
        
        return conf;
	}

}
