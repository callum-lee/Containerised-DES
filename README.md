# Containerised-DES
The purpose of this project is to be able to containerise discrete event simulation models to make them more portable and efficient. There are several key parts in this project: 
- The models folder contains the simple queueing models that we are testing on
- The jaamsim folder contains the original jaamsim code that has been customised to control events and have them passed externally
- The socketapp contains the external message handler alongside the dockerfiles that handle the execution and containerisation of both components

## DES Models
- The models can be adjusted and changed as .cfg files and replaced in the models folder 
- The outputs of the events can be viewed in the jaamsim console


## Containerisation
The servers and clients have it's respective docker files and an overarching docker compose file. To start these containers, you will need to build and then run these files. 

### Build

```
$ docker-compose build
```

### Run
```
$ docker-compose up
```

## Authors
- Callum Lee
- Shannon Lu
- Michael O'Sullivan