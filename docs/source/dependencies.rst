Dependencies
************

In this page we describe the dependencies for the different Otsopack modules.

.. When cloning the Otsopack repository, all the external dependencies will be downloaded from https://dev.morelab.deusto.es/svn/otsopack_externals/

Using the available ant tasks, **3 different jars** can be generated:
  * {module-name}-dist: the jar containing just the *Otsopack* compiled code. The downloadable versions are "-dist versions".
  * {module-name}-sources: the jar containing both the compiled code and the source code.
  * {module-name}-all: the jar containing all the dependencies.

Using *distribution versions* requires you to add dependencies to the classpath. These dependencies are listed bellow. 

Common dependencies
===================

====================================================   ========================     ============
**Name**                                               **Last tested version**      **Optional**
====================================================   ========================     ============
microjena                                              (own)                        No
`rdf2go.api <http://semanticweb.org/wiki/RDF2Go>`_     4.7.4                        No
`cglib-nodep <http://cglib.sourceforge.net/>`_         2.2                          No
`objenesis <http://code.google.com/p/objenesis/>`_     1.2                          No
`apache commons io <http://commons.apache.org/io/>`_   N/A                          No
`restlet <http://www.restlet.org>`_                    2.0.13                       No
`restlet.ext.jackson <http://www.restlet.org>`_        2.0.13                       No
`jackson-all <http://jackson.codehaus.org/>`_          1.7.3                        No
====================================================   ========================     ============


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
`rdf2go.impl.sesame23 <http://semanticweb.org/wiki/RDF2Go/>`_      4.7.4                       Yes
`sesame-runtime-osgi <http://www.openrdf.org/>`_                   2.3.1                       Yes
`simple <http://www.simpleframework.org>`_                         4.1.21                      Recommended
`slf4j-api <http://www.slf4j.org>`_                                1.6.1                       No
`slf4j-nop <http://www.slf4j.org>`_                                1.6.1                       Recommended
`sqlitejdbc <http://www.zentus.com/sqlitejdbc/>`_                  056                         Recommended
==============================================================     ========================    ============