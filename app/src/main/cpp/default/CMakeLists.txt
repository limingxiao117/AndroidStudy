# 继承上一层的CMakeLists.txt的变量，也可以在这里重新赋值
# C 的编译选项是 CMAKE_C_FLAGS
# 指定编译参数，可选
#SET(CMAKE_CXX_FLAGS "-Wno-error=format-security -Wno-error=pointer-sign")

# 生成so动态库
ADD_LIBRARY(native-lib SHARED native-lib.cpp)

target_link_libraries(native-lib log)