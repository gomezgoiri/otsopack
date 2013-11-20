Dependencies
************

Otsopack uses `maven <http://maven.apache.org/>`_ .
Therefore, using the *pom.xml* of the root directory, you can easily build and generate distribution packages.
For more information on how to do it, `check this page <build>`_.

To use Otsopack, you need to add the version you want to use and its dependencies to your *classpath*.


The build process will copy all the dependencies into a folder (once again, is detailed `here <build>`_).
Anyway, they are listed in the following sections.

Common dependencies
===================

======================================================   ========================     ============
**Name**                                                 **Last tested version**      **Optional**
======================================================   ========================     ============
`rdf2go.api <http://semanticweb.org/wiki/RDF2Go>`_       4.8.3                        No
`cglib-nodep <http://cglib.sourceforge.net/>`_           2.2                          No
`objenesis <http://code.google.com/p/objenesis/>`_       1.2                          No
`apache commons io <http://commons.apache.org/io/>`_     2.4                          No
`restlet <http://www.restlet.org>`_                      2.0.13                       No
`restlet.ext.jackson <http://www.restlet.org>`_          2.0.13                       No
`jackson-all <https://github.com/FasterXML/jackson>`_    1.7.3                        No
======================================================   ========================     ============


Android version
===============

==============================================================     ========================    ============
**Name**                                                           **Last tested version**     **Optional**
==============================================================     ========================    ============
`restlet (Android version) <http://www.restlet.org>`_              2.0.13                      No
`restlet.ext.net (Android version) <http://www.restlet.org>`_      2.0.13                      No
==============================================================     ========================    ============


Java SE version
===============

==============================================================     ========================    ============
**Name**                                                           **Last tested version**     **Optional**
==============================================================     ========================    ============
otsoCommons                                                        N/A                         No
`restlet (SE version) <http://www.restlet.org>`_                   2.0.13                      No
`restlet.ext.simple <http://www.restlet.org>`_                     2.0.13                      No
`rdf2go.impl.sesame <http://semanticweb.org/wiki/RDF2Go>`_         4.8.3                       Yes
`sesame-runtime <http://www.openrdf.org/>`_                   2.7.1                       Yes
`simple <http://www.simpleframework.org>`_                         4.1.21                      Recommended
`slf4j-api <http://www.slf4j.org>`_                                1.7.5                       No
`slf4j-nop <http://www.slf4j.org>`_                                1.7.5                       Recommended
`sqlitejdbc <http://www.zentus.com/sqlitejdbc/>`_                  056                         Recommended
==============================================================     ========================    ============