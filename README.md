DBTools for Android
=================

DBTools for Android is an Android ORM library that makes it easy to work with SQLite Databases.


Setup
=====

*For a working implementation of DBTools for Android see the Android-Template application (https://github.com/jeffdcamp/android-template)

  1. Add DBTools Generator to your "buildscript" section of the build.gradle file

        buildscript {
            repositories {
                mavenCentral()
            }
            dependencies {
                classpath 'com.android.tools.build:gradle:0.7.+'
                classpath 'org.dbtools:dbtools-gen:1.+'
            }
        }

  2. Be sure that "mavenCental()" is a part of your "repositories" section of the build.gradle file

        repositories {
            mavenCentral()
        }

  3. Add dbtools dependency to your "dependencies" section of the build.gradle file.  (latest version is found in Maven Central Repo)

        dependencies {
            compile 'org.dbtools:dbtools-android:<latest version>'
        }

  4. Add dbtools "task" to your build.gradle file.  Be sure to modify the variables/properties in this task (especially "baseOutputDir" and "basePackageName")

        task dbtools {
            description = 'Generate DBTools domain classes'
            doLast {
                System.out.println("Generating DBTools Classes...")

                // properties
                String schemaFilename = "src/main/database/schema.xml";
                boolean injectionSupport = true; // support for CDI
                boolean dateTimeSupport = true; // support for jsr DateTime (Joda Time)
                String baseOutputDir = "src/main/java/org/company/project/domain";
                String basePackageName = "org.company.project.domain";
                org.dbtools.gen.android.AndroidDBObjectBuilder.buildAll(schemaFilename, baseOutputDir, basePackageName, injectionSupport, dateTimeSupport);
            }
        }

  5. Add schema.xml file (and optionally the XSD definition file (this may help writing the XML file in some IDE's)), to the /src/main/database directory.  This file contains a list of all of the databases and tables in each database.  The following is a sample of this file:

        <?xml version="1.0" encoding="UTF-8" ?>
        <dbSchema xmlns='https://github.com/jeffdcamp/dbtools-gen'
                  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
                  xsi:schemaLocation='https://github.com/jeffdcamp/dbtools-gen dbschema.xsd'>
            <database name="main">
                <table name="INDIVIDUAL_TYPE" className="IndividualType" enumerations="HEAD,SPOUSE,CHILD">
                    <field name="_id" jdbcDataType="BIGINT" increment="true" primaryKey="true" notNull="true"/>
                    <field name="NAME" jdbcDataType="VARCHAR" size="255" notNull="true" unique="true"/>
                </table>

                <table name="INDIVIDUAL">
                    <field name="_id" jdbcDataType="BIGINT" increment="true" primaryKey="true" notNull="true"/>
                    <field name="INDIVIDUAL_TYPE_ID" jdbcDataType="INTEGER" varName="individualType" foreignKeyTable="INDIVIDUAL_TYPE" foreignKeyField="_id" foreignKeyType="ENUM" enumerationDefault="HEAD"/>
                    <field name="NAME" jdbcDataType="VARCHAR" size="255" notNull="true"/>
                    <field name="BIRTH_DATE" jdbcDataType="TIMESTAMP"/>
                    <field name="PHONE" jdbcDataType="VARCHAR" size="255"/>
                    <field name="EMAIL" jdbcDataType="VARCHAR" size="255"/>
                </table>
            </database>
        </dbSchema>

  6. Create DatabaseManager.java.  This class manages all database connections, creates and updates databases (this version uses CDI injection for the context).  (This file should initially be generated in the future)

        package org.company.project.domain;

        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.util.Log;
        import org.company.project.ForApplication;
        import org.company.project.MyApplication;
        import org.company.project.domain.individual.Individual;
        import org.company.project.domain.individualtype.IndividualType;
        import org.dbtools.android.domain.AndroidDatabase;
        import org.dbtools.android.domain.AndroidDatabaseManager;
        import java.io.File;
        import javax.inject.Inject;
        import javax.inject.Singleton;

        /**
         * This class helps open, create, and upgrade the database file.
         */
        @Singleton
        public class DatabaseManager extends AndroidDatabaseManager {
            private static final String TAG = MyApplication.createTag(DatabaseManager.class);

            public static final int DATABASE_VERSION = 1;
            public static final String MAIN_DATABASE_NAME = "main"; // !!!! WARNING be SURE this matches the value in the schema.xml !!!!

            @ForApplication
            @Inject
            public Context context;


            @Override
            public void identifyDatabases() {
                addDatabase(context, DatabaseManager.MAIN_DATABASE_NAME, DatabaseManager.DATABASE_VERSION);
            }

            @Override
            public void onCreate(AndroidDatabase androidDatabase) {
                Log.i(TAG, "Creating database: " + androidDatabase.getName());
                SQLiteDatabase database = androidDatabase.getSqLiteDatabase();

                // use any record manager to begin/end transaction
                database.beginTransaction();

                // Enum Tables
                BaseManager.createTable(database, IndividualType.CREATE_TABLE);

                // Regular Tables
                BaseManager.createTable(database, Individual.CREATE_TABLE);

                // end transaction
                database.setTransactionSuccessful();
                database.endTransaction();
            }

            @Override
            public void onUpgrade(AndroidDatabase androidDatabase, int oldVersion, int newVersion) {
                Log.i(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);

                // Wipe database version if it is different.
                if (oldVersion != DATABASE_VERSION) {
                    onCleanDatabase(androidDatabase);
                }
            }

            @Override
            public void onCleanDatabase(AndroidDatabase androidDatabase) {
                Log.i(TAG, "Cleaning Database");
                SQLiteDatabase database = androidDatabase.getSqLiteDatabase();
                String databasePath = androidDatabase.getPath();
                database.close();

                Log.i(TAG, "Deleting database: [" + databasePath + "]");
                File databaseFile = new File(databasePath);
                if (databaseFile.exists() && !databaseFile.delete()) {
                    String errorMessage = "FAILED to delete database: [" + databasePath + "]";
                    Log.e(TAG, errorMessage);
                    throw new IllegalStateException(errorMessage);
                }

                connectDatabase(androidDatabase.getName(), false);  // do not update here, because it will cause a recursive call
            }
        }


  7. Create BaseRecord.java and BaseManager.java files in the "baseOutputDir" (as specified in the build.gradle).  (These files should initially be generated in the future)

        // BaseManager.java
        package org.company.project.domain;

        //import net.sqlcipher.database.SQLiteDatabase;  // used for sqlcipher databases
        //import org.dbtools.android.domain.secure.AndroidBaseManager; // used for sqlcipher databases

        import android.database.sqlite.SQLiteDatabase;
        import org.dbtools.android.domain.AndroidBaseManager;


        import javax.inject.Inject;

        public abstract class BaseManager<T extends BaseRecord> extends AndroidBaseManager<T> {

            @Inject
            public DatabaseManager databaseManager;

            public SQLiteDatabase getReadableDatabase() {
                return databaseManager.getReadableDatabase(getDatabaseName());
            }

            public SQLiteDatabase getReadableDatabase(String databaseName) {
                return databaseManager.getReadableDatabase(databaseName);
            }

            public SQLiteDatabase getWritableDatabase() {
                return databaseManager.getWritableDatabase(getDatabaseName());
            }

            public SQLiteDatabase getWritableDatabase(String databaseName) {
                return databaseManager.getWritableDatabase(databaseName);
            }
        }


        // BaseRecord.java
        package org.company.project.domain;

        import org.dbtools.android.domain.AndroidBaseRecord;

        public abstract class BaseRecord extends AndroidBaseRecord {
        }


  8. Use DBTools Generator to generate domain classes.  Execute gradle task:

        ./gradlew dbtools

  9. DBTools Generator will create the following files for each table

        individual/
               Individual.java (extends IndividualBaseRecord and is used for developer customizations) (NEVER overwritten by generator)
               IndividualBaseRecord.java (contains boiler-plate code for doing CRUD operations and contains CONST names of the table and all columns (used to help writing queries)) (this file is ALWAYS overwritten by generator)

               IndividualManager.java (extends IndividualBaseManager and is used for developer customizations (such as adding new findByXXX(...) methods) (NEVER overwritten by generator)
               IndividualBaseManager.java (contains boiler-plate code for doing CRUD operations) (this file is ALWAYS overwritten by generator)

Usage
=====

  At this point DBTools for Android is all setup and your Domain classes have been created.  The following are some use cases:

  * Add data to the database

        @Inject
        IndividualManager individualManager;

        ...

        // create a new domain object
        Individual individual = new Individual();
        individual.setName("Jeff Campbell");
        individual.setPhone("801-555-1234");

        individualManager.save(individual);

  * Update data to the database

        Individual individual = individualManager.findByRowID(1);
        individual.setPhone("801-555-0000");
        individualManager.save(individual);

  * Delete data from the database

        Individual individual = individualManager.findByRowID(1);
        individualManager.delete(individual);

  DBTools Manager has a bunch of built-in methods that make working with tables even easier.  Here is a few examples:

  * Get records

        Individual individual = individualManager.findByRowID(1);
        Individual individual = individualManager.findBySelection(Individual.C_PHONE + " LIKE ?, new String[]{"555"}); // find FIRST individual who has "555" in their phone number

        List<Individual> allIndividuals = individualManager.findAll();
        List<Individual> allOrderedIndividuals = individualManager.findAllOrderBy(Individual.C_NAME);
        List<Individual> specificIndividuals = individualManager.findAllBySelection(Individual.C_PHONE + " LIKE ?, new String[]{"555"}); // find all those who have "555" in their phone number

  * Using cursors

        Cursor cursor = individualManager.findCursorBySelection(null, null, Individual.C_NAME); // find all, order by NAME column
        Cursor cursor = individualManager.findCursorBySelection(Individual.C_PHONE + " LIKE ?, new String[]{"555"}); // find cursor of those who have "555" in their phone number

  * Count number of items in the database

        int count = individualManager.findCount();
        int count = individualManager.findCountBySelection(Individual.C_PHONE + " LIKE ?, new String[]{"555"}); // find count of those who have "555" in their phone number



License
=======

    Copyright 2013 Jeff Campbell

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
