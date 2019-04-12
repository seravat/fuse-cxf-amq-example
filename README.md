# fuse-cxf-amq-example

## Running the application

```text
mvn clean package spring-boot:run
```

## Input and Output

SOAP request in:

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:per="http://www.person.bbc.com">
   <soapenv:Header/>
   <soapenv:Body>
      <per:Person>
         <!--Optional:-->
         <per:firstName>joao</per:firstName>
         <!--Optional:-->
         <per:lastName>tavares</per:lastName>
      </per:Person>
   </soapenv:Body>
</soapenv:Envelope>
```

AMQ JSON out (will be in the queue bbcOutputQueue)

NOTE:
Found out that the queue in AMQ7.2, when using the JMS artemis component in Camel needs to be: jms.queue.bbcOutputQueue

```json
{"firstName":"joao","lastName":"tavares"}
```

SOAP response received by web service client:

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="http://webservice.index.mdm.sun.com/">
   <soapenv:Header/>
   <soapenv:Body>
      <ESBResponse>
         <status>OK</status>
      </ESBResponse>
   </soapenv:Body>
</soapenv:Envelope>
```

Load testing the application
Siege can be used to load test and send many SOAP payloads to the application. In order to do so, run the following:

mvn clean spring-boot:run
siege --rc=.siegerc
Change the configuration in the .siegerc file.

Further Documentation
https://access.redhat.com/documentation/en-us/red_hat_fuse/7.2/
https://access.redhat.com/documentation/en-us/red_hat_fuse/7.2/html/deploying_into_spring_boot/index
