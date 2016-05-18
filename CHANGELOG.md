Change Log
==========

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

