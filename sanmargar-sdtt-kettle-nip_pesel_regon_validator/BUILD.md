### to build under Eclipse you need

* Apache Ivy Library
* Apache IvyDE Eclipse plugins

### build
1. run ant 

### test
1. copy zip file to data-integration/plugins/steps folder
2. restart PDI
3. run embedded in project PeselNipRegonValidate.ktr transformation

### test Marketplace
1. copy marketplaces.xml to ~/.kettle
2. correct marketplac.xml location in .kettle file
3. run PDI
4. go to Help->Marketplace
5. install Sanmargar components

### debug
1. Add jars from data-integration/lib to plugin project class path
2. Create run configuration Java Application type.
3. Set main class to org.pentaho.di.ui.spoon.Spoon
4. Set data-integration folder as Working Directory on Arguments Pane
5. Add options to VM arguments: 
-DKETTLE_PLUGIN_CLASSES=pl.sanmargar.sdtt.kettle.idsmess.NipPeselRegonMessMeta,pl.sanmargar.sdtt.kettle.nipvalidator.NIPValidatorMeta,pl.sanmargar.sdtt.kettle.peselvalidator.PESELValidatorMeta,pl.sanmargar.sdtt.kettle.regonvalidator.RegonValidatorMeta
6. Run or debug 


