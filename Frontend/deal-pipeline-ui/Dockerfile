# ---------- STEP 1: Build Angular ----------
FROM node:20-alpine AS build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

# ---------- STEP 2: Serve with Nginx ----------
FROM nginx:alpine

# 🔥 THIS LINE MUST MATCH YOUR dist FOLDER
COPY --from=build /app/dist/deal-pipeline-ui/browser /usr/share/nginx/html

# 🔥 Replace default nginx config (newly added)
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
