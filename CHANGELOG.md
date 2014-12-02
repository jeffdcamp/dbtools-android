Change Log
==========

  Version 2.5.1 *(2014-11)*
----------------------------

 * Removed deprecated methods
 * Minor improvements to AndroidBaseManager insert(...) update(...) delete(...) methods

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

