Presto on EMR
=============

This is to demonstrate Presto on EMR comparing with Hive.

# Install Presto on EMR

Replace ACCESS_KEY & SECRET_KEY with your keys

    aws emr create-cluster \
    --name presto-cluster \
    --ami-version 3.1.0 \
    --instance-groups InstanceGroupType=MASTER,InstanceCount=1,InstanceType=m1.large InstanceGroupType=CORE,InstanceCount=3,InstanceType=m3.xlarge \
    --no-auto-terminate \
    --use-default-role \
    --log-uri s3://yifeng2/logs/emr/ \
    --termination-protected \
    --tags Name=EMR \
    --ec2-attributes \
    '
        {
            "SubnetId": "subnet-dcaa8db4",
            "KeyName": "awsmac"
        }
    ' \
    --applications \
    '
      [
        {
          "Name": "HIVE"
        }
      ]
    ' \
    --bootstrap-actions \
    '
      [
        {
          "Name": "Configure core-site.xml",
          "Path": "s3://elasticmapreduce/bootstrap-actions/configure-hadoop",
          "Args": [
            "-c", "fs.s3.awsAccessKeyId=ACCESS_KEY",
            "-c", "fs.s3.awsSecretAccessKey=SECRET_KEY"
          ]
        },
        {
          "Path": "s3://presto-bucket/install_presto_0.71.rb",
          "Args": [
            "-t", "1GB",
            "-l", "DEBUG",
            "-j", "-server -Xmx1G -XX:+UseConcMarkSweepGC -XX:+ExplicitGCInvokesConcurrent -XX:+AggressiveOpts -XX:+HeapDumpOnOutOfMemoryError -XX:OnOutOfMemoryError=kill -9 %p -Dhive.config.resources=/home/hadoop/conf/core-site.xml,/home/hadoop/conf/hdfs-site.xml","-v","0.72","-s","1GB","-a","1h","-p","http://central.maven.org/maven2/com/facebook/presto/"],
          "Name": "Install Presto"
        }
      ]
    '


# Run Prsto

## Create table in Hive

    hive

    -- create hive table
    DROP TABLE IF EXISTS apachelog;
    CREATE  EXTERNAL  TABLE apachelogs
    (
      log STRING
    )
    ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' STORED AS TEXTFILE
    LOCATION  's3://rosetta2-logs/apache';


    -- Hive JSON query
    select b.*
    from apachelogs a
    LATERAL VIEW json_tuple(a.log, 'code', 'path') b
    as code, path
    limit 100;

50秒以上かかります

    Time taken: 52.636 seconds, Fetched: 100 row(s)

Hiveを終了する

    quit;



## Start Presto and run the JSON query:

    # Set Presto Pager to null for clean display
    export PRESTO_PAGER=

     # Launch Presto
    ./presto --catalog hive

    -- Show tables to prove that Presto is seeing Hive's tables
    show tables;

    -- JSON query in Presto
    select json_extract_scalar(a.log, '$.code') code,
    json_extract_scalar(a.log, '$.path') path
    from apachelogs a
    limit 100;


数秒で完了

    Query 20141119_051746_00014_xy2dm, FINISHED, 3 nodes
    Splits: 202 total, 107 done (52.97%)
    0:02 [16.9K rows, 6.65MB] [8.48K rows/s, 3.34MB/s]

Prestoを終了

    quit;

# Wrap-up

多くのクエリでは、PrestoはHive (MapReduce engine利用の場合)よりも早いことを確認。
