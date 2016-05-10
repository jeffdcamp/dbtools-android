Migration to 7.0.x
==================
 * DBTools now uses a wrapped version of ContentValues (to help support unit testing)... All record classes will need to change its constructor: from
 
            public Individual(ContentValues values) 
            
            to 
            
            public Asset(DBToolsContentValues values)
            
 * Any Manager class that creates a Content value (via "new ContentValues()") need to change to "DBToolsContentValues values = createNewDBToolsContentValues();"

DatabaseManager

 * A newly generated file (AppDatabaseConfig) will need to be assigned to DatabaseManager
 * Change DatabaseManager constructor to:
 
         public DatabaseManager(DatabaseConfig databaseConfig) {
             super(databaseConfig);
         }
     
 * Migrate content from DatabaseManager.identifyDatabases() to AppDatabaseConfig.identifyDatabases() then remove DatabaseManager.identifyDatabases()
 * Migrate content from DatabaseManager.createNewDatabaseWrapper() to AppDatabaseConfig.createNewDatabaseWrapper() then remove DatabaseManager.createNewDatabaseWrapper()
 * Assign the AppDatabaseConfig to the DatabaseManager
 
        Injection:
            @Provides
            @Singleton
            DatabaseConfig provideDatabaseConfig(Application application) {
                return new AppDatabaseConfig(application);
            }
        
        Non-Injection:
            // in App.java (your Application java file)
            databaseManager = new DatabaseManager(new AppDatabaseConfig(this));
 

Migration to 3.x
=========================

build.gradle
------------
 * Update dbtools-android and dbtools-gen to the latest 3.x.x version
 * Remove genConfig.setEncryptionSupport(true/false) from dbtools task

Regenerate domain java files
----------------------------
 * run "./gradlew dbtools"

DatabaseManager.java
--------------------
 * Add createNewDatabaseWrapper(...) method

     @Override
     public DatabaseWrapper createNewDatabaseWrapper(AndroidDatabase androidDatabase) {
        return new AndroidDatabaseWrapper(androidDatabase.getPath());
     }

Proguard
--------
 * If using proguard, update your proguard.cfg to include the proguard items specified in the README.md file


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


Migration from 1.x to 2.x
=========================

General Code Changes
--------------------
 * Rename any findByRowID(...) to findByRowId(...)
 * Remove BaseManager.java and BaseRecord.java (no longer needed)

DatabaseManager.java
--------------------
 * Change extend to DatabaseBaseManager
 * Remove onCreate(...).  Default database creation can now be done by DatabaseBaseManager.onCreate().  You can still override this (if needed).

XXXManager.java
---------------
 * Rename any executeSQL(...) to executeSql(...)

schema.xml
----------
 * If you are currently using a "table" to map to a "view"... change "table" to "view" and remove "primaryKey" attribute from any "view" fields

build.gradle
------------
 * Change org.dbtools.gen.android.AndroidDBObjectBuilder.buildAll(...) to org.dbtools.gen.android.AndroidObjectsBuilder.buildAll(...)
