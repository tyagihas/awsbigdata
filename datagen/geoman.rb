#!/usr/bin/env ruby

require "pp"
require "json"
require 'aws-sdk'
require 'base64'

def encode64(str)
  Base64.encode64(str).delete("\n")
end

# Load station data
# sample station record
# { "type": "Feature", "properties": { "N02_001": "11", "N02_002": "2", "N02_003": "山手線", "N02_004": "東日本旅客鉄道", "N02_005": "目黒" }, "geometry": { "type": "LineString", "coordinates": [ [ 139.71636, 35.63136 ], [ 139.71585, 35.63284 ], [ 139.71574, 35.63327 ], [ 139.71562, 35.63369 ], [ 139.71557, 35.63395 ], [ 139.71551, 35.63421 ], [ 139.71544, 35.63452 ] ] } }

# FIXME: bug in the geojson data? Detected multiple Shijyuku station of Yamanote-line. There are some Yamanote-line stations not found in the data.

here = File.expand_path(File.dirname(__FILE__))
station_file = File.join(here, "./data/railways-gml/geojson/N02-12_Station.json")
stations = JSON.load(File.read(station_file))

# TODO: We only use Yamanote-line right now
yamanote_line = []
stations["features"].each do |x|
  next unless x["properties"]["N02_003"] == "山手線" && x["properties"]["N02_004"] == "東日本旅客鉄道"
  yamanote_line << x
end

AWS.config({
  :access_key_id => ENV['AWS_ACCESS_KEY_ID'],
  :secret_access_key => ENV['AWS_SECRET_ACCESS_KEY'],
  # :region => ENV['AWS_REGION']
  :region => "us-east-1"
})
puts "Initializing Kinesis client..."
@client = AWS.kinesis.client

KINESIS_STREAM = "aws-jp-big-data-bootcamp"
USER_NUM = 100
STATION_NUM = yamanote_line.size
MAX_LOOP = USER_NUM * STATION_NUM * 10

users = (1..USER_NUM).map {|n| 10000 + n}
curr_stations = {}
loops = 0
# while true || loops > MAX_LOOP do
while true do
  user = 10000 + Random.rand(USER_NUM)
  curr_station_no = curr_stations[user] || 0
  curr_line = "山手" # TODO
  curr_station = yamanote_line[curr_station_no]["properties"]["N02_005"]
  geo = yamanote_line[curr_station_no]["geometry"]["coordinates"][0]
  latitude = geo[1]
  longitude = geo[0]
  record = {
    :user       => user,
    :line       => curr_line,
    :station    => curr_station,
    :latitude   => latitude,
    :longitude  => longitude
  }

  payload = JSON.generate(record)

  data = {
            :stream_name => KINESIS_STREAM,
            :data => encode64(payload),
            :partition_key => "USER_PARTITION_1" # TODO
          }

  @client.put_record(data)
  puts "[#{loops}] User #{user} at #{curr_line}線 #{curr_station}駅, latitude=#{latitude}, longitude=#{longitude}"
  # TODO: we only target at Meguro station right now
  if curr_station == "目黒" then
    puts
    puts "BINGO!"
    # break
  end

  sleep 0.1
  curr_station_no += 1
  curr_station_no = 0 if curr_station_no >= STATION_NUM
  curr_stations[user] = curr_station_no
  loops += 1
end
