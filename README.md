# elastic-search-poc
A POC project for tech enthusiasts to learn how to make a use of Elasticsearch in the real time applications.

- This Spring boot project is all about indexing the csv file data into the ElasticSearch Server.

- I took a sample CSV file, which is present in this repository with name 'Sacramentorealestatetransactions.csv'.

- To index the data into the elastic search one need to create mappings json for the file. to map the key with the data type. Under 'src/main/resources/config/' this path you will   find the same file. by making valid changes into this mappings json, one can index any csv file into ElasticSearch Server.

- The application built is fully dockerized. All things required to run this project are resides in the container. ElasticServer will be running along with the application           once the container is up and running.

- In the root directory of the project , there are two files placed. Dockerfile and docker-compose.yml file. Docker Compose is a tool for defining and running multi-container       Docker applications. In this application, two containers are there. one for application and another for ElasticSearch Server.

- After cloning this application on local machine, one can directly apply docker-compose commands.
        - #docker-compose build: this command will build the image using Dockerfile and docker-compose file. 
        - #docker-compose up: this command will create and run the container using previously built image.
       
- After successfull execution, this elastic-search-poc is up and running on you local machine.

- two API are written under this project.

1. Create Index: This api will create the index with name and file, user has provided within the requst body.

                 - URL :   http://localhost:8081/transaction/v1/index
                 - method: POST
                 - content-type : multipart/formdata
                 - request body :  (POSTMAN -> body -> form-data)
                                    indexName : index_name (text)
                                    csvFile   : Select_csv_file (file)
                 - resposnse :  
                                  {
                                    "statusCode": 200,
                                    "message": "Index Created",
                                    "data": {
                                              "recordsProcessed": 985,
                                              "executionTime": {
                                                                  "micros": 513000,
                                                                  "microsFrac": 513000.0,
                                                                  "millisFrac": 513.0,
                                                                  "secondsFrac": 0.513,
                                                                  "minutesFrac": 0.00855,
                                                                  "hoursFrac": 1.425E-4,
                                                                  "daysFrac": 5.9375E-6,
                                                                  "nanos": 513000000,
                                                                  "days": 0,
                                                                  "hours": 0,
                                                                  "minutes": 0,
                                                                  "millis": 513,
                                                                  "seconds": 0,
                                                                  "stringRep": "513ms"
                                                                },
                                              "hasFailures": false
                                            }
                                  }
                 - CURL request :               
                                curl --location --request POST 'http://localhost:8081/transaction/v1/index' \
                                --header 'Content-Type: multipart/form-data' \
                                --form 'indexName={Index_name}' \
                                --form 'csvFile=@{File_location}'
                        
2. Search Document: This api will Search document through the index with value you specified with respective Key.

                 - URL : http://localhost:8081/transaction/v1/indexdata
                 - method : POST
                 - Content-type : application/json
                 - request body : (POSTMAN -> body -> raw)
                                        
                                 {
                                   "indexName":"{$name of the index to search through}", 
                                   "key":"{$key will be any key from document to search for}", 
                                   "value":"{$value for the key}", 
                                   "limit":"{$ limit for the search rows, default is 10}"
                                 }          
                  - response : 
                                {
                                  "statusCode": 200,
                                  "message": "Success",
                                  "data": {
                                                "staus": "OK",
                                                "executionTime": {
                                                "micros": 78000,
                                                "microsFrac": 78000.0,
                                                "millisFrac": 78.0,
                                                "secondsFrac": 0.078,
                                                "minutesFrac": 0.0013,
                                                "hoursFrac": 2.1666666666666667E-5,
                                                "daysFrac": 9.027777777777778E-7,
                                                "nanos": 78000000,
                                                "days": 0,  
                                                "hours": 0,
                                                "minutes": 0,
                                                "millis": 78,
                                                "seconds": 0,
                                                "stringRep": "78ms"
                                        },
                                "successfulShards": 3,
                                "failedShards": 0,
                                "numberOfSearchedHints": 1,
                                "searchResult": [
                                                        {
                                                                "street": "5980 79TH ST",
                                                                "city": "SACRAMENTO",
                                                                "zip": 95824,
                                                                "state": "CA",
                                                                "beds": 2,
                                                                "baths": 1,
                                                                "sq__ft": 868,
                                                                "type": "Residential",
                                                                "sale_date": "Tue May 20 00:00:00 EDT 2008",
                                                                "price": 90000,
                                                                "latitude": 38.518373,
                                                                "longitude": -121.411779
                                                          }   
                                                ]
                              }  
                  - Cur REquest      
                                curl --location --request POST 'http://localhost:8081/transaction/v1/indexdata' \
                                --header 'Content-Type: application/json' \
                                --data-raw '{
                                        "indexName":"index1",
                                        "key":"zip",
                                        "value":"95824",
                                        "limit":1
                                        }'       
                                
                        
  


                                    
                   
                                                              
                              
                                    
                                    
                  
               
                  






