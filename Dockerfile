# Use official Node.js Image (Lightweight Alpine)
FROM node:18-alpine

# Set Working Directory
WORKDIR /usr/src/app

# Copy package definition
COPY backend/package*.json ./

# Install dependencies (Production only)
RUN npm install --only=production

# Copy Source Code
COPY backend/ .

# Expose Port
EXPOSE 5000

# Start Command
CMD [ "node", "src/server.js" ]
