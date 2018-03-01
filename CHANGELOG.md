Change Log
==========

Version 10.6.0 *(2018-03)*
--------------------------
* Support Libraries 27.1.0
* Architecture Components Paging 1.0.0-alpha6
* Architecture Components LifeCycle 1.1.0
* Kotlin 1.2.30
* kotlinx-coroutines-android:0.22.3

Version 10.5.0 *(2018-01)*
--------------------------
* Fixed DBToolsLiveData to prevent repetitive calls when onActive() is called
* Kotlin 1.2.10
* kotlinx-coroutines-android:0.21

Version 10.4.0 *(2017-12)*
-------------------------
* Architecture Components Paging 1.0.0-alpha4
* Added DBToolsPositionalDataSource (Deprecated DBToolsTiledDataSource)

Version 10.3.1 *(2017-12)*
--------------------------
* Kotlin 1.2.0
* kotlinx-coroutines-android:0.20
* kotlin-stdlib-jdk7
* supportLibVersion 27.0.2

Version 10.3.0 *(2017-11)*
--------------------------
* Architecture Components LifeCycle runtime 1.0.3
* Architecture Components LifeCycle extensions 1.0.0
* Architecture Components Paging 1.0.0-alpha3

Version 10.1.4 *(2017-10)*
--------------------------
* Added DBToolsTiledDataSource
* Architecture Components LifeCycle runtime 1.0.3
* Architecture Components LifeCycle extensions 1.0.0-rc2
* Architecture Components Paging 1.0.0-alpha3

Version 10.1.4 *(2017-09)*
--------------------------
* Architecture Components LifeCycle runtime 1.0.0
* Architecture Components LifeCycle extensions 1.0.0-beta1

Version 10.1.3 *(2017-09)*
--------------------------
* Android Support Libraries 26.1.0
* Architecture Components LifeCycle runtime 1.0.0
* Architecture Components LifeCycle extensions 1.0.0-alpha9-1

Version 10.1.2 *(2017-09)*
--------------------------
* Added LiveData support using DBToolsLiveData.toLiveData(...) (requires Kotlin and coroutines)

Version 10.0.2 *(2017-09)*
--------------------------
* Added AndroidBaseManager.executeSql(.., .., splitStatements) to allow ignoring splitting sql statements

Version 10.0.1 *(2017-07)*
---------------------------
* Minor fixes to dbtools-gen

Version 10.0.0 *(2017-07)*
---------------------------
* Target Android O / Android SDK 26
* Fixed issue with JdbcSqliteDatabaseWrapper not throwing an exception on sql errors

Version 9.0.0 *(2017-04)*
---------------------------
* Changed from RxJava 1 to RxJava 2
* Added findAllxxxRxStream(...) functions that keep the cursor open and emits results to an Observable<T>
* Added support text enums (use VARCHAR as field jdbcDataType)

**Migration**

* xxxRx() functions now all Return Single<T> and Single<List<T>> OR Maybe<T> and Maybe<List<T>>
* findValueBySelection(..., rowId, ...) has been replaced with findValueByRowId(..., rowId, ...)


Version 8.2.1 *(2017-03)*
---------------------------
* Added support for multi field indexes (Added <index/> section to schema.xsd to support multiple column indexes)


Version 8.2.0 *(2017-03)*
---------------------------
* Improved table notifications and subscriptions across managers and multiple databases
* Improved manager lastModifiedTs across multiple databases
* Fixed issues with dbtools-init with sub projects (ex: ./gradlew app:dbtools-init)
* Changed the variable name for versions to a proper const naming (in DatabaseManager)


Version 8.1.1 *(2017-03)*
---------------------------
* Fixed generated statement binding for REAL and DECIMAL
* Change Kotlin AndroidBaseRecord.tableName to AndroidBaseRecord.getTableName()
* Fixed issues with dbtools-gen with sub projects (ex: ./gradlew app:dbtools-gen)


Version 8.1.0 *(2017-02)*
---------------------------
* Added databaseManager.swapDatabase(...)


Version 8.0.0 *(2017-01)*
---------------------------
* Kotlin generated classes use functions with Default Arguments
* Removed default constructors on all Record classes 
* Enum columns will no longer cause a ArrayIndexOutOfBoundsException (if the database contains an invalid ordinal), instead it will fall-back to default value
* Removed xxxAsync methods (apps should handle their own threads and background calls) 
* All NULLABLE String/VARCHAR columns now default to null (previously they would default to "")
* Fixed issues with nullable Double and Byte[]

**Migration**

* Remove unused constructors from all Record classes
* Move CREATE_VIEW and DROP_VIEW from Record class to Manager class
* Move QUERY from Record class to Manager class
* Kotlin: Change function calls to use Default Arguments


Version 7.2.2 *(2017-01)*
---------------------------
* Improvements with dbtools-gen and Kotlin


Version 7.2.1 *(2016-12)*
---------------------------
* Min SDK 15
* Added bindArgs to AndroidBaseManager.executeSql(...)


Version 7.2.0 *(2016-11)*
---------------------------
* Added support for no primary key on a table
 
 
Version 7.1.1 *(2016-11)*
---------------------------
* Added locking to listeners (to prevent ConcurrentModificationException) 
* Adjusted AndroidBaseManagerWritable.lastTableModifiedTs = -1L


Version 7.1.0 *(2016-10)*
---------------------------
* Added rowId and table name to DatabaseTableChange object (for helping to identify if a specific row was modified)
* Added lastTableModifiedTs to each manager (for helping to identify when table content changes)
* Improved consistency of all find*Rx(...) calls
* Removed all back pressure issues from Rx Managers
    * findRx(...) now only emit 1 value
    * findOrderByRx(...) now only emit 1 value
    * findBySelectionRx(...) now only emit 1 value
    * findValueBySelectionRx(...) now only emit 1 value
* Added findAll*Rx(...) to Rx Managers (emits a list of items, matching non-Rx Managers)
* Improved support for "groupBy", "having", and "limit"
* dbtools-gen now marks all Kotlin vars "open"
* Fixed Kotlin generated logging in DatabaseManager
* Removed CustomQueryRecord

### Migration for Rx users
* Search for the following function calls:
 
        findRx(...)
        findOrderByRx(...)
        findBySelectionRx(...)
        findValueBySelectionRx(...)
        findByRawQueryRx(...)
   
* if you expect multiple values, then change to:

        findAll*Rx(...)


Version 7.0.11 *(2016-09)*
---------------------------
* Added AndroidBaseManagerWritable.inTransaction(...)
* Updated provided versions 


Version 7.0.10 *(2016-07)*
---------------------------
* Fixed issues with closing database
* Made Kotlin DatabaseBaseManager methods open


Version 7.0.9 *(2016-07)*
---------------------------
* Added copy constructor to XXXBaseRecord


Version 7.0.8 *(2016-06)*
---------------------------
* Fixed dbtools-gen issues with Kotlin and Date type


Version 7.0.7 *(2016-06)*
---------------------------
* Added new findValueBySelection() that takes a rowId
* Added closeAllAndReset() and closeAll() to DatabaseManager
* Fixes for JdbcMemoryCursor


Version 7.0.5 *(2016-05)*
---------------------------
* Improved handling of Insert and Update statements when working with multiple databases and swapping databases
* Added multiple database support to TestDatabaseConfig


Version 7.0.4 *(2016-05)*
---------------------------
* Fixes for RxJava
* Added sample-rxjava


Version 7.0.3 *(2016-05)*
---------------------------
* Set aar min SDK to 10
* Fixed issues with TableChangeListener
* Added full SQL logging to JdbcSqliteDatabaseWrapper
* Fixed issues with JdbcSqliteDatabaseWrapper


Version 7.0.2 *(2016-05)*
---------------------------
* Changed the project from Maven to Gradle
* Changed Artifact from jar to aar
* Full inserts and updates now use prepared statements
* JDBCSqlite support for Unit tests support
* Added DatabaseConfig to DatabaseManager to better support custom database configurations
* Added sample projects (for Java and Kotlin)
* Added copy() to Record classes


Version 6.0.0 *(2016-04)*
----------------------------
* Improved RxJava and queries with DatabaseManager to prevent back pressure
* Replaced DBToolsEventBus with DatabaseTableChange listeners
* Improved RxJava Published changed listener to only notify when of changes after endTransaction or when NOT in a transaction


Version 5.0.3 *(2016-02)*
----------------------------
* Fix for dbtools-gen Kotlin generated files (better handling of nullable fields)


Version 5.0.2 *(2016-02)*
----------------------------
* Kotlin base managers now extend KotlinBaseManager (allowing for managers that use Kotlin rich calls)
* Fixed dependency issues with JODA
* Fixed issue with extra unused imports on Cont classes


Version 5.0.1 *(2016-02)*
----------------------------
* Added Support for Kotlin
* Added Support for JSR-310 (use dateType 'JSR-310' in the dbtools-gen plugin)
* dbtools-gen will now create RecordConst file that contains all the static fields and methods from the BaseRecord


Version 4.1.2 *(2015-10)*
----------------------------
* Improved RxJava Support in BaseManager (Instead of returning Observable<List<T>>... return Observable<T>)
* Added RxJava Observables to BaseManager for rowChanges and tableChanges
 
 
Version 4.0.2 *(2015-10)*
----------------------------
* Added rxJavaSupport to plugin (generator will use a RxJava BaseManager) (Currently Android ONLY)
* Removed generated setters for readonly tables / queries / views
* Made RxJava dependency optional
 
   
Version 4.0.0 *(2015-10)*
----------------------------
* RxJava support (added Observable AndroidBaseManager.xxxRx() methods)
* Added support for merging similar databases


Version 3.5.0 *(2015-09)*
----------------------------
* Improved support for static calls to DatabaseManager and other areas
* Added DatabaseManager.mergeDatabase(...)
* Added DatabaseManager.findTableNames(...)
 
 
Version 3.5.0 *(2015-08)*
----------------------------
* Added support for readOnly databases
* Added support for readOnly tables
* DatabaseManager.deleteDatabase(...) will delete sqlite extra files
* Added GreenRobot and Otto EventBus default implementations for DBToolsEventBus interface


Version 3.4.1 *(2015-08)*
----------------------------
* Fixed issue with view version not being saved correctly
* Removed toString() from BaseRecord (dbtools-gen) 

 
Version 3.4.0 *(2015-08)*
----------------------------
* Change EventBus support to be generic (implement DBToolsEventBus interface)
 
 
Version 3.3.0 *(2015-07)*
----------------------------
* Added orderBy to findValueBySelection (Issue #6)
* Fixed issue with BaseManager.save(...) return true when newId returned is less than 0
* dbtools-gen: Added ability to annotate generated domain setters/getters with jsr305 annotations (@Nullable, @Nonnull)
 
 
Version 3.2.0 *(2015-04)*
----------------------------
* Support for sqlQueryBuilderSupport flag on dbtools-gen (used when generating template view and query objects)
* Allow entitites passed to AndroidBaseManager to be null
 
 
Version 3.1.0 *(2015-02)*
----------------------------
* Be sure to use dbtools-gen-3.1.1.jar
* Improved loading of sqliteX library
* Improvements to findCursorBySelection(...)
* Bug fixes and performance improvements


Version 3.0.0 *(2015-01)*
----------------------------
* Changed DBTools to use DatabaseWrapper interface to identify the type of SQLite Database to use (Build-in Android, SQLCipher, SQLite.org build of SQLite, etc).  Allows the use of any custom build of SQLite.
* Added several default implementations of DatabaseWrapper interface (AndroidDatabaseWrapper, SQLCipherDatabaseWrapper, SQLiteDatabaseWrapper)
* Removed unneeded
* Minor bug fixes from 2.6.0


Version 2.6.0 *(2015-01)*
----------------------------
* Consolidated all of the different findXXXBySelection(...) to findValueBySelection(...) to allow better support for more datatypes
* Migration to 2.6.0

   findXXXBySelection(...) to findValueBySelection(class, ...)
   findXXXByRawQuery(...) to findValueByRawQuery(class, ...)
   findAllIntByRawQuery(...) to findAllValuesByRawQuery(class, ...)
   NEW: findAllValuesBySelection(class, ...)


Version 2.5.4 *(2015-01)*
----------------------------
* Added findLongBySelection(...), findIntBySelection(...), findStringBySelection(...), findBooleanBySelection(...), findDateBySelection(...), findDateTimeBySelection(...) 
* Improved findXXXByRawQuery(...) support for Int, Boolean, Date, DateTime
* Fix for issue #5 (ConnectAllDatabases should call IdentifyDatabases)


Version 2.5.2 *(2014-11)*
----------------------------
* Removed deprecated methods
* Minor improvements to AndroidBaseManager insert(...) update(...) delete(...) methods
* Bug fixes


Version 2.5.0 *(2014-10)*
----------------------------
* NEW Support for Async writes (insertAsync, updateAsync, deleteAsync, saveAsync)


Version 2.3.0 *(2014-10)*
----------------------------
* NEW Query database type (ability to query across attached databases)
* NEW Merge cursor support
* NEW Matrix cursor support
* NEW JSR 305 support (@Nullable / @Notnull)
* NEW Otto Event But support
* Improved support for pulling data from a Cursor (Individual.getName(cursor))
* Bug fixes


Version 2.1.0 *(2014-05-15)*
----------------------------
* NEW JSR 305 support (@Nullable / @Nonnull)
* GENERATOR: Fixed issues with databases that have a '.' in the name
* GENERATOR: Changed getColumnIndex(...) to getColumnIndexOrThrow(...) for better error messages
 
 
Version 2.0.0 *(2014-04)*
----------------------------
* NEW DatabaseBaseManager.java is generated from dbtools-gen Gradle/Android plugin
* NEW DatabaseBaseManager.java creates
* NEW View Support <view/> element added to schema.xml file
* dbschema.xsd is auto-updated from dbtools-gen Gradle/Android plugin
* Bug fixes

