DBTools for Android
=================

DBTools for Android is an Android ORM library that makes it easy to work with SQLite Databases.

Instructions for migration from 1.x to 2.x (https://github.com/jeffdcamp/dbtools-android/blob/master/MIGRATION-1.x-2.x.md)

Setup
=====

*For a working implementation of DBTools for Android see the Android-Template application (https://github.com/jeffdcamp/android-template)

  1. Add DBTools Generator to your "buildscript" section of the build.gradle file

        buildscript {
            repositories {
                mavenCentral()
            }
            dependencies {
                classpath 'com.android.tools.build:gradle:0.10.+'
                classpath 'org.dbtools:dbtools-gen:<latest version>' // (2.+)
            }
        }

  2. Be sure that "mavenCental()" is a part of your "repositories" section of the build.gradle file

        repositories {
            mavenCentral()
        }

  3. Add dbtools dependency to your "dependencies" section of the build.gradle file.  (latest version is found in Maven Central Repo)

        dependencies {
            compile 'org.dbtools:dbtools-android:<latest version>' // (2.+)
        }

  4. Add dbtools "task" to your build.gradle file.  Be sure to modify the variables/properties in this task (especially "baseOutputDir" and "basePackageName")

        task dbtools {
            description = 'Generate DBTools domain classes'
            doLast {
                System.out.println("Generating DBTools Classes...")

                // properties
                org.dbtools.gen.android.AndroidObjectsBuilder builder = new org.dbtools.gen.android.AndroidObjectsBuilder();
                builder.setXmlFilename("src/main/database/schema.xml");
                builder.setOutputBaseDir("src/main/java/org/company/project/domain");
                builder.setPackageBase("org.company.project.domain");
                builder.setInjectionSupport(true);
                builder.setJsr305Support(true);
                builder.setDateTimeSupport(true);
                builder.setEncryptionSupport(false);
                builder.build();
            }
        }

  5. Add schema.xml file (after executing the "dbtools" task (from above) an XSD definition file will be created (this may help writing the XML file in some IDE's)), to the /src/main/database directory.  This file contains a list of all of the databases and tables in each database.  The following is a sample of this file:

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

  6. Use DBTools Generator to generate DatabaseManager and all domain classes.  Execute gradle task:

        ./gradlew dbtools

        ... or ...

        From Android Studio:  RIGHT-CLICK on the "task dbtools {", in the build.gradle file, and select "Run 'gradle:dbtools'"

  DBTools Generator will create the following files to manage all database connections and create/update database tables:

       DatabaseManager.java (extends DatabaseBaseManager and is used for developer customizations.  Contains CONST names and versions of the databases and versions) (NEVER overwritten by generator)
       DatabaseBaseManager.java (contains boiler-plate code creating all tables and views for all databases defined in the schema.xml) (this file is ALWAYS overwritten by generator)

  DBTools Generator will create the following files for each table (example for the Individual table):

        individual/
               Individual.java (extends IndividualBaseRecord and is used for developer customizations) (NEVER overwritten by generator)
               IndividualBaseRecord.java (contains boiler-plate code for doing CRUD operations and contains CONST names of the table and all columns (used to help writing queries)) (this file is ALWAYS overwritten by generator)

               IndividualManager.java (extends IndividualBaseManager and is used for developer customizations (such as adding new findByXXX(...) methods) (NEVER overwritten by generator)
               IndividualBaseManager.java (contains boiler-plate code for doing CRUD operations) (this file is ALWAYS overwritten by generator)

Usage
=====

  At this point DBTools for Android is all setup and your Domain classes have been created.  The following are some use cases:

  * Use manager classes to perform CRUD operations on tables (2 options: With injection or without injection)

        // USING INJECTION (recommended) (when using injection frameworks such as Dagger, RoboGuice, etc)
        @Inject
        IndividualManager individualManager;  // simply "Inject" your manager (it has access to the database directly)

        public void onSaveClicked() {
            // save
            individualManager.save(individual);
        }

        ... or ...

        // NO INJECTION
        public void onSaveClicked() {
            // get your database (can be pulled from shared location)
            DatabaseManager databaseManager = new DatabaseManager();
            databaseManager.setContext(this);
            SQLiteDatabase db = databaseManager.getWritableDatabase(DatabaseManager.MAIN_DATABASE_NAME);

            // save
            IndividualManager.save(db, individual); // static method call to manager
        }

  * Add data to the database

        // create a new domain object
        Individual individual = new Individual();
        individual.setName("Jeff Campbell");
        individual.setPhone("801-555-1234");
        individual.setIndividualType(IndividualType.HEAD); // enum table example

        individualManager.save(individual);

  * Transactions

        individualManager.beginTransaction();  // managers share transactions (use any manager to begin/end a transaction)
        boolean success = true;

        individualManager.save(individual1);
        individualManager.save(individual2);
        individualManager.save(individual3);
        individualManager.save(individual4);
        individualManager.save(individual5);

        individualManager.endTransaction(success); // if false, transaction is reverted

  * Update data to the database

        Individual individual = individualManager.findByRowId(1);
        individual.setPhone("801-555-0000");
        individualManager.save(individual);

  * Delete data from the database

        Individual individual = individualManager.findByRowId(1);
        individualManager.delete(individual);

  DBTools Manager has a bunch of built-in methods that make working with tables even easier.  Here is a few examples:

  * Get records

        Individual individual = individualManager.findByRowId(1);
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

    Copyright 2014 Jeff Campbell

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[migration]: https://github.com/jeffdcamp/dbtools-android/blob/master/MIGRATION-1.x-2.x.md