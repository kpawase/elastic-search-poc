#foundation image
FROM node:13.12.0-alpine

#A directory in the docker container
WORKDIR /usr/src/app

# copy package.json and package-lock.json
COPY package*.json ./

# install all node packages
RUN npm install

# Copy project directory to docker
COPY . .

#port to access the application
EXPOSE 3000

# command to run the application
CMD [ "npm", "start" ]
