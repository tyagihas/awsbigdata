GEOMAN
======

A railway transit simulator and Amazon Kinesis data generator.
Only some of the Yamanote-line stations are supported in current version.

Sample record in Kinesis:

    "{\"user\":\"10008\",\"line\":\"山手線\",\"station\":\"目黒\",\"latitude\":35.63136,\"longitude\":139.71636}"

Sample console output:

    [0] User 10006 at 山手線線 高田馬場駅, latitude=35.7141, longitude=139.70423
    [1] User 10009 at 山手線線 代々木駅, latitude=35.6845, longitude=139.70203

# Get the GEO station data

    mkdir data
    curl -o data/N02-12_Station.json -L https://s3-us-west-2.amazonaws.com/yifeng-public/data/geodata/N02-12_Station.json

# Start geoman

    nohup ./geoman.rb >> geoman.log 2>&1 &
