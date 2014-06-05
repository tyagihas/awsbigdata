cd /home/ec2-user
git clone https://github.com/tyagihas/awsbigdata.git
cd ./awsbigdata/kinesisapp
/home/ec2-user/ant/bin/ant > build.log
chmod 755 *.sh
chown -R ec2-user:ec2-user /home/ec2-user
export AWS_ACCESS_KEY=AAAAAAAAAAA  >> /home/ec2-user/.bash_profile\n",
export AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY >> /home/ec2-user/.bash_profile
export AWS_SECRET_ACCESS_KEY=BBBBBBBBB >> /home/ec2-user/.bash_profile\n",
export AWS_SECRET_KEY=$AWS_SECRET_ACCESS_KEY >> /home/ec2-user/.bash_profile
