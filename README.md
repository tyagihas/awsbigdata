AWS Big Data Boot Camp
==========
## Background
AWS Big Data Boot Camp aims for learning various data oriented AWS services. 
The system built throughout the boot camp is "target advertisement" which requires analyzing historical data
as well as real time streaming data.

## 
awsbigdata repository contains all materials and source code including :
- Hive queries for Amazon EMR
- DDL and SQL Statements for Amazon Redshift
- Data generator for Amazon Kinesis
- Event handler for Amazon Kinesis

## Access log format
Access logs are already [generated](https://github.com/uprush/apache_log_gen) and stored on Amazon S3. 


## Messaging format
- Data generator will generate current coordinates of users and continue generating as time goes by.
- The target area is mostly within Tokyo 23 wards. - South West coords : 35.53,139.56, North East coords : 35.81,139.91
- The message format of corrdiates is as follows.
```javascript
<userid>,<latitude>,<longitude>

eg. 
10290,35.54,139.78
20135,35.56,139.65
10290...
```


