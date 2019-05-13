# **********************************************************************
#
#    kafka消费者启动脚本
#                              -- 陈文督 2019年5月13日
#
#
# **********************************************************************
#!/bin/bash

base_dir=$(dirname $0)
app_dir=$base_dir/../

app_name=com.chenwen.consumer.ConsumerMain

source /etc/profile

# 当系统开始启动的时候，运行的函数
start() {
    pid=`jps -l | grep $app_name | awk '{print $1}'`
    if [ ! -z $pid ]; then
        pid=`jps -l | grep $app_name | awk '{print $1}'`
        echo "$app_name 正在运行, pid为$pid "
    else
        nohup java -cp $CLASSPATH:$app_dir/config:$app_dir/lib/* com.chenwen.consumer.ConsumerMain >> /dev/null &
    fi
}

status() {
    pid=`jps -l | grep $app_name | awk '{print $1}'`
    if [ ! -z $pid ]; then
        pid=`jps -l | grep $app_name | awk '{print $1}'`
        echo "$app_name 正在运行, pid为$pid"
    else
        echo "$app_name 没有运行.."
    fi
}

# 当系统开始停止的时候，运行的函数
stop() {
    pid=`jps -l | grep $app_name | awk '{print $1}'`
    if [ ! -z $pid ]; then
        kill -9 $pid;
        echo "kill -9 $app_name"
    else
        echo "$app_name 没有运行.."
    fi
}

# 当系统开始重启的时候，运行的函数
restart() {
    stop
    start
}

# 这里可以认为是对上面三个函数进行分支调用
case "$1" in
  start)
    start               # 调用start函数
    ;;
  stop)
    stop                # 调用stop函数
    ;;
  status)
    status
    ;;
  restart|reload)
    restart             # 调用restart函数
    ;;
  *)
    echo "Usage: $0 {start|stop|restart|status}"
    exit 1
esac

# 退出并返回执行结果
exit $?
