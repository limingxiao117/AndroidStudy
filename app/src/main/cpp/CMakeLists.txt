
cmake_minimum_required(VERSION 3.4.1)

find_library( # Sets the name of the path variable.
              log-lib

              log )


ADD_SUBDIRECTORY(study)
ADD_SUBDIRECTORY(daemon)