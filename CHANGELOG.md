Change Log
==========

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