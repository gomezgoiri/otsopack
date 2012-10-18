
REST Services
*************

This pages details the REST services provided by each node divided into different categories.

.. contents::


Prefix management
=================

/prefixes/
----------
 * *HTTP verb*: GET
 * *Description*: retrieves the prefixes used by this node.
 * *Status code*: -
 * *Content-type*: html, json

   * Example:

.. code-block:: javascript

  {
    "http://www.w3.org/1999/02/22-rdf-syntax-ns#":"rdf",
    "http://www.w3.org/2002/07/owl#":"owl",
    "http://www.w3.org/2001/XMLSchema#":"xsd",
    "http://www.w3.org/2000/01/rdf-schema#":"rdfs"
  }


/prefixes/{prefixuri}
---------------------

 * *HTTP verb*: GET
 * *Description*: retrieve the prefix for an URI if it is registered.
 * *Status codes*:

   * 404: When the given uri has not a prefix associated

 * *Content-type*: json

   * Example:

.. code-block:: javascript

  "prefix1"



Space management
================

/spaces/
--------

 * *HTTP verb*: GET
 * *Description*: retrieves the spaces a node is connected to.
 * *Status code*: -
 * *Content-type*: html, json

/spaces/{space}
---------------

 * *HTTP verb*: GET
 * *Description*: retrieve the spaces a node is connected to.
 * *Status code*: -
 * *Content-type*: html, json


Authorization
=============

/login
------

 * *HTTP verb*: GET

   * *Description*: checks whether the user is logged or not
   * *Status codes*:

     * 402: Unauthorized client cannot read this graph.


Triple Space primitives
=======================

/spaces/{space}/graphs
----------------------

 * Pending to determine whether it makes sense offering this service or not
 * *Arguments*
   * space: the URI of the space in which the graph is stored
 * *HTTP verb*: POST
   * *Description*: write({space},{graph}): graphURI
   * *Status codes*:
     * 404: When the node is not joined to the {space} provided
     * 406: The requested content-type cannot be retrieved
     * 500: The information could not be stored
   * *Content-type*: semantic formats

/spaces/{space}/graphs/{graph}
------------------------------

 * *Arguments*

  * space: the URI of the space in which the graph is stored
  * graph: the URI of the graph to be read

 * *HTTP verb*: GET

   * *Description*: read({space},{graph})
   * *Status codes*:

     * 402: Unauthorized client cannot read this graph.
     * 403: The client has not permissions to read this graph
     * 404: When the node is not joined to the {space} provided (starts with SpaceNotExistException.HTTPMSG) or the graph with {graph} URI does not exist
     * 406: The requested content-type cannot be retrieved

   * *Content-type*: semantic formats, html

 * *HTTP verb*: DELETE

   * *Description*: take({space},{graph})
   * *Status codes*:

     * 402: Unauthorized client cannot read this graph.
     * 403: The client has not permissions to read this graph
     * 404: When the node is not joined to the {space} provided (starts with SpaceNotExistException.HTTPMSG) or the graph with {graph} URI does not exist
     * 406: The requested content-type cannot be retrieved

   * *Content-type*: semantic formats

/spaces/{space}/graphs/wildcards/{subject}/{predicate}/{object-uri}
-------------------------------------------------------------------

 * *Arguments*

   * space: the URI of the space in which the graph is stored
   * subject: the URI of the subject or "{{{*}}}"
   * predicate: the URI of the predicate or "{{{*}}}"
   * object-uri: the URI of the object or "{{{*}}}"

 * *HTTP verb*: GET

   * *Description*: read({space},{template}), where {template} is made up of {subject}, {predicate} and {object-uri}
   * *Status codes*:

     * 400: The template cannot be created with the provided arguments
     * 404: When the node is not joined to the {space} provided (starts with SpaceNotExistException.HTTPMSG) or there is no graph in the node which has a triple which matches with the given template
     * 406: The requested content-type cannot be retrieved
     * 500: A non-existing prefix was used in the template

 * *HTTP verb*: DELETE

   * *Description*: take({space},{template}), where {template} is made up of {subject}, {predicate} and {object-uri}
   * *Status codes*:

     * 400: The template cannot be created with the provided arguments
     * 404: When the node is not joined to the {space} provided (starts with SpaceNotExistException.HTTPMSG) or there is no graph in the node which has a triple which matches with the given template
     * 406: The requested content-type cannot be retrieved
     * 500: A non-existing prefix was used in the template or the information could not be removed from the store.


/spaces/{space}/graphs/wildcards/{subject}/{predicate}/{object-type}/{object-value}
-----------------------------------------------------------------------------------

 * *Arguments*

   * space: the URI of the space in which the graph is stored
   * subject: the URI of the subject or "{{{*}}}"
   * predicate: the URI of the predicate or "{{{*}}}"
   * object-type: the XSD type for the given literal
   * object-value: the string representation of the literal

 * *HTTP verb*: GET

   * *Description*: read({space},{template}), where {template} is made up of {subject}, {predicate}, {object-type} and {object-value}
   * *Status codes*:

     * 400: The template cannot be created with the provided arguments
     * 404: When the node is not joined to the {space} provided (starts with SpaceNotExistException.HTTPMSG) or there is no graph in the node which has a triple which matches with the given template
     * 406: The requested content-type cannot be retrieved
     * 500: A non-existing prefix was used in the template

 * *HTTP verb*: DELETE

   * *Description*: take({space},{template}), where {template} is made up of {subject}, {predicate}, {object-type} and {object-value}
   * *Status codes*:

     * 400: The template cannot be created with the provided arguments
     * 404: When the node is not joined to the {space} provided (starts with SpaceNotExistException.HTTPMSG) or there is no graph in the node which has a triple which matches with the given template
     * 406: The requested content-type cannot be retrieved
     * 500: A non-existing prefix was used in the template

/spaces/{space}/query/wildcards/{subject}/{predicate}/{object-uri}
------------------------------------------------------------------

 * *Arguments*

   * space: the URI of the space in which the graph is stored
   * subject: the URI of the subject or "{{{*}}}"
   * predicate: the URI of the predicate or "{{{*}}}"
   * object-uri: the URI of the object or "{{{*}}}"

 * *HTTP verb*: GET

   * *Description*: read({space},{template}), where {template} is made up of {subject}, {predicate} and {object-uri}
   * *Status codes*:

     * 400: The template cannot be created with the provided arguments
     * 404: When the node is not joined to the {space} provided (starts with SpaceNotExistException.HTTPMSG) or there is no triple which matches with the given template in the {space}
     * 406: The requested content-type cannot be retrieved
     * 500: A non-existing prefix was used in the template

/spaces/{space}/query/wildcards/{subject}/{predicate}/{object-type}/{object-value}
----------------------------------------------------------------------------------

 * *Arguments*

   * space: the URI of the space in which the graph is stored
   * subject: the URI of the subject or "{{{*}}}"
   * predicate: the URI of the predicate or "{{{*}}}"
   * object-type: the XSD type for the given literal
   * object-value: the string representation of the literal

 * *HTTP verb*: GET

   * *Description*: query({space},{template}), where {template} is made up of {subject}, {predicate}, {object-type} and {object-value}
   * *Status codes*:

     * 400: The template cannot be created with the provided arguments
     * 404: When the node is not joined to the {space} provided (starts with SpaceNotExistException.HTTPMSG) or there is no triple which matches with the given template in the {space}
     * 406: The requested content-type cannot be retrieved
     * 500: A non-existing prefix was used in the template