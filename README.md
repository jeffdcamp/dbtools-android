DBTools for Android
=================

DBTools for Android is an Android ORM library that makes it easy to work with SQLite Databases.

Documentation
=============

DBTools-Android Javadoc: http://jeffdcamp.github.io/dbtools-android/javadoc/

Usage
=====

The following are some examples DBTools can be used:

  * Insert

        Individual individual = new Individual();
        individual.setName("Jeff Campbell");
        individual.setPhone("801-555-1234");
        individual.setIndividualType(IndividualType.HEAD);

        individualManager.save(individual);

  * Update

        Individual individual = individualManager.findByRowId(1);
        individual.setPhone("801-555-0000");
        individualManager.save(individual);

  * Delete

        // Delete using record object
        Individual individual = individualManager.findByRowId(1);
        individualManager.delete(individual);
        
        // Delete using the primary key id      
        individualManager.delete(1); 
        
        // Delete all individuals who has "555" in their phone number
        individualManager.delete(IndividualConst.C_PHONE + " LIKE ?, new String[]{"555"}); 
        
  * Transactions

        // Start transaction (all record managers share transactions)
        individualManager.beginTransaction();  
        boolean success = true;

        individualManager.save(individual1);
        individualManager.save(individual2);
        individualManager.save(individual3);
        individualManager.save(individual4);
        individualManager.save(individual5);

        // End transaction.  (if success false, transaction is reverted)
        individualManager.endTransaction(success); 

  DBTools Manager classes have many built-in methods that make working with tables even easier.  Here is a few examples:

  * Find records

        // Individual Record by primary key
        Individual individual = individualManager.findByRowId(1);
        
        // Find FIRST individual who has "555" in their phone number
        Individual individual = individualManager.findBySelection(IndividualConst.C_PHONE + " LIKE ?", new String[]{"555"}); 

        // All Records
        List<Individual> allIndividuals = individualManager.findAll();
        
        // All Records, ordered by the "NAME" column
        List<Individual> allOrderedIndividuals = individualManager.findAllOrderBy(IndividualConst.C_NAME);
        
        // ALL Records who have "555" in their phone number
        List<Individual> specificIndividuals = individualManager.findAllBySelection(IndividualConst.C_PHONE + " LIKE ?, new String[]{"555"}); 

  * Using cursors

        // Find all, order by NAME column
        Cursor cursor = individualManager.findCursorBySelection(null, null, IndividualConst.C_NAME); 
        
        // Find cursor of those who have "555" in their phone number
        Cursor cursor = individualManager.findCursorBySelection(IndividualConst.C_PHONE + " LIKE ?, new String[]{"555"}); 
        
  * Access data from Cursor

        Cursor cursor;
        // populate all items from cursor into Individual
        Individual individual = new Individual(cursor); 
        
        // Get data from a single field, from a cursor.  (for use in places such as Adapters, etc) Examples:
        String name = Individual.getName(cursor);
        IndividualType type = Individual.getType(cursor);
        Date birthDate = Individual.getBirthDate(cursor);

  * Count number of items in the database

        // Find count of ALL records in a table
        int count = individualManager.findCount();
        
        // Find count of ALL records who have "555" in their phone number
        int count = individualManager.findCountBySelection(IndividualConst.C_PHONE + " LIKE ?, new String[]{"555"}); 

  Support for ASync writes (guarantees single write per database)

  * Sample async save

        Individual individual = individualManager.findByRowId(1);
        individual.setPhone("801-555-0000");
        individualManager.saveAsync(individual);


  Table Change Listeners

  * Add Listener
  
        // Listener
        individualManager.addTableChangeListener(new DBToolsTableChangeListener() {
            @Override
            public void onTableChange(DatabaseTableChange event) {
                onTableChange(event);
            }
        });

RxJava
======

  * Setup
  
  Tell DBTools to support RxJava:
  
       dbtools {
           rxJavaSupport true // support RxJava
       }
       
  Include the RxJava dependecy:
  
      // RxJava
      compile 'io.reactivex:rxandroid:<latest version>'
      compile 'io.reactivex:rxjava:<latest version>'
      
  * Usage
  
  Managers
  
  Use xxxRx(...) method calls on the manager classes to return an Observable
  
        individualManager.findAllRx()
              .subscribe(individual -> Log.i(TAG, "Individual: " + individual.getFirstName()));

  Subscribe to table changes (following example will output 2 table changes)
  
        public void changeNames() {
            // Subscribe to TABLE changes
            Subscription tableChangeSubscription = individualManager.tableChanges()
                    .subscribe(changeType -> handleTableChange(changeType));
            
            // Make some changes
            Individual individual = individualManager.findAll().get(0);
            if (individual != null) {
                // change name
                individual.setFirstName("Bobby");
                individualManager.save(individual);
            
                // change name (again)
                individual.setFirstName("John");
                individualManager.save(individual);
            }
        
            // Unsubscribe
            tableChangeSubscription.unsubscribe();
        }
        
        public void handleTableChange(DatabaseTableChange change) {
            Log.e(TAG, "Individual Table Changed: [" + change.hasChange() + "]");
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
        }

        dbtools {
            type 'ANDROID' // or 'ANDROID-KOTLIN'

            basePackageName 'org.company.project.domain'
            outputSrcDir 'src/main/java/org/company/project/domain'

            // optional items
            injectionSupport true // support for @Inject (using JEE, Dagger, Guice, etc)
            jsr305Support true // support for @Notnull / @Nullable etc
            includeDatabaseNameInPackage true // place each set of domain objects into a package named after its database
            dateType 'JSR-310' // DATE, JSR-310, JODA
            rxJavaSupport false // support RxJava
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

       DatabaseManager.java (extends DatabaseBaseManager and is used for developer customizations.  Contains CONST versions of the databases) (NEVER overwritten by generator)
       DatabaseBaseManager.java (contains boiler-plate code creating all tables and views for all databases defined in the schema.xml) (this file is ALWAYS overwritten by generator)
       DatabaseManagerConst.java (contains constant values of the database names) (this file is ALWAYS overwritten by generator)
       AppDatabaseConfig.java (DatabaseManager configuration) (NEVER overwritten by generator)

  DBTools Generator will create the following files for each table (example for the Individual table):

        individual/
               Individual.java (extends IndividualBaseRecord and is used for developer customizations) (NEVER overwritten by generator)
               IndividualConst.java (static fields/methods for table and columns) (this file is ALWAYS overwritten by generator)
               IndividualBaseRecord.java (contains boiler-plate code for doing CRUD operations and contains CONST names of the table and all columns (used to help writing queries)) (this file is ALWAYS overwritten by generator)

               IndividualManager.java (extends IndividualBaseManager and is used for developer customizations (such as adding new findByXXX(...) methods) (NEVER overwritten by generator)
               IndividualBaseManager.java (contains boiler-plate code for doing CRUD operations) (this file is ALWAYS overwritten by generator)

Proguard Rules
==============

    # DBTools
    -dontwarn org.dbtools.query.**
    -dontwarn org.sqlite.**
    -dontwarn net.sqlcipher.**
    -dontwarn com.squareup.otto.**

    # SQLCipher (if using SQLCipher)
    -keep public class net.sqlcipher.** { *; }
    -keep public class net.sqlcipher.database.** { *; }

    # SQLite.org (if using sqlite from sqlite.org)
    -keep public class org.sqlite.** { *; }
    -keep public class org.sqlite.database.** { *; }

    # Threetenbp
    -dontwarn org.threeten.**

Upgrade
=======
Migration guide (https://github.com/jeffdcamp/dbtools-android/blob/master/MIGRATION.md)

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
