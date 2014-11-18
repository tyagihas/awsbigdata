Data Pipeline Hands-on
======================

# CLIインストール

[Data Pipeline Command Line Tool](http://aws.amazon.com/developertools/AWS-Data-Pipeline/2762849641295030)をダウンロードし、解凍する。

下記内容の認証情報ファイルを作成

    $ vi ~/.aws/dp-config.json

    {
      "access-id": "access_key_id",
      "private-key": "secret_access_key",
      "endpoint": "https://datapipeline.us-east-1.amazonaws.com",
      "region": "us-east-1",
      "log-uri": "s3://myawsbucket/logfiles"
    }

# インポート用のjsonファイルを編集

    $ vi dp-bigdata-bootcamp.json

編集する項目:

- s3://MY_BUCKET (数箇所)
- MY_KEY_PAIR
- MY_SG


# Import Pipeline

    $ datapipeline --credentials ~/.aws/dp-config.json --create bigdata-bootcamp --put dp-bigdata-bootcamp.json

Data Pipelineコンソールからインポートしたpipelineを確認し、activateさせる。コンソールにて、pipelineによって起動されたEMRクラスタや、Redshiftのテーブルにロードされたデータを確認。
