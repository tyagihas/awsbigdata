#!/bin/bash

. ./config.sh

JVM_Params="-Xms256m -Xmx256m"

APPNAME=KinesisApp
STREAM=$1
ACCESSPATH="/item/software/2878"

COORDNE="35.71,139.75"
COORDSW="35.60,139.58"

QUERY="select distinct(user1) from logs where path = ? and geoip = 'JP'"

CPATH=
for i in `find ../../lib -name "*.jar"`
do
  CPATH=${CPATH}:${i}
done

java -cp $CPATH:./dist/kinesisapp.jar $JVM_Params -Dkinesisapp.name=$APPNAME -Dkinesisapp.stream=$STREAM -Dkinesisapp.coordsw=$COORDSW -Dkinesisapp.coordne=$COORDNE -Dkinesisapp.accesspath=$ACCESSPATH -Dkinesisapp.query="$QUERY" -Dkinesisapp.jdbcurl=$JDBCURL -Dkinesisapp.dbuser=$DBUSER -Dkinesisapp.dbpassword=$DBPASSWORD com.amazonaws.services.kinesis.app.KinesisServer
