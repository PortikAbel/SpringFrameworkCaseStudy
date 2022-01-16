FROM node:14

WORKDIR /usr/src/idde/react_client

COPY package*.json .
RUN npm install

COPY . .

ENV PORT=3000
EXPOSE 3000

CMD ["npm", "start"]
