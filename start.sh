#!/bin/bash
work_dir=`pwd`
nohup java -jar ${work_dir}/*.jar --spring.profiles.active=prod & echo $! | tee server.pid