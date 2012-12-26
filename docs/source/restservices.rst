
REST Services
*************

This pages details the REST services provided by each node divided into different categories.

.. contents::


Prefix management
=================
     
.. http:get:: /prefixes/
  
  Retrieves the prefixes used by this node.

  **Accepted content-types**: html, json
  
  :status 200: :http:statuscode:`200`
  
  **Example response**:

  .. sourcecode:: http

    HTTP/1.1 200 OK
    Vary: Accept
    Content-Type: text/javascript

    {
      "http://www.w3.org/1999/02/22-rdf-syntax-ns#":"rdf",
      "http://www.w3.org/2002/07/owl#":"owl",
      "http://www.w3.org/2001/XMLSchema#":"xsd",
      "http://www.w3.org/2000/01/rdf-schema#":"rdfs"
    }

|

.. http:get:: /prefixes/(uri:prefix)
  
  Retrieves the prefix for an URI provided it is registered.

  **Accepted content-types**: json
  
  :param prefix: URI whose prefix one is looking for (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type prefix: URI 
  :status 200: :http:statuscode:`200`
  :status 404: When the given uri has not a prefix associated ( :http:statuscode:`404` )
  
  **Example response**:

  .. sourcecode:: http

    HTTP/1.1 200 OK
    Vary: Accept
    Content-Type: text/javascript

    "prefix1"


Space management
================

.. http:get:: /spaces/
  
  Retrieves the spaces a node is connected to.
  
  **Accepted content-types**: html, json
  
  :status 200: :http:statuscode:`200`

|

.. http:get:: /spaces/(uri:space)
  
  Retrieves a list of the REST services (TSC primitives) which can be consumed on that space. The purpose of showing a representation of this resource is to enable browsing.
  
  **Accepted content-types**: html, json
  
  :param space: the URI which identifies the space (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type space: URI 
  :status 200: :http:statuscode:`200`


Authorization
=============

.. http:get:: /login
  
  Checks whether the user is logged or not.
  
  **Accepted content-types**: html, json
  
  :status 402: Unauthorized client cannot read this graph. ( :http:statuscode:`402` )


Triple Space primitives
=======================

Graph level operations
----------------------

In this subsection we describe the primitives related to the RDF Graphs on a space.

/spaces/{space}/graphs
----------------------

 * *Arguments*
 
   * space: the URI of the space in which the graph is stored
   
 * *HTTP verb*: GET

   * *Description*: retrieves a list of the graphs written into that space on that node.
   * *Status codes*:

     * 406: The requested content-type cannot be retrieved

   * *Content-type*: html

 * *HTTP verb*: POST
 
   * Pending to determine whether it makes sense offering this service or not
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


Space level operations
----------------------

In this subsection we describe the query primitive, which cares about the RDF triples written into a space.
In other words, it does not care to which graph each returned RDF triple belongs to.

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