Migration from 1.x to 2.x
=========================

General Code Changes
--------------------
 * Rename findByRowID(...) -> findByRowId(...)
 * Remove BaseManager.java and BaseRecord.java (no longer needed)

DatabaseManager
---------------
 * Change extend to DatabaseBaseManager
 * Remove onCreate(...).  Default database creation can now be done by DatabaseBaseManager.onCreate().  You can still override this (if needed).

RecordManager
-------------
 * Rename executeSQL(...) -> executeSql(...)

schema.xml
----------
 * If you are currently using <table/> to map to a view... change <table/> to <view/> and remove "primaryKey" attribute from any <view><field/></view>

build.gradle
------------
 * Change org.dbtools.gen.android.AndroidDBObjectBuilder -> org.dbtools.gen.android.AndroidObjectsBuilder
