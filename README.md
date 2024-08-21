建议使用docker打包部署

启动脚本如下：
1、镜像生成：docker build -t noise-receiver-image .
2、容器运行：docker run -d --restart always --privileged  -p 9092:9092 -p 30405:30405 --name noise-receiver noise-receiver-image
