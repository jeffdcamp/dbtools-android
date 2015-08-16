DBTools for Android
=================

DBTools for Android is an Android ORM library that makes it easy to work with SQLite Databases.

Usage
=====

The following are some examples DBTools can be used:

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

        // managers share transactions (use any manager to begin/end a transaction)
        individualManager.beginTransaction();  
        boolean success = true;

        individualManager.save(individual1);
        individualManager.save(individual2);
        individualManager.save(individual3);
        individualManager.save(individual4);
        individualManager.save(individual5);

        // if false, transaction is reverted
        individualManager.endTransaction(success); 

  * Update data to the database

        Individual individual = individualManager.findByRowId(1);
        individual.setPhone("801-555-0000");
        individualManager.save(individual);

  * Delete data from the database

        Individual individual = individualManager.findByRowId(1);
        individualManager.delete(individual);
        
        individualManager.delete(1); // delete by primary key id
        
        // delete all individuals who has "555" in their phone number
        individualManager.delete(Individual.C_PHONE + " LIKE ?, new String[]{"555"}); 

  DBTools Manager classes have a bunch of built-in methods that make working with tables even easier.  Here is a few examples:

  * Get records

        Individual individual = individualManager.findByRowId(1);
        
        // find FIRST individual who has "555" in their phone number
        Individual individual = individualManager.findBySelection(Individual.C_PHONE + " LIKE ?", new String[]{"555"}); 


        List<Individual> allIndividuals = individualManager.findAll();
        List<Individual> allOrderedIndividuals = individualManager.findAllOrderBy(Individual.C_NAME);
        
        // find all those who have "555" in their phone number
        List<Individual> specificIndividuals = individualManager.findAllBySelection(Individual.C_PHONE + " LIKE ?, new String[]{"555"}); 

  * Using cursors

        // find all, order by NAME column
        Cursor cursor = individualManager.findCursorBySelection(null, null, Individual.C_NAME); 
        
        // find cursor of those who have "555" in their phone number
        Cursor cursor = individualManager.findCursorBySelection(Individual.C_PHONE + " LIKE ?, new String[]{"555"}); 
        
  * Access data from Cursor

        Cursor cursor;
        // populate all items from cursor into Individual
        Individual individual = new Individual(cursor); 
        
        // Get data from a single field, from a cursor.  (for use in places such as Adapters, etc) Examples:
        String name = Individual.getName(cursor);
        IndividualType type = Individual.getType(cursor);
        Date birthDate = Individual.getBirthDate(cursor);

  * Count number of items in the database

        int count = individualManager.findCount();
        // find count of those who have "555" in their phone number
        int count = individualManager.findCountBySelection(Individual.C_PHONE + " LIKE ?, new String[]{"555"}); 

  Support for ASync writes (guarantees single write per database)

  * Sample save

        Individual individual = individualManager.findByRowId(1);
        individual.setPhone("801-555-0000");
        individualManager.saveAsync(individual);


  EventBus support.  This allow your app to be notified if the database changed.  Examples:

  * Watch for any Insert/Update/Delete event

        @Subscribe
        public void onDatabaseChanged(DatabaseChangeEvent event) {
            Log.i(TAG, "Database changed on table " + event.getTableName());
        }

  * Watch for any Insert event

        @Subscribe
        public void onInsert(DatabaseInsertEvent event) {
            Log.i(TAG, "Item inserted on table " + event.getTableName());
            Log.i(TAG, "Item ID inserted " + event.getNewId());
        }

  * Watch for any Update event

        @Subscribe
        public void onUpdate(DatabaseUpdateEvent event) {
            Log.i(TAG, "Item inserted on table " + event.getTableName());
            Log.i(TAG, "RowsAffected " + event.getRowsAffected());
        }

  * Transactions.  Events will NOT be posted if in a transaction.  You can subscribe to watch for the end of a transaction:

        @Subscribe
        public void onDatabaseChangedTransaction(DatabaseEndTransactionEvent event) {
            Log.i(TAG, "Database changed, transaction end.  Tables changed: " + event.getAllTableName());
            boolean myTableUpdated = event.containsTable(Individual.TABLE);
        }

Setup
=====

*For a working implementation of DBTools for Android see the Android-Template application (https://github.com/jeffdcamp/android-template)

  1. Add DBTools Gradle Plugin and dbtools-android dependency to build.gradle file

        buildscript {
            repositories {
                mavenCentral()
            }
            dependencies {
                classpath 'org.dbtools:gradle-dbtools-plugin:<latest-version>'
            }
        }

        apply plugin: 'dbtools'

        dependencies {
            compile 'org.dbtools:dbtools-android:<latest dbtools-android version>'
            compile 'org.dbtools:dbtools-query:<latest dbtools-query version>' // optional
        }

        dbtools {
            type 'ANDROID'

            basePackageName 'org.company.project.domain'
            outputSrcDir 'src/main/java/org/company/project/domain'

            injectionSupport true // support for @Inject (using JEE, Dagger, Guice, etc)
            jsr305Support true // support for @Notnull / @Nullable etc
            includeDatabaseNameInPackage true // place each set of domain objects into a package named after its database
            eventBusSupport true // support Event Bus
            dateTimeSupport true // support Joda DateTime
        }

  2. For new projects, create initial schema.xml files (Default: new files will be created in src/main/database)

        ./gradlew dbtools-init

        ... or ...

        From Android Studio:  DOUBLE-CLICK on "dbtools-init" task from the "Gradle" Tools Window

  3. Define your database: Add schema.xml file (after executing the "dbtools-init" task (from above) an XSD definition file will be created (this may help writing the XML file in some IDE's)), to the /src/main/database directory.  This file contains a list of all of the databases and tables in each database.  The following is a sample of this file:

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

  4. Use DBTools Generator to generate DatabaseManager and all domain classes.  Execute gradle task:

        ./gradlew dbtools-genclasses

        ... or ...

        From Android Studio:  DOUBLE-CLICK on "dbtools-genclasses" task from the "Gradle" Tools Window

  DBTools Generator will create the following files to manage all database connections and create/update database tables:

       DatabaseManager.java (extends DatabaseBaseManager and is used for developer customizations.  Contains CONST names and versions of the databases and versions) (NEVER overwritten by generator)
       DatabaseBaseManager.java (contains boiler-plate code creating all tables and views for all databases defined in the schema.xml) (this file is ALWAYS overwritten by generator)

  DBTools Generator will create the following files for each table (example for the Individual table):

        individual/
               Individual.java (extends IndividualBaseRecord and is used for developer customizations) (NEVER overwritten by generator)
               IndividualBaseRecord.java (contains boiler-plate code for doing CRUD operations and contains CONST names of the table and all columns (used to help writing queries)) (this file is ALWAYS overwritten by generator)

               IndividualManager.java (extends IndividualBaseManager and is used for developer customizations (such as adding new findByXXX(...) methods) (NEVER overwritten by generator)
               IndividualBaseManager.java (contains boiler-plate code for doing CRUD operations) (this file is ALWAYS overwritten by generator)

Proguard Rules
==============

    # DBTools
    -dontwarn org.dbtools.query.**
    -dontwarn org.sqlite.**
    -dontwarn net.sqlcipher.**

    # SQLCipher (if using SQLCipher)
    -keep public class net.sqlcipher.** { *; }
    -keep public class net.sqlcipher.database.** { *; }

    # SQLite.org (if using sqlite from sqlite.org)
    -keep public class org.sqlite.** { *; }
    -keep public class org.sqlite.database.** { *; }


Upgrade
=======
Instructions for migration from 2.x to 3.x+ (https://github.com/jeffdcamp/dbtools-android/blob/master/MIGRATION-3.x.md)
Instructions for migration from 2.x to 2.3+ (https://github.com/jeffdcamp/dbtools-android/blob/master/MIGRATION-2.x-2.3.md)
Instructions for migration from 1.x to 2.x  (https://github.com/jeffdcamp/dbtools-android/blob/master/MIGRATION-1.x-2.x.md)

Other Projects
==============
DBTools Query - https://github.com/jeffdcamp/dbtools-query

Android Template - https://github.com/jeffdcamp/android-template

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
