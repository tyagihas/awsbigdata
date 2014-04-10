AWS Big Data Boot Camp
==========
## Background
AWS Big Data Boot Camp aims for learning various data oriented AWS services. 
The system built throughout the boot camp is "target advertisement" which requires analyzing historical data
as well as processing real time streaming data.


## 
awsbigdata repository contains all materials and source code including :
- Hive queries for Amazon EMR
- DDL and SQL Statements for Amazon Redshift
- Data generator for Amazon Kinesis
- Event handler for Amazon Kinesis


## Where to start
Click this [link](https://console.aws.amazon.com/cloudformation/home?region=us-east-1#/stacks/new?templateURL=https://s3.amazonaws.com/awsbigdata-boot-camp/awsbigdata.template&stackName=AWSBigData) to initiate CloudFormation. 


## Access log format
Access logs are already [generated](https://github.com/uprush/apache_log_gen) and stored on Amazon S3. 


## Messaging format
- Data generator will generate current coordinates of users and continue generating as time goes by.
- The target area is mostly within Tokyo 23 wards. - South West coords : 35.53,139.56, North East coords : 35.81,139.91
- The message format of corrdiates is as follows.
```javascript
{"user":10027,"line":"山手","station":"大塚","latitude":35.73154,"longitude":139.72893}
{"user":10078,"line":"山手","station":"目黒","latitude":35.63136,"longitude":139.71636}
```

## Schema definition
- Records are stored in Amazon Redshift.

```javascript
CREATE table logs (
user1 VARCHAR(10),
path VARCHAR(2048),
time VARCHAR(24),
geoip VARCHAR(2)
);
```



