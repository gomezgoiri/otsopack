
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
  
  :status 401: Unauthorized client cannot read this graph. ( :http:statuscode:`401` )


Triple Space primitives
=======================

Graph level operations
----------------------

In this subsection we describe the primitives related to the RDF Graphs on a space.

.. http:get:: /spaces/(uri:space)/graphs
  
  Retrieves a list of the graphs written into that space on that node.
  
  **Accepted content-types**: html
  
  :param space: the URI of the space where the graph is written (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type space: URI 
  :status 200: :http:statuscode:`200`
  :status 406: The requested content-type cannot be retrieved ( :http:statuscode:`406` ).

|

.. http:post:: /spaces/(uri:space)/graphs

  | write({space},{graph}): graphURI
  | (pending to determine whether it makes sense offering this service or not)

  **Accepted content-types**: semantic formats
  
  :param space: the URI of the space where the graph is written (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type space: URI 
  :status 200: :http:statuscode:`200`
  :status 404: The node has not joined to the *space* provided ( :http:statuscode:`404` ).
  :status 406: The requested content-type cannot be retrieved ( :http:statuscode:`406` ).
  :status 500: The information cannot be stored ( :http:statuscode:`500` ).


|

.. http:get:: /spaces/(uri:space)/graphs/(uri:graph)

  read({space},{graph})

  **Accepted content-types**: semantic formats, html
  
  :param space: the URI of the space where the graph is written (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type space: URI 
  :param graph: the URI of the graph to be read (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type graph: URI 
  :status 200: :http:statuscode:`200`
  :status 402: Unauthorized client cannot read this graph ( :http:statuscode:`402` ).
  :status 403: The client has not permissions to read this graph ( :http:statuscode:`403` ).
  :status 404: When the node has not joined to the {space} provided (starts with SpaceNotExistException.HTTPMSG) or the graph with {graph} URI does not exist ( :http:statuscode:`404` ).
  :status 406: The requested content-type cannot be retrieved ( :http:statuscode:`406` ).

|

.. http:delete:: /spaces/(uri:space)/graphs/(uri:graph)

  take({space},{graph})

  **Accepted content-types**: semantic formats, html
  
  :param space: the URI of the space where the graph is written (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type space: URI 
  :param graph: the URI of the graph to be read (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type graph: URI 
  :status 200: :http:statuscode:`200`
  :status 402: Unauthorized client cannot read this graph ( :http:statuscode:`402` ).
  :status 403: The client has not permissions to read this graph ( :http:statuscode:`403` ).
  :status 404: When the node has not joined to the {space} provided (starts with SpaceNotExistException.HTTPMSG) or the graph with {graph} URI does not exist ( :http:statuscode:`404` ).
  :status 406: The requested content-type cannot be retrieved ( :http:statuscode:`406` ).

|

.. http:get:: /spaces/(uri:space)/graphs/wildcards/(uri:subject)/(uri:predicate)/(uri:object)

  read({space},{template}), where {template} is made up of {subject}, {predicate} and {object}

  **Accepted content-types**: semantic formats, html
  
  :param space: the URI of the space where the graph is written (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type space: URI 
  :param subject: the URI of the subject or "*" (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type subject: URI
  :param predicate: the URI of the predicate or "*" (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type predicate: URI 
  :param object: the URI of the object or "*" (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type object: URI 
  :status 200: :http:statuscode:`200`
  :status 400: The template cannot be created with the provided arguments ( :http:statuscode:`400` ).
  :status 404: When the node has not joined to the {space} provided (starts with SpaceNotExistException.HTTPMSG) or the graph with {graph} URI does not exist ( :http:statuscode:`404` ).
  :status 406: The requested content-type cannot be retrieved ( :http:statuscode:`406` ).
  :status 500: A non-existing prefix was used in the template ( :http:statuscode:`500` ).

|

.. http:delete:: /spaces/(uri:space)/graphs/wildcards/(uri:subject)/(uri:predicate)/(uri:object)

  take({space},{template}), where {template} is made up of {subject}, {predicate} and {object}

  **Accepted content-types**: semantic formats, html
  
  :param space: the URI of the space where the graph is written (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type space: URI 
  :param subject: the URI of the subject or "*" (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type subject: URI
  :param predicate: the URI of the predicate or "*" (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type predicate: URI 
  :param object: the URI of the object or "*" (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type object: URI 
  :status 200: :http:statuscode:`200`
  :status 400: The template cannot be created with the provided arguments ( :http:statuscode:`400` ).
  :status 404: When the node has not joined to the {space} provided (starts with SpaceNotExistException.HTTPMSG) or the graph with {graph} URI does not exist ( :http:statuscode:`404` ).
  :status 406: The requested content-type cannot be retrieved ( :http:statuscode:`406` ).
  :status 500: A non-existing prefix was used in the template or the information could not be removed from the store ( :http:statuscode:`500` ).

|

.. http:get:: /spaces/(uri:space)/graphs/wildcards/(uri:subject)/(uri:predicate)/(object-type)/(object-value)

  read({space},{template}), where {template} is made up of {subject}, {predicate}, {object-type} and {object-value}

  **Accepted content-types**: semantic formats, html
  
  :param space: the URI of the space where the graph is written (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type space: URI 
  :param subject: the URI of the subject or "*" (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type subject: URI
  :param predicate: the URI of the predicate or "*" (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type predicate: URI 
  :param object-type: the XSD type for the given literal
  :param object-value: the string representation of the literal
  :status 200: :http:statuscode:`200`
  :status 400: The template cannot be created with the provided arguments ( :http:statuscode:`400` ).
  :status 404: When the node has not joined to the {space} provided (starts with SpaceNotExistException.HTTPMSG) or the graph with {graph} URI does not exist ( :http:statuscode:`404` ).
  :status 406: The requested content-type cannot be retrieved ( :http:statuscode:`406` ).
  :status 500: A non-existing prefix was used in the template ( :http:statuscode:`500` ).

|

.. http:delete:: /spaces/(uri:space)/graphs/wildcards/(uri:subject)/(uri:predicate)/(object-type)/(object-value)

  take({space},{template}), where {template} is made up of {subject}, {predicate}, {object-type} and {object-value}

  **Accepted content-types**: semantic formats, html
  
  :param space: the URI of the space where the graph is written (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type space: URI 
  :param subject: the URI of the subject or "*" (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type subject: URI
  :param predicate: the URI of the predicate or "*" (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type predicate: URI 
  :param object-type: the XSD type for the given literal
  :param object-value: the string representation of the literal
  :status 200: :http:statuscode:`200`
  :status 400: The template cannot be created with the provided arguments ( :http:statuscode:`400` ).
  :status 404: When the node has not joined to the {space} provided (starts with SpaceNotExistException.HTTPMSG) or the graph with {graph} URI does not exist ( :http:statuscode:`404` ).
  :status 406: The requested content-type cannot be retrieved ( :http:statuscode:`406` ).
  :status 500: A non-existing prefix was used in the template ( :http:statuscode:`500` ).


Space level operations
----------------------

In this subsection we describe the query primitive, which cares about the RDF triples written into a space.
In other words, it does not care to which graph each returned RDF triple belongs to.


.. http:get:: /spaces/(uri:space)/query/wildcards/(uri:subject)/(uri:predicate)/(uri:object)

  query({space},{template}), where {template} is made up of {subject}, {predicate} and {object}

  **Accepted content-types**: semantic formats, html
  
  :param space: the URI of the space where the graph is written (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type space: URI 
  :param subject: the URI of the subject or "*" (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type subject: URI
  :param predicate: the URI of the predicate or "*" (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type predicate: URI 
  :param object: the URI of the object or "*" (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type object: URI 
  :status 200: :http:statuscode:`200`
  :status 400: The template cannot be created with the provided arguments ( :http:statuscode:`400` ).
  :status 404: When the node has not joined to the {space} provided (starts with SpaceNotExistException.HTTPMSG) or the graph with {graph} URI does not exist ( :http:statuscode:`404` ).
  :status 406: The requested content-type cannot be retrieved ( :http:statuscode:`406` ).
  :status 500: A non-existing prefix was used in the template ( :http:statuscode:`500` ).

|

.. http:get:: /spaces/(uri:space)/query/wildcards/(uri:subject)/(uri:predicate)/(object-type)/(object-value)

  query({space},{template}), where {template} is made up of {subject}, {predicate}, {object-type} and {object-value}

  **Accepted content-types**: semantic formats, html
  
  :param space: the URI of the space where the graph is written (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type space: URI 
  :param subject: the URI of the subject or "*" (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type subject: URI
  :param predicate: the URI of the predicate or "*" (`must be encoded <http://www.w3schools.com/tags/ref_urlencode.asp>`_ )
  :type predicate: URI 
  :param object-type: the XSD type for the given literal
  :param object-value: the string representation of the literal
  :status 200: :http:statuscode:`200`
  :status 400: The template cannot be created with the provided arguments ( :http:statuscode:`400` ).
  :status 404: When the node has not joined to the {space} provided (starts with SpaceNotExistException.HTTPMSG) or the graph with {graph} URI does not exist ( :http:statuscode:`404` ).
  :status 406: The requested content-type cannot be retrieved ( :http:statuscode:`406` ).
  :status 500: A non-existing prefix was used in the template ( :http:statuscode:`500` ).