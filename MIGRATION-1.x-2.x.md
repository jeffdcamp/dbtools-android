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
