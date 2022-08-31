# Define the base image
FROM nginx:1.10.1

# Copy the source files to a destination
COPY src/html /Users/callum/GitHub/Containerised-DES

# Expose the host ports
# EXPOSE 80

# Define entry point
# CMD ["nginx", "-g" ,"daemon off;"]