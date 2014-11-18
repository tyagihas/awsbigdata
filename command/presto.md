Presto on EMR
=============

# Install Presto

    emr --create \
     --name "Presto" \
     --alive \
     --hive-interactive \
     --ami-version 3.1.0 \
     --num-instances 3 \
     --master-instance-type m3.xlarge \
     --slave-instance-type m3.xlarge \
     --bootstrap-action s3://presto-bucket/install_presto_0.71.rb \
     --args "-t","1GB","-l","DEBUG","-j","-server \
     -Xmx1G -XX:+UseConcMarkSweepGC \
     -XX:+ExplicitGCInvokesConcurrent \
     -XX:+AggressiveOpts \
     -XX:+HeapDumpOnOutOfMemoryError \
     -XX:OnOutOfMemoryError=kill \
     -9 %p \
     -Dhive.config.resources=/home/hadoop/conf/core-site.xml,/home/hadoop/conf/hdfs-site.xml","-v","0.72","-s","1GB","-a","1h","-p","http://central.maven.org/maven2/com/facebook/presto/" \
      --bootstrap-name "Install Presto"


# Run Prsto

## Run in Hive
    DROP TABLE IF EXISTS apachelog;
    CREATE EXTERNAL TABLE apachelog (
      host STRING,
      IDENTITY STRING,
      USER STRING,
      TIME STRING,
      request STRING,
      STATUS STRING,
      SIZE STRING,
      referrer STRING,
      agent STRING
    )
    PARTITIONED BY(iteration_no int)
    LOCATION 's3://publicprestodemodata/apachelogsample/hive';

    -- ALTER TABLE apachelog RECOVER PARTITIONS;
    MSCK REPAIR TABLE apachelog;

    select * from apachelog where iteration_no=101 limit 10;


    select count(* )from apachelog where iteration_no=101;

    Total MapReduce CPU Time Spent: 6 seconds 830 msec
    OK
    118035
    Time taken: 34.981 seconds, Fetched: 1 row(s)


    exit;



## Start Presto and run a test query:

    # Set Presto Pager to null for clean display
    export PRESTO_PAGER=

     # Launch Presto
    ./presto --catalog hive

    # Show tables to prove that Presto is seeing Hive's tables
    show tables;

    # Run test query in Presto
    select * from apachelog where iteration_no=101 limit 10;



# JSON query
# create hive table
CREATE  EXTERNAL  TABLE apache_logs
(
  log STRING
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' STORED AS TEXTFILE
LOCATION  's3://rosetta-logs/apache';


# Hive JSON query
select b.*
from apache_logs a
LATERAL VIEW json_tuple(a.log, 'code', 'path') b
as code, path
limit 100;

Time taken: 116.436 seconds, Fetched: 100 row(s)


# Presto JSON query
select json_extract_scalar(a.log, '$.code') code,
json_extract_scalar(a.log, '$.path') path
from apache_logs a
limit 100;


Query 20140725_060527_00017_2fq68, FINISHED, 2 nodes
Splits: 253 total, 65 done (25.69%)
0:01 [10.4K rows, 4.1MB] [10.7K rows/s, 4.2MB/s]
