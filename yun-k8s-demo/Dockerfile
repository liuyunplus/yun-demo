FROM node:latest

# 指定工作目录，如果不存在则新建目录
WORKDIR /app
# 复制当前目录到容器的/app目录
COPY . /app
# 在容器内运行npm install命令
RUN npm install --registry=https://registry.npm.taobao.org/
# 暴露8080端口
EXPOSE 8080
CMD [ "node", "server.js" ]