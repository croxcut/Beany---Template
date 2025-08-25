cmake_minimum_required(VERSION 3.22.1)
project("tinyllama")

add_library(
        llama-jni
        SHARED
        llama_jni.cpp
        llama.cpp/llama.cpp
        llama.cpp/ggml.c
)

find_library(log-lib log)

target_include_directories(llama-jni PRIVATE llama.cpp)

target_link_libraries(
        llama-jni
        ${log-lib}
)
