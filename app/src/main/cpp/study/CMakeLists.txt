

add_library( study
             SHARED
             study.cpp )



target_link_libraries( study
                       ${log-lib} )