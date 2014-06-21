from boto.dynamodb2.fields import HashKey, RangeKey, GlobalAllIndex
from boto.dynamodb2.layer1 import DynamoDBConnection
from boto.dynamodb2.table import Table
from boto.dynamodb2.items import Item

table = Table('bootcamplog')
items = table.query(user__eq='10011')
for item in items:
	print item['latitude']
