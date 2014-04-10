#!/usr/bin/env ruby

require "pp"
require "json"
require 'aws-sdk'
require 'base64'

def encode64(str)
  Base64.encode64(str).delete("\n")
end

if ARGV.size != 2
  puts "Usage: geoman.rb <number_of_user> <kinesis_stream>"
  exit 1
end

user_num = ARGV[0].to_i
user_num = 100 if user_num == 0
kinesis_stream = ARGV[1] || "aws-jp-big-data-bootcamp"

# Load station data
# sample station record
# { "type": "Feature", "properties": { "N02_001": "11", "N02_002": "2", "N02_003": "山手線", "N02_004": "東日本旅客鉄道", "N02_005": "目黒" }, "geometry": { "type": "LineString", "coordinates": [ [ 139.71636, 35.63136 ], [ 139.71585, 35.63284 ], [ 139.71574, 35.63327 ], [ 139.71562, 35.63369 ], [ 139.71557, 35.63395 ], [ 139.71551, 35.63421 ], [ 139.71544, 35.63452 ] ] } }

# FIXME: bug in the geojson data? Detected multiple Shijyuku station of Yamanote-line.
# There are some Yamanote-line stations not found in the data.
# Stations are not sorted..
YAMANOTE_STATIONS = %w(大崎 品川 田町 浜松町 新橋 有楽町 東京 神田 秋葉原 御徒町 上野 鶯谷 日暮里 西日暮里 田端 駒込 巣鴨 大塚 池袋 目白 高田馬場 新大久保 新宿 代々木 原宿 渋谷 恵比寿 目黒 五反田)

here = File.expand_path(File.dirname(__FILE__))
station_file = File.join(here, "./data/N02-12_Station.json")
stations = JSON.load(File.read(station_file))

# TODO: We only use Yamanote-line right now
yamanote_line = []
stations["features"].each do |x|
  station_name = x["properties"]["N02_005"]
  next if yamanote_line.include?(station_name)
  next unless x["properties"]["N02_003"] == "山手線" && x["properties"]["N02_004"] == "東日本旅客鉄道"
  x["station_no"] = YAMANOTE_STATIONS.index(station_name)
  yamanote_line << x
end

yamanote_line.sort! { |x, y|
  x["station_no"] <=> y["station_no"]
}

AWS.config({
  :access_key_id => ENV['AWS_ACCESS_KEY_ID'],
  :secret_access_key => ENV['AWS_SECRET_ACCESS_KEY'],
  # :region => ENV['AWS_REGION']
  :region => "us-east-1"
})
puts "Initializing Kinesis client..."
@client = AWS.kinesis.client

STATION_NUM = yamanote_line.size
MAX_LOOP = user_num * STATION_NUM * 10

users = (1..user_num).map {|n| 10000 + n}
curr_stations = {}
loops = 0
# while true || loops > MAX_LOOP do
while true do
  user = 10000 + Random.rand(user_num)
  curr_station_no = curr_stations[user] || Random.rand(yamanote_line.size - 1)
  curr_line = "山手線" # TODO
  curr_station = yamanote_line[curr_station_no]["properties"]["N02_005"]
  geo = yamanote_line[curr_station_no]["geometry"]["coordinates"][0]
  latitude = geo[1]
  longitude = geo[0]
  record = {
    :user       => user.to_s,
    :line       => curr_line,
    :station    => curr_station,
    :latitude   => latitude,
    :longitude  => longitude
  }

  payload = JSON.generate(record)

  data = {
            :stream_name => kinesis_stream,
            :data => payload,
            :partition_key => "USER_PARTITION_1" # TODO
          }
  @client.put_record(data)
  puts "[#{loops}] User #{user} at #{curr_line} #{curr_station}駅, latitude=#{latitude}, longitude=#{longitude}"
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
