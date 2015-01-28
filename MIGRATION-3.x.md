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