# Etapa de construção
FROM node:18 AS builder

WORKDIR /app

COPY package.json .
COPY package-lock.json .
RUN npm ci

COPY . .
RUN npm run build

# Etapa de execução
FROM nginx

COPY --from=builder /app/build /usr/share/nginx/html
COPY ./nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 3000

CMD ["nginx", "-g", "daemon off;"]