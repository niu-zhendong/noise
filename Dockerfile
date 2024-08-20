FROM apache/kafka:3.8.0

# 设置工作目录
WORKDIR /app

# 将本地jar包复制到容器中
COPY target/noise-0.0.1-SNAPSHOT.jar noise.jar

# 增加启动脚本
RUN sed -i 'N;36a\nohup java -jar /app/noise.jar &' /etc/kafka/docker/run

# 设置容器启动时执行的命令
ENTRYPOINT ["/__cacert_entrypoint.sh","/etc/kafka/docker/run"]

# 暴露应用端口
EXPOSE 9092 30405