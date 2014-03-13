lucene-solr-entrez
==================

Entrez search engine features implemented for Lucene/Solr

How to build jar
----------------

    $ mvn package

Jar hopefully should appear in target directory. See

	example/core/solrconfig.xml

for [<lib/> directive](https://cwiki.apache.org/confluence/display/solr/Lib+Directives+in+SolrConfig)
to make Solr load custom classes.

How to run example core
-----------------------

Use start.jar from Solr distribution

    java -Dsolr.solr.home=/path/to/lucene-solr-entrez/example -jar start.jar

Then proceed to http://localhost:8983/solr/

Sample index document that uses EntrezDate field and MeSH tokenizer

    <add>
      <doc>
        <field name="id">1</field>
        <field name="meshTopic">Breast Neoplasms/diagnosis</field>
        <field name="meshTopic">Diagnostic Imaging/methods</field>
        <field name="pubDate">1992/11</field>
      </doc>
    </add>
