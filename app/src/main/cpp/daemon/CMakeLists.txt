

add_library( daemon
             SHARED
             daemon.cpp )



target_link_libraries( daemon
                       ${log-lib} )