#nginx_Dockerfile
FROM nginx:1.11

RUN rm -rf /etc/nginx/conf.d/default.conf

COPY ./nginx/conf.d/app.conf  /etc/nginx/conf.d/app.conf
COPY ./nginx/conf.d/nginx.conf  /etc/nginx/nginx.conf

VOLUME ["/data", "/etc/nginx", "/var/log/nginx"]

WORKDIR /etc/nginx

CMD ["nginx"]

EXPOSE 80