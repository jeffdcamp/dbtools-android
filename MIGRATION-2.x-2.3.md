Migration from 2.x to 2.3
=========================

General Code Changes
--------------------
 * AndroidBaseManager.delete() now returns int (instead of long)

build.gradle
------------
 * Change org.dbtools.gen.android.AndroidObjectsBuilder to use GenConfig.  Example:
        org.dbtools.gen.android.AndroidObjectsBuilder builder = new org.dbtools.gen.android.AndroidObjectsBuilder();

        builder.setXmlFilename("src/main/database/schema.xml");
        builder.setOutputBaseDir("src/main/java/org/company/project/domain");
        builder.setPackageBase("org.company.project.domain");

        org.dbtools.gen.GenConfig genConfig = new org.dbtools.gen.GenConfig();
        genConfig.setInjectionSupport(true);
        genConfig.setJsr305Support(false);
        genConfig.setDateTimeSupport(true);
        genConfig.setEncryptionSupport(false);
        genConfig.setIncludeDatabaseNameInPackage(true);
        genConfig.setOttoSupport(true);

        builder.setGenConfig(genConfig);
        builder.build();