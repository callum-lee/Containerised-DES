# Define the base image
FROM nginx:1.10.1

# Copy the source files to a destination
COPY src/html /usr/share/nginx/html

# Expose the host ports
# EXPOSE 80

# Define entry point
# CMD ["nginx", "-g" ,"daemon off;"]